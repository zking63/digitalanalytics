/*package com.coding.LojoFundrasing.Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table (name="contenttest")
public class Contenttest {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String senddate;
	private String type;
	private String RecipientsList;
	private Long recipients;
	private String name;
	private String jtk;
	private String topic;
	private String test;
	private String fullistWinner;
	private String GoWinner;
	private String ClickRcvWinner;
	private String VariantA;
	private Long ARecipientNumber;
	private Double AClickRate;
	private Double AOpenRate;
	private Long AOpens;
	private Double AGiftOpens;
	private String VariantB;
	private Long BRecipientNumber;
	private Double BClickRate;
	private Long BOpens;
	private Double BGiftOpens;
	private Double BOpenRate;
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="committees_id")
    private Committees committee;
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="test_id")
    private test bigtest;
	
	public Contenttest() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenddate() {
		return senddate;
	}

	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecipientsList() {
		return RecipientsList;
	}

	public void setRecipientsList(String recipientsList) {
		RecipientsList = recipientsList;
	}

	public Long getRecipients() {
		return recipients;
	}

	public void setRecipients(Long recipients) {
		this.recipients = recipients;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJtk() {
		return jtk;
	}

	public void setJtk(String jtk) {
		this.jtk = jtk;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getFullistWinner() {
		return fullistWinner;
	}

	public void setFullistWinner(String fullistWinner) {
		this.fullistWinner = fullistWinner;
	}

	public String getGoWinner() {
		return GoWinner;
	}

	public void setGoWinner(String goWinner) {
		GoWinner = goWinner;
	}

	public String getClickRcvWinner() {
		return ClickRcvWinner;
	}

	public void setClickRcvWinner(String clickRcvWinner) {
		ClickRcvWinner = clickRcvWinner;
	}

	public String getVariantA() {
		return VariantA;
	}

	public void setVariantA(String variantA) {
		VariantA = variantA;
	}

	public Long getARecipientNumber() {
		return ARecipientNumber;
	}

	public void setARecipientNumber(Long aRecipientNumber) {
		ARecipientNumber = aRecipientNumber;
	}

	public Double getAClickRate() {
		return AClickRate;
	}

	public void setAClickRate(Double aClickRate) {
		AClickRate = aClickRate;
	}

	public Long getAOpens() {
		return AOpens;
	}

	public void setAOpens(Long aOpens) {
		AOpens = aOpens;
	}

	public Double getAGiftOpens() {
		return AGiftOpens;
	}

	public void setAGiftOpens(Double aGiftOpens) {
		AGiftOpens = aGiftOpens;
	}

	public String getVariantB() {
		return VariantB;
	}

	public void setVariantB(String variantB) {
		VariantB = variantB;
	}

	public Long getBRecipientNumber() {
		return BRecipientNumber;
	}

	public void setBRecipientNumber(Long bRecipientNumber) {
		BRecipientNumber = bRecipientNumber;
	}

	public Double getBClickRate() {
		return BClickRate;
	}

	public void setBClickRate(Double bClickRate) {
		BClickRate = bClickRate;
	}

	public Long getBOpens() {
		return BOpens;
	}

	public void setBOpens(Long bOpens) {
		BOpens = bOpens;
	}

	public Double getBGiftOpens() {
		return BGiftOpens;
	}

	public void setBGiftOpens(Double bGiftOpens) {
		BGiftOpens = bGiftOpens;
	}

	public Committees getCommittee() {
		return committee;
	}

	public void setCommittee(Committees committee) {
		this.committee = committee;
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

	public test getBigtest() {
		return bigtest;
	}

	public void setBigtest(test bigtest) {
		this.bigtest = bigtest;
	}

	public Double getAOpenRate() {
		return AOpenRate;
	}

	public void setAOpenRate(Double aOpenRate) {
		AOpenRate = aOpenRate;
	}

	public Double getBOpenRate() {
		return BOpenRate;
	}

	public void setBOpenRate(Double bOpenRate) {
		BOpenRate = bOpenRate;
	}
	
	
}*/
