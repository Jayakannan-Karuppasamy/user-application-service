package com.scb.loanapi.userapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scb.loanapi.userapi.model.UserDetail;
import com.scb.loanapi.userapi.service.LoanUserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	LoanUserService loanUserService;
	
	@PostMapping
	public ResponseEntity<UserDetail> createUser(@RequestBody UserDetail user) {
		String bcryptPassword=bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(bcryptPassword);
		UserDetail saved=loanUserService.saveRecord(user);
		return new ResponseEntity<UserDetail>(saved,HttpStatus.CREATED);
	}

}
