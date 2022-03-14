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
    public List<Emails> findEmailById(List<Long> ids) {
    	System.out.println("in impl");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Emails> query = cb.createQuery(Emails.class);
        Root<Emails> email = query.from(Emails.class);
        

        Path<String> emailPath = email.get("id");

        List<Predicate> predicates = new ArrayList<>();
        for (Long id : ids) {
            predicates.add(cb.le(email.get("id"), id));
        }
        query.select(email)
            .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        System.out.println("pred size " + predicates);

       // List<Emails> emails = entityManager.createQuery(query)
           // .getResultList();
        System.out.println("emails size " + entityManager.createQuery(query)
        .getResultList());
        return entityManager.createQuery(query)
                .getResultList();
    }
}
