package org.sid.secservice.service;

import org.sid.secservice.entities.AppRole;
import org.sid.secservice.entities.AppUser;
import org.sid.secservice.repo.AppRoleRepository;
import org.sid.secservice.repo.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;

    private AppRoleRepository approleRepository;
    private PasswordEncoder passwordEncoder;
    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository approleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.approleRepository = approleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {

        return approleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        AppUser  appUser=appUserRepository.findByUserName(userName);
        AppRole appRole=approleRepository.findByRoleName(roleName);
        appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String userName) {
        return appUserRepository.findByUserName(userName);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }
    @Override
    public boolean ifEmailExist(String email) {
        return appUserRepository.existsByEmail(email);
    }

    @Override
    public AppUser getUserByEmail(String mail) {
        return this.appUserRepository.findByEmail(mail);
    }

    @Override
    public AppUser updateUser(AppUser appUser) {
        return (AppUser) appUserRepository.save(appUser);
    }
}
