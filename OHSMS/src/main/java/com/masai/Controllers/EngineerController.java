package com.masai.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Exceptions.InvalidInputException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Complaint;
import com.masai.Payload.Request.UpdateComplaintRequest;
import com.masai.Services.EngineerServices;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/ohsms/engineer")
@ApiOperation(tags = "ENGINNER", value = "")
public class EngineerController {

	@Autowired
	private EngineerServices engineerServices;

	@GetMapping("/complaints/new")
	@Operation(summary = "GET A LIST OF ALL NEW COMPLAINTS", description = "THIS API WILL RETURN YOU A LIST OF ALL THE COMPLAINTS THAT ARE NEWLY ASSIGNED TO THE LOGEDIN ENGINNER.")
	public ResponseEntity<List<Complaint>> getNewlyAssignedComplaints(HttpServletRequest httpServletRequest) throws RecordsNotFoundException{
		List<Complaint> complaints = engineerServices.getNewlyAssignedComplaints(httpServletRequest);
		return new ResponseEntity<List<Complaint>>(complaints,HttpStatus.OK);
	}

	@GetMapping("/complaints/resolved")
	@Operation(summary = "GET A LIST OF ALL RESOLVED COMPLAINTS", description = "THIS WILL GIVE A LIST OF ALL THE COMPLAINTS WITH STATUS AS RESOLVED. THIS LIST WILL ONLY CONTAIN THE COMPLAINTS THAT ARE RESOLVED BY THE LOGEDIN ENGINEER.")
	public ResponseEntity<List<Complaint>> getAllResolvedComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException {
		List<Complaint> complaints = engineerServices.getAllResolvedComplaints(httpServletRequest);
		return new ResponseEntity<List<Complaint>>(complaints, HttpStatus.OK);
	}

	@GetMapping("/complaints/under-progress")
	@Operation(summary = "GET A LIST OF ALL UNDER-PROGRESS COMPLAINTS", description = "THIS WILL GIVE A LIST OF ALL THE COMPLAINTS WITH STATUS AS UNDER-PROGRESS. THIS LIST WILL ONLY CONTAIN THE COMPLAINTS THAT ARE UNDER-PROGRESS BY THE LOGEDIN ENGINEER.")
	public ResponseEntity<List<Complaint>> getAllUnderPrgressComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException {
		List<Complaint> complaints = engineerServices.getAllUnderProgressComplaints(httpServletRequest);
		return new ResponseEntity<List<Complaint>>(complaints, HttpStatus.OK);
	}

	@PutMapping("/complaint/update")
	@Operation(summary = "UPDATE THE STATUS OF A COMPLAINT", description = "THIS API WILL UPDATE THE EXISTING COMPLAINT STATUS WITH THE STATUS THAT HAS BEEN PASSED IN THE BODY.")
	public ResponseEntity<Complaint> updateStatusOfComplaint(HttpServletRequest httpServletRequest,
			@RequestBody UpdateComplaintRequest complaintRequest)
					throws RecordsNotFoundException, InvalidInputException {
		Complaint complaint = engineerServices.UpdateStatusOfComplaint(httpServletRequest, complaintRequest);
		return new ResponseEntity<Complaint>(complaint, HttpStatus.OK);
	}
}
