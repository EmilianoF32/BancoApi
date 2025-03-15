package com.example.Api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Api.Model.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {

}
