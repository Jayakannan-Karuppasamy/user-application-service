package com.scb.loanapi.userapi;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.scb.loanapi.userapi.model.UserDetail;
import com.scb.loanapi.userapi.repoistory.LoanUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserLoanManagementServiceApplicationTests {
	
	@Autowired
	LoanUserRepository loanUserRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void saveLoanUserTest() {
		UserDetail registerNewuser=new UserDetail();
		registerNewuser.setUserName("testAdmin");
		registerNewuser.setEmailId("testadmin1@gmail.com");
		String bcryptPassword=bCryptPasswordEncoder.encode("testadmin1@123");
		registerNewuser.setPassword(bcryptPassword);
		registerNewuser.setRole("Admin");
		
		UserDetail findByemailId=loanUserRepository.findByEmailId(registerNewuser.getEmailId());
		if(findByemailId == null) {
			UserDetail createdUser=loanUserRepository.save(registerNewuser);
			assertNotNull(createdUser);
		}		
		
	}

}
