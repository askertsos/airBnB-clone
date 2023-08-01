package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
