package org.sid.secservice;

import org.sid.secservice.entities.AppRole;
import org.sid.secservice.entities.AppUser;
import org.sid.secservice.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;

@SpringBootApplication
@EnableAsync
public class SecServiceApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(SecServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

  //  @Bean
  // CommandLineRunner start(AccountService accountService){
      //  return args ->{
       //     accountService.addNewRole(new AppRole(null,"USER"));
          //  accountService.addNewRole(new AppRole(null,"ADMIN"));
        //   accountService.addNewRole(new AppRole(null,"CUSTOMMER_MANAGER"));
        //   accountService.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
       //    accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));
      //     accountService.addNewUser(new AppUser(null,"user1","8080",new ArrayList<>()));
     //      accountService.addNewUser(new AppUser(null,"admin","8080",new ArrayList<>()));
      //     accountService.addNewUser(new AppUser(null,"user2","8080",new ArrayList<>()));
      //      accountService.addNewUser(new AppUser(null,"user3","8080",new ArrayList<>()));
      //     accountService.addNewUser(new AppUser(null,"user4","8080",new ArrayList<>()));
     //      accountService.addRoleToUser("user1","USER");
      //      accountService.addRoleToUser("admin","USER");
     //       accountService.addRoleToUser("admin","ADMIN");
      //     accountService.addRoleToUser("user2","USER");
      //      accountService.addRoleToUser("user2","CUSTOMMER_MANAGER");
       //    accountService.addRoleToUser("user3","USER");
         //  accountService.addRoleToUser("user3","PRODUCT_MANAGER");
       //     accountService.addRoleToUser("user4","USER");
       //    accountService.addRoleToUser("user4","BILLS_MANAGER");

     //   };
//}
}
