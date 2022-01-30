package com.coding.LojoFundrasing.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.User;

@Repository
public interface UserRepo extends CrudRepository <User, Long>{
	User findByEmail(String email);
	List<User> findAll();
	@Query(value = "SELECT * FROM committees where id != :committee_id", nativeQuery = true)
	List<Committees> findAllexcept(@Param("committee_id") Long committee_id);
}
