package com.coding.LojoFundrasing.Models;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="data_donors")
public class DonorData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Double donoraverage;
	private Double donorsum;
	private Integer donor_contributioncount;
	private Double hpc;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="donor_id")
    private Donor datadonor;
    
    public DonorData() {
    	
    }
    

	public DonorData(Donor donor, Double donoraverage, Double donorsum, Integer donor_contributioncount, 
			Double hpc) {
		this.datadonor = donor;
		this.donoraverage = donoraverage;
		this.donorsum = donorsum;
		this.donor_contributioncount = donor_contributioncount;
		this.hpc = hpc;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Donor getDatadonor() {
		return datadonor;
	}

	public void setDatadonor(Donor datadonor) {
		this.datadonor = datadonor;
	}

	public Double getDonoraverage() {
		return donoraverage;
	}

	public void setDonoraverage(Double donoraverage) {
		this.donoraverage = donoraverage;
	}


	public Double getDonorsum() {
		return donorsum;
	}


	public void setDonorsum(Double donorsum) {
		this.donorsum = donorsum;
	}


	public Integer getDonor_contributioncount() {
		return donor_contributioncount;
	}


	public void setDonor_contributioncount(Integer donor_contributioncount) {
		this.donor_contributioncount = donor_contributioncount;
	}
	
	public Double getHpc() {
		return hpc;
	}


	public void setHpc(Double hpc) {
		this.hpc = hpc;
	}


	public String getDonorAverageFormatted() {
		if (this.donoraverage == null) {
			this.donoraverage = 0.0;
		}
		double donoraverage1 = (double) getDonoraverage();
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(donoraverage1);
	}
	public String getDonorSumFormatted() {
		if (this.donorsum == null) {
			this.donorsum = 0.0;
		}
		double donorsum1 = (double) getDonorsum();
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(donorsum1);
	}
}