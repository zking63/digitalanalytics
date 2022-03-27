package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
//import com.coding.LojoFundrasing.Models.Data;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.DonorData;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.Link;
import com.coding.LojoFundrasing.Models.User;
//import com.coding.LojoFundrasing.Repos.DataRepo;
import com.coding.LojoFundrasing.Repos.DonationRepo;
import com.coding.LojoFundrasing.Repos.EmailRepo;
import com.coding.LojoFundrasing.Repos.EmailRepositoryCustom;

@Service
public class EmailService {
	@PersistenceContext
	private EntityManager entitymanager;
	@Autowired
	private EmailRepo erepo;
	
	@Autowired
	private LinkService lservice;
	
	/*@Autowired
	private DataRepo datarepo;*/
	
	@Autowired
	private DonationRepo drepo;
	
	@Autowired
	private EmailGroupService egservice;
	
	@Autowired
	private CommitteeService cservice;
	
	@Autowired
	private EmailRepositoryCustom erc;
	
	@Autowired
	private QueryService queryservice;
	
	public Emails createEmail(Emails email) {
		return erepo.save(email);
	}
	
	public Emails updateEmail(Emails email) {
		return erepo.save(email);
	}
	
	public List<Emails> allEmails(){
		return erepo.findAll();
	}

	public List<Emails> listemails() {
		return erepo.findsome();
	}
	
	public Emails findEmailbyId(long id) {
		return erepo.findById(id).orElse(null);
	}
	
	public List<Emails> findEmailswithoutGroup(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findemailswithoutGroup(startdateE, enddateE, committee_id);
	}
	
	public Emails findEmailbyRefcode(String emailRefcode) {
		return erepo.findByemailRefcode1(emailRefcode);
	}
	public Emails findEmailbyRefcodeandCommittee(String emailRefcode, String emailRefcode2, Committees committee) {
		Long committee_id = committee.getId();
		return erepo.findByemailRefcodeandCommittee(emailRefcode, emailRefcode2, committee_id);
	}
	public Emails findEmailbyOneRefcodeandCommittee(String emailRefcode, Committees committee) {
		Long committee_id = committee.getId();
		return erepo.findByemailOneRefcodeandCommittee(emailRefcode, committee_id);
	}
	public Emails findEmailbyRefcodeTWOandCommittee(String emailRefcode2, Committees committee) {
		Long committee_id = committee.getId();
		return erepo.findByemailRefcodeTWOandCommittee(emailRefcode2, committee_id);
	}
	public void delete(long id) {
		erepo.deleteById(id);
	}
	public List<Donation> getEmailDonations(Emails email){
		Long id = email.getId();
		return drepo.findByemailDonation(id);
	}
	public Emails setEmailThroughDonation(String refcode, String refcode2, Committees committee, User uploader) throws ParseException {
		Emails email = null;
		Boolean refcodesFiled = false;
		Boolean committeeSetList = false;
		
		while (refcodesFiled == false) {
			if (refcode2 == null || refcode2.isEmpty() || refcode2 == " ") {
				if (refcode == null || refcode.isEmpty() || refcode == " ") {
					System.out.println("no refcodes");
					//make a find by name/date with null refcodes
					refcodesFiled = true;
				}
				else {
					System.out.println("refcode2 == null && refcode != null");
					email = findEmailbyOneRefcodeandCommittee(refcode, committee);
					refcodesFiled = true;
				}
			}
			else {
				if (refcode == null || refcode.isEmpty() || refcode == " ") {
					System.out.println("refcode2 != null && refcode == null");
					email = findEmailbyRefcodeTWOandCommittee(refcode2, committee);
					refcodesFiled = true;
				}
				else {
					System.out.println("refcode2 != null && refcode != null");
					email = findEmailbyRefcodeandCommittee(refcode, refcode2, committee);
					refcodesFiled = true;
				}
			}
		}
		if (email == null) {
    		String undate1 = "0001-01-01 01:01";
    		Date undate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(undate1);
    		email = new Emails();
        	email.setEmailName(null);
        	System.out.println("TEMP DATE: " + undate);
        	email.setEmaildate(undate);
        	email.setEmailRefcode1(refcode);
        	email.setEmailRefcode2(refcode2);
        	email.setBounces(null);
        	email.setClicks(null);
        	email.setOpeners(null);
        	email.setRecipients(null);
        	email.setUnsubscribers(null);
        	email.setExcludedList(null);
        	email.setList(null);
        	email.setSender(null);
        	email.setSubjectLine(null);
        	email.setEmailCategory(null);
        	email.setTesting(null);
        	email.setVariant(null);
        	email.setLink(null);
        	email.setEmail_uploader(uploader);
        	createEmail(email);
        	while (committeeSetList == false) {
    			if (committee.getEmails() == null || committee.getEmails().size() == 0) {
    				email.setCommittee(committee);
    				List<Emails> emailCommittee = new ArrayList<Emails>();
    				emailCommittee.add(email);
    				committee.setEmails(emailCommittee);
    				cservice.updateCommittee(committee);
    				committeeSetList = true;
    			}
    			else {
    				email.setCommittee(committee);
    				List<Emails> emailCommittee = committee.getEmails();
    				emailCommittee.add(email);
    				committee.setEmails(emailCommittee);
    				cservice.updateCommittee(committee);
    				committeeSetList = true;
    			}
        	}
        	updateEmail(email);
        	String tempname = "ID: " + email.getId();
        	email.setEmailName(tempname);
        	System.out.println("TEMP NAME: " + tempname);
        	updateEmail(email);
        	return email;
		}
		else {
        	System.out.println("found email: " + email.getId() + ", " + email.getEmailName());
			return email;
		}
	}
	
	public void setUpEmailsfromUpload(String recipientList, String excludedList, Long openers, Long bounces, Long unsubscribers, 
			Long clicks, Long recipients, User uploader, String nameValue, String refcode, String refcode2,  
			Date date, Committees committee, String sender, String subject, String category, 
			String content, Integer tandemdonations, Double tandemrev, String parentid,
			String testing, String variant, String link, Double revenue, Double recurringrev, 
			Integer donations, Integer recurringdonations, Date dateforgroup, Integer rowNumber) {
		System.out.println("email set up found");
		//System.out.println("*****NAME " + nameValue);
		//System.out.println("*****rev " + revenue);
		System.out.println("*****variant " + variant);
		System.out.println("*****test " + testing);
		System.out.println("*****category " + category);
		Emails email = null;
		Boolean refcodesFiled = false;
		Boolean committeeSetList = false;
		Boolean variantSet = false;
		List<Emails> emails = null;
		String test = null;
		Boolean LinkSetList = false;
		Link overalllink = null;
		String oldparentid = null;
		
		if (tandemrev == null) {
			tandemrev = 0.0;
		}
		
		if (tandemdonations == null) {
			tandemdonations = 0;
		}
		if (revenue == null) {
			revenue = 0.0;
		}
		
		if (donations == null) {
			donations = 0;
		}
		if (recurringrev == null) {
			recurringrev = 0.0;
		}
		
		if (recurringdonations == null) {
			recurringdonations = 0;
		}
		//
		if (openers == null) {
			openers = (long) 0;
		}
		if (clicks == null) {
			clicks = (long) 0;
		}
		if (recipients == null) {
			recipients = (long) 0;
		}
		if (bounces == null) {
			bounces = (long) 0;
		}
		if (unsubscribers == null) {
			unsubscribers = (long) 0;
		}
		if (sender != null) {
			sender = sender.trim();
		}
		if (subject != null) {
			subject = subject.trim();
		}
		if (variant != null) {
			variant = variant.trim();
		}
		if (nameValue != null) {
			String name = nameValue.toUpperCase();
			if(nameValue.contains("- FR -") || name.contains("_FR_") || name.contains("FUND")
					|| name.contains("-FR -") || name.contains("- FR-") || name.contains(" FR")) {
				category = "fundraiser";
			}
			else if(name.contains("- SU -") || name.contains("_SU_") || name.contains("SURVEY")
					|| name.contains("-SU -") || name.contains("- SU-") || name.contains(" SU")) {
				category = "survey";
			}
			if(name.contains("- PET -") || name.contains("_PET_") || name.contains("PETITION")
					|| name.contains("-PET -") || name.contains("- PET-") || name.contains(" PET")) {
				category = "petition";
			}
		}
		System.out.println("*****category " + category);
		if (category != null) {
			category = category.toUpperCase();
			//System.out.println("*****category " + category);
			if (category.contains("PET")) {
				category = "petition";
				//System.out.println("*****category " + category);
			}
			if (category.contains("SU")) {
				category = "survey";
				//System.out.println("*****category " + category);
			}
			if (category.contains("FUND")) {
				category = "fundraiser";
				//System.out.println("*****category " + category);
			}
		}
		System.out.println("*****category 2 " + category);
		/*if ((category == null || category.isEmpty() 
				|| category == "" || category == " ") && nameValue != null) {
			if(nameValue.contains("- FR -") || nameValue.contains("_FR_") 
					|| nameValue.contains("-FR -") || nameValue.contains("- FR-") || nameValue.contains(" FR")) {
				category = "fundraiser";
			}
			else if(nameValue.contains("- SU -") || nameValue.contains("_SU_") 
					|| nameValue.contains("-SU -") || nameValue.contains("- SU-") || nameValue.contains(" SU")) {
				category = "survey";
			}
			else if(nameValue.contains("- PET -") || nameValue.contains("_PET_") 
					|| nameValue.contains("-PET -") || nameValue.contains("- PET-") || nameValue.contains(" PET")) {
				category = "petition";
			}
		}*/
		
		while (refcodesFiled == false) {
			if (refcode2 == null || refcode2.isEmpty() || refcode2 == " ") {
				if (refcode == null || refcode.isEmpty() || refcode == " ") {
					System.out.println("no refcodes");
					//make a find by name/date with null refcodes
					refcodesFiled = true;
				}
				else {
					System.out.println("refcode2 == null && refcode != null");
					email = findEmailbyOneRefcodeandCommittee(refcode, committee);
					refcodesFiled = true;
				}
			}
			else {
				if (refcode == null || refcode.isEmpty() || refcode == " ") {
					System.out.println("refcode2 != null && refcode == null");
					email = findEmailbyRefcodeTWOandCommittee(refcode2, committee);
					refcodesFiled = true;
				}
				else {
					System.out.println("refcode2 != null && refcode != null");
					email = findEmailbyRefcodeandCommittee(refcode, refcode2, committee);
					refcodesFiled = true;
				}
			}
		}
		//System.out.println("                                          TESTING: " + testing);
		while (variantSet == false) {
			if (testing == null || testing.isEmpty() || testing == " ") {
				variantSet = true;
			}
			else {
				test = testing.toUpperCase();
				if (test.contains("SENDER")) {
					System.out.println("                                          TESTING = sender: " + testing);
					variant = sender;
					variantSet = true;
				}
				else if (test.contains("SUBJECT")) {
					System.out.println("                                          TESTING = subject: " + testing);
					variant = subject;
					variantSet = true;
				}
				else {
					System.out.println("                                          TESTING = other: " + testing);
					variant = variant;
					variantSet = true;
				}
			}
		}
		if (link != null && !link.isEmpty() && link != " ") {
			overalllink = lservice.findAndSetUpLinkfromUpload(link, committee);
		}
		if (email == null) {
			if (nameValue == null || nameValue.isEmpty() || date == null) {
				rowNumber = rowNumber +1;
				//System.out.println("*****NAME " + nameValue);
				//System.out.println("*****DATE " + date);
				//System.out.println("*****NOTHING IN THIS ROW " + rowNumber);
				return;
			}
			System.out.println("*****email not found ");
        	email = new Emails();
        	email.setEmailName(nameValue);
        	email.setEmaildate(date);
        	email.setEmailRefcode1(refcode);
        	email.setEmailRefcode2(refcode2);
        	email.setBounces(bounces);
        	email.setClicks(clicks);
        	email.setOpeners(openers);
        	email.setRecipients(recipients);
        	email.setUnsubscribers(unsubscribers);
        	email.setExcludedList(excludedList);
        	email.setList(recipientList);
        	email.setEmail_uploader(uploader);
        	email.setSender(sender);
        	email.setSubjectLine(subject);
        	email.setEmailCategory(category);
        	email.setTesting(test);
        	email.setVariant(variant);
        	email.setLink(link);
        	email.setOveralllink(overalllink);
        	email.setRecurringDonationCount(recurringdonations);
        	email.setRecurringRevenue(recurringrev);
        	email.setEmaildonationsum(revenue);
        	email.setEmaildonationcount(donations);
        	email.setParentid(parentid);
        	email.setTandemdonations(tandemdonations);
        	email.setTandemrevenue(tandemrev);
        	email.setContent(content);
        	createEmail(email);
        	while (committeeSetList == false) {
    			if (committee.getBigtest() == null || committee.getEmails().size() == 0) {
    				email.setCommittee(committee);
    				List<Emails> emailCommittee = new ArrayList<Emails>();
    				emailCommittee.add(email);
    				committee.setEmails(emailCommittee);
    				cservice.updateCommittee(committee);
    				committeeSetList = true;
    			}
    			else {
    				email.setCommittee(committee);
    				List<Emails> emailCommittee = committee.getEmails();
    				emailCommittee.add(email);
    				committee.setEmails(emailCommittee);
    				cservice.updateCommittee(committee);
    				committeeSetList = true;
    			}
        	}
		}
		else if (email !=  null) {
			Link originalLink = null;
			if (email.getOveralllink() != null) {
				originalLink = email.getOveralllink();
				if (overalllink != null) {
					if (overalllink != originalLink) {
						//System.out.println("OG link not matching new " + originalLink.getLinkname() + " " + overalllink.getLinkname());
						List<Emails> OGemailLink = originalLink.getEmails();
						OGemailLink.remove(email);
						originalLink.setEmails(OGemailLink);
						email.setOveralllink(overalllink);
						updateEmail(email);
						lservice.CalculateLinkData (originalLink, committee.getId());
					}
				}
				else {
					link = email.getLink();
					overalllink = email.getOveralllink();
				}
			}
			String originaltesting = null;
			if (email.getTesting() != null) {
				System.out.println("email og testing " + email.getTesting() );
				originaltesting = email.getTesting();
				if (test != null) {
					if (!test.contentEquals(originaltesting)) {
						System.out.println("testing not matching new " );
						test = test;
					}
				}
				else {
					test = originaltesting;
				}
			}
			String originalvariant = null;
			if (email.getVariant() != null) {
				System.out.println("email og variant " + email.getVariant());
				originalvariant = email.getVariant();
				if (variant != null) {
					if (!variant.equals(originalvariant)) {
						System.out.println("testing not matching new " );
						variant = variant;
					}
				}
				else {
					variant = originalvariant;
				}
			}
			String originalname = null;
			if (email.getEmailName() != null) {
				originalname = email.getEmailName();
				if (nameValue == null || nameValue == " " || nameValue.isEmpty()) {
					nameValue = originalname;
				}
			}
			if (email.getEmaildate() != null) {
				if (date == null) {
					date = email.getEmaildate();
				}
			}
			if (email.getList() != null) {
				if (recipientList == null || recipientList == " " || recipientList.isEmpty()) {
					recipientList = email.getList();
				}
			}
			if (email.getSender() != null) {
				if (sender == null || sender == " " || sender.isEmpty()) {
					sender = email.getSender();
				}
			}
			if (email.getSubjectLine() != null) {
					if (subject == null || subject == " " || subject.isEmpty()) {
						subject = email.getSubjectLine();
					}
			}
			if (email.getEmailCategory() != null) {
					if (category == null || category == " " || category.isEmpty()) {
						category = email.getEmailCategory() ;
					}
			}
			if (email.getParentid() != null) {
					if (parentid == null || parentid == " " || parentid.isEmpty()) {
						parentid = email.getParentid();
					}
					else if (!email.getParentid().contentEquals(parentid)) {
						oldparentid = email.getParentid();
					}
			}
			if (email.getContent() != null) {
					if (content == null || content == " " || content.isEmpty()) {
						content = email.getContent();
					}
			}
			//
			if (email.getClicks() != null && email.getClicks() != 0) {
				if (clicks == null || clicks == 0) {
					clicks = email.getClicks();
				}
			}
			if (email.getOpeners() != null && email.getOpeners() != 0) {
				if (openers == null || openers == 0) {
					openers = email.getOpeners();
				}
			}
			if (email.getRecipients() != null && email.getRecipients() != 0) {
				if (recipients == null || recipients == 0) {
					recipients = email.getRecipients();
				}
			}
			if (email.getUnsubscribers() != null && email.getUnsubscribers() != 0) {
				if (unsubscribers == null ||unsubscribers == 0) {
					unsubscribers = email.getUnsubscribers();
				}
			}
			if (email.getBounces() != null && email.getBounces() != 0) {
				if (bounces == null || bounces == 0) {
					bounces = email.getBounces();
				}
			}
			if (email.getEmaildonationcount() != null && email.getEmaildonationcount() != 0) {
				if (donations == null || donations == 0) {
					donations = email.getEmaildonationcount();
				}
			}
			if (email.getEmaildonationsum() != null && email.getEmaildonationsum() != 0.0) {
				if (revenue == null || revenue == 0.0) {
					revenue = email.getEmaildonationsum();
				}
			}
			if (email.getTandemdonations() != null && email.getTandemdonations() != 0) {
				if (tandemdonations == null ||tandemdonations == 0) {
					tandemdonations = email.getTandemdonations();
				}
			}
			if (email.getTandemrevenue() != null && email.getTandemrevenue() != 0.0) {
				if (tandemrev == null || tandemrev == 0.0) {
					tandemrev = email.getTandemrevenue();
				}
			}
			if (email.getRecurringDonationCount() != null && email.getRecurringDonationCount() != 0) {
				if (recurringdonations == null || recurringdonations == 0) {
					recurringdonations = email.getRecurringDonationCount();
				}
			}
			if (email.getRecurringRevenue() != null && email.getRecurringRevenue() != 0.0) {
				if (recurringrev == null || recurringrev == 0.0) {
					recurringrev = email.getRecurringRevenue();
				}
			}
        	System.out.println("found email: " + email.getId() + ", " + email.getEmailName());
        	email.setEmailName(nameValue);
        	email.setEmaildate(date);
        	email.setEmailRefcode1(refcode);
        	email.setEmailRefcode2(refcode2);
        	email.setBounces(bounces);
        	email.setClicks(clicks);
        	email.setOpeners(openers);
        	email.setRecipients(recipients);
        	email.setUnsubscribers(unsubscribers);
        	email.setExcludedList(excludedList);
        	email.setList(recipientList);
        	email.setEmail_uploader(uploader);
        	email.setSender(sender);
        	email.setSubjectLine(subject);
        	email.setEmailCategory(category);
        	email.setTesting(test);
        	System.out.println("variant set: " + variant);
        	email.setVariant(variant);
        	email.setLink(link);
        	email.setContent(content);
        	email.setParentid(parentid);
        	email.setTandemdonations(tandemdonations);
        	email.setTandemrevenue(tandemrev);
        	email.setOveralllink(overalllink);
        	email.setRecurringDonationCount(recurringdonations);
        	email.setRecurringRevenue(recurringrev);
        	email.setEmaildonationsum(revenue);
        	email.setEmaildonationcount(donations);
        	updateEmail(email);
		}
    	while (LinkSetList == false) {
    		if (overalllink == null) {
    			LinkSetList = true;
    		}
    		else {
    			if (overalllink.getEmails() == null || overalllink.getEmails().size() == 0) {
    				List<Emails> emailsLink = new ArrayList<Emails>();
    				emailsLink.add(email);
    				overalllink.setEmails(emailsLink);
    				lservice.updateLink(overalllink);
    				LinkSetList = true;
    			}
    			else {
    				List<Emails> emailsLink = new ArrayList<Emails>();
    				emailsLink.add(email);
    				overalllink.setEmails(emailsLink);
    				lservice.updateLink(overalllink);
    				LinkSetList = true;
    			}
    		}
    	}
    	System.out.println("*****parent id " + parentid + " email parent id " + email.getParentid());
    	if (email.getEmailgroup() == null || parentid.isEmpty() || email.getParentid() == null 
    			|| email.getParentid() == "" || email.getParentid() == " " || oldparentid != null) {
    		System.out.println("*****email group set up ");
    		egservice.findorCreateEmailGroup(email, committee.getId(), dateforgroup, oldparentid);
    	}
		CalculateEmailData(email, committee.getId());
		System.out.println("Id: " + email.getId() + " Email: " + email.getEmailName());
		return;
	}
	
	public void CalculateEmailData(Emails email, Long committee_id) {
		Long id = email.getId();
		Double esum = email.getEmaildonationsum();
		Double eaverage = 0.00;
		Integer donationscount = email.getEmaildonationcount();
		Integer donorscount = 0;
		Integer recurringDonorCount = 0;
		Integer recurringDonationCount = email.getRecurringDonationCount();
		Double recurringRevenue = email.getRecurringRevenue();
		Double unsubscribeRate = 0.00;
		Double clickRate = 0.00;
		Double openRate = 0.00;
		Double bounceRate = 0.00;
		Double donationsOpens = 0.00;
		Double donationsClicks = 0.00;
		Double donorsOpens = 0.00;
		Double donorsClicks = 0.00;
		Double clicksOpens = 0.00;
		Integer tandemdonations = email.getTandemdonations();
		Integer donationsforcalculation = donationscount;
		Double totalrevenue = email.getTandemrevenue() + email.getEmaildonationsum();

				if (tandemdonations > donationscount) {
					donationsforcalculation = tandemdonations; 
				}
				if (donationsforcalculation != 0) {
					eaverage = totalrevenue/donationsforcalculation;
				}
				
			//aggregate functions
				Double unsubs = (double) email.getUnsubscribers();
				Double receps = (double) email.getRecipients();
				Double clicks = (double) email.getClicks();
				Double opens = (double) email.getOpeners();
				Double bounces = (double) email.getBounces();
			if (email.getRecipients() != null && email.getRecipients() != 0) {
				//variables for aggregate functions
				//functions
				openRate = opens/receps;
				clickRate = clicks/receps;
				bounceRate = bounces/receps;
			}
			if (email.getClicks() != null && email.getClicks() != 0) {
				donationsClicks = donationsforcalculation/clicks;
				donorsClicks = donorscount/clicks;
			}
			if (email.getOpeners() != null && email.getOpeners() != 0) {
				unsubscribeRate = unsubs/opens;
				clicksOpens = clicks/opens;
				donationsOpens = donationsforcalculation/opens;
				donorsOpens = donorscount/opens;
			}
			email.setEmaildonationaverage(eaverage);
			email.setEmaildonationsum(esum);
			email.setRecurringDonationCount(recurringDonationCount);
			email.setRecurringDonorCount(recurringDonorCount);
			email.setRecurringRevenue(recurringRevenue);
			email.setEmaildonationcount(donationscount);
			email.setEmaildonorcount(donorscount);
			email.setEmailunsubscribeRate(unsubscribeRate);
			System.out.println("unsubscribeRate set: " + email.getEmailunsubscribeRate());
			email.setEmailclickRate(clickRate);
			email.setEmailopenRate(openRate);
			email.setBounceRate(bounceRate);
			email.setEmaildonationsOpens(donationsOpens);
			email.setEmaildonationsClicks(donationsClicks);
			email.setEmailclicksOpens(clicksOpens);
			email.setEmaildonorsOpens(donorsOpens);
			email.setEmaildonorsClicks(donorsClicks);
			email.setTotalrevenue(totalrevenue);
			email.setDonationsforcalculation(donationsforcalculation);
			erepo.save(email);
    		if (email.getEmailgroup() != null) {
    			egservice.getEmailGroupData(email.getEmailgroup().getId(), committee_id);
    		}
    		if (email.getOveralllink() != null) {
    			lservice.CalculateLinkData (email.getOveralllink(), committee_id);
    		}
	}
	
	public List<Emails> PredicateCreator(String sort, String direction, Integer field, List<String> categories, List<String> operandsList, List<Predicate> predicates, String startdateD, String enddateD, 
			Committees committee, String type, String operator, List<String> operands, String operand) throws ParseException, IOException {
		System.out.println("pred create");
		System.out.println("operand: " +operand);
		System.out.println("operator in pred: " +operator);
		System.out.println("operands size in pred first " + operands.size());
		
        CriteriaBuilder cb = entitymanager.getCriteriaBuilder();
        CriteriaQuery<Emails> query = cb.createQuery(Emails.class);
        Root<Emails> emails = query.from(Emails.class);
        emails.alias("emails");
        Path<String> groupPath = emails.get("emailName");
        
		//date/committee preds
    	enddateD = enddateD + " 23:59:59";
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startdateD);
		Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(enddateD);
        Predicate committeePredicate = cb.equal(emails.get("committee"), committee);
        Predicate datePredicate =  cb.between(emails.<Date>get("Emaildate"), start, end);
        
        if (type.contentEquals("Refcode 1")) {
        	System.out.println("emailRefcode1");
        	groupPath = emails.get("emailRefcode1");
        }
        if (type.contentEquals("Refcode 2")) {
        	System.out.println("emailRefcode2");
        	groupPath = emails.get("emailRefcode2");
        }
        if (type.contentEquals("Title")) {
        	System.out.println("emailgroupName");
        	groupPath = emails.get("emailName");
        }
        if (type.contentEquals("Category")) {
        	System.out.println("emailCategory");
        	groupPath = emails.get("emailCategory");
        }
        if (type.contentEquals("Subject")) {
        	System.out.println("subjectLine");
        	groupPath = emails.get("subjectLine");
        }
        if (type.contentEquals("Sender")) {
        	System.out.println("sender");
        	groupPath = emails.get("sender");
        }
        if (type.contentEquals("Testing")) {
        	System.out.println("testing");
        	groupPath = emails.get("testing");
        }
        if (type.contentEquals("Link")) {
        	System.out.println("link");
        	groupPath = emails.get("link");
        }
        if (type.contentEquals("Content")) {
        	System.out.println("content");
        	groupPath = emails.get("content");
        }
        if (type.contentEquals("Select")) {
        	System.out.println("type is Select");
        	groupPath = emails.get("emailName");
        }
        
        if (operator.contentEquals("Select") || type.contentEquals("Select") 
        		|| operator.contentEquals("Is blank") ) {
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator blank " + operator);
				predicates.add(cb.isNull(groupPath));
				System.out.println("preds   " + predicates.size());
			}
        	System.out.println("select");
        	predicates.add(datePredicate);
        	predicates.add(committeePredicate);
        	

			if (categories != null && categories.size() > 0 && categories.size() < 5) {
				groupPath = emails.get("emailCategory");
				Predicate categoryPredicate = cb.equal(groupPath, categories.get(0));
		        List<Predicate> categorypreds = new ArrayList<>();
				if (categories.size() > 1) {
					for (int i = 0; i <categories.size(); i++) {
						categoryPredicate = cb.equal(groupPath, categories.get(i));
						if (categories.get(i).contentEquals("Other")) {
							List<Predicate> otherPreds = new ArrayList<>();
							categoryPredicate = cb.notEqual(groupPath, "Fundraiser");
							otherPreds.add(categoryPredicate);
							categoryPredicate = cb.notEqual(groupPath, "Petition");
							otherPreds.add(categoryPredicate);
							categoryPredicate = cb.notEqual(groupPath, "Survey");
							otherPreds.add(categoryPredicate);	
							Predicate other
							  = cb.and(otherPreds.toArray(new Predicate[categorypreds.size()]));
							categoryPredicate = cb.and(other);
						}
						categorypreds.add(categoryPredicate);
						System.out.println("CONTAINS OR");
						if (i == categories.size() -1) {
							Predicate orPredicate
								  = cb.or(categorypreds.toArray(new Predicate[categorypreds.size()]));
								
					
							 Predicate finalP = cb.and(orPredicate);

								predicates.add(finalP);
						}
					}
				}
				else {
					if (categories.get(0).contentEquals("Other")) {
						List<Predicate> otherPreds = new ArrayList<>();
						categoryPredicate = cb.notEqual(groupPath, "Fundraiser");
						otherPreds.add(categoryPredicate);
						categoryPredicate = cb.notEqual(groupPath, "Petition");
						otherPreds.add(categoryPredicate);
						categoryPredicate = cb.notEqual(groupPath, "Survey");
						otherPreds.add(categoryPredicate);	
						Predicate other
						  = cb.and(otherPreds.toArray(new Predicate[categorypreds.size()]));
						categoryPredicate= cb.and(other);
					}
					predicates.add(categoryPredicate);
					System.out.println("preds after else  " + predicates.size());
				}
			}
			System.out.println("preds  " + predicates.size());
			List<Emails> emaillist = erc.PredPlugin(predicates);
        	return emaillist;
        }
        
        if (predicates.size() == 0 && operand != null 
        		&& !operand.isEmpty() && operands.size() == 0) {
        	System.out.println("predicates:   " + predicates.size());
        	System.out.println("operands:   " + operands.size());
        	System.out.println("operand:   " + operand);
        	queryservice.GetOperands(sort, direction, field, categories, operandsList, predicates, startdateD, enddateD, committee, type, operator, operand);
        }
        
		if (operands.size() > 0 && !operands.get(0).isEmpty() 
				&& !operands.get(0).contentEquals(" ")) {
	    	System.out.println("type:   " + type);
	    	System.out.println("operator:   " + operator);
	    	
	    	System.out.println("operands size:   " + operands.size());

	    
	       
	        List<Predicate> finalPredicates = new ArrayList<>();
	        List<Predicate> temppreds = new ArrayList<>();
	        Predicate finalP = cb.equal(emails.get("committee"), committee);
			Predicate orPredicate = cb.or(predicates.toArray(new Predicate[predicates.size()]));
			// String finaloperand = "%" + operands.get(0) + "%";
	        for (int i = 0; i < operands.size(); i++) {
	        	operandsList.add(operands.get(i));
	        	String finaloperand = operands.get(i);
	        	Predicate temppredicate = cb.equal(groupPath, finaloperand);
	        	/*if (i > 0) {
	        		finaloperand = finaloperand + " && " + "%" + operands.get(i) + "%";
	        	}*/
	        	System.out.println("finaloperand" + finaloperand +".");
	        	System.out.println("operands size in pred  " + operands.size());
	        	//System.out.println("emailPath  " + emailPath);
				if (operator.contentEquals("Equals")) {
					System.out.println("operator " + operator);
					temppredicate = cb.equal(groupPath, finaloperand);
					System.out.println("preds   " + predicates.size());
					if (operands.size() > 1) {
						temppreds.add(temppredicate);
						System.out.println("CONTAINS OR");
						if (i == operands.size() -1) {
								orPredicate
								  = cb.or(temppreds.toArray(new Predicate[temppreds.size()]));
								
								//finalPredicates.add(equalPredicate);
								finalP = cb.and(orPredicate);
								//predicates = finalPredicates;
								predicates.add(finalP);
						}
					}
					else {
						predicates.add(temppredicate);
						System.out.println("preds after else  " + predicates.size());
					}
				}
				else if (operator.contentEquals("Contains")) {
					System.out.println("operator contain " + operator);
					finaloperand = "%" + operands.get(i) + "%";
					temppredicate = cb.like(groupPath, finaloperand);
					System.out.println("temp pred alias " + temppredicate.getAlias());
					System.out.println("temp pred expression " + temppredicate.getExpressions());
					System.out.println("final op " + finaloperand);
					if (operands.size() > 1) {
						temppreds.add(temppredicate);
						System.out.println("CONTAINS OR");
						if (i == operands.size() -1) {
							System.out.println("last op filed " + finaloperand);
							System.out.println("temp size " + temppreds.size());
								orPredicate
								  = cb.or(temppreds.toArray(new Predicate[temppreds.size()]));
								
								//finalPredicates.add(equalPredicate);
								finalP = cb.and(orPredicate);
								//predicates = finalPredicates;
								predicates.add(finalP);
						}
					}
					else {
						predicates.add(temppredicate);
					}
					System.out.println("preds   " + predicates.size());
				}
				else {
					System.out.println("operator else " + operator);
					System.out.println("preds   " + predicates.size());
				}
			}
			if (operand != null && !operand.contentEquals(" ") && !operand.isEmpty()) { 
				System.out.println("preds before reload operands " + predicates.size());
				System.out.println("operand:" + operand +".");
				queryservice.GetOperands(sort, direction, field, categories, operandsList, predicates, startdateD, enddateD, committee, type, operator, operand);
				return null;
			}
			else {
				operand = null;
				queryservice.GetOperands(sort, direction, field, categories, operandsList, predicates, startdateD, enddateD, committee, type, operator, operand);
				return null;
			}
		}
		else {
			//categoryPredicate
			if (categories != null && categories.size() > 0 && categories.size() < 5) {
				groupPath = emails.get("emailCategory");
				Predicate categoryPredicate = cb.equal(groupPath, categories.get(0));
		        List<Predicate> categorypreds = new ArrayList<>();
				if (categories.size() > 1) {
					for (int i = 0; i <categories.size(); i++) {
						categoryPredicate = cb.equal(groupPath, categories.get(i));
						if (categories.get(i).contentEquals("Other")) {
							List<Predicate> otherPreds = new ArrayList<>();
							categoryPredicate = cb.notEqual(groupPath, "Fundraiser");
							otherPreds.add(categoryPredicate);
							categoryPredicate = cb.notEqual(groupPath, "Petition");
							otherPreds.add(categoryPredicate);
							categoryPredicate = cb.notEqual(groupPath, "Survey");
							otherPreds.add(categoryPredicate);	
							Predicate other
							  = cb.and(otherPreds.toArray(new Predicate[categorypreds.size()]));
							categoryPredicate = cb.and(other);
						}
						categorypreds.add(categoryPredicate);
						System.out.println("CONTAINS OR");
						if (i == categories.size() -1) {
							Predicate orPredicate
								  = cb.or(categorypreds.toArray(new Predicate[categorypreds.size()]));
								
								//finalPredicates.add(equalPredicate);
							 Predicate finalP = cb.and(orPredicate);
								//predicates = finalPredicates;
								predicates.add(finalP);
						}
					}
				}
				else {
					if (categories.get(0).contentEquals("Other")) {
						List<Predicate> otherPreds = new ArrayList<>();
						categoryPredicate = cb.notEqual(groupPath, "Fundraiser");
						otherPreds.add(categoryPredicate);
						categoryPredicate = cb.notEqual(groupPath, "Petition");
						otherPreds.add(categoryPredicate);
						categoryPredicate = cb.notEqual(groupPath, "Survey");
						otherPreds.add(categoryPredicate);	
						Predicate other
						  = cb.and(otherPreds.toArray(new Predicate[categorypreds.size()]));
						categoryPredicate= cb.and(other);
					}
					predicates.add(categoryPredicate);
					System.out.println("preds after else  " + predicates.size());
				}
			}
			predicates.add(committeePredicate);
			predicates.add(datePredicate);
			System.out.println("********FINAL PREDS:   " + predicates.size());
			List<Emails> emaillist = erc.PredPlugin(predicates);
			System.out.println("Emails size in custom " + emaillist.size());
			System.out.println("operands list " + operandsList);
			return emaillist;
		}
	}

	//sorting
	//date/time
	public List<Emails> EmailTest(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
	}
	public List<Emails> EmailTestAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByOrderByAsc(startdateE, enddateE, committee_id);
	}
	//averages
	public List<Emails> AvDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByAverageOrderByDesc(startdateE, enddateE, committee_id);
	}
	public List<Emails> AverageAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByAverageOrderByAsc(startdateE, enddateE, committee_id);
	}
	//sums
	public List<Emails> SumDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findBySumOrderByDesc(startdateE, enddateE, committee_id);
	}
	public List<Emails> SumAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findBySumOrderByAsc(startdateE, enddateE, committee_id);
	}
	//donation count
	public List<Emails> DonationsCountDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByDonationsCountOrderByDesc(startdateE, enddateE, committee_id);
	}
	public List<Emails> DonationsCountAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByDonationsCountOrderByAsc(startdateE, enddateE, committee_id);
	}
	//donor count 
	public List<Emails> DonorCountDesc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByDonorCountOrderByDesc(startdateE, enddateE, committee_id);
	}
	public List<Emails> DonorCountAsc(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return erepo.findByDonorCountOrderByAsc(startdateE, enddateE, committee_id);
	}
	
	public List<Emails> EmailListForExport(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, @Param("enddateE") 
	@DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id, String type, String operator, String operand){
		System.out.println("operator " + operator);
		System.out.println("operand " + operand);
		if (type.contentEquals("Refcode 1")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.Refcode1Equals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.Refcode1Contains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.Refcode1isBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Refcode 2")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.Refcode2Equals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.Refcode2Contains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.Refcode2isBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Title")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.nameEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.nameContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.nameisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Category")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.categoryEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.categoryContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.categoryisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Subject")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.subjectEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.subjectContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.subjectisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Sender")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.senderEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.senderContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.senderisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Testing")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.testingEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.testingContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.testingisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		if (type.contentEquals("Link")) {
			System.out.println("type registered in ref " + type);
			if (operator.contentEquals("Equals")) {
				System.out.println("operator " + operator);
				return erepo.linkEquals(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Contains")) {
				System.out.println("operator after equals " + operator);
				return erepo.linkContains(startdateE, enddateE, committee_id, operand);
			}
			if (operator.contentEquals("Is blank")) {
				System.out.println("operator after equals " + operator);
				return erepo.linkisBlank(startdateE, enddateE, committee_id);
			}
			else {
				return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
			}
		}
		return erepo.findByOrderByDesc(startdateE, enddateE, committee_id);
	}

}
