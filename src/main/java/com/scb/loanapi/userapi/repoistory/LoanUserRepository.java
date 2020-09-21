package com.scb.loanapi.userapi.repoistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scb.loanapi.userapi.model.UserDetail;

@Repository
public interface LoanUserRepository extends JpaRepository<UserDetail, Long> {

	UserDetail findByEmailId(String emailId);
}
