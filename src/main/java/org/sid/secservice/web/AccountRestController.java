package org.sid.secservice.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.sid.secservice.entities.*;
import org.sid.secservice.service.AccountService;
import org.sid.secservice.service.EmailService;
import org.sid.secservice.util.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class AccountRestController {
    private AccountService accountService;
    private EmailService emailService;
private PasswordEncoder passwordEncoder;
    public AccountRestController(EmailService emailService) {
        this.emailService = emailService;
    }

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    public AccountRestController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public AccountRestController(EmailService emailService, AccountService accountService,PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/users")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }

    @PostMapping(path = "/users")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path = "/roles")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }

    @PostMapping(path = "/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authToken = request.getHeader("Authorization");

        if (authToken != null && authToken.startsWith("Bearer ")) {
            try {
                String jwt = authToken.substring(7);
                Algorithm algorithm = Algorithm.HMAC256("mySecret123");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();
                AppUser appUser = accountService.loadUserByUsername(username);
                String jwtAccessToken = JWT.create()
                        .withSubject(appUser.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", appUser.getAppRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> idToken = new HashMap<>();
                idToken.put("accesToken", jwtAccessToken);
                idToken.put("refreshToken", jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);
            } catch (Exception e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            throw new RuntimeException("Refresh Token rtequired");
        }

    }

    @GetMapping(path = "/profile")
    public AppUser profile(Principal principal) {
        return accountService.loadUserByUsername(principal.getName());
    }

    // http://localhost:8080/checkEmail
    @PostMapping(path = "/checkEmail")
    public AccountResponse resetPassword(@RequestBody ResetPassword resetPassword) {

        //  boolean result = this.accountService.ifEmailExist(resetPassword.getEmail());
        AppUser user = this.accountService.getUserByEmail(resetPassword.getEmail());
        AccountResponse accountResponse = new AccountResponse();

        if (user != null) {
            String code = Code.getCode();
            Mail mail = new Mail(resetPassword.getEmail(), code);
            this.emailService.sendCodeByMail(mail);
            user.setCode(code);
            this.accountService.updateUser(user);
            accountResponse.setResult(1);
        } else {
            accountResponse.setResult(0);
        }

        return accountResponse;
    }

    @PostMapping(path = "/resetPassword")
    public AccountResponse resetPassword(@RequestBody NewPassword newPassword) {
        AppUser user = this.accountService.getUserByEmail(newPassword.getEmail());
        AccountResponse accountResponse = new AccountResponse();
        if (user != null) {
            System.out.println(user.getCode().equals(newPassword.getCode()));
            System.out.println(newPassword.getCode());
            System.out.println(this.passwordEncoder.encode(newPassword.getPassword()));
            System.out.println(user);
            if (user.getCode().equals(newPassword.getCode())) {

                user.setPassword(passwordEncoder.encode(newPassword.getPassword()));

                this.accountService.updateUser(user);
                accountResponse.setResult(1);
            } else {
                accountResponse.setResult(0);
            }

        } else {
            accountResponse.setResult(0);
        }
        return accountResponse;
    }
}
@Data
class RoleUserForm{
    private String username;
    private String roleName;
}
