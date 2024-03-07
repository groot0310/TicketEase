package com.masai.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.Models.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {

	public Optional<Users> findByUsername(String username);
}
