package com.coding.LojoFundrasing.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.User;

@Repository
public interface UserRepo extends CrudRepository <User, Long>{
	User findByEmail(String email);
	List<User> findAll();
	@Query(value = "SELECT * FROM committees where id != :committee_id", nativeQuery = true)
	List<Committees> findAllexcept(@Param("committee_id") Long committee_id);
	   @Query(value="SELECT * FROM users WHERE verification_code = :code and email = :email", nativeQuery = true)
	    User findByVerificationCode(String code, String email);
	   @Modifying  
	   @Transactional
	   @Query(value="Update users set enabled = 1 where verification_code = :code", nativeQuery = true)
	    Integer updateEnabled(String code);
	   @Modifying  
	   @Transactional
	   @Query(value="Update users set verification_code = NULL where verification_code = :code", nativeQuery = true)
	    Integer updateVerificationCode(String code);

}
