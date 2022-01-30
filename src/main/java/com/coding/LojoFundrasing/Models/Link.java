package com.coding.LojoFundrasing.Models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name="links")
public class Link {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String linkname;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="overalllink")
	private List<Emails> emails;
    
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="committees_id")
    private Committees committee;
    
    private Long donations;
    private Long donors;
    private Double revenue;
    
    private Long emailsUsingLink;
    
    private Long clicksFromEmail;
    
    private Double donorsEmailClicks;
    private Double donationsEmailClicks;
    private Double revenuenperEmailClick;
    
    private Long recurringDonations;
    private Long recurringDonors;
    private Double recurringRevenue;
    
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Emails> getEmails() {
		return emails;
	}
	public void setEmails(List<Emails> emails) {
		this.emails = emails;
	}
	public Long getDonations() {
		return donations;
	}
	public void setDonations(Long donations) {
		this.donations = donations;
	}
	public Long getDonors() {
		return donors;
	}
	public void setDonors(Long donors) {
		this.donors = donors;
	}
	public Long getClicksFromEmail() {
		return clicksFromEmail;
	}
	public void setClicksFromEmail(Long clicksFromEmail) {
		this.clicksFromEmail = clicksFromEmail;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Committees getCommittee() {
		return committee;
	}
	public void setCommittee(Committees committee) {
		this.committee = committee;
	}
	public String getLinkname() {
		return linkname;
	}
	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}
	public Long getEmailsUsingLink() {
		return emailsUsingLink;
	}
	public void setEmailsUsingLink(Long emailsUsingLink) {
		this.emailsUsingLink = emailsUsingLink;
	}
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	public Double getDonorsEmailClicks() {
		return donorsEmailClicks;
	}
	public void setDonorsEmailClicks(Double donorsEmailClicks) {
		this.donorsEmailClicks = donorsEmailClicks;
	}
	public Double getDonationsEmailClicks() {
		return donationsEmailClicks;
	}
	public void setDonationsEmailClicks(Double donationsEmailClicks) {
		this.donationsEmailClicks = donationsEmailClicks;
	}
	public Long getRecurringDonations() {
		return recurringDonations;
	}
	public void setRecurringDonations(Long recurringDonations) {
		this.recurringDonations = recurringDonations;
	}
	public Long getRecurringDonors() {
		return recurringDonors;
	}
	public void setRecurringDonors(Long recurringDonors) {
		this.recurringDonors = recurringDonors;
	}
	public Double getRecurringRevenue() {
		return recurringRevenue;
	}
	public void setRecurringRevenue(Double recurringRevenue) {
		this.recurringRevenue = recurringRevenue;
	}
	public Double getRevenuenperEmailClick() {
		return revenuenperEmailClick;
	}
	public void setRevenuenperEmailClick(Double revenuenperEmailClick) {
		this.revenuenperEmailClick = revenuenperEmailClick;
	}
	
	
}
