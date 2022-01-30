package com.coding.LojoFundrasing.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.test;

public interface testrepo extends CrudRepository<test, Long> {
	List<test> findAll();
	
	@Query(value = "SELECT * FROM test WHERE committees_id = :committee_id AND id = :testid", nativeQuery = true)
	test findTestsbyIdandCommittee(Long committee_id, Long testid);
	
	@Query(value = "SELECT * FROM test WHERE committees_id = :committee_id AND testname = :testname AND varianta = :varianta AND variantb = :variantb", nativeQuery = true)
	Optional<test> findbyTest(String testname, Long committee_id, String varianta, String variantb);
	
	@Query(value = "SELECT * FROM test WHERE committees_id = :committee_id", nativeQuery = true)
	List<test> findTestsbyCommittee(Long committee_id);
	
	//find tests with an email in range
	@Query(value = "SELECT test.* FROM emails left join test ON emails.testing = test.testname WHERE test.committees_id = :committee_id AND emails.emaildate >= DATE(:startdateE) and emails.emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) group by test.id order by test.testname ASC", nativeQuery = true)
	List<test> findTestswithinRangeOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find tests within updated_at range
	/*@Query(value = "SELECT * FROM test WHERE committees_id = :committee_id AND updated_at >= DATE(:startdateE) and updated_at < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by test.updated_at Desc", nativeQuery = true)
	List<test> findTestswithinRangeOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);*/
	
	//good for when you want to see every email
	/*@Query(value = "SELECT * FROM test left join emailgroups ON emailgroups.test_id = test.id right join emails ON emails.emailgroup_id = emailgroups.id WHERE test.committees_id = :committee_id AND emails.emaildate >= DATE(:startdateE) and emails.emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emails.emaildate Desc", nativeQuery = true)
	List<test> findTestswithinRangeOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);*/
	
	//find sender or subject tests
	@Query(value = "SELECT * FROM test WHERE committees_id = :committee_id AND testcategory = :testcategory AND varianta = :varianta", nativeQuery = true)
	Optional<test> findbyVariantA(String testcategory, Long committee_id, String varianta);
	
	@Query(value = "SELECT * FROM test WHERE committees_id = :committee_id AND testcategory = :testcategory AND variantb = :variantb", nativeQuery = true)
	Optional<test> findbyVariantB(String testcategory, Long committee_id, String variantb);
	
	//emails per test calculation
	@Query(value = "SELECT COUNT(DISTINCT emails.id) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id", nativeQuery = true)
	Long testEmailsCount(Long committee_id, Long testid);
	
	@Query(value = "SELECT COUNT(DISTINCT emails.id) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Long testAEmailsCount(Long committee_id, Long testid);
	
	@Query(value = "SELECT COUNT(DISTINCT emails.id) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Long testBEmailsCount(Long committee_id, Long testid);
	
	//total recipients calculation
	@Query(value = "SELECT SUM(emails.recipients) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Long variantARecipients(Long committee_id, Long testid);
	
	@Query(value = "SELECT SUM(emails.recipients) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Long variantBRecipients(Long committee_id, Long testid);
	
	//total openers calculation
	@Query(value = "SELECT SUM(emails.openers) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Long variantAOpens(Long committee_id, Long testid);
	
	@Query(value = "SELECT SUM(emails.openers) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Long variantBOpens(Long committee_id, Long testid);
	
	//total clicks calculation
	@Query(value = "SELECT SUM(emails.clicks) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Long variantAClicks(Long committee_id, Long testid);
	
	@Query(value = "SELECT SUM(emails.clicks) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Long variantBClicks(Long committee_id, Long testid);
	
	//total donations calculation
	@Query(value = "SELECT SUM(emails.emaildonationcount) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Long variantADonations(Long committee_id, Long testid);
	
	@Query(value = "SELECT SUM(emails.emaildonationcount) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Long variantBDonations(Long committee_id, Long testid);
	
	//total revenue calculation
	@Query(value = "SELECT SUM(emails.emaildonationsum) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Double variantARevenue(Long committee_id, Long testid);
	
	@Query(value = "SELECT SUM(emails.emaildonationsum) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Double variantBRevenue(Long committee_id, Long testid);
	
	//total averages per donation calculation
	@Query(value = "SELECT AVG(emails.emaildonationsum) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.varianta = emails.variant", nativeQuery = true)
	Double variantAaverageperEmail(Long committee_id, Long testid);
	
	@Query(value = "SELECT AVG(emails.emaildonationsum) FROM emailgroups LEFT JOIN emails on emailgroups.id = emails.emailgroup_id RIGHT JOIN test on emailgroups.test_id = test.id WHERE test.id = :testid AND test.committees_id = :committee_id AND test.variantb = emails.variant", nativeQuery = true)
	Double variantBaverageperEmail(Long committee_id, Long testid);
}
