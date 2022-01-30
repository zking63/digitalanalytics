package com.coding.LojoFundrasing.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.User;

@Repository
public interface CommitteesRepo extends CrudRepository<Committees, Long>{
	List<Committees> findAll();
	List<Committees> findByusers(long user_id);
	Optional<Committees> findById(Long committee_id);
	@Query(value = "SELECT * from committees left join committees_users on committees_id = committees.id where committees_id != :committee_id and user_id = :user_id", nativeQuery = true)
	List<Committees> findAllexcept(@Param("committee_id") Long committee_id, Long user_id);
}
