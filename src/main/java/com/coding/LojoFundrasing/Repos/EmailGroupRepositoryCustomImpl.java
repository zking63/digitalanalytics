package com.coding.LojoFundrasing.Repos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;

public class EmailGroupRepositoryCustomImpl implements EmailGroupRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
	public List<EmailGroup> CustomEmailGroupListForExport(@Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
			 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, Committees committee,
			String type, String operator, List<String> operands) throws ParseException {
	    

	    	System.out.println("type:   " + type);
	    	System.out.println("operator:   " + operator);
	    	
	    	System.out.println("operands size:   " + operands.size());
	    	
	    	System.out.println("startdate:   " + startdateD);
	    	System.out.println("enddate:   " + enddateD);
	    	
	    	enddateD = enddateD + " 23:59:59";
	    	
	    	System.out.println("enddate2:   " + enddateD);
	    	
			Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startdateD);
			Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(enddateD);

	    	System.out.println("start:   " + start);
	    	System.out.println("end:   " + end);
	    	
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<EmailGroup> query = cb.createQuery(EmailGroup.class);
	        Root<EmailGroup> groups = query.from(EmailGroup.class);
	        Join<EmailGroup, Emails> emails = groups.join("Emails");
	        
	  
	        Path<String> groupPath = groups.get("emailgroupName");
	        if (type.contentEquals("Refcode 1")) {
	        	System.out.println("emailRefcode1");
	        	groupPath = emails.get("emailRefcode1");
	        }
	        if (type.contentEquals("Refcode 2")) {
	        	
	        }
	        if (type.contentEquals("Title")) {
	        	System.out.println("emailgroupName");
	        	groupPath = groups.get("emailgroupName");
	        }
	        if (type.contentEquals("Category")) {
	        	
	        }
	        if (type.contentEquals("Subject")) {
	        	
	        }
	        if (type.contentEquals("Sender")) {
	        	
	        }
	        if (type.contentEquals("Testing")) {
	        	
	        }
	        if (type.contentEquals("Link")) {
	        	
	        }
	        if (type.contentEquals("Content")) {
	        	System.out.println("content");
	        	groupPath = emails.get("content");
	        }
	       // if (!search.contentEquals("search")) {
	        	
	       // }
	       // else {
	       // }
	        
	        Predicate committeePredicate = cb.equal(groups.get("committee"), committee);
	      
	        Predicate datePredicate =  cb.between(groups.<Date>get("date"), start, end);
	       
	       List<Predicate> predicates = new ArrayList<>();
	        List<Predicate> finalPredicates = new ArrayList<>();
	        Predicate finalP = cb.equal(groups.get("committee"), committee);
			Predicate equalPredicate = cb.or(predicates.toArray(new Predicate[predicates.size()]));
	      // String finaloperand = "%" + operands.get(0) + "%";
	        for (int i = 0; i < operands.size(); i++) {
	        	/*if (i > 0) {
	        		finaloperand = finaloperand + " && " + "%" + operands.get(i) + "%";
	        	}*/
	        	String finaloperand = operands.get(i);
	        	System.out.println("finaloperand  " + finaloperand);
	        	//System.out.println("emailPath  " + emailPath);
				if (operator.contentEquals("Equals")) {
					System.out.println("operator " + operator);
					predicates.add(cb.equal(groupPath, finaloperand));
				}
				else if (operator.contentEquals("Contains")) {
					System.out.println("operator contain " + operator);
					finaloperand = "%" + operands.get(i) + "%";
					predicates.add(cb.like(groupPath, finaloperand));
				}
				else if (operator.contentEquals("Is blank")) {
					System.out.println("operator blank " + operator);
					predicates.add(cb.isNull(groupPath));
				}
				else {
					System.out.println("operator else " + operator);
					predicates.add(committeePredicate);
					predicates.add(datePredicate);
					System.out.println("preds   " + predicates.size());
			        query.select(groups).where(predicates.toArray(new Predicate[predicates.size()]));
			        return entityManager.createQuery(query).getResultList();
				}
			}
	        System.out.println("preds before equal:   " + predicates.size());
			if (operator.contentEquals("Equals")) {
				equalPredicate
				  = cb.or(predicates.toArray(new Predicate[predicates.size()]));
				
				//finalPredicates.add(equalPredicate);
				finalP = cb.and(equalPredicate, committeePredicate, datePredicate);
				//predicates = finalPredicates;
				query
		        .select(groups)
		        .where(finalP)
		        .orderBy(cb.asc(groups.get("id")))
		        .distinct(true);
		        return entityManager.createQuery(query).getResultList();
			}
			predicates.add(committeePredicate);
			predicates.add(datePredicate);
	        //query.select(groups).where(predicates.toArray(new Predicate[predicates.size()]));
			query
			        .select(groups)
			        .where(predicates.toArray(new Predicate[] {}))
			        .orderBy(cb.asc(groups.get("id")))
			        .distinct(true);
			
			System.out.println("preds   " + predicates.size());

	       // List<Emails> emails = entityManager.createQuery(query)
	           // .getResultList();

	        return entityManager.createQuery(query).getResultList();
	}
}
