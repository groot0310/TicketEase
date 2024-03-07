package com.masai.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.masai.Enums.Role;
import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Complaint;
import com.masai.Models.Employee;
import com.masai.Models.Users;
import com.masai.Payload.Request.NewComplaintRequest;
import com.masai.Payload.Request.SignUpRequest;
import com.masai.Repositories.ComplaintRepo;
import com.masai.Repositories.EmployeeRepo;
import com.masai.Repositories.UserRepo;

import net.bytebuddy.utility.RandomString;

@Service
public class EmployeeServicesImpl implements EmployeeServices {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ServiceHelper helper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ComplaintRepo complaintRepo;


	@Override
	public Employee registerNewEmployee(SignUpRequest signUpRequest) throws InvalidCredentialsException {

		Optional<Users> existingUser = userRepo.findByUsername(signUpRequest.getUsername());

		if (existingUser.isPresent())
			throw new InvalidCredentialsException("User already Exists with this username");

		Users user = mapper.map(signUpRequest, Users.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.EMPLOYEE);
		Users savedUser = userRepo.save(user);

		Employee savedEmployee = employeeRepo.save(mapper.map(savedUser, Employee.class));

		return savedEmployee;
	}

	@Override
	public List<Complaint> getAllComplaints(HttpServletRequest httpServletRequest) throws RecordsNotFoundException {
		String username = helper.getUserName(httpServletRequest);

		Optional<Employee> employee = employeeRepo.findByUsername(username);

		List<Complaint> complaints = employee.get().getComplaints();
		if (complaints.size() == 0)
			throw new RecordsNotFoundException("No Complaints Found");

		return complaints;
	}

	@Override
	public Complaint getComplaintById(HttpServletRequest httpServletRequest, String complaintId)
			throws RecordsNotFoundException {
		String username = helper.getUserName(httpServletRequest);

		Optional<Employee> employee = employeeRepo.findByUsername(username);

		List<Complaint> complaint = employee.get().getComplaints().stream().filter(c -> c.getId() == complaintId)
				.collect(Collectors.toList());
		if (complaint.size() == 0)
			throw new RecordsNotFoundException("No Complaints Found");

		return complaint.get(0);
	}

	@Override
	public Complaint raiseNewComplaint(HttpServletRequest httpServletRequest, NewComplaintRequest complaintRequest) {
		String username = helper.getUserName(httpServletRequest);

		Optional<Employee> employee = employeeRepo.findByUsername(username);

		Complaint newComplaint = mapper.map(complaintRequest, Complaint.class);

		String UUID = RandomString.make(6) + "$" + employee.get().getId();

		newComplaint.setId(UUID);
		newComplaint.setRaisedBy(employee.get());

		Complaint savedComplaint = complaintRepo.save(newComplaint);
		return savedComplaint;
	}


}
