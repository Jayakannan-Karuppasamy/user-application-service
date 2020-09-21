package com.scb.loanapi.userapi.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.loanapi.userapi.model.LoginRequestModel;
import com.scb.loanapi.userapi.model.UserDetail;
import com.scb.loanapi.userapi.service.LoanUserService;
import com.scb.loanapi.userapi.serviceImpl.LoanUserServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	Logger log=LoggerFactory.getLogger(AuthenticationFilter.class);
	
	@Autowired
	LoanUserService loanUserService;
	
	@Autowired
	private Environment env;
	
	//private AuthenticationManager authenticationManager;
	
	
	
	/*
	 * public AuthenticationFilter(LoanUserService loanUserService, Environment env,
	 * AuthenticationManager authenticationManger) { this.loanUserService =
	 * loanUserService; this.env = env;
	 * super.setAuthenticationManager(authenticationManger); }
	 */

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginRequestModel creds;
		try {
			creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
			log.info("creds::::"+creds.toString());
			
			 Authentication authentication=getAuthenticationManager().authenticate(
						new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
			
			return authentication;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest reqeust,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult)throws IOException, ServletException {
		 //super.successfulAuthentication(reqeust, response,chain, authResult);
		
		String userName=((User) authResult.getPrincipal()).getUsername();
		/*
		 * log.info("successfulAuthenication::::"+userName); log.info("token.secret"
		 * +env.getProperty("token.secret")); log.info("token.expiration_time"
		 * +env.getProperty("token.expiration_time"));
		 */
		
		
	//	UserDetail userDetail=loanUserService.getUserDetailByEmail(userName);
		//log.info("userDetail::::"+userDetail.toString());
		// Generate JWT Token and added in header for subsequence of request access

		String token = Jwts.builder().setSubject(userName)
				.setExpiration(
						new Date(System.currentTimeMillis() + Long.parseLong("86400000")))
				.signWith(SignatureAlgorithm.HS512, "loanapi123").compact();
		
		log.info("Genrated Token :::" + token);

		response.addHeader("token", token);
		response.addHeader("userId", userName);
		
		
	}
	

}
