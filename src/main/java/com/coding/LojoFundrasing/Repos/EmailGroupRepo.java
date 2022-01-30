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
	
	//find number of emails in email group
	@Query(value = "SELECT COUNT(DISTINCT emails.id) FROM emails WHERE committees_id = :committee_id AND emailgroup_id = :groupid", nativeQuery = true)
	Integer countEmailsinEmailGroup(Long groupid, Long committee_id);
	
	//find email group
	@Query(value = "SELECT * FROM emailgroups WHERE committees_id = :committee_id AND emailgroup_name = :groupname", nativeQuery = true)
	EmailGroup findbyNameandCommittee(String groupname, Long committee_id);
	
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
	@Query(value = "Select COUNT(DISTINCT donations.donor_id) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id", nativeQuery = true)
	Integer GroupDonors(@Param("groupid") Long groupid, Long committee_id);
	
	//group donation count
	@Query(value = "Select COUNT(DISTINCT donations.id) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id", nativeQuery = true)
	Integer GroupDonations(@Param("groupid") Long groupid, Long committee_id);
	
	//group average revenue (per donation)
	@Query(value = "Select AVG(donations.amount) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id", nativeQuery = true)
	Double GroupAveragePerDonation(@Param("groupid") Long groupid, Long committee_id);
	
	//group sum
	@Query(value = "Select SUM(donations.amount) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id", nativeQuery = true)
	Double GroupRevenue(@Param("groupid") Long groupid, Long committee_id);
	
	//recurring functions
	@Query(value = "Select COUNT(DISTINCT donations.donor_id) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id AND (donations.recurring = 'unlimited' OR donations.recurring >= 1)", nativeQuery = true)
	Integer GroupRecurringDonors(@Param("groupid") Long groupid, Long committee_id);
	
	@Query(value = "Select COUNT(DISTINCT donations.id) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id AND (donations.recurring = 'unlimited' OR donations.recurring >= 1)", nativeQuery = true)
	Integer GroupRecurringDonations(@Param("groupid") Long groupid, Long committee_id);
	
	@Query(value = "Select SUM(donations.amount) from donations left join emails on donations.email_id = emails.id WHERE emails.emailgroup_id = :groupid AND emails.committees_id = :committee_id AND donations.committees_id = :committee_id AND (donations.recurring = 'unlimited' OR donations.recurring >= 1)", nativeQuery = true)
	Double GroupRecurringRevenue(@Param("groupid") Long groupid, Long committee_id);
	
	//find an email in the list with recipients
	@Query(value = "Select * from emails WHERE emailgroup_id = :groupid AND committees_id = :committee_id AND recipients IS NOT NULL LIMIT 1", nativeQuery = true)
	Emails findEmailinGroupWithRecipients(@Param("groupid") Long groupid, Long committee_id);
}
