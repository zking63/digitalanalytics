package com.coding.LojoFundrasing.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.Donor;
import com.coding.LojoFundrasing.Models.DonorData;
import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Repos.CommitteesRepo;
import com.coding.LojoFundrasing.Repos.DonationRepo;
import com.coding.LojoFundrasing.Repos.DonorDataRepo;
import com.coding.LojoFundrasing.Repos.DonorRepo;


@Service
public class DonorService {
	@Autowired
	private DonorRepo drepo;
	
	@Autowired
	private DonorDataRepo dondrepo;
	
	@Autowired
	private DonationRepo donationrepo;
	
	@Autowired
	private CommitteesRepo comrepo;
	
	public Donor createDonor(Donor donor) {
		System.out.println("new donor created");
		return drepo.save(donor);
	}
	public Donor updateDonor(Donor donor) {
		return drepo.save(donor);
	}
	public Donor findDonorbyEmail(String email) {
		return drepo.findBydonorEmail(email).orElse(null);
	}
	public Donor findDonorByEmailandCommittee(String email, Long committee_id) {
		return drepo.findByemailandCommittee(email, committee_id).orElse(null);
	}
	public Donor findDonorByIdandCommittee(Long id, Long committee_id) {
		return drepo.findByIDandCommittee(id, committee_id);
	}
	public List<Donor> allDonors() {
		return drepo.findAll();
	}
	public List<Donor> allDonorsinCommittee(Long committee_id) {
		return drepo.findDonorsinCommittee(committee_id);
	}
	public Donor findbyId(long id) {
		return drepo.findById(id).orElse(null);
	}
	public void delete(long id) {
		drepo.deleteById(id);
	}
	
	public Donor setUpDonorThroughUpload (String nameValue, String LNValue, String emailValue, String address, 
			String city, String country, String phone, String Zipcode, String state, Committees committee, 
			User uploader) {
		Donor donor = null;
		Boolean committteeListSet = false;
 	   if (findDonorByEmailandCommittee(emailValue, committee.getId()) == null) {
 		System.out.println("                         /////??????????????NEWWWW DONOR ");
 		donor = new Donor();
     	//set donor
     	donor.setDonorFirstName(nameValue);
     	donor.setDonorLastName(LNValue);
     	donor.setDonorEmail(emailValue);
     	donor.setUploader(uploader);
     	donor.setCommittee(committee);
     	donor.setAddress(address);
     	donor.setCity(city);
     	donor.setCountry(country);
     	donor.setPhone(phone);
     	donor.setZipcode(Zipcode);
     	donor.setState(state);
     	createDonor(donor);
		while (committteeListSet == false) {
			if (committee.getDonors() == null || committee.getDonors().size() == 0) {
				List<Donor> donorCommittees = new ArrayList<Donor>();
				donorCommittees.add(donor);
				committee.setDonors(donorCommittees);
				comrepo.save(committee);
				committteeListSet = true;
			}
			else {
				List<Donor> donorCommittees = committee.getDonors();
				donorCommittees.add(donor);
				committee.setDonors(donorCommittees);
				comrepo.save(committee);
				committteeListSet = true;
			}
		}
     	//set donor committee
     	/*donors = committee.getDonors();
     	donors.add(donor);
     	committee.setDonors(donors);
     	for (int i = 0; i < donors.size(); i++) {
     		System.out.println("                   COMMITTEE DONORS : " + donors.get(i).getDonorEmail() + ' ' + donors.get(i).getId());
     	}
     	System.out.println("UPLOADER FROM DONOR: " + donor.getUploader().getId());
     	createDonor(donor);
     	comrepo.save(committee);
     	for (int i = 0; i < donors.size(); i++) {
     		System.out.println("                   COMMITTEE DONORS after create donors: " + donors.get(i).getDonorEmail() + ' ' + donors.get(i).getId());
     	}
     	System.out.println("ID FROM Donor: " + donor.getId());
     	
     	//check donor contributions
     	List <Donation> DonorDonations = donor.getContributions();
     	if (DonorDonations != null) {
         	System.out.println("donor donations size before create donation: " + DonorDonations.size());
         	for (int j = 0; j < DonorDonations.size(); j++) {
         		System.out.println("                   DONATIONS DONORS : " + DonorDonations.get(j).getId());
         	}
     	}*/
     	return donor;
     }
     else {
     	//set up donor
     	System.out.println("                         NOT /////??????????????NEWWWW DONOR ");
     	donor = findDonorByEmailandCommittee(emailValue, committee.getId());
     	Long id = donor.getId();
     	System.out.println("ID: " + id);
     	donor.setDonorFirstName(nameValue);
     	donor.setDonorLastName(LNValue);
     	donor.setUploader(uploader);
     	donor.setAddress(address);
     	donor.setCity(city);
     	donor.setCountry(country);
     	donor.setPhone(phone);
     	donor.setZipcode(Zipcode);
     	donor.setState(state);
     	updateDonor(donor);
     	return donor;
     }
	}
	
	public Date getMostRecentdonation(Long id) {
	Donor donor = drepo.findById(id).orElse(null);
	List<Donation> contributions = donor.getContributions();
	List<Date> dates = new ArrayList<Date>();
	if (contributions.size() > 0) {
		for (int i = 0; i < contributions.size(); i++) {
			dates.add(contributions.get(i).getDondate());
			}
	}
	Date mostRecent = Collections.max(dates);
	return mostRecent;
    }
	public List<Donation> getDonationsbydonor(Long id){
		Donor donor = drepo.findById(id).orElse(null);
		List<Donation> contributions = donor.getContributions();
		return contributions;
	}
	
	public DonorData getDonorData(Donor donor, Long committee_id) {
		Long id = donor.getId();
		DonorData donordata = dondrepo.findByDonorId(id).orElse(null);
		System.out.println("Donor id in donor data: " + id);
		Double daverage = 0.0;
		Double donorsum = 0.0;
		Integer donationcount = 0;
		Double hpc = 0.0;
		Long mostrecent_donation_id = null;
		Date mostrecentDate = null;
		Date mostrecenttime = null;
		Double mostrecentamount = 0.0;
		List<DonorData> allDonordata = dondrepo.findAll();
		if (donordata != null) {
			if (dondrepo.findByDonorId(donor.getId()) != null) {
				System.out.println("donor data found: " + donordata.getId());
				System.out.println("donor id " + id);
				donordata = dondrepo.findById(donordata.getId()).orElse(null);
				Long ddid = donordata.getId();
				System.out.println("donordata id " + ddid);
				if (drepo.donordoncount(id) == 0) {
					donordata = new DonorData(donor, daverage, donorsum, donationcount, hpc);
					return dondrepo.save(donordata);
				}
				donordata = dondrepo.findById(ddid).orElse(null);
				daverage = drepo.donoraverages(id);
				donorsum = drepo.donorsums(id);
				donationcount = drepo.donordoncount(id);
				hpc = drepo.hpcvalues(id, committee_id);
				donordata.setDonoraverage(daverage);
				System.out.println("average " + daverage);
				donordata.setDonor_contributioncount(donationcount);
				System.out.println("contribution " + donationcount);
				donordata.setDonorsum(donorsum);
				System.out.println("sum " + donorsum);
				donordata.setHpc(hpc);
				mostrecent_donation_id = drepo.mostRecentDonationDate(id);
				Donation mostrecent = donationrepo.findById(mostrecent_donation_id).orElse(null);
				mostrecentDate = mostrecent.getDondate();
				mostrecentamount = mostrecent.getAmount();
				donor.setMostrecentDate(mostrecentDate);
				donor.setMostrecentamount(mostrecentamount);
				System.out.println("date " + mostrecent_donation_id);
				return dondrepo.save(donordata);
				}
			    System.out.println("make");
				return dondrepo.save(donordata);
		}
		else {
			System.out.println("new donor: " + id);
			if (drepo.donordoncount(id) == 0) {
				donordata = new DonorData(donor, daverage, donorsum, donationcount, hpc);
				return dondrepo.save(donordata);
			}
			else {
				daverage = drepo.donoraverages(id);
				donorsum = drepo.donorsums(id);
				donationcount = drepo.donordoncount(id);
				mostrecent_donation_id = drepo.mostRecentDonationDate(id);
				System.out.println("mostrecent_donation_id  " + mostrecent_donation_id);
				Donation mostrecent = donationrepo.findById(mostrecent_donation_id).orElse(null);
				mostrecentDate = mostrecent.getDondate();
				//mostrecenttime = mostrecent.getDontime();
				mostrecentamount = mostrecent.getAmount();
				donor.setMostrecentDate(mostrecentDate);
				System.out.println("mostrecentDate " + mostrecentDate);
				donor.setMostrecentamount(mostrecentamount);
				System.out.println("mostrecentamount  " + mostrecentamount);
				hpc = drepo.hpcvalues(id, committee_id);
				//donor.setMostrecenttime(mostrecenttime);
				//donor.setMostrecentDonationbyDonor(mostrecent);
				System.out.println("donor average:" + daverage);
				donordata = new DonorData(donor, daverage, donorsum, donationcount, hpc);
				//System.out.println("donordata id: " + donordata.getId());
				System.out.println("finish donordata");
				return dondrepo.save(donordata);
			}
		}
	}
	public List<Donor> orderbyDonorDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findAllWithMostRecent(startdate, enddate, committee_id);
	}
	public List<Donor> orderMostRecentbyDonorDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findAllWithMostRecentinRange(startdate, enddate, committee_id);
	}
	public List<Donor> orderMostRecentbyDonorAsc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findAllWithMostRecentDondateAfterAscinRange(startdate, enddate, committee_id);
	}
	public List<Donor> orderDonorCountDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findByContributionCountByDesc(startdate, enddate, committee_id);
	}
	public List<Donor> orderDonorCountAsc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findByContributionCountByAsc(startdate, enddate, committee_id);
	}
	public List<Donor> orderAverageAsc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findByDonorAverageByAsc(startdate, enddate, committee_id);
	}
	public List<Donor> orderAverageDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findByDonorAverageByDesc(startdate, enddate, committee_id);
	}
	public List<Donor> orderDonorsumDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findByDonorsumByDesc(startdate, enddate, committee_id);
	}
	public List<Donor> orderDonorsumAsc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.findByDonorsumByAsc(startdate, enddate, committee_id);
	}
	public List<Donor> orderMostrecentAmountDesc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.MostrecentamountSortDesc(startdate, enddate, committee_id);
	}
	public List<Donor> orderMostrecentAmountAsc(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-dd") String enddate, Long committee_id){
		return drepo.MostrecentamountSortAsc(startdate, enddate, committee_id);
	}
	/*public List<Donor> void DonorsWithinRangeList(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-d;d") String enddate, Long committee_id){
		Committees committee = comrepo.findById(committee_id).orElse(null);
		List <Donor> donors = new ArrayList<Donor>();
		for (int j = 0; j < committee.getDonors().size(); j++) {
			Donor donor = committee.getDonors().get(j);
			Long donorid = donor.getId();
			Long mostRecentinRangeId = drepo.donationswithinrange(startdate, enddate, committee_id, donorid);
			if (mostRecentinRangeId != null) {
				donors.add(donor);
				Donation mostrecent = donationrepo.findById(mostRecentinRangeId).orElse(null);
				Date mostrecentDateinRange = mostrecent.getDondate();
				Double mostrecentamountinRange = mostrecent.getAmount();
				donor.setMostRecentDateinRange(mostrecentDateinRange);
				donor.setMostrecentInrangeAmount(mostrecentamountinRange);
				drepo.save(donor);
				System.out.println(" mostrecentDateinRange " + mostrecentDateinRange);
			}
		}
		System.out.println("Donors " + donors.size());
		//return donors;
	}*/
	public void DonorsWithinRange(@Param("startdate") @DateTimeFormat(pattern ="yyyy-MM-dd") String startdate, 
			@Param("enddate") @DateTimeFormat(pattern ="yyyy-MM-d;d") String enddate, Long committee_id){
		//List<Donor> donors = DonorsWithinRangeList(startdate, enddate, committee_id);
		System.out.println(" donors within range called ");
		List<Donor> donors = drepo.findAllWithinRange(startdate, enddate, committee_id);
		System.out.println(" size of donor query " + donors.size());
		for (int i= 0; i < donors.size(); i++) {
			Long id = donors.get(i).getId();
			Donor donor = drepo.findByIDandCommittee(id, committee_id);
			
			Long mostRecentinRangeId = drepo.donationswithinrange(startdate, enddate, committee_id, id);
			Donation mostrecent = donationrepo.findById(mostRecentinRangeId).orElse(null);
			Date mostrecentDateinRange = mostrecent.getDondate();
			Double mostrecentamountinRange = mostrecent.getAmount();
			donor.setMostRecentDateinRange(mostrecentDateinRange);
			donor.setMostrecentInrangeAmount(mostrecentamountinRange);
			System.out.println(" mostrecentDateinRange " + mostrecentDateinRange);
			System.out.println(" mostrecentDateinRange FROM donor " + donor.getMostRecentDateinRange());
			
			Integer countinrange = drepo.donordoncountRange(id, startdate, enddate, committee_id);
			Double suminrange = drepo.donorsumRange(id, startdate, enddate, committee_id);
			Double avginrange = drepo.donoravgRange(id, startdate, enddate, committee_id);
			Double hpcwithinrange = drepo.hpcwithinrange(id, startdate, enddate, committee_id);
			System.out.println("BEFORE Donor " + donor.getDonorFirstName());
			System.out.println(" Count " + countinrange);
			System.out.println(" Sum " + suminrange);
			System.out.println(" HPC " + hpcwithinrange);
			System.out.println(" Average " + avginrange);
			System.out.println(" ID " + id);
			donor.setCountwithinrange(countinrange);
			donor.setSumwithinrange(suminrange);
			donor.setAveragewithinrange(avginrange);
			donor.setHpcwithinrange(hpcwithinrange);
			String name = donor.getDonorFirstName();
			Integer count = donor.getCountwithinrange();
			Double sum = donor.getSumwithinrange();
			Double average = donor.getAveragewithinrange();
			System.out.println("Donor " + name);
			System.out.println(" Count " + count);
			System.out.println(" Sum " + suminrange);
			System.out.println(" HPC " + hpcwithinrange);
			System.out.println(" Sum from donor " + sum);
			System.out.println(" Average from donor " + average);
			System.out.println(" Average " + avginrange);
			System.out.println(" ID " + donor.getId());
			drepo.save(donor);
		}
	}
}
