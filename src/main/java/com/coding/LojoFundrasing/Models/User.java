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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table (name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message="First name must not be empty.")
	private String firstName;
	@NotEmpty(message="Last name must not be empty.")
	private String lastName;
	@NotEmpty
	@Email(message="Must be a valid email.")
	private String email;
	@NotEmpty
	@Size(min=5, message="Password must be at least 5 characters.")
	private String password;
	@NotEmpty
	@Transient
	private String passwordConfirmation;
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="uploader")
    private List<Donor> donorsUploaded;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="donation_uploader")
    private List<Donation> donationsUploaded;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="email_uploader")
    private List<Emails> emailsUploaded;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="group_creator")
    private List<EmailGroup> emailgroups;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	        name = "committees_users", 
	        joinColumns = @JoinColumn(name = "user_id"), 
	        inverseJoinColumns = @JoinColumn(name = "committees_id")
			)
	private List<Committees> committees;
	
	public User() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
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
	
	public List<Donor> getDonorsUploaded() {
		return donorsUploaded;
	}

	public void setDonorsUploaded(List<Donor> donorsUploaded) {
		this.donorsUploaded = donorsUploaded;
	}
	

	public List<Donation> getDonationsUploaded() {
		return donationsUploaded;
	}

	public void setDonationsUploaded(List<Donation> donationsUploaded) {
		this.donationsUploaded = donationsUploaded;
	}
	
	public List<Emails> getEmailsUploaded() {
		return emailsUploaded;
	}

	public void setEmailsUploaded(List<Emails> emailsUploaded) {
		this.emailsUploaded = emailsUploaded;
	}
	

	public List<Committees> getCommittees() {
		return committees;
	}

	public void setCommittees(List<Committees> committees) {
		this.committees = committees;
	}
	
	

	public List<EmailGroup> getEmailgroups() {
		return emailgroups;
	}

	public void setEmailgroups(List<EmailGroup> emailgroups) {
		this.emailgroups = emailgroups;
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