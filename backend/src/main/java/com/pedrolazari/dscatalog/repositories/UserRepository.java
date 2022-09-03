package com.pedrolazari.dscatalog.repositories;

import com.pedrolazari.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
