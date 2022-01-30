package com.coding.LojoFundrasing.Repos;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;


@Repository
public interface DonationRepo extends CrudRepository<Donation, Long>{
	List<Donation> findAll();
	List<Donation> findBydonor(Long donor_id);
	List<Donation> findByemailDonation(Long email_id);
	List<Donation> findAllByOrderByAmountAsc();
	List<Donation> findAllByOrderByAmountDesc();
	@Query(value = "SELECT COUNT(DISTINCT donations.donor_id) FROM donations LEFT JOIN emails on emails.id = donations.email_id where emails.emailgroup_id = :group_id and donations.committees_id = :committee_id", nativeQuery = true)
	Integer findDonorsinGroup(Long committee_id, Long group_id);
	@Query(value = "SELECT COUNT(DISTINCT donations.donor_id) FROM donations LEFT JOIN emails on emails.id = donations.email_id where emails.emailgroup_id = :group_id and donations.committees_id = :committee_id and donations.recurring IS NOT null", nativeQuery = true)
	Integer findRecurringDonorsinGroup(Long committee_id, Long group_id);
	@Query(value = "SELECT * FROM donations LEFT JOIN committees ON committees.id = donations.committees_id WHERE committees.id = :committee_id AND donations.act_blue_id = :actblueid", nativeQuery = true)
	List<Donation> findbyActBlueIdandCommittee_id(String actblueid, Long committee_id);
	
	
	@Query(value = "SELECT * FROM donations WHERE committees_id = :committee_id AND act_blue_id = :actblueid AND Dondate = :dondate AND donor_id = :donorid", nativeQuery = true)
	List<Donation> findbyActBlueIdandCommittee_idandDate(String actblueid, Long committee_id, @DateTimeFormat(pattern="yyyy-MM-dd") Date dondate, Long donorid);
	//////count
	@Query(value = "SELECT COUNT(DISTINCT id) FROM donations WHERE committees_id = :committee_id AND act_blue_id = :actblueid AND donor_id = :donorid", nativeQuery = true)
	Integer donationCountwithABid(String actblueid, Long committee_id, Long donorid);
	
	@Query(value = "SELECT COUNT(DISTINCT donations.id) FROM donations LEFT JOIN committees ON committees.id = donations.committees_id WHERE committees.id = :committee_id", nativeQuery = true)
	Integer donationCountwithCOMMITTEE(Long committee_id);
	
	@Query(value = "SELECT * FROM donations LEFT JOIN committees ON committees.id = donations.committees_id WHERE committees.id = :committee_id AND donations.Dondate >= DATE(:startdate) and donations.Dondate < DATE_ADD(DATE(:enddate), INTERVAL 1 DAY) order by donations.Dondate DESC", nativeQuery = true)
	List <Donation> findAllWithDondateAfter(@Param("startdate") @DateTimeFormat(pattern="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern="yyyy-MM-dd") String enddate, Long committee_id);
	@Query(value = "SELECT * FROM donations LEFT JOIN committees ON committees.id = donations.committees_id WHERE committees.id = :committee_id AND donations.Dondate >= DATE(:startdate) and donations.Dondate < DATE_ADD(DATE(:enddate), INTERVAL 1 DAY) order by donations.Dondate ASC", nativeQuery = true)
	List <Donation> findAllWithDondateAfterAsc(@Param("startdate") @DateTimeFormat(pattern="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern="yyyy-MM-dd") String enddate, Long committee_id);
	@Query(value = "SELECT * FROM donations LEFT JOIN committees ON committees.id = donations.committees_id WHERE committees.id = :committee_id AND donations.Dondate >= DATE(:startdate) and donations.Dondate < DATE_ADD(DATE(:enddate), INTERVAL 1 DAY) order by donations.amount DESC", nativeQuery = true)
	List<Donation> findByOrderByAmountDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id);
	@Query(value = "SELECT * FROM donations LEFT JOIN committees ON committees.id = donations.committees_id WHERE committees.id = :committee_id AND donations.Dondate >= DATE(:startdate) and donations.Dondate < DATE_ADD(DATE(:enddate), INTERVAL 1 DAY) order by donations.amount ASC", nativeQuery = true)
	List<Donation> findByOrderByAmountAsc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss") String enddate, Long committee_id);
}