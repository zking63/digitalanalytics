package com.coding.LojoFundrasing.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.Link;
import com.coding.LojoFundrasing.Repos.LinkRepo;

@Service
public class LinkService {
	
	@Autowired
	private LinkRepo lrepo;
	
	@Autowired
	private CommitteeService cservice;
	
	Date date = new Date();
	
	public Link createLink(Link link) {
		return lrepo.save(link);
	}
	
	public Link updateLink(Link link) {
		return lrepo.save(link);
	}
	
	public Link findAndSetUpLinkfromUpload(String emaillink, Committees committee) {
		Link link = null;
		Boolean committeeSetList = false;
		if (emaillink == null) {
			return link;
		}
		else {
			System.out.println("emaillink: " + emaillink);
			int iend = emaillink.indexOf("?"); 

			if (iend != -1) 
			{
			    emaillink = emaillink.substring(0 , iend); //this will give abc
			}
			System.out.println("emaillink after substring: " + emaillink);
			link = lrepo.findLinkbyNameandCommittee(emaillink, committee.getId());
			if (link == null) {
				link = new Link();
				link.setCreatedAt(date);
				link.setLinkname(emaillink);
				link.setCommittee(committee);
	        	while (committeeSetList == false) {
	    			if (committee.getLinks() == null || committee.getLinks().size() == 0) {
	    				List<Link> links = new ArrayList<Link>();
	    				links.add(link);
	    				committee.setLinks(links);
	    				cservice.createCommittee(committee);
	    				committeeSetList = true;
	    			}
	    			else {
	    				List<Link> links = committee.getLinks();
	    				links.add(link);
	    				committee.setLinks(links);
	    				cservice.createCommittee(committee);
	    				committeeSetList = true;
	    			}
	        	}
			}
			updateLink(link);
			return link;
		}
	}
	public void CalculateLinkData (Link link, Long committee_id) {
		if (link == null) {
			return;
		}
		//fundraising data
		Long donations = lrepo.donationscount(link.getId(), committee_id);
	    Long donors = lrepo.donorscount(link.getId(), committee_id);
	    Double revenue = lrepo.revenue(link.getId(), committee_id);
	    
	    //recurring fundraising data
	    Long recurringDonations = lrepo.recurringdonationscount(link.getId(), committee_id);
	    Long recurringDonors = lrepo.recurringdonorscount(link.getId(), committee_id);
	    Double recurringRevenue = lrepo.recurringrevenue(link.getId(), committee_id);
	    
	    //email performance
	    Long emailsUsingLink = lrepo.emailscount(link.getId(), committee_id);
	    Long clicksFromEmail = lrepo.clicksfromEmailcount(link.getId(), committee_id);
	    
	    //rates
	    Double donorsEmailClicks = 0.0;
	    Double donationsEmailClicks = 0.0;
	    Double revenueperEmailClick = 0.0;
	    
	    
	    link.setClicksFromEmail(clicksFromEmail);
	    link.setDonations(donations);
	    link.setDonors(donors);
	    link.setEmailsUsingLink(emailsUsingLink);
	    link.setRevenue(revenue);
	    link.setRecurringRevenue(recurringRevenue);
	    link.setRecurringDonors(recurringDonors);
	    link.setRecurringDonations(recurringDonations);
	    updateLink(link);
	    
	    if (link.getClicksFromEmail() != null && clicksFromEmail != 0) {
	    	donorsEmailClicks = (double) donors/clicksFromEmail;
	    	donationsEmailClicks = (double) donations/clicksFromEmail;
	    	if (revenue != null) {
	    		revenueperEmailClick = (double) revenue/clicksFromEmail;
	    	}
	    }
	    
	    link.setRevenuenperEmailClick(revenueperEmailClick);
	    link.setDonationsEmailClicks(donationsEmailClicks);
	    link.setDonorsEmailClicks(donorsEmailClicks);
	    
	    link.setUpdatedAt(date);
	    updateLink(link);
	}
}
