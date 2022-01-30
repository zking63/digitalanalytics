/*package com.coding.LojoFundrasing.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.Contenttest;

@Repository
public interface ContentTestRepo extends CrudRepository<Contenttest, Long>{
	List<Contenttest> findAll();
	@Query(value = "SELECT * FROM contenttest WHERE committees_id = :committee_id AND jtk = :jtk AND recipients_list = :recipientslist", nativeQuery = true)
	Optional<Contenttest> findbyTest(String recipientslist, String jtk, Long committee_id);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND go_winner = :varianta", nativeQuery = true)
	Integer VariantAGoWinnerCount(Long test_id, Long committee_id, String varianta);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND go_winner = :variantb", nativeQuery = true)
	Integer VariantBGoWinnerCount(Long test_id, Long committee_id, String variantb);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND go_winner = :tied", nativeQuery = true)
	Integer TiedGoWinnerCount(Long test_id, Long committee_id, String tied);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND click_rcv_winner = :varianta", nativeQuery = true)
	Integer VariantAClickRcvWinner(Long test_id, Long committee_id, String varianta);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND click_rcv_winner = :variantb", nativeQuery = true)
	Integer VariantBClickRcvWinner(Long test_id, Long committee_id, String variantb);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND click_rcv_winner = :tied", nativeQuery = true)
	Integer TiedClickRcvWinner(Long test_id, Long committee_id, String tied);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND fullist_winner = :varianta", nativeQuery = true)
	Integer VariantAFulllistWinner(Long test_id, Long committee_id, String varianta);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND fullist_winner = :variantb", nativeQuery = true)
	Integer VariantBFulllistWinner(Long test_id, Long committee_id, String variantb);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM contenttest WHERE committees_id = :committee_id AND test_id = :test_id AND fullist_winner = :tied", nativeQuery = true)
	Integer TiedFulllistWinner(Long test_id, Long committee_id, String tied);
}*/