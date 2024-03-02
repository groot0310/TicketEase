package com.masai.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Complaint;
import com.masai.Payload.Request.NewComplaintRequest;
import com.masai.Services.EmployeeServices;

import io.swagger.v3.oas.annotations.Operation;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/ohsms/employee")
public class EmployeeController {

	@Autowired
	private EmployeeServices employeeServices;

	@PostMapping("/complaint")
	@Operation(summary = "RAISE A NEW COMPLAINT.", description = "THIS API WILL SEND A POST METHOD WHICH HAS A BODY OF COMPLAINT WHICH CREATES A NEW COMPLAINT WITH DEFAULT STATUS AS UNASSIGNED. AND RETURNS THE JSON OBJECT OF THE NEWLY CREATED COMPLAINT.")
	public ResponseEntity<Complaint> registerNewComplaint(HttpServletRequest httpServletRequest,
			@RequestBody NewComplaintRequest complaintRequest) {
		Complaint complaint = employeeServices.raiseNewComplaint(httpServletRequest, complaintRequest);
		return new ResponseEntity<Complaint>(complaint,HttpStatus.CREATED);
	}

	@GetMapping("/complaint")
	@Operation(summary = "GET A LIST OF ALL COMPLAINTS RAISED.", description = "THIS API WILL GIVE YOU A LIST OF ALL THE COMPLAINTS RAISED BY THE LOGEDIN EMPLOYEE. INCASE THERE ARE NO COMPLAINTS FOUND THEN THE API WILL THROW AN ERROR WITH MESSAGE AS NO COMPLAINTS FOUND.")
	public ResponseEntity<List<Complaint>> getAllComplaints(HttpServletRequest httpServletRequest) throws RecordsNotFoundException{
		List<Complaint> complaints = employeeServices.getAllComplaints(httpServletRequest);
		return new ResponseEntity<List<Complaint>>(complaints,HttpStatus.OK);
	}

	@GetMapping("/complaint/{comId}")
	@Operation(summary = "GET A COMPLAINT BY THE ID", description = "THIS API WILL RETURN A OBJECT OF A COMPLAINT WITH GIVEN COMPLAINT ID, IF THE COMPLAINT IS NOT FOUND THEN IT WILL SEND A ERROR WITH MESSAGE A NO COMPLAINT FOUND OR INVALID COMPLAINT ID.")
	public ResponseEntity<Complaint> getComplaintByComplaintId(HttpServletRequest httpServletRequest,
			@PathVariable("comId") String complaintId) throws RecordsNotFoundException {
		Complaint complaint = employeeServices.getComplaintById(httpServletRequest, complaintId);
		return new ResponseEntity<Complaint>(complaint, HttpStatus.OK);
	}
}
