package com.coding.LojoFundrasing.Services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Repos.CommitteesRepo;
import com.coding.LojoFundrasing.Repos.UserRepo;

@Service
public class CommitteeService {
	@Autowired
	private CommitteesRepo crepo;
	@Autowired UserRepo urepo;
	
	Date date = new Date();
	
	public Committees createCommittee(Committees committee) {
		committee.setCreatedAt(date);
		return crepo.save(committee);
	}
	
	public Committees updateCommittee(Committees committee) {
		committee.setUpdatedAt(date);
		return crepo.save(committee);
	}
	
	public Committees findbyId(Long id) {
		return crepo.findById(id).orElse(null);
	}
	
	public List<Committees> findAllCommittees() {
		return crepo.findAll();
	}
	
	public List<Committees> findCommitteesbyUser(long user_id){
		return crepo.findByusers(user_id);
	}
	public List<Committees> findAllexcept(Long committee_id, Long user_id){
		return crepo.findAllexcept(committee_id, user_id);
	}
}
