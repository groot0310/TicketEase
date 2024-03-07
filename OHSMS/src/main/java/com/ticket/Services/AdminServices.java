package com.masai.Services;

import java.util.List;

import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.InvalidInputException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Admin;
import com.masai.Models.Complaint;
import com.masai.Models.Employee;
import com.masai.Models.Engineer;
import com.masai.Payload.Request.SignUpRequest;

public interface AdminServices {


	public Admin registerNewAdmin(SignUpRequest signUpRequest) throws InvalidCredentialsException;

	public List<Admin> getAllAdmins() throws RecordsNotFoundException;

	public List<Employee> getAllEmployees() throws RecordsNotFoundException;

	public List<Engineer> getAllEngineer() throws RecordsNotFoundException;

	public List<Complaint> getAllComplaints() throws RecordsNotFoundException;

	public List<Complaint> getAllUnAssignedComplaints() throws RecordsNotFoundException;

	public Complaint assignEngineerToComplaint(Integer engineerId, String complaintId)
			throws RecordsNotFoundException, InvalidInputException;
}
