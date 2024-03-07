package com.masai.Services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Complaint;
import com.masai.Models.Employee;
import com.masai.Payload.Request.NewComplaintRequest;
import com.masai.Payload.Request.SignUpRequest;

public interface EmployeeServices {

	// Method to register new Employee;
	public Employee registerNewEmployee(SignUpRequest signUpRequest) throws InvalidCredentialsException;

	//	Method to raise a complaint
	public Complaint raiseNewComplaint(HttpServletRequest httpServletRequest, NewComplaintRequest complaintRequest);

	//	Method to get All complaints raised so far.
	public List<Complaint> getAllComplaints(HttpServletRequest httpServletRequest) throws RecordsNotFoundException;

	// Method to get Complaint by Id;
	public Complaint getComplaintById(HttpServletRequest httpServletRequest, String complaintId)
			throws RecordsNotFoundException;

}
