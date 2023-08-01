package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
