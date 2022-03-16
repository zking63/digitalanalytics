package com.coding.LojoFundrasing.Repos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Emails;

public class EmailRepositoryCustomImpl implements EmailRepositoryCustom{
	
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    //where both parameters must be true
    public List<Emails> findEmailByName(List<String> names) {
    	System.out.println("in impl");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Emails> query = cb.createQuery(Emails.class);
        Root<Emails> emails = query.from(Emails.class);
        

        Path<String> emailPath = emails.get("emailName");
        System.out.println("emailPath name  " + emailPath);
        List<Predicate> predicates = new ArrayList<>();
        String name = "%" + names.get(0) + "%";
        for (int i = 0; i < names.size(); i++) {
        	if (i > 0) {
        		name = name + " && " + "%" + names.get(i) + "%";
        	}
        	
        	System.out.println("name   " + name);
			predicates.add(cb.like(emailPath, name));
		}
        query.select(emails)
            .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        System.out.println("preds   " + predicates.size());

       // List<Emails> emails = entityManager.createQuery(query)
           // .getResultList();

 
        return entityManager.createQuery(query)
                .getResultList();
    }

    public List<Emails> CustomEmailListForExport(@Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
			 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, Committees committee, String type, String operator, List<String> operands) throws ParseException {
    	System.out.println("in impl");
    	

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
        CriteriaQuery<Emails> query = cb.createQuery(Emails.class);
        Root<Emails> emails = query.from(Emails.class);
        
  
        Path<String> emailPath = emails.get("emailName");
        if (type.contentEquals("Refcode 1")) {
        	System.out.println("emailRefcode1");
        	emailPath = emails.get("emailRefcode1");
        }
        if (type.contentEquals("Refcode 2")) {
        	
        }
        if (type.contentEquals("Title")) {
        	System.out.println("emailName");
        	emailPath = emails.get("emailName");
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
        	
        }
       // if (!search.contentEquals("search")) {
        	
       // }
       // else {
       // }
        
        Predicate committeePredicate = cb.equal(emails.get("committee"), committee);
      
        Predicate datePredicate =  cb.between(emails.<Date>get("Emaildate"), start, end);
       
       List<Predicate> predicates = new ArrayList<>();
        List<Predicate> finalPredicates = new ArrayList<>();
        Predicate finalP = cb.equal(emails.get("committee"), committee);
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
				predicates.add(cb.equal(emailPath, finaloperand));
			}
			else if (operator.contentEquals("Contains")) {
				System.out.println("operator contain " + operator);
				finaloperand = "%" + operands.get(i) + "%";
				predicates.add(cb.like(emailPath, finaloperand));
			}
			else if (operator.contentEquals("Is blank")) {
				System.out.println("operator blank " + operator);
				predicates.add(cb.isNull(emailPath));
			}
			else {
				System.out.println("operator else " + operator);
				predicates.add(committeePredicate);
				predicates.add(datePredicate);
				System.out.println("preds   " + predicates.size());
		        query.select(emails).where(predicates.toArray(new Predicate[predicates.size()]));
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
	        query.select(emails).where(finalP);
	        return entityManager.createQuery(query).getResultList();
		}
		predicates.add(committeePredicate);
		predicates.add(datePredicate);
        query.select(emails).where(predicates.toArray(new Predicate[predicates.size()]));
        System.out.println("preds   " + predicates.size());

       // List<Emails> emails = entityManager.createQuery(query)
           // .getResultList();

        return entityManager.createQuery(query).getResultList();
    }
}
