package com.masai.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.Models.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer> {

	public Optional<Admin> findByUsername(String username);
}
