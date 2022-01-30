package com.coding.LojoFundrasing.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Repos.DonationRepo;
import com.coding.LojoFundrasing.Repos.EmailGroupRepo;

@Service
public class EmailGroupService {
	@Autowired
	private EmailGroupRepo egrepo;
	
	@Autowired
	private DonationRepo donrepo;
	
	@Autowired
	private TestService tservice;
	
	@Autowired
	private CommitteeService cservice;
	
	public EmailGroup createEmailGroup(EmailGroup emailgroup) {
		return egrepo.save(emailgroup);
	}
	public EmailGroup updateEmailGroup(EmailGroup emailgroup) {
		return egrepo.save(emailgroup);
	}
	public EmailGroup findEmailGroupbyName(String emailGroupname, Long committee_id) {
		EmailGroup group = egrepo.findbyNameandCommittee(emailGroupname, committee_id);
		return group;
	}
	public EmailGroup findEmailGroupbyId(Long id, Long committee_id) {
		EmailGroup group = egrepo.findbyIdandCommittee(id, committee_id);
		return group;
	}
	public void getEmailGroupData(Long emailGroupId, Long committee_id) {
		EmailGroup emailgroup = egrepo.findbyIdandCommittee(emailGroupId, committee_id);
		System.out.println(emailgroup.getEmailgroupName());
		Committees committee = cservice.findbyId(committee_id);
		
		//email performance
		Long groupOpeners = egrepo.GroupOpeners(emailGroupId, committee_id);
		Long groupRecipients = egrepo.GroupRecipients(emailGroupId, committee_id);
		Long groupClicks = egrepo.GroupClicks(emailGroupId, committee_id);
		Long groupBounces = egrepo.GroupBounces(emailGroupId, committee_id);
		Long groupUnsubscribers = egrepo.GroupUnsubscribers(emailGroupId, committee_id);
		//donation info
		Double groupaverage = egrepo.GroupAveragePerDonation(emailGroupId, committee_id);
		Double groupsum = egrepo.GroupRevenue(emailGroupId, committee_id);
		Integer groupdonationcount = egrepo.GroupDonations(emailGroupId, committee_id);
		Integer groupdonorcount = egrepo.GroupDonors(emailGroupId, committee_id);
		//recurring
		Integer groupRecurringDonorCount = egrepo.GroupRecurringDonors(emailGroupId, committee_id);
		Integer groupRecurringDonationCount = egrepo.GroupRecurringDonations(emailGroupId, committee_id);
		Double groupRecurringRevenue = egrepo.GroupRecurringRevenue(emailGroupId, committee_id);
		//rates
		Double groupunsubscribeRate = 0.0;
		Double groupclickRate = 0.0;
		Double groupopenRate = 0.0;
		Double groupbounceRate = 0.0;
		
		Double groupdonationsOpens = 0.0;
		Double groupdonationsClicks = 0.0;
		Double groupclicksOpens = 0.0;
		Double groupdonorsOpens = 0.0;
		Double groupdonorsClicks = 0.0;
		//strings
		String test = emailgroup.getEmails().get(0).getTesting();
		String category = null;
		
		//email count
		Integer groupemailcount = egrepo.countEmailsinEmailGroup(emailgroup.getId(), committee_id);
		
		//test
		test overallTest = null;
		
		//set lists
		Boolean testListSet = false;
		Boolean committeeListSet = false;
		
		//variants
		String variantA = emailgroup.getVariantA();
		String variantB = emailgroup.getVariantB();
		Boolean variantASet = false;
		Boolean variantBSet = false;
		
		emailgroup.setGroupOpeners(groupOpeners);
		emailgroup.setGroupRecipients(groupRecipients);
		emailgroup.setGroupClicks(groupClicks);
		emailgroup.setGroupBounces(groupBounces);
		emailgroup.setGroupUnsubscribers(groupUnsubscribers);
		emailgroup.setGroupemailcount(groupemailcount);
		
		emailgroup.setGroupsum(groupsum);
		emailgroup.setGroupdonationcount(groupdonationcount);
		emailgroup.setGroupRecurringDonationCount(groupRecurringDonationCount);
		emailgroup.setGroupRecurringRevenue(groupRecurringRevenue);
		emailgroup.setGroupaverage(groupaverage);
		emailgroup.setGroupdonorcount(groupdonorcount);
		emailgroup.setGroupRecurringDonorCount(groupRecurringDonorCount);
		
		//calculate email performance + fundraising stats
		if (groupOpeners != null && groupOpeners != 0) {
			System.out.println("groupOpeners is 0 " + groupOpeners);
			groupclicksOpens = (double) groupClicks/groupOpeners;
			System.out.println("groupclicksOpens " + groupclicksOpens);
			groupdonationsOpens = (double) groupdonationcount/groupOpeners;
			System.out.println("groupdonationsOpens " + groupdonationsOpens);
			groupdonorsOpens = (double) groupdonorcount/groupOpeners;
			System.out.println("groupdonorsOpens " +  groupdonorsOpens);
		}
		if (groupClicks != null && groupClicks != 0) {
			System.out.println("groupClicks is NOT 0 " + groupClicks);
			System.out.println("groupdonationcount " + groupdonationcount);
			groupdonationsClicks = (double) groupdonationcount/groupClicks;
			System.out.println("groupdonationsClicks " + groupdonationsClicks);
			groupdonorsClicks = (double) groupdonorcount/groupClicks;
			System.out.println("groupdonorcount " + groupdonorcount);
			System.out.println("groupdonorsClicks " + groupdonorsClicks);
		}
		
		//calculate email performance stats
		if (groupRecipients != null && groupRecipients != 0) {
			System.out.println("groupRecipients is 0 " + groupRecipients);
			if (groupUnsubscribers != null && groupUnsubscribers != 0) {
				groupunsubscribeRate = (double) groupUnsubscribers/groupRecipients;
			}
			if (groupBounces != null && groupBounces != 0) {
				groupbounceRate = (double) groupBounces/groupRecipients;
			}
			if (groupClicks != null && groupClicks != 0) {
				groupclickRate = (double) groupClicks/groupRecipients;
			}
			if (groupOpeners != null && groupOpeners != 0) {
				groupopenRate = (double) groupOpeners/groupRecipients;
			}
		}
		if (variantA == null || variantA.isEmpty() || variantA == " " ) {
			variantASet = false;
			System.out.println("variant A is null " + variantA);
		}
		else {
			variantASet = true;
			System.out.println("variant A is already set " + variantA);
		}
		if (variantB == null || variantB.isEmpty() || variantB == " " ) {
			variantBSet = false;
			System.out.println("variant B is null " + variantB);
		}
		else {
			variantBSet = true;
			System.out.println("variant b is already set " + variantB);
		}
		if (test == null || test.isEmpty() || test == " ") {
			System.out.println("test is null ");
			variantASet = true;
			variantBSet = true;
		}
		while (variantASet == false) {
				for (int i = 0; i < emailgroup.getEmails().size(); i++) {
					String variant = emailgroup.getEmails().get(i).getVariant();
					if (!test.toUpperCase().contains("SENDER") && !test.toUpperCase().contains("SUBJECT")) {
						variant = variant.toUpperCase();
						System.out.println("TEST IS " + test);
					}
					System.out.println("variant in A loop " + variant);
					if (variant == null || variant.isEmpty() || variant == " ") {
						System.out.println("variant is null " + variant);
					}
					else {
						System.out.println("variant works " + variant);
						variantA = variant;
						emailgroup.setVariantA(variantA);
						System.out.println("variant A " + variantA);
						variantASet = true;
						break;
					}
				}
				emailgroup.setVariantA(variantA);
				System.out.println("variant A " + variantA);
				variantASet = true;
				break;
		}
		while (variantBSet == false && variantA != null) {
				for (int i = 0; i < emailgroup.getEmails().size(); i++) {
					String variant = emailgroup.getEmails().get(i).getVariant();
					if (!test.toUpperCase().contains("SENDER") && !test.toUpperCase().contains("SUBJECT")) {
						variant = variant.toUpperCase();
						System.out.println("TEST IS " + test);
					}
					System.out.println("variant in B loop " + variant);
					System.out.println("variant in A check " + variantA);
					if (variant == null || variant.isEmpty() || variant == " " || variant.contentEquals(variantA)) {
						System.out.println("variant doesn't work " + variant);
					}
					else {
						System.out.println("variant works " + variant);
						variantB = variant;
						emailgroup.setVariantB(variantB);
						System.out.println("variant B " + variantB);
						variantBSet = true;
						break;
					}
				}
				emailgroup.setVariantB(variantB);
				System.out.println("variant B " + variantB);
				variantBSet = true;
		}
		emailgroup.setGroupTest(test);
		emailgroup.setGroupopenRate(groupopenRate);
		emailgroup.setGroupdonationsOpens(groupdonationsOpens);
		emailgroup.setGroupdonorsOpens(groupdonorsOpens);
		emailgroup.setGroupclickRate(groupclickRate);
		emailgroup.setGroupclicksOpens(groupclicksOpens);
		emailgroup.setGroupdonationsClicks(groupdonationsClicks);
		emailgroup.setGroupdonorsClicks(groupdonorsClicks);
		emailgroup.setGroupunsubscribeRate(groupunsubscribeRate);
		emailgroup.setGroupbounceRate(groupbounceRate);
		updateEmailGroup(emailgroup);
		if (emailgroup.getGroupTest() == null || emailgroup.getGroupTest().isEmpty() 
				|| emailgroup.getGroupTest() == " ") {
			overallTest = null;
			emailgroup.setTest(overallTest);
			updateEmailGroup(emailgroup);
		}
		if ((emailgroup.getGroupTest() != null && !emailgroup.getGroupTest().isEmpty() 
				&& emailgroup.getGroupTest() != " ")  && (!emailgroup.getGroupTest().contains("SENDER")
				|| !emailgroup.getGroupTest().contains("SUBJECT"))) {
			overallTest = tservice.SetUpContentTestfromGroup(committee_id, emailgroup);
			emailgroup.setTest(overallTest);
        	while (testListSet == false) {
    			if (overallTest.getEmailgroups() == null || overallTest.getEmailgroups().size() == 0) {
    				List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
    				emailgroups.add(emailgroup);
    				overallTest.setEmailgroups(emailgroups);
    				tservice.updateTest(overallTest);
    				testListSet = true;
    			}
    			else {
    				emailgroup.setTest(overallTest);
    				List<EmailGroup> emailgroups = overallTest.getEmailgroups();
    				emailgroups.add(emailgroup);
    				overallTest.setEmailgroups(emailgroups);
    				tservice.updateTest(overallTest);
    				testListSet = true;
    			}
        	}
		}
    	while (committeeListSet == false) {
			if (committee.getEmailgroups() == null || committee.getEmailgroups().size() == 0) {
				List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
				emailgroups.add(emailgroup);
				committee.setEmailgroups(emailgroups);
				cservice.createCommittee(committee);
				committeeListSet = true;
			}
			else {
				List<EmailGroup> emailgroups = committee.getEmailgroups();
				emailgroups.add(emailgroup);
				committee.setEmailgroups(emailgroups);
				cservice.createCommittee(committee);
				committeeListSet = true;
			}
    	}
    	updateEmailGroup(emailgroup);
    	if (overallTest != null) {
        	System.out.println("OVERALL TEST " + overallTest.getTestcategory());
        	tservice.CalculateTestData(overallTest, committee);
    	}
	}
	public List<EmailGroup> EmailGroupList(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		List <EmailGroup> emails = new ArrayList <EmailGroup>();
		System.out.println("made it to service ");
		List<Long> Ids = egrepo.findGroupByOrderByDesc(startdateE, enddateE, committee_id);
		System.out.println("ids size " + Ids.size());
		for (int i = 0; i < Ids.size(); i++) {
			emails.add(egrepo.findbyIdandCommittee(Ids.get(i), committee_id));
		}
		System.out.println("emails size " + emails.size());
		return emails;
	}
}
