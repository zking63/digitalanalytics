package com.coding.LojoFundrasing.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Repos.testrepo;

@Service
public class TestService {
	@Autowired
	private testrepo trepo;
	
	@Autowired
	private CommitteeService cservice;
	
	Date date = new Date();
	public test createTest(test test) {
		test.setCreatedAt(date);
		return trepo.save(test);
	}
	public test updateTest(test test) {
		test.setUpdatedAt(date);
		return trepo.save(test);
	}
	public List<test> findAllTests(Long committee_id){
		return trepo.findTestsbyCommittee(committee_id);
	}
	public List<test> findTestswithinRange(@Param("startdateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdateE, 
			@Param("enddateE") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddateE, Long committee_id){
		return trepo.findTestswithinRangeOrderByDesc(startdateE, enddateE, committee_id);
	}
	/*public test findTestByNameandCommittee(String testcategory, Long committee_id) {
		return trepo.findbyTestName(testcategory, committee_id).orElse(null);
	}*/
	/*public List<test> SetUpSenderTestfromGroup(String sender, Emails email, Long committee_id) {
		Committees committee = cservice.findbyId(committee_id);
		Boolean committeeSetList = false;
		Boolean emailgroupListSet = false;
		String testname = "SENDER";
		String varianta = sender;
		
		test test = trepo.findbyVariantA(testname, committee.getId(), emailgroup.getVariantA()).orElse(null);
		
		if (testA == null) {
			testA = new test();
			System.out.println("NEW TEST");
			testA.setTestcategory(testcategory);
			testA.setVariantA(emailgroup.getVariantA());
        	while (committeeSetList == false) {
    			if (committee.getBigtest() == null || committee.getBigtest().size() == 0) {
    				testA.setCommittee(committee);
    				List<test> tests = new ArrayList<test>();
    				tests.add(testA);
    				committee.setBigtest(tests);
    				cservice.createCommittee(committee);
    				committeeSetList = true;
    			}
    			else {
    				testA.setCommittee(committee);
    				List<test> tests = committee.getBigtest();
    				tests.add(testA);
    				committee.setBigtest(tests);
    				cservice.createCommittee(committee);
    				committeeSetList = true;
    			}
        	}
        	while (emailgroupListSet == false) {
    			if (testA.getEmailgroups() == null || testA.getEmailgroups().size() == 0) {
    				emailgroup.setTest(testA);
    				List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
    				emailgroups.add(emailgroup);
    				test.setEmailgroups(emailgroups);
    				updateTest(test);
    				emailgroupListSet = true;
    			}
    			else {
    				emailgroup.setTest(test);
    				List<EmailGroup> emailgroups = test.getEmailgroups();
    				emailgroups.add(emailgroup);
    				test.setEmailgroups(emailgroups);
    				updateTest(test);
    				emailgroupListSet = true;
    			}
        	}
        	createTest(test);
        	return test;
		}
		else {
			//if you want to make it so you can change the variant names in an email group and the tests will 
			// readjust, you need this code but you also need to update it in the email group to prevent old variant names from sticking
			/*test oldtest = trepo.findTestsbyIdandCommittee(committee_id, emailgroup.getTest().getId());
			if (oldtest.getId() != test.getId()) {
				System.out.println("OG test not matching new " + oldtest.getId() + " " + test.getId());
				List<EmailGroup> oldTestemailgroups = oldtest.getEmailgroups();
				oldTestemailgroups.remove(emailgroup);
				oldtest.setEmailgroups(oldTestemailgroups);
				updateTest(oldtest);
				CalculateTestData(oldtest, committee);
			}*/
			/*System.out.println("OLD TEST");
			test.setVariantA(emailgroup.getVariantA());
			test.setVariantB(emailgroup.getVariantB());
			return test;
		}
	}*/
	public test SetUpContentTestfromGroup(Long committee_id, EmailGroup emailgroup) {
		String testname = emailgroup.getGroupTest().toUpperCase();
		String testcategory = testname;
		Committees committee = cservice.findbyId(committee_id);
		Boolean committeeSetList = false;
		Boolean emailgroupListSet = false;
		
		if (!testcategory.contains("SENDER") && !testcategory.contains("SUBJECT")) {
			testcategory = "CONTENT TEST";
		}
		
		System.out.println("TEST CATEGORY: " + testcategory);
		System.out.println("TEST NAME: " + testname);
		
		test test = trepo.findbyTest(testname, committee.getId(), emailgroup.getVariantA(), emailgroup.getVariantB()).orElse(null);
		if (test == null) {
			test = new test();
			System.out.println("NEW TEST");
			test.setTestname(testname);
			test.setTestcategory(testcategory);
			test.setVariantA(emailgroup.getVariantA());
			test.setVariantB(emailgroup.getVariantB());
			createTest(test);
        	while (committeeSetList == false) {
    			if (committee.getBigtest() == null || committee.getBigtest().size() == 0) {
    				test.setCommittee(committee);
    				List<test> tests = new ArrayList<test>();
    				tests.add(test);
    				committee.setBigtest(tests);
    				cservice.createCommittee(committee);
    				committeeSetList = true;
    			}
    			else {
    				test.setCommittee(committee);
    				List<test> tests = committee.getBigtest();
    				tests.add(test);
    				committee.setBigtest(tests);
    				cservice.createCommittee(committee);
    				committeeSetList = true;
    			}
        	}
        	while (emailgroupListSet == false) {
    			if (test.getEmailgroups() == null || test.getEmailgroups().size() == 0) {
    				emailgroup.setTest(test);
    				List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
    				emailgroups.add(emailgroup);
    				test.setEmailgroups(emailgroups);
    				updateTest(test);
    				emailgroupListSet = true;
    			}
    			else {
    				emailgroup.setTest(test);
    				List<EmailGroup> emailgroups = test.getEmailgroups();
    				emailgroups.add(emailgroup);
    				test.setEmailgroups(emailgroups);
    				updateTest(test);
    				emailgroupListSet = true;
    			}
        	}
        	updateTest(test);
        	return test;
		}
		else {
			//if you want to make it so you can change the variant names in an email group and the tests will 
			// readjust, you need this code but you also need to update it in the email group to prevent old variant names from sticking
			/*test oldtest = trepo.findTestsbyIdandCommittee(committee_id, emailgroup.getTest().getId());
			if (oldtest.getId() != test.getId()) {
				System.out.println("OG test not matching new " + oldtest.getId() + " " + test.getId());
				List<EmailGroup> oldTestemailgroups = oldtest.getEmailgroups();
				oldTestemailgroups.remove(emailgroup);
				oldtest.setEmailgroups(oldTestemailgroups);
				updateTest(oldtest);
				CalculateTestData(oldtest, committee);
			}*/
			System.out.println("OLD TEST");
			test.setTestname(testname);
			test.setTestcategory(testcategory);
			test.setVariantA(emailgroup.getVariantA());
			test.setVariantB(emailgroup.getVariantB());
        	updateTest(test);
			return test;
		}
	}
	
	public void CalculateTestData(test test, Committees committee) {
		if (test == null) {
			return;
		}
		System.out.println("made it to calculate test ");
		
	    Long variantARecipients = trepo.variantARecipients(committee.getId(), test.getId());
	    Long variantBRecipients = trepo.variantBRecipients(committee.getId(), test.getId());
	    
	    Long variantAOpens = trepo.variantAOpens(committee.getId(), test.getId());
	    Long variantBOpens = trepo.variantBOpens(committee.getId(), test.getId());
	    
	    Long variantAClicks = trepo.variantAClicks(committee.getId(), test.getId());
	    Long variantBClicks = trepo.variantBClicks(committee.getId(), test.getId());
	    
	    Long variantADonations = trepo.variantADonations(committee.getId(), test.getId());
	    Long variantBDonations = trepo.variantBDonations(committee.getId(), test.getId());
	    
	    System.out.println("variantADonations " + variantADonations);
	    
	    Double variantARevenue = trepo.variantARevenue(committee.getId(), test.getId());
	    Double variantBRevenue = trepo.variantBRevenue(committee.getId(), test.getId());
	    
	    System.out.println("variantARevenue " + variantARevenue);
	    
	    Double variantAOpenRate = 0.0;
	    Double variantBOpenRate = 0.0;
	    
	    Double variantAClickOpens = 0.0;
	    Double variantBClickOpens = 0.0;
	    
	    Double variantAClickRate = 0.0;
	    Double variantBClickRate = 0.0;
	    
	    Double variantADonationsOpens = 0.0;
	    Double variantBDonationsOpens = 0.0;
	    
	    Double variantADonationsClicks = 0.0;
	    Double variantBDonationsClicks = 0.0;
	    
	    Double variantAaverageDonation = 0.0;
	    Double variantBaverageDonation = 0.0;
	    
	    Double variantAaveragerevenueperEmail = trepo.variantAaverageperEmail(committee.getId(), test.getId());
	    Double variantBaveragerevenueperEmail = trepo.variantBaverageperEmail(committee.getId(), test.getId());
	    
		test.setVariantAaverageRevenueperEmail(variantAaveragerevenueperEmail);
		test.setVariantBaverageRevenueperEmail(variantBaveragerevenueperEmail);
	    
    	Long emailscount = trepo.testEmailsCount(committee.getId(), test.getId());
    	Long variantaemailcount = trepo.testAEmailsCount(committee.getId(), test.getId());
    	Long variantbemailcount = trepo.testBEmailsCount(committee.getId(), test.getId());
    	System.out.println("variantaemailcount: " + variantaemailcount);
    	System.out.println("variantbemailcount: " + variantbemailcount);
    	 
    	test.setEmailcount(emailscount);
    	test.setVariantAemailcount(variantaemailcount);
    	test.setVariantBemailcount(variantbemailcount);
		
		test.setVariantARecipients(variantARecipients);
		test.setVariantBRecipients(variantBRecipients);
		
		test.setVariantAOpens(variantAOpens);
		test.setVariantBOpens(variantBOpens);
		
		test.setVariantAClicks(variantAClicks);
		test.setVariantBClicks(variantBClicks);
		
		test.setVariantADonations(variantADonations);
		test.setVariantBDonations(variantBDonations);
		
		test.setVariantARevenue(variantARevenue);
		test.setVariantBRevenue(variantBRevenue);
		
		//getting variant a rates if possible
		if (variantARecipients != null && variantARecipients != 0) {
			System.out.println("variantARecipients is NOT 0 " + variantARecipients);
			variantAOpenRate = (double) variantAOpens/variantARecipients;
			variantAClickRate = (double) variantAClicks/variantARecipients;
		}
		if (variantAOpens != null && variantAOpens != 0.0) {
			variantAClickOpens = (double) variantAClicks/variantAOpens;
			variantADonationsOpens = (double) variantADonations/variantAOpens;
		}
		if (variantAClicks != null && variantAClicks != 0.0) {
			variantADonationsClicks = (double) variantADonations/variantAClicks;
		}
		
		//getting variant b rates if possible
		if (variantBRecipients != null && variantBRecipients != 0) {
			System.out.println("variantBRecipients is NOT 0 " + variantBRecipients);
			variantBOpenRate = (double) variantBOpens/variantBRecipients;
			variantBClickRate = (double) variantBClicks/variantBRecipients;
		}
		if (variantBOpens != null && variantBOpens != 0.0) {
			variantBClickOpens = (double) variantBClicks/variantBOpens;
			variantBDonationsOpens = (double) variantBDonations/variantBOpens;
		}
		if (variantBClicks != null && variantBClicks != 0.0) {
			variantBDonationsClicks = (double) variantBDonations/variantBClicks;
		}
		if (variantADonations != null && variantADonations != 0) {
			if (variantARevenue != null) {
				variantAaverageDonation = (double) variantARevenue/variantADonations;
			}
		}
		if (variantBDonations != null && variantBDonations != 0) {
			if (variantBRevenue != null) {
				variantBaverageDonation = (double) variantBRevenue/variantBDonations;
			}
		}
		
		//set averages
		test.setVariantAaverageDonation(variantAaverageDonation);
		test.setVariantBaverageDonation(variantBaverageDonation);
		
		
		//setting rates
		test.setVariantAOpenRate(variantAOpenRate);
		test.setVariantBOpenRate(variantBOpenRate);
		
		test.setVariantAClickRate(variantAClickRate);
		test.setVariantBClickRate(variantBClickRate);
		
		test.setVariantAClickOpens(variantAClickOpens);
		test.setVariantBClickOpens(variantBClickOpens);

		test.setVariantADonationsOpens(variantADonationsOpens);
		test.setVariantBDonationsOpens(variantBDonationsOpens);
		
		test.setVariantADonationsClicks(variantADonationsClicks);
		test.setVariantBDonationsClicks(variantBDonationsClicks);
		updateTest(test);
	}
	
	/*public test AddContenttoTest(Contenttest content) {
		test test = content.getBigtest();
		if (test.getContent() == null) {
			List<Contenttest> contentlist = new ArrayList<Contenttest>();
			contentlist.add(content);
			System.out.println(content.getId());
			System.out.println("Content list:" + contentlist);
			test.setContent(contentlist);
			return trepo.save(test);
		}
		else {
			for (int i = 0; i < test.getContent().size(); i++) {
				if (test.getContent().get(i).getId() == content.getId()) {
					System.out.println("Content i:" + test.getContent().get(i).getId());
					System.out.println("Content test:" + content.getId());
					return trepo.save(test);
				}
				else {
					System.out.println("Content i:" + test.getContent().get(i).getId());
					System.out.println("Content test:" + content.getId());
					i++;
				}
			}
			List<Contenttest> contentlist = test.getContent();
			contentlist.add(content);
			System.out.println(content.getId());
			System.out.println("Content list after loop:" + contentlist);
			test.setContent(contentlist);
			return trepo.save(test);
		}
	}
	
	public void TestVariables(Contenttest content) {
		test test = content.getBigtest();
		String varianta = "Variant A";
		String variantb = "Variant B";
		String tied = "TIED";
	    Double goWinnerPercentType1 = 0.0;
	    Double clickWinnerPercentType1 = 0.0;
	    Double fullsendPercentType1 = 0.0;
	    Double goWinnerPercentType2 = 0.0;
	    Double clickWinnerPercentType2 = 0.0;
	    Double fullsendPercentType2 = 0.0;
	    Double goWinnerPercentTied = 0.0;
	    Double clickWinnerPercentTied = 0.0;
	    Double fullsendPercentTied = 0.0;
	    
		AddContenttoTest(content);
		System.out.println("content size: " + test.getContent().size());
		test.setFullsendCountType(test.getContent().size());
		System.out.println("content size: " + test.getFullsendCountType());
		test.setClickWinnerCountType(test.getContent().size());
		test.setGoWinnerCountType(test.getContent().size());
		if (test.getVariantA() == null && test.getVariantB() == null) {
			test.setVariantA(content.getVariantA());
			test.setVariantB(content.getVariantB());
			System.out.println("variant A: " + test.getVariantA());
			System.out.println("variant B: " + test.getVariantB());
		}
		
		test.setGoWinnerCountType1(ctrepo.VariantAGoWinnerCount(test.getId(), test.getCommittee().getId(), varianta));
		test.setGoWinnerCountType2(ctrepo.VariantBGoWinnerCount(test.getId(), test.getCommittee().getId(), variantb));
		test.setGoWinnerCountTied(ctrepo.TiedGoWinnerCount(test.getId(), test.getCommittee().getId(), tied));
		test.setClickWinnerCountType1(ctrepo.VariantAClickRcvWinner(test.getId(), test.getCommittee().getId(), varianta));
		test.setClickWinnerCountType2(ctrepo.VariantBClickRcvWinner(test.getId(), test.getCommittee().getId(), variantb));
		test.setClickWinnerCountTied(ctrepo.TiedClickRcvWinner(test.getId(), test.getCommittee().getId(), tied));
		test.setFullsendCountType1(ctrepo.VariantAFulllistWinner(test.getId(), test.getCommittee().getId(), varianta));
		test.setFullsendCountType2(ctrepo.VariantBFulllistWinner(test.getId(), test.getCommittee().getId(), variantb));
		test.setFullsendCountTied(ctrepo.TiedFulllistWinner(test.getId(), test.getCommittee().getId(), tied));
		trepo.save(test);
		
		//casting to doubles
		Double gowinnertype1 = (double) test.getGoWinnerCountType1();
		Double gowinnertype2 = (double) test.getGoWinnerCountType2();
		Double gowinnertied = (double) test.getGoWinnerCountTied();
		Double gowinnercount = (double) test.getGoWinnerCountType();
		
		Double clickwinnertype1 = (double) test.getClickWinnerCountType1();
		Double clickwinnertype2 = (double) test.getClickWinnerCountType2();
		Double clickwinnertied = (double) test.getClickWinnerCountTied();
		Double clickwinnercount = (double) test.getClickWinnerCountType();
		
		Double fullsendwinnertype1 = (double) test.getFullsendCountType1();
		Double fullsendwinnertype2 = (double) test.getFullsendCountType2();
		Double fullsendwinnertied = (double) test.getFullsendCountTied();
		Double fullsendwinnercount = (double) test.getFullsendCountType();

		//GO Percents
		goWinnerPercentType1 = gowinnertype1/gowinnercount;
		test.setGoWinnerPercentType1(goWinnerPercentType1);
		goWinnerPercentType2 = gowinnertype2/gowinnercount;
		test.setGoWinnerPercentType2(goWinnerPercentType2);
		goWinnerPercentTied = gowinnertied/gowinnercount;
		test.setGoWinnerPercentTied(goWinnerPercentTied);
		
		//Click Percents
		clickWinnerPercentType1 = clickwinnertype1/clickwinnercount;
		test.setClickWinnerPercentType1(clickWinnerPercentType1);
		clickWinnerPercentType2 = clickwinnertype2/clickwinnercount;
		test.setClickWinnerPercentType2(clickWinnerPercentType2);
		clickWinnerPercentTied = clickwinnertied/clickwinnercount;
		test.setClickWinnerPercentTied(clickWinnerPercentTied);
		
		//Full Send Percents
		fullsendPercentType1 = fullsendwinnertype1/fullsendwinnercount;
		test.setFullsendPercentType1(fullsendPercentType1);
		fullsendPercentType2 = fullsendwinnertype2/fullsendwinnercount;
		test.setFullsendPercentType2(fullsendPercentType2);
		fullsendPercentTied = fullsendwinnertied/fullsendwinnercount;
		test.setFullsendPercentTied(fullsendPercentTied);
		
		trepo.save(test);
		
		if (test.getGoWinnerCountType1() > test.getGoWinnerCountType2()) {
			test.setOverallGoWinner(test.getVariantA());
			test.setOverallGoWinnerPercent(goWinnerPercentType1);
		}
		else if (test.getGoWinnerCountType1() == test.getGoWinnerCountType2()){
			test.setOverallGoWinner("TIED");
			test.setOverallGoWinnerPercent(goWinnerPercentType1);
		}
		else {
			test.setOverallGoWinner(test.getVariantB());
			test.setOverallGoWinnerPercent(goWinnerPercentType2);
		}
		if (test.getClickWinnerCountType1() > test.getClickWinnerCountType2()) {
			test.setOverallClickWinner(test.getVariantA());
			test.setOverallClickWinnerPercent(clickWinnerPercentType1);
		}
		else if (test.getClickWinnerCountType1() == test.getClickWinnerCountType2()){
			test.setOverallClickWinner("TIED");
			test.setOverallClickWinnerPercent(clickWinnerPercentType1);
		}
		else {
			test.setOverallClickWinner(test.getVariantB());
			test.setOverallClickWinnerPercent(clickWinnerPercentType2);
		}
		if (test.getFullsendCountType1() > test.getFullsendCountType2()) {
			test.setOverallFullSendWinner(test.getVariantA());
			test.setOverallFullSendWinnerPercent(fullsendPercentType1);
		}
		else if (test.getFullsendCountType1() == test.getFullsendCountType2()) {
			test.setOverallFullSendWinner("TIED");
			test.setOverallFullSendWinnerPercent(fullsendPercentType1);
		}
		else {
			test.setOverallFullSendWinner(test.getVariantB());
			test.setOverallFullSendWinnerPercent(fullsendPercentType2);
		}
		
		trepo.save(test);
		
	}
	
	public List<test> findAllTests(Long committee_id){
		return trepo.findTestsbyCommittee(committee_id);
	}*/
}
