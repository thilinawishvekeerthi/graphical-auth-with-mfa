package com.graphicauth.authservice.repo;

import com.graphicauth.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUserName(String userName);
}
