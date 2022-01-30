package com.coding.LojoFundrasing.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table (name="donations")
public class Donation {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private Double amount;
	private String recurring;
	private Integer recurrenceNumber;
	private String ActBlueId;
	private String DonationRefcode1;
	private String DonationRefcode2;
	@DateTimeFormat(pattern ="yyyy-MM-dd kk:mm")
	private Date Dondate;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="donor_id")  
    private Donor donor;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User donation_uploader;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="email_id")
    private Emails emailDonation;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="committees_id")
    private Committees committee;
    
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	
    public Donation() {
    	
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Date getDondate() {
		return Dondate;
	}

	public void setDondate(Date dondate) {
		Dondate = dondate;
	}

	public Donor getDonor() {
		return donor;
	}

	public void setDonor(Donor donor) {
		this.donor = donor;
	}

	public User getDonation_uploader() {
		return donation_uploader;
	}

	public void setDonation_uploader(User donation_uploader) {
		this.donation_uploader = donation_uploader;
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
    public String getDonationDateFormatted() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm");
    	return df.format(this.Dondate);
    }
    /*public String getDonationTimeFormatted() {
    	SimpleDateFormat df = new SimpleDateFormat("kk:mm");
    	return df.format(this.Dontime);
    }*/

	public Emails getEmailDonation() {
		return emailDonation;
	}

	public void setEmailDonation(Emails emailDonation) {
		this.emailDonation = emailDonation;
	}
	
	

	public String getRecurring() {
		return recurring;
	}

	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}

	public Integer getRecurrenceNumber() {
		return recurrenceNumber;
	}

	public void setRecurrenceNumber(Integer recurrenceNumber) {
		this.recurrenceNumber = recurrenceNumber;
	}
	

	public String getActBlueId() {
		return ActBlueId;
	}

	public void setActBlueId(String actBlueId) {
		ActBlueId = actBlueId;
	}

	public Committees getCommittee() {
		return committee;
	}

	public void setCommittee(Committees committee) {
		this.committee = committee;
	}

	public String getDonationRefcode1() {
		return DonationRefcode1;
	}

	public void setDonationRefcode1(String donationRefcode1) {
		DonationRefcode1 = donationRefcode1;
	}

	public String getDonationRefcode2() {
		return DonationRefcode2;
	}

	public void setDonationRefcode2(String donationRefcode2) {
		DonationRefcode2 = donationRefcode2;
	}

	@PrePersist
	protected void onCreate(){
		this.createdAt = new Date();
	}
	@PreUpdate
	protected void onUpdate(){
	    this.updatedAt = new Date();
	}
    
}
