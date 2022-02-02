package com.coding.LojoFundrasing.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.Link;
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
		EmailGroup emailgroup = egrepo.findGroupByParentId(email.getParentid(), committee_id);
		Boolean committeeSetList = false;
		Boolean emailgroupSetList = false;
		Committees committee = cservice.findbyId(committee_id);
		
		if (emailgroup == null) {
			//get emails in group
			emails = erepo.findEmailsByParentId(email.getParentid(), committee_id);
			System.out.println("emails with same parent id: " + emails);
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
				emailgroup.setParentid(email.getParentid());
				emailgroup.setCreatedAt(date);
				emailgroup.setGroup_creator(email.getEmail_uploader());
				createEmailGroup(emailgroup);
				
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
		Integer groupdonationcount = egrepo.GroupDonations(emailGroupId, committee_id);
		Double groupaverage = 0.0;
		//recurring
		Integer groupRecurringDonationCount = egrepo.GroupRecurringDonations(emailGroupId, committee_id);
		Double groupRecurringRevenue = egrepo.GroupRecurringRevenue(emailGroupId, committee_id);
		
		//tandem
		Double tandemrevenue = egrepo.GroupTandemRevenue(emailGroupId, committee_id);
		Integer tandemdonations = egrepo.GroupTandemDonations(emailGroupId, committee_id);
		//total revenue
		Double totalrevenue = tandemrevenue + groupsum;
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
		
		//donations for count
		Integer donationsforcalculation = egrepo.GroupDonationsforCalculation(emailGroupId, committee_id);
		
		//email count
		Integer groupemailcount = egrepo.countEmailsinEmailGroup(emailgroup.getId(), committee_id);
		
		emailgroup.setGroupOpeners(groupOpeners);
		emailgroup.setGroupRecipients(groupRecipients);
		emailgroup.setGroupClicks(groupClicks);
		emailgroup.setGroupBounces(groupBounces);
		emailgroup.setGroupUnsubscribers(groupUnsubscribers);
		emailgroup.setGroupemailcount(groupemailcount);
		emailgroup.setTotalrevenue(totalrevenue);
		emailgroup.setDonationsforcalculation(donationsforcalculation);
		emailgroup.setTandemdonations(tandemdonations);
		emailgroup.setTandemrevenue(tandemrevenue);
		
		emailgroup.setGroupsum(groupsum);
		emailgroup.setGroupdonationcount(groupdonationcount);
		emailgroup.setGroupRecurringDonationCount(groupRecurringDonationCount);
		emailgroup.setGroupRecurringRevenue(groupRecurringRevenue);
		updateEmailGroup(emailgroup);
		
		if (donationsforcalculation != 0 && donationsforcalculation != null) {
			groupaverage = (double) totalrevenue/donationsforcalculation;
		}
		//calculate email performance + fundraising stats
		if (groupOpeners != null && groupOpeners != 0) {
			groupclicksOpens = (double) groupClicks/groupOpeners;
			groupdonationsOpens = (double) donationsforcalculation/groupOpeners;
		}
		if (groupClicks != null && groupClicks != 0) {
			groupdonationsClicks = (double) donationsforcalculation/groupClicks;
		}
		
		//calculate email performance stats
		if (groupRecipients != null && groupRecipients != 0) {
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
		getEmailGroupTesting(emailGroupId, committee_id);
	}
	
public void getEmailGroupTesting(Long emailGroupId, Long committee_id) {
	EmailGroup emailgroup = egrepo.findbyIdandCommittee(emailGroupId, committee_id);
	System.out.println(emailgroup.getEmailgroupName());
	Committees committee = cservice.findbyId(committee_id);
	
	String test = egrepo.findEmailTesting(emailGroupId, committee_id);
	Boolean testSet = false;
	
	//test
	test overallTest = null;
	
	//set lists
	Boolean testListSet = false;
	Boolean committeeListSet = false;
	
	//variants
	String variantA = null;
	String variantB = null;
	Boolean variantASet = false;
	Boolean variantBSet = false;
	
	if (test != null && !test.isEmpty() && test != " ") {
		if (emailgroup.getTest() != null) {
			if (!emailgroup.getTest().equals(test)) {
				System.out.println("TEST doesn't match OG " + test + " " + emailgroup.getGroupTest());
				emailgroup.setGroupTest(test);
				testSet = true;
			}
			else {
				emailgroup.setGroupTest(test);
				variantA = emailgroup.getVariantA();
				variantB = emailgroup.getVariantB();
				testSet = true;
			}
		}
		else {
			emailgroup.setGroupTest(test);
			testSet = true;
		}
	}
	else {
		if (emailgroup.getGroupTest() != null) {
			testSet = true;
			variantA = emailgroup.getVariantA();
			variantB = emailgroup.getVariantB();
		}
		else {
			testSet = false;
			variantA = emailgroup.getVariantA();
			variantB = emailgroup.getVariantB();
		}
	}
		//testing info
		if (variantA == null || variantA.isEmpty() || variantA == " " ) {
			variantASet = false;
		}
		else {
			variantASet = true;
		}
		if (variantB == null || variantB.isEmpty() || variantB == " " ) {
			variantBSet = false;
		}
		else {
			variantBSet = true;
		}
		while (testSet == false) {
			Emails emailA = erepo.findVariantA(emailgroup.getId(), committee_id);
			Emails emailB = null;
			if (emailA != null) {
				emailB = erepo.findVariantB(emailgroup.getId(), emailA.getList(), committee_id);
			}
			if (emailA == null || emailB == null) {
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
			else if (!emailA.getSender().equals(emailB.getSender())) {
				test = "SENDER";
				variantA = emailA.getSender();
				variantB = emailB.getSender();
				emailgroup.setVariantA(variantA);
				emailgroup.setVariantB(variantB);
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
			else if (!emailA.getSubjectLine().equals(emailB.getSubjectLine())) {
				test = "SUBJECT";
				variantA = emailA.getSubjectLine();
				variantB = emailB.getSubjectLine();
				emailgroup.setVariantA(variantA);
				emailgroup.setVariantB(variantB);
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
			else {
				variantASet = true;
				variantBSet = true;
				testSet = true;
			}
		}
		while (variantASet == false) {
				for (int i = 0; i < emailgroup.getEmails().size(); i++) {
					String variant = emailgroup.getEmails().get(i).getVariant();
					if (!test.toUpperCase().contains("SENDER") && !test.toUpperCase().contains("SUBJECT")) {
						//variant = variant.toUpperCase();
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
				variantASet = true;
				break;
		}
		while (variantBSet == false && variantA != null) {
				for (int i = 0; i < emailgroup.getEmails().size(); i++) {
					String variant = emailgroup.getEmails().get(i).getVariant();
					if (!test.toUpperCase().contains("SENDER") && !test.toUpperCase().contains("SUBJECT")) {
						//variant = variant.toUpperCase();
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
			test originaltest = null;
			if (emailgroup.getTest() != null) {
				originaltest = emailgroup.getTest();
				if (overallTest != originaltest) {
						System.out.println("OG test not matching new ");
						List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
						emailgroups.remove(emailgroup);
						originaltest.setEmailgroups(emailgroups);
						tservice.CalculateTestData(originaltest, committee);
				}
			}
			emailgroup.setTest(overallTest);
			updateEmailGroup(emailgroup);
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
    		for (int i = 0; i < emailgroup.getEmails().size(); i++) {
    			Emails email = emailgroup.getEmails().get(i);
    			String variant = email.getVariant();
    			if (test.contentEquals("SUBJECT")) {
    				variant = email.getSubjectLine();
    			}
    			else if (test.contentEquals("SENDER")) {
    				variant = email.getSender();
    			}
    			email.setVariant(variant);
    			email.setTesting(test);
    			erepo.save(email);
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
	public List<EmailGroup> EmailGroupExporter(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String type, String operator, String operand){
		if (type.contentEquals("Refcode 1")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.Refcode1Equals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.Refcode1Contains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.Refcode1isBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findAll();
				//return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Refcode 2")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.Refcode2Equals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.Refcode2Contains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.Refcode2isBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Title")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.nameEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.nameContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.nameisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Category")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.categoryEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.categoryContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.categoryisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Subject")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.subjectEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.subjectContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.subjectisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Sender")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.senderEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.senderContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.senderisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Testing")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.testingEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.testingContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.testingisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Link")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return egrepo.linkEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return egrepo.linkContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return egrepo.linkisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		return egrepo.findByOrderByDesc(startdateE, enddateE, committee_id);
	}
	/*public void SortEmailsandEmailGroupsId(String startdate, String enddate, Long committee_id){
		List<EmailGroup> emailgroups = egrepo.sortEmailgroupsbyRevenue(startdate, enddate, committee_id);
		List<Emails> emails = erepo.sortEmailswithoutGroupbyRevenue(startdate, enddate, committee_id);
		System.out.println("emails " + emails.size() );
		List<Long> ids = new ArrayList<Long>();
		if (emails == null || emails.size() == 0) {
			for (int i = 0; i <emailgroups.size(); i++) {
				System.out.println("emailgroup " + emailgroups.get(i).getEmailgroupName() + " " + emailgroups.get(i).getGroupsum());
			}
		}
		else {
			for (int i = 0; i <emailgroups.size(); i++) {
				Double max = emailgroups.get(i).getGroupsum();
				Long maxid = emailgroups.get(i).getId();
				System.out.println("max " + max );
				System.out.println("email donation sum " + emails.get(i).getEmaildonationsum() );
				if (emails.get(i).getEmaildonationsum() == null) {
					return;
				}
				if (emails.get(i).getEmaildonationsum() > max) {
					max = emails.get(i).getEmaildonationsum();
					ids.add(maxid);
				}
			}
		}
		for (int i = 0; i <emailgroups.size(); i++) {
			System.out.println("emailgroup " + ids.get(i));
		}
	}*/
}
