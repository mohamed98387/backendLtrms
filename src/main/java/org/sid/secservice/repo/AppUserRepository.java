package org.sid.secservice.repo;

import org.sid.secservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUserName(String userName);
    AppUser findByEmail(String email);
    boolean existsByEmail(String email);

}
