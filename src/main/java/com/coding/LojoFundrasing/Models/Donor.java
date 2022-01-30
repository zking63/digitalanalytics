package com.coding.LojoFundrasing.Models;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table (name="donors")
public class Donor {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String donorFirstName;
	private String donorLastName;
	private String address;
	private String state;
	private String country;
	private String Zipcode;
	private String phone;
	private String city;
	@NotEmpty
	@Email(message="Must be a valid email.")
	private String donorEmail;
	
    @OneToMany(fetch=FetchType.LAZY, mappedBy="donor")
    private List<Donation> contributions;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User uploader;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="committees_id")
    private Committees committee;
    
	@OneToOne(mappedBy="datadonor", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private DonorData donordata;
	
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="donation_id")
    private Donation mostrecentDonationbyDonor;
    
    @DateTimeFormat(pattern ="yyyy-MM-dd kk:mm:ss")
    private Date mostrecentDate;
    
	/*@DateTimeFormat(pattern ="kk:mm")
	private Date mostrecenttime;*/
	
	private Double mostrecentamount;
	
	//within range functions
	private Integer countwithinrange;
	private Double sumwithinrange;
	private Double averagewithinrange;
	private Double hpcwithinrange;
	private Double mostrecentInrangeAmount;
	@DateTimeFormat(pattern ="yyyy-MM-dd kk:mm:ss")
	private Date mostRecentDateinRange;
    
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	
	public Donor() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDonorFirstName() {
		return donorFirstName;
	}

	public void setDonorFirstName(String donorFirstName) {
		this.donorFirstName = donorFirstName;
	}

	public String getDonorLastName() {
		return donorLastName;
	}

	public void setDonorLastName(String donorLastName) {
		this.donorLastName = donorLastName;
	}

	public List<Donation> getContributions() {
		return contributions;
	}

	public void setContributions(List<Donation> contributions) {
		this.contributions = contributions;
	}

	public User getUploader() {
		return uploader;
	}

	public void setUploader(User uploader) {
		this.uploader = uploader;
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
	
	public String getDonorEmail() {
		return donorEmail;
	}

	public void setDonorEmail(String email) {
		this.donorEmail = email;
	}

	@PrePersist
	protected void onCreate(){
		this.createdAt = new Date();
	}
	@PreUpdate
	protected void onUpdate(){
	    this.updatedAt = new Date();
	}

	public DonorData getDonordata() {
		return donordata;
	}

	public void setDonordata(DonorData donordata) {
		this.donordata = donordata;
	}

	public Donation getMostrecentDonationbyDonor() {
		return mostrecentDonationbyDonor;
	}

	public void setMostrecentDonationbyDonor(Donation mostrecentDonationbyDonor) {
		this.mostrecentDonationbyDonor = mostrecentDonationbyDonor;
	}

	public Date getMostrecentDate() {
		return mostrecentDate;
	}

	public void setMostrecentDate(Date mostrecentDate) {
		this.mostrecentDate = mostrecentDate;
	}

	public Double getMostrecentamount() {
		return mostrecentamount;
	}

	public void setMostrecentamount(Double mostrecentamount) {
		this.mostrecentamount = mostrecentamount;
	}
	
    public String getRecentDateFormatted() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	return df.format(this.mostrecentDate);
    }
	public String getDonorRecentAmountFormatted() {
		if (this.mostrecentamount == null) {
			this.mostrecentamount = 0.0;
		}
		double mostrecentamount1 = (double) getMostrecentamount();
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(mostrecentamount1);
	}

	public Integer getCountwithinrange() {
		return countwithinrange;
	}

	public void setCountwithinrange(Integer countwithinrange) {
		this.countwithinrange = countwithinrange;
	}

	public Double getSumwithinrange() {
		return sumwithinrange;
	}

	public void setSumwithinrange(Double sumwithinrange) {
		this.sumwithinrange = sumwithinrange;
	}

	public Double getAveragewithinrange() {
		return averagewithinrange;
	}

	public void setAveragewithinrange(Double averagewithinrange) {
		this.averagewithinrange = averagewithinrange;
	}

	public Committees getCommittee() {
		return committee;
	}

	public void setCommittee(Committees committee) {
		this.committee = committee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return Zipcode;
	}

	public void setZipcode(String zipcode) {
		Zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getHpcwithinrange() {
		return hpcwithinrange;
	}

	public void setHpcwithinrange(Double hpcwithinrange) {
		this.hpcwithinrange = hpcwithinrange;
	}

	public Double getMostrecentInrangeAmount() {
		return mostrecentInrangeAmount;
	}

	public void setMostrecentInrangeAmount(Double mostrecentInrangeAmount) {
		this.mostrecentInrangeAmount = mostrecentInrangeAmount;
	}

	public Date getMostRecentDateinRange() {
		return mostRecentDateinRange;
	}

	public void setMostRecentDateinRange(Date mostRecentDateinRange) {
		this.mostRecentDateinRange = mostRecentDateinRange;
	}
    public String getRecentDateinRangeFormatted() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	return df.format(this.mostRecentDateinRange);
    }
	
}
