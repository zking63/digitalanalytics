package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.Link;
import com.coding.LojoFundrasing.Repos.LinkRepo;

@Service
public class LinkService {
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private LinkRepo lrepo;
	
	@Autowired
	private CommitteeService cservice;
	
	@Autowired
	private QueryService queryservice;
	
	Date date = new Date();
	
	public Link createLink(Link link) {
		return lrepo.save(link);
	}
	
	public Link updateLink(Link link) {
		return lrepo.save(link);
	}
	public List<Link> findall(Long committee_id) {
		return lrepo.findAll(committee_id);
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
	   // Long donors = lrepo.donorscount(link.getId(), committee_id);
	    Double revenue = lrepo.revenue(link.getId(), committee_id);
	    
	    //recurring fundraising data
	    Long recurringDonations = lrepo.recurringdonationscount(link.getId(), committee_id);
	    //Long recurringDonors = lrepo.recurringdonorscount(link.getId(), committee_id);
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
	   // link.setDonors(donors);
	    link.setEmailsUsingLink(emailsUsingLink);
	    link.setRevenue(revenue);
	    link.setRecurringRevenue(recurringRevenue);
	   // link.setRecurringDonors(recurringDonors);
	    link.setRecurringDonations(recurringDonations);
	    updateLink(link);
	    
	    if (link.getClicksFromEmail() != null && clicksFromEmail != 0) {
	    	//donorsEmailClicks = (double) donors/clicksFromEmail;
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
	public List<Link> PredicateCreator(String sort, String direction, Integer field, List<String> categories, List<String> operandsList, List<Predicate> predicates, String startdateD, String enddateD, 
			Committees committee, String type, String operator, List<String> operands, String operand) throws ParseException, IOException {
		System.out.println("pred create");
		System.out.println("operand: " +operand);
		System.out.println("sort: " +sort);
		System.out.println("direction: " +direction);
		//System.out.println("operands size in pred first " + operands.size());
		if (operand == null && (operands == null || operands.size() < 1 || operands.isEmpty())){
			System.out.println("operand: " +operand);
			if (operands != null) {
				System.out.println("operands: " +operands.size());
			}
			
			System.out.println("null in return");
			return null;
		}
		
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Link> query = cb.createQuery(Link.class);
        Root<Link> links = query.from(Link.class);
        links.alias("links");
        Join<Link, Emails> emails = links.join("emails");
        emails.alias("emails");
        Path<String> groupPath = links.get("linkname");
        
		//date/committee preds
    	enddateD = enddateD + " 23:59:59";
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startdateD);
		Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(enddateD);
        Predicate committeePredicate = cb.equal(links.get("committee"), committee);
        Predicate datePredicate =  cb.between(emails.<Date>get("Emaildate"), start, end);
        if (type.contentEquals("URL")) {
        	System.out.println("URL");
        	groupPath = links.get("linkname");
        }
        if (type.contentEquals("Email title")) {
        	System.out.println("emailName");
        	groupPath = emails.get("emailName");
        }
        if (type.contentEquals("Email content")) {
        	System.out.println("Email content");
        	groupPath = emails.get("content");
        }
        if (type.contentEquals("Select")) {
        	System.out.println("type is Select");
        	groupPath = links.get("linkname");
        }
        
        if (operator.contentEquals("Select") || type.contentEquals("Select") 
        		|| type.contentEquals("All") 
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
	        if (direction.contentEquals("desc")) {
				query
		        .select(links)
		        .where(predicates.toArray(new Predicate[] {}))
		        .orderBy(cb.desc(links.get(sort)))
		        .distinct(true);
	        }
	        if (direction.contentEquals("asc")) {
				query
		        .select(links)
		        .where(predicates.toArray(new Predicate[] {}))
		        .orderBy(cb.asc(links.get(sort)))
		        .distinct(true);
	        }
        	return entityManager.createQuery(query).getResultList();
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
	        Predicate finalP = cb.equal(links.get("committee"), committee);
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
					System.out.println("final op " + operands.size());
					System.out.println("current operand " + operand);

					
					if (operands.size() > 1) {
						temppreds.add(temppredicate);
						if (finaloperand.contains("Biden")) {
							System.out.println("CONTAIN BIDEN " + operand);
							List<Link> l = new ArrayList<Link>();
							return l;
						}
						System.out.println("CONTAINS OR IN CONTAINS");
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
				else if (operator.contentEquals("Is blank")) {
					System.out.println("operator blank " + operator);
					predicates.add(cb.isNull(groupPath));
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
				//operands = null;
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
			
	        if (direction.contentEquals("desc")) {
				query
		        .select(links)
		        .where(predicates.toArray(new Predicate[] {}))
		        .orderBy(cb.desc(links.get(sort)))
		        .distinct(true);
	        }
	        if (direction.contentEquals("asc")) {
				query
		        .select(links)
		        .where(predicates.toArray(new Predicate[] {}))
		        .orderBy(cb.asc(links.get(sort)))
		        .distinct(true);
	        }
        	return entityManager.createQuery(query).getResultList();
		
		}
	}
}
