package com.masai.Security.JWT;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.masai.Models.Users;
import com.masai.Services.ServiceHelper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtHelper {

	@Value("${ohsms.jwt.secretKey}")
	private String secretKey;

	@Value("${ohsms.jwt.cookieName}")
	private String cookieName;

	@Value("${ohsms.jwt.expiration}")
	private int expiration;

	@Autowired
	private ServiceHelper helper;

	public String getTokenfromRequest(HttpServletRequest httpServletRequest) {
		Cookie cookie = WebUtils.getCookie(httpServletRequest, cookieName);

		if (cookie != null)
			return cookie.getValue();

		return null;
	}

	public String getUsernameFromToken(String authToken) {
		String username = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getBody().getSubject();
		return username;
	}

	public String generateTokenFromUsername(String username) {
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + expiration))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();

		return token;
	}

	public ResponseCookie generateCookie(Users user) {
		String authToken = generateTokenFromUsername(user.getUsername());
		ResponseCookie cookie = ResponseCookie.from(cookieName, authToken).path("/ohsms").maxAge(24 * 60 * 60)
				.httpOnly(true).build();
		return cookie;
	}

	public ResponseCookie deleteCookie() {
		ResponseCookie cookie = ResponseCookie.from(cookieName, null).path("/ohsms").build();
		return cookie;
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("Validation failed Due to: " + e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println("Validation failed Due to: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("Validation failed Due to: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("Validation failed Due to: " + e.getMessage());
		} catch (SignatureException e) {
			System.out.println("Validation failed Due to: " + e.getMessage());
		}
		return false;
	}


}
