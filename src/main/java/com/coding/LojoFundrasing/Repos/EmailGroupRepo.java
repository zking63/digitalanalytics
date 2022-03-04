package com.coding.LojoFundrasing.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;

@Repository
public interface EmailGroupRepo extends CrudRepository<EmailGroup, Long>{
	List<EmailGroup> findAll();
	@Query(value = "SELECT * FROM emailgroups WHERE emailgroups.id = :id and committees_id = :committee_id", nativeQuery = true)
	EmailGroup findbyIdandCommittee(Long id, Long committee_id);
	@Query(value = "SELECT DISTINCT(emailgroups.id) FROM emailgroups LEFT JOIN emails ON emails.emailgroup_id = emailgroups.id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY)", nativeQuery = true)
	List<Long> findGroupByOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emails in group by parentid
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND parentid = :parentid", nativeQuery = true)
	List<Emails> findEmailsinGroupByParentId(String parentid, Long committee_id);
	
	//find group by parentid
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND parentid = :parentid", nativeQuery = true)
	EmailGroup findGroupByParentId(String parentid, Long committee_id);
	
	//find group by refcode2
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND parentid = :refcode2", nativeQuery = true)
	EmailGroup findGroupByRefcode2(String refcode2, Long committee_id);
	
	//find emails in group by refcode2
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND email_refcode2 = :refcode2", nativeQuery = true)
	List<Emails> findEmailsinGroupRefcode2(String refcode2, Long committee_id);
	
	//find number of emails in email group
	@Query(value = "SELECT COUNT(DISTINCT emails.id) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Integer countEmailsinEmailGroup(Long groupid, Long committee_id);
	
	//find full send emails with group 
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :emailgroupid AND (list like '%full%' OR list like '%Full%') and email_name LIKE '%) remainder%'", nativeQuery = true)
	Emails emailwithfulllistremainder(Long emailgroupid, Long committee_id);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :emailgroupid AND (list like '%donor%' OR list like '%Donor%') and email_name LIKE '%) remainder%'", nativeQuery = true)
	Emails emailwithdonorremainder(Long emailgroupid, Long committee_id);
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :emailgroupid AND (list like '%prospect%' OR list like '%Prospect%')  and email_name LIKE '%) remainder%'", nativeQuery = true)
	Emails emailwithprospectremainder(Long emailgroupid, Long committee_id);
	
	//find email group
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND emailgroup_name = :groupname", nativeQuery = true)
	EmailGroup findbyNameandCommittee(String groupname, Long committee_id);
	
	//find email group
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND emailgroups.test_id = :testid", nativeQuery = true)
	List<EmailGroup> findgroupbytestid(Long testid, Long committee_id);
	
	//find variant a in email group with no test
	@Query(value = "select testing from emails where committees_id = :committee_id and emailgroup_id = :groupid and testing is not null ORDER BY testing DESC LIMIT 1", nativeQuery = true)
	<Optional>String findEmailTesting(Long groupid, Long committee_id);
	
	@Query(value = "select count(distinct id) from emails where committees_id = :committee_id and emailgroup_id = :groupid and testing is not null and email_name NOT LIKE '%) remainder%'", nativeQuery = true)
	Integer findEmailswithTest(Long groupid, Long committee_id);
	
	//find how many tests in email group 
	@Query(value = "select count(distinct testing) from emails where committees_id = :committee_id and emailgroup_id = :groupid and testing is not null and email_name NOT LIKE '%) remainder%'", nativeQuery = true)
	Integer findnumberofdifferenttestsingroup(Long groupid, Long committee_id);
	
	//find variant a in email group with no test
	//@Query(value = "select * from emails where committees_id = :committee_id and emailgroup_id = :groupid and (list = 'donors' OR list = 'FullList' OR list = 'prospects') and email_name like '%(1) A%' ORDER BY list DESC LIMIT 1", nativeQuery = true)
	//Emails findVariantA(Long groupid, Long committee_id);
	
	//find variant a in email group with no test
	//@Query(value = "select * from emails where committees_id = :committee_id and emailgroup_id = :groupid and (list = 'donors' OR list = 'FullList' OR list = 'prospects') and email_name like '%(1) B%' ORDER BY list DESC LIMIT 1", nativeQuery = true)
	//Emails findVariantB(Long groupid, Long committee_id);
	
	//openers calculation
	@Query(value = "SELECT SUM(openers) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Long GroupOpeners(@Param("groupid") Long groupid, Long committee_id);
	
	//recipients calculation
	@Query(value = "SELECT sum(recipients) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Long GroupRecipients(@Param("groupid") Long groupid, Long committee_id);
	
	//bounces calculation
	@Query(value = "SELECT sum(bounces) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Long GroupBounces(@Param("groupid") Long groupid, Long committee_id);
	
	//clicks calculation
	@Query(value = "SELECT sum(clicks) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Long GroupClicks(@Param("groupid") Long groupid, Long committee_id);
	
	//unsubscribers calculation
	@Query(value = "SELECT sum(unsubscribers) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Long GroupUnsubscribers(@Param("groupid") Long groupid, Long committee_id);
	
	//group donor count
	//@Query(value = "Select COUNT(DISTINCT donations.donor_id) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id", nativeQuery = true)
	//Integer GroupDonors(@Param("groupid") Long groupid, Long committee_id);
	
	//group donation count
	@Query(value = "Select sum(emaildonationcount) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Integer GroupDonations(@Param("groupid") Long groupid, Long committee_id);
	
	//group average revenue (per donation)
	//@Query(value = "Select AVG(donations.amount) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id", nativeQuery = true)
	//Double GroupAveragePerDonation(@Param("groupid") Long groupid, Long committee_id);
	
	//group sum
	@Query(value = "Select sum(emaildonationsum) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Double GroupRevenue(@Param("groupid") Long groupid, Long committee_id);
	
	
	//group tandem donation count
	@Query(value = "Select sum(tandemdonations) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Integer GroupTandemDonations(@Param("groupid") Long groupid, Long committee_id);
	
	//group tandem revenue
	@Query(value = "Select sum(tandemrevenue) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Double GroupTandemRevenue(@Param("groupid") Long groupid, Long committee_id);
	
	//group donations for calculation revenue
	@Query(value = "Select sum(donationsforcalculation) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Integer GroupDonationsforCalculation(@Param("groupid") Long groupid, Long committee_id);
	
	//recurring functions
	//@Query(value = "Select COUNT(DISTINCT donations.donor_id) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id AND (donations.recurring = 'unlimited' OR donations.recurring >= 1)", nativeQuery = true)
	//Integer GroupRecurringDonors(@Param("groupid") Long groupid, Long committee_id);
	
	@Query(value = "Select sum(recurring_donation_count) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Integer GroupRecurringDonations(@Param("groupid") Long groupid, Long committee_id);
	
	@Query(value = "Select sum(recurring_revenue) from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id", nativeQuery = true)
	Double GroupRecurringRevenue(@Param("groupid") Long groupid, Long committee_id);
	
	//sort emailgroups by revenue
	@Query(value = "SELECT * FROM emailgroups left join emails on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) order by emailgroups.groupsum DESC", nativeQuery = true)
	List<EmailGroup> sortEmailgroupsbyRevenue(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//query functions
	
	//find emailgroups with emails with refcode1
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode1 = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> Refcode1Equals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode1 LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> Refcode1Contains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emails.email_refcode1 IS NULL or emails.email_refcode1 = ' ' or emails.email_refcode1 = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> Refcode1isBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with emails with refcode2
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode2 = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> Refcode2Equals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_refcode2 LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> Refcode2Contains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emails.email_refcode2 IS NULL or emails.email_refcode2 = ' ' or emails.email_refcode2 = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> Refcode2isBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with emails with subject
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.subject_line = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> subjectEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.subject_line LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> subjectContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emails.subject_line IS NULL or emails.subject_line = ' ' or emails.subject_line = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> subjectisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with emails with sender
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.sender = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> senderEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.sender LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> senderContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emails.sender IS NULL or emails.sender = ' ' or emails.sender = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> senderisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with emails with link
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.link = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> linkEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.link LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> linkContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emails.link IS NULL or emails.link = ' ' or emails.link = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> linkisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with emails with category
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_category = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> categoryEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emails.email_category LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> categoryContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emails.email_category IS NULL or emails.email_category = ' ' or emails.email_category = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> categoryisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with title
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emailgroups.emailgroup_name = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> nameEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emailgroups.emailgroup_name LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> nameContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emailgroups.emailgroup_name IS NULL or  emailgroups.emailgroup_name = ' ' or emailgroups.emailgroup_name = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> nameisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	//find emailgroups with testing
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emailgroups.group_test = :operand group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> testingEquals(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND emailgroups.group_test LIKE %:operand% group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> testingContains(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String operand);
	
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) AND (emailgroups.group_test IS NULL or emailgroups.group_test = ' ' or emailgroups.group_test = '') group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> testingisBlank(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	
	//default list 
	@Query(value = "SELECT emailgroups.* FROM emails left join emailgroups on emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) group by emailgroups.id order by emailgroups.emailgroup_name Desc", nativeQuery = true)
	List<EmailGroup> findByOrderByDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	@Query(value = "SELECT emailgroups.* FROM emails LEFT JOIN emailgroups ON emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) and emailgroups.emailgroup_name not like '%Warmup%' group by emailgroups.id order by emailgroups.groupsum Desc LIMIT 10", nativeQuery = true)
	List<EmailGroup> top10byRevenue(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);

	@Query(value = "SELECT emailgroups.* FROM emails LEFT JOIN emailgroups ON emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) and emailgroups.emailgroup_name not like '%Warmup%' group by emailgroups.id order by emailgroups.groupsum Asc LIMIT 10", nativeQuery = true)
	List<EmailGroup> bottom10byRevenue(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	
	@Query(value = "SELECT emailgroups.* FROM emails LEFT JOIN emailgroups ON emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) and emailgroups.emailgroup_name not like '%Warmup%' group by emailgroups.id order by emailgroups.groupdonations_opens Desc LIMIT 10", nativeQuery = true)
	List<EmailGroup> top10byGO(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	@Query(value = "SELECT emailgroups.* FROM emails LEFT JOIN emailgroups ON emailgroups.id = emails.emailgroup_id WHERE emailgroups.committees_id = :committee_id AND emails.Emaildate >= DATE(:startdateE) and emails.Emaildate < DATE_ADD(DATE(:enddateE), INTERVAL 1 DAY) and emailgroups.emailgroup_name not like '%Warmup%' group by emailgroups.id order by emailgroups.groupdonations_opens Asc LIMIT 10", nativeQuery = true)
	List<EmailGroup> bottom10byGO(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id);
	//Sort emails and email groups together
	//@Query(value = "select * from (SELECT emailgroups.id as id, \"group\" as source from emailgroups where emailgroups.committees_id = :committee_id union SELECT emails.id as id, \"email\" as source FROM emails where emails.emailgroup_id is NULL and emails.committees_id = :committee_id) as tableC", nativeQuery = true)
	//Map<Long, String> SortEmailsandEmailGroupsId(Long committee_id);
	
	//@Query(value = "select * from (SELECT \"group\" as source from emailgroups where emailgroups.committees_id = :committee_id union SELECT  \"email\" as source FROM emails where emails.emailgroup_id is NULL and emails.committees_id = :committee_id) as tableC", nativeQuery = true)
	//List<String> SortEmailsandEmailGroupsCategory(Long committee_id);
	
	//find an email in the list with recipients
	//@Query(value = "Select * from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id AND recipients IS NOT NULL LIMIT 1", nativeQuery = true)
	//Emails findEmailinGroupWithRecipients(@Param("groupid") Long groupid, Long committee_id);
}
