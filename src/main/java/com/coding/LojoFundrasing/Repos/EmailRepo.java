package com.coding.LojoFundrasing.Repos;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.Emails;


@Repository
public interface EmailRepo extends CrudRepository<Emails, Long>, JpaRepository<Emails, Long>, JpaSpecificationExecutor<Emails>{
	
	List<Emails> findAll();
	Emails findByemailRefcode1(String emailRefcode1);
	//find emails without group 
	@Query(value = "SElECT * FROM emails where committees_id = :committee_id and emailgroup_id is NULL AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY)", nativeQuery = true)
	List <Emails> findemailswithoutGroup(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by refcodes and commitee
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND email_refcode1 = :emailRefcode AND email_refcode2 = :emailRefcode2", nativeQuery = true)
	Emails findByemailRefcodeandCommittee(String emailRefcode, String emailRefcode2, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND email_refcode1 = :emailRefcode AND (email_refcode2 IS NULL or email_refcode2 = '' or email_refcode2 = ' ')", nativeQuery = true)
	Emails findByemailOneRefcodeandCommittee(String emailRefcode, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND email_refcode2 = :emailRefcode2 AND (email_refcode1 IS NULL or email_refcode1 = '' or email_refcode1 = ' ')", nativeQuery = true)
	Emails findByemailRefcodeTWOandCommittee(String emailRefcode2, Long committee_id);
	
	//order emails by date
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> findByOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emails.Emaildate Asc", nativeQuery = true)
	List<Emails> findByOrderByAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//average functions
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonationaverage Desc", nativeQuery = true)
	List<Emails> findByAverageOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonationaverage Asc", nativeQuery = true)
	List<Emails> findByAverageOrderByAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	@Query(value = "SELECT AVG(amount) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid", nativeQuery = true)
	Double averages(@Param("emailid") Long id, Long committee_id);
	
	//sum functions
	@Query(value = "SELECT SUM(amount) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid", nativeQuery = true)
	Double sums(@Param("emailid") Long id, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonationsum Asc", nativeQuery = true)
	List<Emails> findBySumOrderByAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonationsum Desc", nativeQuery = true)
	List<Emails> findBySumOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//donation count
	@Query(value = "SELECT COUNT(DISTINCT id) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid", nativeQuery = true)
	Integer donationscount(@Param("emailid") Long id, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonationcount Asc", nativeQuery = true)
	List<Emails> findByDonationsCountOrderByAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonationcount Desc", nativeQuery = true)
	List<Emails> findByDonationsCountOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//donor count
	@Query(value = "SELECT COUNT(DISTINCT donor_id) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid", nativeQuery = true)
	Integer donorscount(@Param("emailid") Long id, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonorcount Asc", nativeQuery = true)
	List<Emails> findByDonorCountOrderByAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emaildonorcount Desc", nativeQuery = true)
	List<Emails> findByDonorCountOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//recurring functions
	@Query(value = "SELECT COUNT(DISTINCT donor_id) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid AND (recurring = 'unlimited' OR recurring >= 1)", nativeQuery = true)
	Integer RecurringDonorCount(@Param("emailid") Long id, Long committee_id);
	
	@Query(value = "SELECT COUNT(id) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid AND (recurring = 'unlimited' OR recurring >= 1)", nativeQuery = true)
	Integer RecurringDonationCount(@Param("emailid") Long id, Long committee_id);
	
	@Query(value = "SELECT SUM(amount) FROM donations WHERE committees_id = :committee_id AND email_id = :emailid AND (recurring = 'unlimited' OR recurring >= 1)", nativeQuery = true)
	Double RecurringDonationSum(@Param("emailid") Long id, Long committee_id);
	
	//find emails by refcode1
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode1 = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> Refcode1Equals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode1 LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> Refcode1Contains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (email_refcode1 IS NULL or email_refcode1 = '' or email_refcode1 = ' ')", nativeQuery = true)
	List<Emails> Refcode1isBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by refcode2
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode2 = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> Refcode2Equals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode2 LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> Refcode2Contains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (email_refcode2 IS NULL or email_refcode2 = ' ' or email_refcode2 = '')", nativeQuery = true)
	List<Emails> Refcode2isBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by name
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_name = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> nameEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_name LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> nameContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (email_name IS NULL or email_name = '' or email_name = ' ')", nativeQuery = true)
	List<Emails> nameisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by category
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_category = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> categoryEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_category LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> categoryContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (email_category IS NULL or email_category = '' or email_category = ' ')", nativeQuery = true)
	List<Emails> categoryisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by subject line
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.subject_line = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> subjectEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.subject_line LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> subjectContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (subject_line IS NULL or subject_line = '' or subject_line = ' ')", nativeQuery = true)
	List<Emails> subjectisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by sender
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.sender = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> senderEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.sender LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> senderContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (sender IS NULL or sender = '' or sender = ' ')", nativeQuery = true)
	List<Emails> senderisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by testing
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND testing = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> testingEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND testing LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> testingContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (testing IS NULL or testing = '' or testing = ' ')", nativeQuery = true)
	List<Emails> testingisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails by link
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND link = :operand order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> linkEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND link LIKE %:operand% order by emails.Emaildate Desc", nativeQuery = true)
	List<Emails> linkContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND Emaildate >= DATE(:startdateE) and Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (link IS NULL or link = '' or link = ' ')", nativeQuery = true)
	List<Emails> linkisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
}
