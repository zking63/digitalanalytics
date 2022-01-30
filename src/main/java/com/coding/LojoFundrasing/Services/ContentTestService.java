/*package com.coding.LojoFundrasing.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Contenttest;
import com.coding.LojoFundrasing.Repos.ContentTestRepo;


@Service
public class ContentTestService {
	@Autowired
	private ContentTestRepo ctrepo;
	
	public Contenttest createContentTest(Contenttest contenttest) {
		return ctrepo.save(contenttest);
	}
	
	public Contenttest findContentTestbyListCommitteeJtk(String recipientslist, String jtk, Long committee_id) {
		return ctrepo.findbyTest(recipientslist, jtk, committee_id).orElse(null);
	}
}*/
