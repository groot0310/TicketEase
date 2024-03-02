package com.masai.Services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.masai.Enums.ComplaintStatus;
import com.masai.Enums.Role;
import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.InvalidInputException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Admin;
import com.masai.Models.Complaint;
import com.masai.Models.Employee;
import com.masai.Models.Engineer;
import com.masai.Models.Users;
import com.masai.Payload.Request.SignUpRequest;
import com.masai.Repositories.AdminRepo;
import com.masai.Repositories.ComplaintRepo;
import com.masai.Repositories.EmployeeRepo;
import com.masai.Repositories.EngineerRepo;
import com.masai.Repositories.UserRepo;

@Service
public class AdminServicesImpl implements AdminServices {

	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private EngineerRepo engineerRepo;

	@Autowired
	private ComplaintRepo complaintRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Admin registerNewAdmin(SignUpRequest signUpRequest) throws InvalidCredentialsException {
		Optional<Users> existingUser = userRepo.findByUsername(signUpRequest.getUsername());

		if (existingUser.isPresent())
			throw new InvalidCredentialsException("User already Exists with this username");

		Users user = mapper.map(signUpRequest, Users.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.ADMIN);
		Users savedUser = userRepo.save(user);

		Admin savedAdmin = adminRepo.save(mapper.map(savedUser, Admin.class));

		return savedAdmin;
	}

	@Override
	public List<Admin> getAllAdmins() throws RecordsNotFoundException {
		List<Admin> admins = adminRepo.findAll();

		if (admins.size() == 0)
			throw new RecordsNotFoundException("No admin records found");

		return admins;
	}

	@Override
	public List<Employee> getAllEmployees() throws RecordsNotFoundException {

		List<Employee> employees = employeeRepo.findAll();

		if (employees.size() == 0)
			throw new RecordsNotFoundException("No Employees found.");

		return employees;
	}

	@Override
	public List<Engineer> getAllEngineer() throws RecordsNotFoundException {

		List<Engineer> engineers = engineerRepo.findAll();

		if (engineers.size() == 0)
			throw new RecordsNotFoundException("No Employees found.");

		return engineers;
	}

	@Override
	public List<Complaint> getAllComplaints() throws RecordsNotFoundException {
		List<Complaint> complaints = complaintRepo.findAll();

		if (complaints.size() == 0)
			throw new RecordsNotFoundException("No Engineers found.");

		return complaints;
	}

	@Override
	public List<Complaint> getAllUnAssignedComplaints() throws RecordsNotFoundException {
		List<Complaint> unassignedComplaints = complaintRepo.findByStatus(ComplaintStatus.UNASSIGNED);

		if (unassignedComplaints.size() == 0)
			throw new RecordsNotFoundException("No unassigned Complaints found");

		return unassignedComplaints;
	}

	@Override
	public Complaint assignEngineerToComplaint(Integer engineerId, String complaintId)
			throws RecordsNotFoundException, InvalidInputException {
		Optional<Engineer> existingEngineer = engineerRepo.findById(engineerId);
		Optional<Complaint> existingComplaint = complaintRepo.findById(complaintId);

		if (!existingEngineer.isPresent())
			throw new InvalidInputException("Engineer not found with given Id.");

		if (!existingComplaint.isPresent())
			throw new InvalidInputException("Invalid complaint Id");

		existingComplaint.get().setAssignedTo(existingEngineer.get());
		existingComplaint.get().setStatus(ComplaintStatus.ASSIGNED);
		Complaint updatedComplaint = complaintRepo.save(existingComplaint.get());

		return updatedComplaint;
	}

}
