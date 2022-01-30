package com.coding.LojoFundrasing.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name="committees")
public class Committees {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String CommitteeName;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	        name = "committees_users", 
	        joinColumns = @JoinColumn(name = "committees_id"), 
	        inverseJoinColumns = @JoinColumn(name = "user_id")
			)
	private List<User> users;
	
    @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<Donation> donations;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<Emails> emails;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<Donor> donors;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<EmailGroup> emailgroups;
    
   /* @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<Contenttest> contenttest;*/
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<test> bigtest;
	
    @OneToMany(fetch=FetchType.LAZY, mappedBy="committee")
	private List<Link> links;
	
	public Committees() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCommitteeName() {
		return CommitteeName;
	}
	public void setCommitteeName(String committeeName) {
		CommitteeName = committeeName;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Donation> getDonations() {
		return donations;
	}
	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	public List<Emails> getEmails() {
		return emails;
	}
	public void setEmails(List<Emails> emails) {
		this.emails = emails;
	}
	public List<Donor> getDonors() {
		return donors;
	}
	public void setDonors(List<Donor> donors) {
		this.donors = donors;
	}
	public List<EmailGroup> getEmailgroups() {
		return emailgroups;
	}
	public void setEmailgroups(List<EmailGroup> emailgroups) {
		this.emailgroups = emailgroups;
	}
	/*public List<Contenttest> getContenttest() {
		return contenttest;
	}
	public void setContenttest(List<Contenttest> contenttest) {
		this.contenttest = contenttest;
	}*/
	public List<test> getBigtest() {
		return bigtest;
	}
	public void setBigtest(List<test> bigtest) {
		this.bigtest = bigtest;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	
}
