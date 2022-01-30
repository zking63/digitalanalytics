/*package com.coding.LojoFundrasing.Models;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="data_funds")
public class Data {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Double emailaverage;
	private Double emailsum;
	private Integer donationcount;
	private Integer donorcount;
	private Double unsubscribeRate;
	private Double clickRate;
	private Double openRate;
	private Double bounceRate;
	private Double donationsOpens;
	private Double donationsClicks;
	private Double clicksOpens;
	private Double donorsOpens;
	private Double donorsClicks;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="email_id")
    private Emails dataemail;
    
	public Data() {

	}
	
	public Data(Double emailaverage, Double emailsum, Integer donationcount, Integer donorcount, Double unsubscribeRate,
			Double clickRate, Double openRate, Double bounceRate, Double donationsOpens, Double donationsClicks,
			Double clicksOpens, Double donorsOpens, Double donorsClicks, Emails dataemail) {
		this.emailaverage = emailaverage;
		this.emailsum = emailsum;
		this.donationcount = donationcount;
		this.donorcount = donorcount;
		this.unsubscribeRate = unsubscribeRate;
		this.clickRate = clickRate;
		this.openRate = openRate;
		this.bounceRate = bounceRate;
		this.donationsOpens = donationsOpens;
		this.donationsClicks = donationsClicks;
		this.clicksOpens = clicksOpens;
		this.donorsOpens = donorsOpens;
		this.donorsClicks = donorsClicks;
		this.dataemail = dataemail;
	}

	public Double getEmailAverage() {
		return emailaverage;
	}

	public void setEmailAverage(Double emailaverage) {
		this.emailaverage = emailaverage;
	}
	
	public String getEmailAverageFormatted() {
		if (this.emailaverage == null) {
			this.emailaverage = 0.0;
		}
		double emailAverage1 = (double) getEmailAverage();
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(emailAverage1);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Emails getDataEmail() {
		return dataemail;
	}
	public void setDataEmail(Emails dataemail) {
		this.dataemail = dataemail;
	}

	public Double getEmailsum() {
		return emailsum;
	}

	public void setEmailsum(Double emailsum) {
		this.emailsum = emailsum;
	}

	public Integer getDonationcount() {
		return donationcount;
	}

	public void setDonationcount(Integer donationcount) {
		this.donationcount = donationcount;
	}

	public Integer getDonorcount() {
		return donorcount;
	}

	public void setDonorcount(Integer donorcount) {
		this.donorcount = donorcount;
	}

	public Double getEmailaverage() {
		return emailaverage;
	}

	public void setEmailaverage(Double emailaverage) {
		this.emailaverage = emailaverage;
	}

	public Double getUnsubscribeRate() {
		return unsubscribeRate;
	}

	public void setUnsubscribeRate(Double unsubscribeRate) {
		this.unsubscribeRate = unsubscribeRate;
	}

	public Double getClickRate() {
		return clickRate;
	}

	public void setClickRate(Double clickRate) {
		this.clickRate = clickRate;
	}

	public Double getBounceRate() {
		return bounceRate;
	}

	public void setBounceRate(Double bounceRate) {
		this.bounceRate = bounceRate;
	}

	public Double getDonationsOpens() {
		return donationsOpens;
	}

	public void setDonationsOpens(Double donationsOpens) {
		this.donationsOpens = donationsOpens;
	}

	public Double getDonationsClicks() {
		return donationsClicks;
	}

	public void setDonationsClicks(Double donationsClicks) {
		this.donationsClicks = donationsClicks;
	}

	public Emails getDataemail() {
		return dataemail;
	}

	public void setDataemail(Emails dataemail) {
		this.dataemail = dataemail;
	}

	public Double getOpenRate() {
		return openRate;
	}

	public void setOpenRate(Double openRate) {
		this.openRate = openRate;
	}

	public Double getClicksOpens() {
		return clicksOpens;
	}

	public void setClicksOpens(Double clicksOpens) {
		this.clicksOpens = clicksOpens;
	}

	public Double getDonorsOpens() {
		return donorsOpens;
	}

	public void setDonorsOpens(Double donorsOpens) {
		this.donorsOpens = donorsOpens;
	}

	public Double getDonorsClicks() {
		return donorsClicks;
	}

	public void setDonorsClicks(Double donorsClicks) {
		this.donorsClicks = donorsClicks;
	}
	
	
}*/
