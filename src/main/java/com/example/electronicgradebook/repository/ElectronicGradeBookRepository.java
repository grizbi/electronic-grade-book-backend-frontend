package com.example.electronicgradebook.repository;

import com.example.electronicgradebook.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ElectronicGradeBookRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
