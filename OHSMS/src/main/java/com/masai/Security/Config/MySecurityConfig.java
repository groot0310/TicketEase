package com.masai.Security.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.masai.Security.JWT.JwtAuthenticationFilter;
import com.masai.Security.JWT.JwtEntryPoint;
import com.masai.Services.CustomUserDetailsServiceImpl;

@Configuration
public class MySecurityConfig {

	@Autowired
	private JwtEntryPoint entryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private CustomUserDetailsServiceImpl userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	private final String [] AUTH_PERMIT_ALL= {
			"/swagger-resources/**", "/v3/api-docs/**", "/swagger-ui/**", "/ohsms/authenticate/**"
	};


	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(entryPoint)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers("/ohsms/admin/**").hasAuthority("ADMIN")
		.antMatchers("/ohsms/employee/**").hasAuthority("EMPLOYEE")
		.antMatchers("/ohsms/engineer/**").hasAuthority("ENGINEER")
		.antMatchers(AUTH_PERMIT_ALL).permitAll()
		.anyRequest()
		.authenticated();

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
