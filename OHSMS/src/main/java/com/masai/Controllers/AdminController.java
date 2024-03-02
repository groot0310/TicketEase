package com.masai.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Enums.ComplaintStatus;
import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.InvalidInputException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Admin;
import com.masai.Models.Complaint;
import com.masai.Models.Employee;
import com.masai.Models.Engineer;
import com.masai.Payload.Request.SignUpRequest;
import com.masai.Services.AdminServices;
import com.masai.Services.EmployeeServices;
import com.masai.Services.EngineerServices;

import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
//@ApiOperation(value = "ADMIN ACESS API's")
@RequestMapping("/ohsms/admin")
public class AdminController {

	@Autowired
	private AdminServices adminServices;

	@Autowired
	private EngineerServices engineerServices;

	@Autowired
	private EmployeeServices employeeServices;

	// Endpoint to handle the creation of new Admin.
	@PostMapping("/create-admin")
	// (http://localhost:8088/ohsms/admin/create)
	@Operation(summary = "ADD NEW ADMIN TO DATABASE.", description = "SEND THE SIGNUP REQUEST TO ADD NEW ADMIN TO THE DATABASE. "
			+ "THIS WILL RETURN A ERROR MESSAGE IF A USER IS ALREADY PRESENT WITH THE PROVIDED USERNAME. "
			+ "TO AVOID PLEASE USE ----(\"CHECK IF USERNAME IS AVALIABLE\")----ENDPOINT FIRST. "
			+ "Link-> (http://localhost:8088/ohsms/admin/create)")
	public ResponseEntity<Admin> addNewAdmin(@RequestBody SignUpRequest signUpRequest)
			throws InvalidCredentialsException {
		Admin admin = adminServices.registerNewAdmin(signUpRequest);
		return new ResponseEntity<Admin>(admin, HttpStatus.CREATED);
	}

	@PostMapping("/create-engineer")
	@Operation(summary = "ADD NEW ENGINEER TO DATABASE.", description = "SEND THE SIGNUP REQUEST TO ADD NEW ENGINEER TO THE DATABASE. "
			+ "THIS WILL RETURN A ERROR MESSAGE IF A USER IS ALREADY PRESENT WITH THE PROVIDED USERNAME. "
			+ "TO AVOID PLEASE USE ----(\"CHECK IF USERNAME IS AVALIABLE\")----ENDPOINT FIRST. "
			+ "Link-> (http://localhost:8088/ohsms/admin/create)")
	public ResponseEntity<Engineer> registerNewEngineer(SignUpRequest signUpRequest)
			throws InvalidCredentialsException {
		Engineer engineer = engineerServices.registerNewEngineer(signUpRequest);
		return new ResponseEntity<Engineer>(engineer, HttpStatus.CREATED);
	}

	@PostMapping("/create-employee")
	@Operation(summary = "ADD NEW EMPLOYEE TO DATABASE.", description = "SEND THE SIGNUP REQUEST TO ADD NEW EMPLOYEE TO THE DATABASE. "
			+ "THIS WILL RETURN A ERROR MESSAGE IF A USER IS ALREADY PRESENT WITH THE PROVIDED USERNAME. "
			+ "TO AVOID PLEASE USE ----(\"CHECK IF USERNAME IS AVALIABLE\")----ENDPOINT FIRST. "
			+ "Link-> (http://localhost:8088/ohsms/admin/create)")
	public ResponseEntity<Employee> registerNewEmployee(@RequestBody SignUpRequest signUpRequest)
			throws InvalidCredentialsException {
		Employee employee = employeeServices.registerNewEmployee(signUpRequest);
		return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
	}

	@GetMapping("/admins")
	// (http://localhost:8088/ohsms/admin/admins)
	@Operation(summary = "GET A LIST OF ALL REGISTERED ADMINS", 
	description = "THIS API WILL GIVE YOU A LIST OF ALL THE REGISTERED ADMINS"
			+ "Link-> (http://localhost:8088/ohsms/admin/admins)")
	public ResponseEntity<List<Admin>> getAllAdmins() throws RecordsNotFoundException {
		List<Admin> admins = adminServices.getAllAdmins();
		return new ResponseEntity<List<Admin>>(admins, HttpStatus.OK);
	}

	@GetMapping("/employees")
	// (http://localhost:8088/ohsms/admin/employees)
	@Operation(summary = "GET A LIST OF ALL REGISTERED EMPLOYEES",
	description = "THIS API WILL GIVE YOU A LIST OF ALL REGISTERED EMPLOYEES."
			+ "IF THERE ARE NO ANY EMPLOYEES FOUND THE API WILL RETURN A ERROR WITH MESSAGE \"NO EMPLOYEES FOUND\"")
	public ResponseEntity<List<Employee>> getAllEmployees() throws RecordsNotFoundException {
		List<Employee> employees = adminServices.getAllEmployees();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@GetMapping("/engineers")
	// (http://localhost:8088/ohsms/admin/engineers)
	@Operation(summary = "GET A LIST OF ALL REGISTERED ENGINEERS", 
	description = "THIS API WILL GIVE YOU A LIST OF ALL REGISTERED ENGINEERS."
			+ "IF THERE ARE NO ANY ENGINEERS FOUND THE API WILL RETURN A ERROR WITH MESSAGE \"NO ENGINEERS FOUND\"")
	public ResponseEntity<List<Engineer>> getAllEngineers() throws RecordsNotFoundException {
		List<Engineer> engineers = adminServices.getAllEngineer();
		return new ResponseEntity<List<Engineer>>(engineers, HttpStatus.OK);
	}

	@GetMapping("/complaints")
	// (http://localhost:8088/ohsms/admin/complaints)
	@Operation(summary = "GET ALL COMPLAINTS LIST",
	description = "THIS API WILL GIVE A LIST OF ALL THE COMPLAINTS THAT ARE RAISED TILL NOW.")
	public ResponseEntity<List<Complaint>> getAllComplaints() throws RecordsNotFoundException {
		List<Complaint> complaints = adminServices.getAllComplaints();
		return new ResponseEntity<List<Complaint>>(complaints, HttpStatus.OK);
	}

	@GetMapping("/complaints/{status}")
	// (http://localhost:8088/ohsms/admin/complaints/{ComplaintStaus})
	@Operation(summary = "GET LIST OF COMPLAINTS BASED ON COMPLAINT STATUS",
	description = "THIS API WILL GIVE A LIST OF COMPLAINTS BASED ON THE STATUS THAT HAS BEEN PROVIDED AS PATHVARIBLE")
	public ResponseEntity<List<Complaint>> getComplaintsByStatus(ComplaintStatus status) {
		System.out.println(status);
		return null;
	}

	@GetMapping("/complaints/unassigned")
	@Operation(summary = "GET A LIST OF ALL UNASSIGNED COMPLAINTS", 
	description ="THIS API WILL GIVE A LIST OF COMPLAINTS WITH STATUS UNASSIGNED. IF NO COMPLAINTS"
			+ "ARE FOUND THEN IT WILL RETURN AN ERROR WITH MESSAGE NO COMPLAINTS FOUND.")
	public ResponseEntity<List<Complaint>> getAllUnassignedComplaints() throws RecordsNotFoundException {
		List<Complaint> complaints = adminServices.getAllUnAssignedComplaints();
		return new ResponseEntity<List<Complaint>>(complaints, HttpStatus.OK);
	}

	@PutMapping("/complaints/assign-complaint")
	@Operation(summary = "ASSIGN A COMPLAINT TO AN ENGINEER",description = "THIS API WILL ASSIGN THE COMPLAINT WITH GIVEN COMPLAINT ID"
			+ "TO AN ENGINEER WITH PROVIDED ENGINEER ID.")
	public ResponseEntity<Complaint> assignComplaintToEnginner(@RequestParam Integer engId, @RequestParam String compId)
			throws RecordsNotFoundException, InvalidInputException {
		Complaint complaint = adminServices.assignEngineerToComplaint(engId, compId);
		return new ResponseEntity<Complaint>(complaint, HttpStatus.OK);
	}

}
