package com.masai.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.Enums.ComplaintStatus;
import com.masai.Models.Complaint;

public interface ComplaintRepo extends JpaRepository<Complaint, String> {

	public List<Complaint> findByStatus(ComplaintStatus status);
}
