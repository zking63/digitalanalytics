package com.coding.LojoFundrasing.Repos;

import java.util.List;
import java.util.Map;

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
	
	@Query(value = "SELECT * FROM emails WHERE committees_id = :committee_id AND parentid = :parentid", nativeQuery = true)
	List<Emails> findEmailsinGroupByParentId(String parentid, Long committee_id);
	
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND parentid = :parentid", nativeQuery = true)
	EmailGroup findGroupByParentId(String parentid, Long committee_id);
	
	//find number of emails in email group
	@Query(value = "SELECT COUNT(DISTINCT emails.id) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Integer countEmailsinEmailGroup(Long groupid, Long committee_id);
	
	//find email group
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND emailgroup_name = :groupname", nativeQuery = true)
	EmailGroup findbyNameandCommittee(String groupname, Long committee_id);
	
	//find variant a in email group with no test
	@Query(value = "select testing from emails where committees_id = :committee_id and emailgroup_id = :groupid and email_name like '%) A%' ORDER BY testing DESC LIMIT 1", nativeQuery = true)
	<Optional>String findEmailTesting(Long groupid, Long committee_id);
	
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
	
	//Sort emails and email groups together
	//@Query(value = "select * from (SELECT emailgroups.id as id, \"group\" as source from emailgroups where emailgroups.committees_id = :committee_id union SELECT emails.id as id, \"email\" as source FROM emails where emails.emailgroup_id is NULL and emails.committees_id = :committee_id) as tableC", nativeQuery = true)
	//Map<Long, String> SortEmailsandEmailGroupsId(Long committee_id);
	
	//@Query(value = "select * from (SELECT \"group\" as source from emailgroups where emailgroups.committees_id = :committee_id union SELECT  \"email\" as source FROM emails where emails.emailgroup_id is NULL and emails.committees_id = :committee_id) as tableC", nativeQuery = true)
	//List<String> SortEmailsandEmailGroupsCategory(Long committee_id);
	
	//find an email in the list with recipients
	//@Query(value = "Select * from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id AND recipients IS NOT NULL LIMIT 1", nativeQuery = true)
	//Emails findEmailinGroupWithRecipients(@Param("groupid") Long groupid, Long committee_id);
}
