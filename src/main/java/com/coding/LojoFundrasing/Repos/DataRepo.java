/*package com.coding.LojoFundrasing.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.Data;





@Repository
public interface DataRepo extends CrudRepository<Data, Long>{
	List<Data>findAll();
	@Query(value = "SELECT * FROM data_funds where email_id = :email_id", nativeQuery = true)
	Data findEmailData(Long email_id);
}*/
