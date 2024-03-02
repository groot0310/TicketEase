package com.masai.Payload.Response;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.masai.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private Integer Id;

	private String firstName;

	private String LastName;

	private String username;

	@Enumerated(EnumType.STRING)
	private Role role;

}
