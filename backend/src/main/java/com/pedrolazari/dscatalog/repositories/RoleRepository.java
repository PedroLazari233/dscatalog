package com.pedrolazari.dscatalog.repositories;

import com.pedrolazari.dscatalog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
