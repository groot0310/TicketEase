package com.masai.Services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.InvalidInputException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Complaint;
import com.masai.Models.Engineer;
import com.masai.Payload.Request.SignUpRequest;
import com.masai.Payload.Request.UpdateComplaintRequest;

public interface EngineerServices {

	// Method to register Engineer;
	public Engineer registerNewEngineer(SignUpRequest signUpRequest) throws InvalidCredentialsException;

	// Method to see newly assigned complaints
	public List<Complaint> getNewlyAssignedComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException;

	//	Method to see all resolved complaints
	public List<Complaint> getAllResolvedComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException;

	//	Method to see all underprogress complaints
	public List<Complaint> getAllUnderProgressComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException;

	// Start Working on a complaint OR change the status of the complaint.
	public Complaint UpdateStatusOfComplaint(HttpServletRequest httpServletRequest,
			UpdateComplaintRequest complaintRequest) throws RecordsNotFoundException, InvalidInputException;
}
