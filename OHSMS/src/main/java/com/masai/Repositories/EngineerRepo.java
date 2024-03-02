package com.masai.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.Models.Engineer;

public interface EngineerRepo extends JpaRepository<Engineer, Integer> {

	public Optional<Engineer> findByUsername(String username);
}
