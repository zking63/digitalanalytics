package com.coding.LojoFundrasing.Repos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.coding.LojoFundrasing.Models.Emails;

public class EmailRepositoryCustomImpl implements EmailRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    //where both parameters must be true
    public List<Emails> findEmailByName(List<String> names) {
    	System.out.println("name1   " + names.get(0));
    	//System.out.println("name2   " + names.get(1));
    	System.out.println("in impl");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Emails> query = cb.createQuery(Emails.class);
        Root<Emails> emails = query.from(Emails.class);
        

        Path<String> emailPath = emails.get("emailName");

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
        System.out.println("emails " + emails);
        System.out.println("query size " + entityManager.createQuery(query)
        .getResultList());
        return entityManager.createQuery(query)
                .getResultList();
    }
}
