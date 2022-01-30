package com.coding.LojoFundrasing.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.coding.LojoFundrasing.Models.Donor;
import com.coding.LojoFundrasing.Repos.DonorRepo;

@Component
public class DonorValidation implements Validator{
	@Autowired
	private DonorRepo drepo;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Donor.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
        Donor donor = (Donor) target;
        Long id = donor.getCommittee().getId();
        
        if(this.drepo.findByemailandCommittee(donor.getDonorEmail(), id) != null) {
        	System.out.println(drepo.findBydonorEmail(donor.getDonorEmail()));
        	errors.rejectValue("donorEmail", "Unique");
        }
	}
}
