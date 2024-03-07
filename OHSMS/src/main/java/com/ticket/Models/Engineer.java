package com.masai.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Engineer {

	@Id
	@Column(name = "engineer_Id")
	private Integer Id;

	private String firstName;

	private String LastName;

	private String username;

	@JsonIgnore
	private String password;

	@OneToMany(mappedBy = "assignedTo")
	@JsonIgnore
	private List<Complaint> complaints = new ArrayList<>();
}
