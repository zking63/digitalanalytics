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
@Table (name="test")
public class test {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="test")
	private List<EmailGroup> emailgroups;

    private String testcategory;
    private String testname;
    
    private Long emailcount;
    private Long variantAemailcount;
    private Long variantBemailcount;
    
    private String VariantA;
    private String VariantB;
    
    private Long variantARecipients;
    private Long variantBRecipients;
    
    private Long variantAOpens;
    private Long variantBOpens;
    
    private Long variantAClicks;
    private Long variantBClicks;
    
    private Long variantADonations;
    private Long variantBDonations;
    
    private Double variantARevenue;
    private Double variantBRevenue;
    
    private Double variantAOpenRate;
    private Double variantBOpenRate;
    
    private Double variantAClickOpens;
    private Double variantBClickOpens;
    
    private Double variantAClickRate;
    private Double variantBClickRate;
    
    private Double variantADonationsOpens;
    private Double variantBDonationsOpens;
    
    private Double variantADonationsClicks;
    private Double variantBDonationsClicks;
    
    private Double variantADonorsOpens;
    private Double variantBDonorsOpens;
    
    private Double variantADonorsClicks;
    private Double variantBDonorsClicks;
    
    private Double variantAaverageDonation;
    private Double variantBaverageDonation;
    
    private Double variantAaverageRevenueperEmail;
    private Double variantBaverageRevenueperEmail;
    
    
 /*   private Integer goWinnerCountType1;
    private Integer clickWinnerCountType1;
    private Integer fullsendCountType1;
    private Double goWinnerPercentType1;
    private Double clickWinnerPercentType1;
    private Double fullsendPercentType1;
    
    private Integer goWinnerCountType2;
    private Integer clickWinnerCountType2;
    private Integer fullsendCountType2;
    private Double goWinnerPercentType2;
    private Double clickWinnerPercentType2;
    private Double fullsendPercentType2;
    
    private Integer goWinnerCountTied;
    private Integer clickWinnerCountTied;
    private Integer fullsendCountTied;
    private Double goWinnerPercentTied;
    private Double clickWinnerPercentTied;
    private Double fullsendPercentTied;
    
    private Integer goWinnerCountType;
    private Integer clickWinnerCountType;
    private Integer fullsendCountType;
    
    private String OverallGoWinner;
    private String OverallFullSendWinner;
    private String OverallClickWinner;
    
    private Double OverallGoWinnerPercent;
    private Double OverallFullSendWinnerPercent;
    private Double OverallClickWinnerPercent;*/
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="committees_id")
    private Committees committee;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTestcategory() {
		return testcategory;
	}
	public void setTestcategory(String testcategory) {
		this.testcategory = testcategory;
	}
	public String getVariantA() {
		return VariantA;
	}
	public void setVariantA(String variantA) {
		VariantA = variantA;
	}
	public String getVariantB() {
		return VariantB;
	}
	public void setVariantB(String variantB) {
		VariantB = variantB;
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
	
	public List<EmailGroup> getEmailgroups() {
		return emailgroups;
	}
	public void setEmailgroups(List<EmailGroup> emailgroups) {
		this.emailgroups = emailgroups;
	}
	public Long getVariantARecipients() {
		return variantARecipients;
	}
	public void setVariantARecipients(Long variantARecipients) {
		this.variantARecipients = variantARecipients;
	}
	public Long getVariantBRecipients() {
		return variantBRecipients;
	}
	public void setVariantBRecipients(Long variantBRecipients) {
		this.variantBRecipients = variantBRecipients;
	}
	public Long getVariantAOpens() {
		return variantAOpens;
	}
	public void setVariantAOpens(Long variantAOpens) {
		this.variantAOpens = variantAOpens;
	}
	public Long getVariantBOpens() {
		return variantBOpens;
	}
	public void setVariantBOpens(Long variantBOpens) {
		this.variantBOpens = variantBOpens;
	}
	public Long getVariantAClicks() {
		return variantAClicks;
	}
	public void setVariantAClicks(Long variantAClicks) {
		this.variantAClicks = variantAClicks;
	}
	public Long getVariantBClicks() {
		return variantBClicks;
	}
	public void setVariantBClicks(Long variantBClicks) {
		this.variantBClicks = variantBClicks;
	}
	public Long getVariantADonations() {
		return variantADonations;
	}
	public void setVariantADonations(Long variantADonations) {
		this.variantADonations = variantADonations;
	}
	public Long getVariantBDonations() {
		return variantBDonations;
	}
	public void setVariantBDonations(Long variantBDonations) {
		this.variantBDonations = variantBDonations;
	}
	public Double getVariantARevenue() {
		return variantARevenue;
	}
	public void setVariantARevenue(Double variantARevenue) {
		this.variantARevenue = variantARevenue;
	}
	public Double getVariantBRevenue() {
		return variantBRevenue;
	}
	public void setVariantBRevenue(Double variantBRevenue) {
		this.variantBRevenue = variantBRevenue;
	}
	public Double getVariantAOpenRate() {
		return variantAOpenRate;
	}
	public void setVariantAOpenRate(Double variantAOpenRate) {
		this.variantAOpenRate = variantAOpenRate;
	}
	public Double getVariantBOpenRate() {
		return variantBOpenRate;
	}
	public void setVariantBOpenRate(Double variantBOpenRate) {
		this.variantBOpenRate = variantBOpenRate;
	}
	public Double getVariantAClickOpens() {
		return variantAClickOpens;
	}
	public void setVariantAClickOpens(Double variantAClickOpens) {
		this.variantAClickOpens = variantAClickOpens;
	}
	public Double getVariantBClickOpens() {
		return variantBClickOpens;
	}
	public void setVariantBClickOpens(Double variantBClickOpens) {
		this.variantBClickOpens = variantBClickOpens;
	}
	public Double getVariantAClickRate() {
		return variantAClickRate;
	}
	public void setVariantAClickRate(Double variantAClickRate) {
		this.variantAClickRate = variantAClickRate;
	}
	public Double getVariantBClickRate() {
		return variantBClickRate;
	}
	public void setVariantBClickRate(Double variantBClickRate) {
		this.variantBClickRate = variantBClickRate;
	}
	public Double getVariantADonationsOpens() {
		return variantADonationsOpens;
	}
	public void setVariantADonationsOpens(Double variantADonationsOpens) {
		this.variantADonationsOpens = variantADonationsOpens;
	}
	public Double getVariantBDonationsOpens() {
		return variantBDonationsOpens;
	}
	public void setVariantBDonationsOpens(Double variantBDonationsOpens) {
		this.variantBDonationsOpens = variantBDonationsOpens;
	}
	public Double getVariantADonationsClicks() {
		return variantADonationsClicks;
	}
	public void setVariantADonationsClicks(Double variantADonationsClicks) {
		this.variantADonationsClicks = variantADonationsClicks;
	}
	public Double getVariantBDonationsClicks() {
		return variantBDonationsClicks;
	}
	public void setVariantBDonationsClicks(Double variantBDonationsClicks) {
		this.variantBDonationsClicks = variantBDonationsClicks;
	}
	public Double getVariantADonorsOpens() {
		return variantADonorsOpens;
	}
	public void setVariantADonorsOpens(Double variantADonorsOpens) {
		this.variantADonorsOpens = variantADonorsOpens;
	}
	public Double getVariantBDonorsOpens() {
		return variantBDonorsOpens;
	}
	public void setVariantBDonorsOpens(Double variantBDonorsOpens) {
		this.variantBDonorsOpens = variantBDonorsOpens;
	}
	public Double getVariantADonorsClicks() {
		return variantADonorsClicks;
	}
	public void setVariantADonorsClicks(Double variantADonorsClicks) {
		this.variantADonorsClicks = variantADonorsClicks;
	}
	public Double getVariantBDonorsClicks() {
		return variantBDonorsClicks;
	}
	public void setVariantBDonorsClicks(Double variantBDonorsClicks) {
		this.variantBDonorsClicks = variantBDonorsClicks;
	}
	public Double getVariantAaverageDonation() {
		return variantAaverageDonation;
	}
	public void setVariantAaverageDonation(Double variantAaverageDonation) {
		this.variantAaverageDonation = variantAaverageDonation;
	}
	public Double getVariantBaverageDonation() {
		return variantBaverageDonation;
	}
	public void setVariantBaverageDonation(Double variantBaverageDonation) {
		this.variantBaverageDonation = variantBaverageDonation;
	}
	
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public Long getEmailcount() {
		return emailcount;
	}
	public void setEmailcount(Long emailcount) {
		this.emailcount = emailcount;
	}
	public Long getVariantAemailcount() {
		return variantAemailcount;
	}
	public void setVariantAemailcount(Long variantAemailcount) {
		this.variantAemailcount = variantAemailcount;
	}
	public Long getVariantBemailcount() {
		return variantBemailcount;
	}
	public void setVariantBemailcount(Long variantBemailcount) {
		this.variantBemailcount = variantBemailcount;
	}
	public Double getVariantAaverageRevenueperEmail() {
		return variantAaverageRevenueperEmail;
	}
	public void setVariantAaverageRevenueperEmail(Double variantAaverageRevenueperEmail) {
		this.variantAaverageRevenueperEmail = variantAaverageRevenueperEmail;
	}
	public Double getVariantBaverageRevenueperEmail() {
		return variantBaverageRevenueperEmail;
	}
	public void setVariantBaverageRevenueperEmail(Double variantBaverageRevenueperEmail) {
		this.variantBaverageRevenueperEmail = variantBaverageRevenueperEmail;
	}
	
	
	
	
	//old getters/setters
/*	public Integer getGoWinnerCountType1() {
		return goWinnerCountType1;
	}
	public void setGoWinnerCountType1(Integer goWinnerCountType1) {
		this.goWinnerCountType1 = goWinnerCountType1;
	}
	public Integer getClickWinnerCountType11() {
		return clickWinnerCountType1;
	}
	public void setClickWinnerCountType1(Integer clickWinnerCountType1) {
		this.clickWinnerCountType1 = clickWinnerCountType1;
	}
	public Integer getFullsendCountType1() {
		return fullsendCountType1;
	}
	public void setFullsendCountType1(Integer fullsendCountType1) {
		this.fullsendCountType1 = fullsendCountType1;
	}
	
	public Integer getClickWinnerCountType1() {
		return clickWinnerCountType1;
	}
	public void setClickWinnerCountType11(Integer clickWinnerCountType1) {
		this.clickWinnerCountType1 = clickWinnerCountType1;
	}
	public Integer getGoWinnerCountType2() {
		return goWinnerCountType2;
	}
	public void setGoWinnerCountType2(Integer goWinnerCountType2) {
		this.goWinnerCountType2 = goWinnerCountType2;
	}
	public Integer getClickWinnerCountType2() {
		return clickWinnerCountType2;
	}
	public void setClickWinnerCountType2(Integer clickWinnerCountType2) {
		this.clickWinnerCountType2 = clickWinnerCountType2;
	}
	
	public Integer getFullsendCountType2() {
		return fullsendCountType2;
	}
	public void setFullsendCountType2(Integer fullsendCountType2) {
		this.fullsendCountType2 = fullsendCountType2;
	}
	public Integer getGoWinnerCountType() {
		return goWinnerCountType;
	}
	public void setGoWinnerCountType(Integer goWinnerCountType) {
		this.goWinnerCountType = goWinnerCountType;
	}
	public Integer getClickWinnerCountType() {
		return clickWinnerCountType;
	}
	public void setClickWinnerCountType(Integer clickWinnerCountType) {
		this.clickWinnerCountType = clickWinnerCountType;
	}
	public Integer getFullsendCountType() {
		return fullsendCountType;
	}
	public void setFullsendCountType(Integer fullsendCountType) {
		this.fullsendCountType = fullsendCountType;
	}
	public Integer getGoWinnerCountTied() {
		return goWinnerCountTied;
	}
	public void setGoWinnerCountTied(Integer goWinnerCountTied) {
		this.goWinnerCountTied = goWinnerCountTied;
	}
	public Integer getClickWinnerCountTied() {
		return clickWinnerCountTied;
	}
	public void setClickWinnerCountTied(Integer clickWinnerCountTied) {
		this.clickWinnerCountTied = clickWinnerCountTied;
	}
	public Integer getFullsendCountTied() {
		return fullsendCountTied;
	}
	public void setFullsendCountTied(Integer fullsendCountTied) {
		this.fullsendCountTied = fullsendCountTied;
	}
	public Double getGoWinnerPercentType1() {
		return goWinnerPercentType1;
	}
	public void setGoWinnerPercentType1(Double goWinnerPercentType1) {
		this.goWinnerPercentType1 = goWinnerPercentType1;
	}
	public Double getClickWinnerPercentType1() {
		return clickWinnerPercentType1;
	}
	public void setClickWinnerPercentType1(Double clickWinnerPercentType1) {
		this.clickWinnerPercentType1 = clickWinnerPercentType1;
	}
	public Double getFullsendPercentType1() {
		return fullsendPercentType1;
	}
	public void setFullsendPercentType1(Double fullsendPercentType1) {
		this.fullsendPercentType1 = fullsendPercentType1;
	}
	public Double getGoWinnerPercentType2() {
		return goWinnerPercentType2;
	}
	public void setGoWinnerPercentType2(Double goWinnerPercentType2) {
		this.goWinnerPercentType2 = goWinnerPercentType2;
	}
	public Double getClickWinnerPercentType2() {
		return clickWinnerPercentType2;
	}
	public void setClickWinnerPercentType2(Double clickWinnerPercentType2) {
		this.clickWinnerPercentType2 = clickWinnerPercentType2;
	}
	public Double getFullsendPercentType2() {
		return fullsendPercentType2;
	}
	public void setFullsendPercentType2(Double fullsendPercentType2) {
		this.fullsendPercentType2 = fullsendPercentType2;
	}
	public Double getGoWinnerPercentTied() {
		return goWinnerPercentTied;
	}
	public void setGoWinnerPercentTied(Double goWinnerPercentTied) {
		this.goWinnerPercentTied = goWinnerPercentTied;
	}
	public Double getClickWinnerPercentTied() {
		return clickWinnerPercentTied;
	}
	public void setClickWinnerPercentTied(Double clickWinnerPercentTied) {
		this.clickWinnerPercentTied = clickWinnerPercentTied;
	}
	public Double getFullsendPercentTied() {
		return fullsendPercentTied;
	}
	public void setFullsendPercentTied(Double fullsendPercentTied) {
		this.fullsendPercentTied = fullsendPercentTied;
	}
	public String getOverallGoWinner() {
		return OverallGoWinner;
	}
	public void setOverallGoWinner(String overallGoWinner) {
		OverallGoWinner = overallGoWinner;
	}
	public String getOverallFullSendWinner() {
		return OverallFullSendWinner;
	}
	public void setOverallFullSendWinner(String overallFullSendWinner) {
		OverallFullSendWinner = overallFullSendWinner;
	}
	public String getOverallClickWinner() {
		return OverallClickWinner;
	}
	public void setOverallClickWinner(String overallClickWinner) {
		OverallClickWinner = overallClickWinner;
	}
	public Double getOverallGoWinnerPercent() {
		return OverallGoWinnerPercent;
	}
	public void setOverallGoWinnerPercent(Double overallGoWinnerPercent) {
		OverallGoWinnerPercent = overallGoWinnerPercent;
	}
	public Double getOverallFullSendWinnerPercent() {
		return OverallFullSendWinnerPercent;
	}
	public void setOverallFullSendWinnerPercent(Double overallFullSendWinnerPercent) {
		OverallFullSendWinnerPercent = overallFullSendWinnerPercent;
	}
	public Double getOverallClickWinnerPercent() {
		return OverallClickWinnerPercent;
	}
	public void setOverallClickWinnerPercent(Double overallClickWinnerPercent) {
		OverallClickWinnerPercent = overallClickWinnerPercent;
	}*/
	
}
