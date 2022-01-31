package com.coding.LojoFundrasing.Services;

import java.util.ArrayList;
import java.util.Date;
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
import com.coding.LojoFundrasing.Repos.EmailRepo;

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
	
	@Autowired
	private EmailRepo erepo;
	
	Date date = new Date();
	
	public EmailGroup createEmailGroup(EmailGroup emailgroup) {
		emailgroup.setCreatedAt(date);
		return egrepo.save(emailgroup);
	}
	public EmailGroup updateEmailGroup(EmailGroup emailgroup) {
		emailgroup.setUpdatedAt(date);
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
	public void findorCreateEmailGroup(Emails email, Long committee_id) {
		List<Emails> emails = new ArrayList<Emails>();
		//see if email group with same refcode2 already exists
		EmailGroup emailgroup = egrepo.findGroupByRefcode2(email.getEmailRefcode2(), committee_id);
		Boolean committeeSetList = false;
		Boolean emailgroupSetList = false;
		Committees committee = cservice.findbyId(committee_id);
		
		if (emailgroup == null) {
			//see if there are emails with same refcode2 and different refcode1
			emails = erepo.findByemailRefcode2andCommitteeDifferentRefcode1(email.getEmailRefcode1(), email.getEmailRefcode2(), committee_id);
			System.out.println("emails with diff refcode1 and same 2: " + emails);
			if (emails == null || emails.size() == 0) {
				return;
			}
			//other emails with same refcode2 exist but haven't been grouped yet
			else {
				emailgroup = new EmailGroup();
				
				//setting up email group name by using email name
				String emailname = email.getEmailName();
				System.out.println("name: " + emailname);
				System.out.println("name size: " + emailname.length());
				int index = emailname.indexOf("M.");
				System.out.println("index: " + index);
				String subString = emailname;
				if (index != -1) 
				{
					subString = emailname.substring(index, emailname.length());
					System.out.println("subString 1: " + subString);
					index = subString.indexOf("(1)");
					if (index != -1) 
					{
						subString = subString.substring(0, index);
					}
				}
				System.out.println("subString 2: " + subString);
				emailgroup.setEmailgroupName(subString);
				emailgroup.setCreatedAt(date);
				emailgroup.setGroup_creator(email.getEmail_uploader());
				createEmailGroup(emailgroup);
				
				emails = erepo.findByemailRefcode2andCommittee(email.getEmailRefcode2(), committee_id);
				emailgroup.setEmails(emails);
				
	        	while (committeeSetList == false) {
	    			if (committee.getEmailgroups() == null || committee.getEmailgroups().size() == 0) {
	    				emailgroup.setCommittee(committee);
	    				List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
	    				emailgroups.add(emailgroup);
	    				committee.setEmailgroups(emailgroups);
	    				cservice.updateCommittee(committee);
	    				committeeSetList = true;
	    			}
	    			else {
	    				emailgroup.setCommittee(committee);
	    				List<EmailGroup> emailgroups = committee.getEmailgroups();
	    				emailgroups.add(emailgroup);
	    				committee.setEmailgroups(emailgroups);
	    				cservice.updateCommittee(committee);
	    				committeeSetList = true;
	    			}
	        	}
			
	        	updateEmailGroup(emailgroup);
	        	for (int i = 0; i < emails.size(); i++) {
	        		emails.get(i).setEmailgroup(emailgroup);
	        		erepo.save(email);
	        	}
				return;
			}
		}
		else {
			emails = emailgroup.getEmails();
			emails.add(email);
			emailgroup.setEmails(emails);
			updateEmailGroup(emailgroup);
			email.setEmailgroup(emailgroup);
			return;
		}
	}
	public void getEmailGroupData(Long emailGroupId, Long committee_id) {
		System.out.println("email group id: " + emailGroupId);
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
		Double groupsum = egrepo.GroupRevenue(emailGroupId, committee_id);
		System.out.println(groupsum);
		Integer groupdonationcount = egrepo.GroupDonations(emailGroupId, committee_id);
		System.out.println(groupdonationcount);
		Double groupaverage = 0.0;
		System.out.println(groupaverage);
		//recurring
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
		String test = egrepo.findEmailTesting(emailGroupId, committee_id);
		System.out.println("testing " + test);
		Boolean testSet = true;
		if (test == null || test.isEmpty() || test == " ") {
			testSet = false;
		}
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
		updateEmailGroup(emailgroup);
		
		if (groupsum != 0 && groupsum != null) {
			groupaverage = (double) groupsum/groupdonationcount;
		}
		//calculate email performance + fundraising stats
		if (groupOpeners != null && groupOpeners != 0) {
			System.out.println("groupOpeners is 0 " + groupOpeners);
			groupclicksOpens = (double) groupClicks/groupOpeners;
			System.out.println("groupclicksOpens " + groupclicksOpens);
			groupdonationsOpens = (double) groupdonationcount/groupOpeners;
			System.out.println("groupdonationsOpens " + groupdonationsOpens);
		}
		if (groupClicks != null && groupClicks != 0) {
			System.out.println("groupClicks is NOT 0 " + groupClicks);
			System.out.println("groupdonationcount " + groupdonationcount);
			groupdonationsClicks = (double) groupdonationcount/groupClicks;
			System.out.println("groupdonationsClicks " + groupdonationsClicks);
		}
		
		//calculate email performance stats
		if (groupRecipients != null && groupRecipients != 0) {
			System.out.println("groupRecipients is " + groupRecipients);
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
				System.out.println("groupopenrate is " + groupopenRate);
			}
		}
		emailgroup.setGroupopenRate(groupopenRate);
		emailgroup.setGroupdonationsOpens(groupdonationsOpens);
		emailgroup.setGroupaverage(groupaverage);
		emailgroup.setGroupdonationsOpens(groupdonationsOpens);
		emailgroup.setGroupdonorsOpens(groupdonorsOpens);
		emailgroup.setGroupclickRate(groupclickRate);
		emailgroup.setGroupclicksOpens(groupclicksOpens);
		emailgroup.setGroupdonationsClicks(groupdonationsClicks);
		emailgroup.setGroupdonorsClicks(groupdonorsClicks);
		emailgroup.setGroupunsubscribeRate(groupunsubscribeRate);
		emailgroup.setGroupbounceRate(groupbounceRate);
		updateEmailGroup(emailgroup);
		
		//testing info
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
		while (testSet == false) {
			if (emailgroup.getTest() != null) {
				test = emailgroup.getTest().getTestname();
				System.out.println("test " + test);
				testSet = true;
			}
			System.out.println("test is null ");
			Emails emailA = erepo.findVariantA(emailgroup.getId(), committee_id);
			Emails emailB = null;
			if (emailA != null) {
				System.out.println("variant A is null ");
				emailB = erepo.findVariantB(emailgroup.getId(), emailA.getList(), committee_id);
			}
			if (emailA == null && emailB == null) {
				System.out.println("both variants are null ");
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
			else if (!emailA.getSender().equals(emailB.getSender())) {
				System.out.println("emailA: " + emailA.getEmailName());
				System.out.println("emailB: " + emailB.getEmailName());
				System.out.println("senders don't match " + emailA.getSender() + " " + emailB.getSender());
				test = "SENDER";
				variantA = emailA.getSender();
				variantB = emailB.getSender();
				emailgroup.setVariantA(variantA);
				emailgroup.setVariantB(variantB);
				for (int i = 0; i < emailgroup.getEmails().size(); i++) {
					Emails email = emailgroup.getEmails().get(i);
					String variant = email.getSender();
					email.setVariant(variant);
					email.setTesting(test);
					erepo.save(email);
				}
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
			else if (!emailA.getSubjectLine().equals(emailB.getSubjectLine())) {
				System.out.println("emailA: " + emailA.getEmailName());
				System.out.println("emailB: " + emailB.getEmailName());
				System.out.println("subjects don't match " + emailA.getSubjectLine() + " " + emailB.getSubjectLine());
				test = "SUBJECT";
				variantA = emailA.getSubjectLine();
				variantB = emailB.getSubjectLine();
				emailgroup.setVariantA(variantA);
				emailgroup.setVariantB(variantB);
				for (int i = 0; i < emailgroup.getEmails().size(); i++) {
					Emails email = emailgroup.getEmails().get(i);
					String variant = email.getSubjectLine();
					email.setVariant(variant);
					email.setTesting(test);
					erepo.save(email);
				}
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
			else {
				System.out.println("emailA: " + emailA.getEmailName());
				System.out.println("emailB: " + emailB.getEmailName());
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
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
		updateEmailGroup(emailgroup);
		if (emailgroup.getGroupTest() == null || emailgroup.getGroupTest().isEmpty() 
				|| emailgroup.getGroupTest() == " ") {
			overallTest = null;
			emailgroup.setTest(overallTest);
			updateEmailGroup(emailgroup);
		}
		if (emailgroup.getGroupTest() != null && !emailgroup.getGroupTest().isEmpty() 
				&& emailgroup.getGroupTest() != " ") {
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
