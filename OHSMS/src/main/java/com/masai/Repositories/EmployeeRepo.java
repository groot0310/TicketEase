package com.masai.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.Models.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	public Optional<Employee> findByUsername(String username);
}
