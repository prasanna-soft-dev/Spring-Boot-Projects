package com.demo.vote.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.vote.Entity.Admin;
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminNameAndPassword(String adminName, String password);
}
