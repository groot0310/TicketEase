package com.masai.Services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Jwts;

@Component
public class ServiceHelper {

	@Value("${ohsms.jwt.secretKey}")
	private String secretKey;

	@Value("${ohsms.jwt.cookieName}")
	private String cookieName;

	public String getUserName(HttpServletRequest httpServletRequest) {
		String username = Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws((WebUtils.getCookie(httpServletRequest, cookieName)).getValue()).getBody().getSubject();

		return username;
	}
}
