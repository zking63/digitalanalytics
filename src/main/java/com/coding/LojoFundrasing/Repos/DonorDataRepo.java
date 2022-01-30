package com.coding.LojoFundrasing.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.DonorData;

@Repository
public interface DonorDataRepo extends CrudRepository<DonorData, Long>{
	List<DonorData>findAll();
	Optional<DonorData> findById(Long id);
	@Query(value = "SELECT * FROM data_donors WHERE donor_id = :id", nativeQuery = true)
	Optional<DonorData> findByDonorId(Long id);
}
