package com.masai.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Models.Users;
import com.masai.Payload.Request.LoginRequest;
import com.masai.Payload.Response.MessageResponse;
import com.masai.Payload.Response.UserResponse;
import com.masai.Security.JWT.JwtHelper;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/ohsms/authenticate")
public class AuthController {

	@Autowired
	private JwtHelper helper;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ModelMapper mapper;

	@PostMapping("login")
	public ResponseEntity<?> authentivateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		System.out.println("1");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Users user = (Users) authentication.getPrincipal();
		UserResponse userResponse = mapper.map(user, UserResponse.class);
		ResponseCookie cookie = helper.generateCookie(user);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(userResponse);
	}

	@GetMapping("logout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = helper.deleteCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You have been Loged Out"));
	}
}
