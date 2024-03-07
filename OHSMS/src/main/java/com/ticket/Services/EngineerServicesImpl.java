package com.masai.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.masai.Enums.ComplaintStatus;
import com.masai.Enums.Role;
import com.masai.Exceptions.InvalidCredentialsException;
import com.masai.Exceptions.InvalidInputException;
import com.masai.Exceptions.RecordsNotFoundException;
import com.masai.Models.Complaint;
import com.masai.Models.Engineer;
import com.masai.Models.Users;
import com.masai.Payload.Request.SignUpRequest;
import com.masai.Payload.Request.UpdateComplaintRequest;
import com.masai.Repositories.ComplaintRepo;
import com.masai.Repositories.EngineerRepo;
import com.masai.Repositories.UserRepo;

@Service
public class EngineerServicesImpl implements EngineerServices {

	@Autowired
	private EngineerRepo engineerRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ComplaintRepo complaintRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ServiceHelper helper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Engineer registerNewEngineer(SignUpRequest signUpRequest) throws InvalidCredentialsException {

		Optional<Users> existingUser = userRepo.findByUsername(signUpRequest.getUsername());

		if (existingUser.isPresent())
			throw new InvalidCredentialsException("User already Exists with this username");

		Users user = mapper.map(signUpRequest, Users.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.ENGINEER);
		Users savedUser = userRepo.save(user);

		Engineer savedEngineer = engineerRepo.save(mapper.map(savedUser, Engineer.class));

		return savedEngineer;
	}

	@Override
	public List<Complaint> getNewlyAssignedComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException {

		String username = helper.getUserName(httpServletRequest);

		Optional<Engineer> employee = engineerRepo.findByUsername(username);

		if (!employee.isPresent())
			throw new RecordsNotFoundException("Enginner not found.");

		List<Complaint> newComplaints = employee.get().getComplaints().stream()
				.filter(c -> c.getStatus().equals(ComplaintStatus.ASSIGNED)).collect(Collectors.toList());

		if (newComplaints.size() == 0)
			throw new RecordsNotFoundException("No new Complaints Found");

		return newComplaints;
	}

	@Override
	public List<Complaint> getAllResolvedComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException {

		String username = helper.getUserName(httpServletRequest);

		Optional<Engineer> employee = engineerRepo.findByUsername(username);

		List<Complaint> resolvedComplaints = employee.get().getComplaints().stream()
				.filter(c -> c.getStatus().equals(ComplaintStatus.RESOLVED)).collect(Collectors.toList());

		if (resolvedComplaints.size() == 0)
			throw new RecordsNotFoundException("No complaints found in resolved section");

		return resolvedComplaints;
	}

	@Override
	public List<Complaint> getAllUnderProgressComplaints(HttpServletRequest httpServletRequest)
			throws RecordsNotFoundException {

		String username = helper.getUserName(httpServletRequest);

		Optional<Engineer> employee = engineerRepo.findByUsername(username);

		List<Complaint> underProgressComplaints = employee.get().getComplaints().stream()
				.filter(c -> c.getStatus().equals(ComplaintStatus.UNDER_PROGRESS)).collect(Collectors.toList());

		if (underProgressComplaints.size() == 0)
			throw new RecordsNotFoundException("No complaints under progress.");

		return underProgressComplaints;
	}

	@Override
	public Complaint UpdateStatusOfComplaint(HttpServletRequest httpServletRequest,
			UpdateComplaintRequest complaintRequest) throws RecordsNotFoundException, InvalidInputException {

		String username = helper.getUserName(httpServletRequest);

		Optional<Engineer> employee = engineerRepo.findByUsername(username);

		Optional<Complaint> complaint = complaintRepo.findById(complaintRequest.getComplaintId());

		if (!complaint.isPresent())
			throw new RecordsNotFoundException("Invalid Complaint Id");

		if (complaint.get().getAssignedTo().getId() != employee.get().getId())
			throw new RecordsNotFoundException("Acess denied...!");

		if (complaintRequest.getStatus() == null || complaintRequest.getRemarks() == null)
			throw new InvalidInputException("status cannot be null");

		complaint.get().setStatus(complaintRequest.getStatus());
		complaint.get().setRemarks(complaintRequest.getRemarks());

		Complaint updatedComplaint = complaintRepo.save(complaint.get());

		return updatedComplaint;
	}

}
