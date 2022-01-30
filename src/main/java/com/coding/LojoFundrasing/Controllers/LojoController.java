package com.coding.LojoFundrasing.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.Donor;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Services.CommitteeService;
import com.coding.LojoFundrasing.Services.DonationService;
import com.coding.LojoFundrasing.Services.DonorService;
import com.coding.LojoFundrasing.Services.EmailGroupService;
import com.coding.LojoFundrasing.Services.EmailService;
import com.coding.LojoFundrasing.Services.ExcelService;
import com.coding.LojoFundrasing.Services.TestService;
import com.coding.LojoFundrasing.Services.UserService;
import com.coding.LojoFundrasing.Validation.DonorValidation;
import com.coding.LojoFundrasing.Validation.UserValidation;

@Controller
public class LojoController {
	@Autowired
	private UserService uservice;
	
	@Autowired
	private UserValidation uvalidation;
	
	@Autowired
	private DonorValidation dvalidation;
	
	@Autowired
	private DonorService dservice;
	
	@Autowired
	private DonationService donservice;
	
	@Autowired
	private EmailService eservice;
	
	@Autowired 
	private CommitteeService cservice;
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private EmailGroupService egservice;
	
	@Autowired
	private TestService tservice;
	
	@RequestMapping("/")
	public String index(@ModelAttribute("user")User user, Model model) {
		model.addAttribute("committees", this.cservice.findAllCommittees());
		return "loginreg.jsp";
	}
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String registerUser(Model model, @Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		uvalidation.validate(user, result);
		if (result.hasErrors()) {
			model.addAttribute("committees", this.cservice.findAllCommittees());
			return "loginreg.jsp";
		}
		User newUser = uservice.registerUser(user);
		session.setAttribute("user_id", newUser.getId());
		return "redirect:/committees/select";
	}
	 @RequestMapping(value="/login", method=RequestMethod.POST)
	 public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirs) {
	     // if the user is authenticated, save their user id in session
		 boolean isAuthenticated = uservice.authenticateUser(email, password);
		 if(isAuthenticated) {
			 User u = uservice.findUserbyEmail(email);
			 session.setAttribute("user_id", u.getId());
			 return "redirect:/committees/select";
		 }
	     // else, add error messages and return the login page
		 else {
			 redirs.addFlashAttribute("error", "Invalid Email/Password");
			 return "redirect:/";
		 }
	 }
	 
	 @RequestMapping("/logout")
	 public String logout(HttpSession session) {
	     // invalidate session
		 session.invalidate();
	     // redirect to login page
		 return "redirect:/";
	 }
	 @RequestMapping("/committees/select")
	 public String selectCommittee(HttpSession session, Model model) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 return "selectcommittees.jsp";
	 }
	 @PostMapping("/committees/select")
	 public String selectCommitteepost(HttpSession session, Model model, @RequestParam("committee")Committees committee, 
			 String page) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 if (page == null) {
			 page = "http://localhost:8080/home";
		 }
		 System.out.println("page: " + page);
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 session.setAttribute("committee_id", committee.getId());
		 return "redirect:" + page;
	 }
	@RequestMapping("/committees/new")
	public String newCommittee(Model model, @ModelAttribute("committees")Committees committees) {
		return "newcommittee.jsp";
	}
	@PostMapping("/committees/new")
	public String createCommittee(@ModelAttribute("committees")Committees committees) {
		cservice.createCommittee(committees);
		return "home.jsp";
	}
	 @RequestMapping("/newdonor")
	 public String newDonorPage(@ModelAttribute("donor") Donor donor, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 model.addAttribute("committee", committee);
		 return "createDonor.jsp";
	 }
	 @PostMapping(value="/newdonor")
	 public String CreateDonor(@Valid @ModelAttribute("donor") Donor donor, BindingResult result, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 //System.out.println(donor.getDonorEmail());
		 dvalidation.validate(donor, result);
		 if (result.hasErrors()) {
			 System.out.println("past validate");
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("committee", committee);
			 System.out.println(result);
			 return "createDonor.jsp";
		 }
		 dservice.createDonor(donor);
		 return "redirect:/donors";
	 }
	 @RequestMapping("/donors")
	 public String donorsPage(Model model, HttpSession session, HttpServletRequest request, 
			 @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
			 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @Param("field") String field) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 String pagename = request.getRequestURL().toString();
		 System.out.println("page: " + pagename);
		 session.setAttribute("page", pagename);
		 Committees committee = cservice.findbyId(committee_id);
		List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
		 model.addAttribute("committee", committee);
		model.addAttribute("committees", committees);
		 List<Donor> donors = null;
		 if (startdateD == null) {
			 startdateD = dateFormat();
		 }
		 if (enddateD == null) {
			 enddateD = dateFormat();
		 }
		 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
		 if (field == null) {
			 field = "field";
			 donors = this.dservice.orderMostRecentbyDonorDesc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("field")) {
			 donors = this.dservice.orderMostRecentbyDonorDesc(startdateD, enddateD, committee_id);
		 }
		 System.out.println("field: " + field);
		 //desc functions
		 if (field.equals("latestdonation")) {
			 donors = this.dservice.orderMostRecentbyDonorDesc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("donationcount")) {
			 donors = this.dservice.orderDonorCountDesc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("donoraverage")) {
			 donors = this.dservice.orderAverageDesc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("donorsum")) {
			 donors = this.dservice.orderDonorsumDesc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("mostrecentamount")) {
			 donors = this.dservice.orderMostrecentAmountDesc(startdateD, enddateD, committee_id);
		 }
		 //asc functions
		 if (field.equals("latestdonationup")) {
			 donors = this.dservice.orderMostRecentbyDonorAsc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("donationcountup")) {
			 donors = this.dservice.orderDonorCountAsc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("donoraverageup")) {
			 donors = this.dservice.orderAverageAsc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("donorsumup")) {
			 donors = this.dservice.orderDonorsumAsc(startdateD, enddateD, committee_id);
		 }
		 if (field.equals("mostrecentamountup")) {
			 donors = this.dservice.orderMostrecentAmountAsc(startdateD, enddateD, committee_id);
		 }
		 System.out.println("field: " + field);
		 model.addAttribute("donor", donors);
		 model.addAttribute("startdateD", startdateD);
		 model.addAttribute("enddateD", enddateD);
		 model.addAttribute("dateFormat", dateFormat());
		 //model.addAttribute("donorswithin", this.dservice.DonorsWithinRange(startdateD, enddateD));
		 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
		 model.addAttribute("field",field);
		 return "donors.jsp";
	 }
	private String dateFormat() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}
	private String dateFormat2() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return df.format(new Date());
	}
	private String dateFormat7daysAgo() {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		Date sevenDaysAgo = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(sevenDaysAgo);
	}
	private String timeFormat() {
		SimpleDateFormat df = new SimpleDateFormat("kk:mm");
		return df.format(new Date());
	}
	@RequestMapping("/newdonation")
	 public String donationsPage(@ModelAttribute("donation") Donation donation, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 model.addAttribute("committee", committee);
		 model.addAttribute("user", user);
		 model.addAttribute("donor", this.dservice.allDonors());
		 model.addAttribute("email", this.eservice.allEmails());
		 model.addAttribute("dateFormat", dateFormat2());
		 model.addAttribute("timeFormat", timeFormat());
		 return "newdonation.jsp";
	 }
	 @PostMapping(value="/newdonation")
	 public String CreateDonation(@Valid @ModelAttribute("donation") Donation donation, BindingResult result, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 User user = uservice.findUserbyId(user_id);
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 if (result.hasErrors()) {
			 model.addAttribute("committee", committee);
			 model.addAttribute("user", user);
			 model.addAttribute("donor", this.dservice.allDonors());
			 model.addAttribute("email", this.eservice.allEmails());
			 model.addAttribute("dateFormat", dateFormat2());
			 return "newdonation.jsp";
		 }
		 //model.addAttribute("committee", committee);
		 System.out.println("donation actblue id: " + donation.getActBlueId());
		 donservice.createDonation(donation);
		 Emails email = donation.getEmailDonation();
		 Donor donor = dservice.findbyId(donation.getDonor().getId());
		// this.eservice.getEmailData(email, committee_id);
		 this.eservice.CalculateEmailData(email, committee_id);
		 this.dservice.getDonorData(donor, committee_id);
		 return "redirect:/home";
	 }
	 @RequestMapping("/newemail")
	 public String newEmailpage(@ModelAttribute("email") Emails email, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 model.addAttribute("committee", committee);
		 model.addAttribute("user", user);
		 model.addAttribute("dateFormat",  dateFormat2());
		 model.addAttribute("timeFormat", timeFormat());
		 return "newemail.jsp";
	 }
	 @PostMapping(value="/newemail")
	 public String CreateEmail(@Valid @ModelAttribute("email") Emails email, BindingResult result, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 if (result.hasErrors()) {
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("committee", committee);
			 return "newemail.jsp";
		 }
		 model.addAttribute("dateFormat", dateFormat2());
		 model.addAttribute("timeFormat", timeFormat());
		 model.addAttribute("committee", committee);
		 eservice.createEmail(email);
		 //this.eservice.getEmailData(email, committee_id);
		 this.eservice.CalculateEmailData(email, committee_id);
		 return "redirect:/emails";
	 }
	 @RequestMapping("/emails")
	 public String Emailpage(Model model, HttpSession session,
			 @Param("startdateE") @DateTimeFormat(iso = ISO.DATE) String startdateE, 
			 @Param("enddateE") @DateTimeFormat(iso = ISO.DATE) String enddateE, HttpServletRequest request, 
			 @Param("field") String field) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 List<Emails> email = null;
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 if (startdateE == null) {
			 startdateE = dateFormat7daysAgo();
		 }
		 if (enddateE == null) {
			 enddateE = dateFormat();
		 }
		 if (field == null) {
			 field = "field";
			 email = this.eservice.EmailTest(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("field")) {
			 email = this.eservice.EmailTest(startdateE, enddateE, committee_id);
		 }
		 //desc functions
		 if (field.equals("datetime")) {
			 email = eservice.EmailTest(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("average")) {
			 email = eservice.AvDesc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("sum")) {
			 email = eservice.SumDesc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("donationscount")) {
			 email = eservice.DonationsCountDesc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("donorcount")) {
			 email = eservice.DonorCountDesc(startdateE, enddateE, committee_id);
		 }
		 //asc functions
		 if (field.equals("datetimeup")) {
			 email = this.eservice.EmailTestAsc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("averageup")) {
			 email = this.eservice.AverageAsc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("sumup")) {
			 email = eservice.SumAsc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("donationscountup")) {
			 email = eservice.DonationsCountAsc(startdateE, enddateE, committee_id);
		 }
		 if (field.equals("donorcountup")) {
			 email = eservice.DonorCountAsc(startdateE, enddateE, committee_id);
		 }
		 String pagename = request.getRequestURL().toString();
		 System.out.println("page: " + pagename);
		 session.setAttribute("page", pagename);
		 Committees committee = cservice.findbyId(committee_id);
		 List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
		 model.addAttribute("committee", committee);
		 model.addAttribute("committees", committees);
		 model.addAttribute("startdateE", startdateE);
		 model.addAttribute("enddateE", enddateE);
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 model.addAttribute("email", email);
		 model.addAttribute("field",field);
		 return "emails.jsp";
	 }
	 @RequestMapping("/emails/new/group")
	 public String NewEmailGroup(@ModelAttribute("emailgroup") EmailGroup emailgroup, Model model, 
			 HttpSession session, @Param("startdateE") @DateTimeFormat(iso = ISO.DATE) String startdateE, 
			 @Param("enddateE") @DateTimeFormat(iso = ISO.DATE) String enddateE, HttpServletRequest request) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 String pagename = request.getRequestURL().toString();
		 System.out.println("page: " + pagename);
		 session.setAttribute("page", pagename);
		 if (startdateE == null) {
			 startdateE = dateFormat7daysAgo();
		 }
		 if (enddateE == null) {
			 enddateE = dateFormat();
		 }
		 Committees committee = cservice.findbyId(committee_id);
		 List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
		 model.addAttribute("startdateE", startdateE);
		 model.addAttribute("enddateE", enddateE);
		 model.addAttribute("committee", committee);
		 model.addAttribute("committees", committees);
		 model.addAttribute("emails", eservice.findEmailswithoutGroup(startdateE, enddateE, committee_id));
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 return "/emails/newgroup.jsp";
	 }
	 @PostMapping("/emails/new/group/post")
	 public String CreateEmailGroup(@ModelAttribute("emailgroup") EmailGroup emailgroup, Model model, 
			 HttpSession session, @Param("startdateE") @DateTimeFormat(iso = ISO.DATE) String startdateE, 
			 @Param("enddateE") @DateTimeFormat(iso = ISO.DATE) String enddateE, HttpServletRequest request) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 String pagename = request.getRequestURL().toString();
		 System.out.println("page: " + pagename);
		 session.setAttribute("page", pagename);
		 Committees committee = cservice.findbyId(committee_id);
		 List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
		 model.addAttribute("committee", committee);
		 model.addAttribute("committees", committees);
		 model.addAttribute("emails", eservice.findEmailswithoutGroup(startdateE, enddateE, committee_id));
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 this.egservice.createEmailGroup(emailgroup);
		 System.out.println("email group size: " + emailgroup.getEmails().size());
		 for (int i = 0; i < emailgroup.getEmails().size(); i++) {
			 Emails email = emailgroup.getEmails().get(i);
			 email.setEmailgroup(emailgroup);
			 eservice.updateEmail(email);
		 }
		 this.egservice.getEmailGroupData(emailgroup.getId(), committee_id);
		 return "redirect:/emails";
	 }
	 @RequestMapping("/donors/{id}")
	 public String showDonor(@PathVariable("id") long id, Model model, HttpSession session, @ModelAttribute("donor")Donor donor) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 if (committee == this.dservice.findbyId(id).getCommittee()) {
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("donor", this.dservice.findbyId(id));
			 model.addAttribute("committee", committee);
			 return "/donors/showdonor.jsp";
		 }
		 else {
			 return "redirect:/committees/select";
		 }
	 }
	@RequestMapping("/donors/delete/{id}")
	public String DeleteDonor(@PathVariable("id") Long id, HttpSession session, Model model) {
		Long user_id = (Long)session.getAttribute("user_id");
		if (user_id == null) {
			return "redirect:/";
		}
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 if (committee == this.dservice.findbyId(id).getCommittee()) {
			 this.dservice.delete(id);
			 return "redirect:/donors";
		 }
		 else {
			 return "redirect:/committees/select";
		 }
	}
	@RequestMapping(value="/donors/edit/{id}")
	public String EditDonor(@PathVariable("id") long id, HttpSession session, Model model) {
		Long user_id = (Long)session.getAttribute("user_id");
		if (user_id == null) {
			return "redirect:/";
		}
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 if (committee == this.dservice.findbyId(id).getCommittee()) {
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("donor", this.dservice.findbyId(id));
			 model.addAttribute("committee", committee);
			return "/donors/editdonor.jsp";
		 }
		 else {
			 return "redirect:/committees/select";
		 }
	}
	 @RequestMapping(value="/donors/edit/{id}", method=RequestMethod.POST)
	 public String UpdateDonor(@Valid @ModelAttribute("donor") Donor donor, BindingResult result, Model model, HttpSession session) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (result.hasErrors()) {
			 return "redirect:/";
		 }
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 if (committee == this.dservice.findbyId(donor.getId()).getCommittee()) {
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("donor", donor);
			 model.addAttribute("committee", committee);
			 this.dservice.updateDonor(donor);
			 return "redirect:/donors";
		 }
		 else {
			 return "redirect:/committees/select";
		 }
	 }
	 @RequestMapping("/emails/{id}")
	 public String showEmail(@PathVariable("id") long id, Model model, HttpSession session, @ModelAttribute("email")Emails email) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 Committees committee = cservice.findbyId(committee_id);
		 model.addAttribute("committee", committee);
		 if (committee == this.eservice.findEmailbyId(id).getCommittee()) {
			 model.addAttribute("user", user);
			 model.addAttribute("emails", this.eservice.findEmailbyId(id));
		 }
		 else {
			 return "redirect:/committees/select";
		 }
		 return "/emails/showemail.jsp";
	 }
	 @RequestMapping("/home")
	 public String homePage(Model model, HttpSession session, @ModelAttribute("donations")Donation donation,
			 @Param("startdate") @DateTimeFormat(iso = ISO.DATE) String startdate, 
			 @Param("enddate") @DateTimeFormat(iso = ISO.DATE) String enddate, HttpServletRequest request) {
		 Long user_id = (Long)session.getAttribute("user_id");
		 Long committee_id = (Long)session.getAttribute("committee_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		 User user = uservice.findUserbyId(user_id);
		 model.addAttribute("user", user);
		 String pagename = request.getRequestURL().toString();
		 System.out.println("page: " + pagename);
		 session.setAttribute("page", pagename);
		 Committees committee = cservice.findbyId(committee_id);
		List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
		 model.addAttribute("committee", committee);
		model.addAttribute("committees", committees);
		 if (startdate == null) {
			 startdate = dateFormat();
		 }
		 if (enddate == null) {
			 enddate = dateFormat();
		 }
		 model.addAttribute("dateFormat", dateFormat());
		 model.addAttribute("user", uservice.findUserbyId(user_id));
		 model.addAttribute("startdate", startdate);
		 model.addAttribute("enddate", enddate);
		 model.addAttribute("donations", donservice.DonTest(startdate, enddate, committee_id));
		 return "home.jsp";
	 }
		@RequestMapping("/emails/delete/{id}")
		public String DeleteEmail(@PathVariable("id") Long id, HttpSession session, Model model) {
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 Long user_id = (Long)session.getAttribute("user_id");
			 User user = uservice.findUserbyId(user_id);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			if (committee == this.eservice.findEmailbyId(id).getCommittee()) {
				 model.addAttribute("user", user);
				 model.addAttribute("emails", this.eservice.findEmailbyId(id));
				 this.eservice.delete(id);
			 }
			 else {
				 return "redirect:/committees/select";
			 }
			return "redirect:/emails";
		}
		@RequestMapping(value="/emails/edit/{id}")
		public String EditEmail(@PathVariable("id") long id, HttpSession session, Model model) {
			Long user_id = (Long)session.getAttribute("user_id");
			if (user_id == null) {
				return "redirect:/";
			}
			User user = uservice.findUserbyId(user_id);
			Long committee_id = (Long)session.getAttribute("committee_id");
			Committees committee = cservice.findbyId(committee_id);
			if (committee == this.eservice.findEmailbyId(id).getCommittee()) {
				 model.addAttribute("user", user);
				 model.addAttribute("emails", this.eservice.findEmailbyId(id));
					model.addAttribute("committee", committee);
					model.addAttribute("dateFormat", dateFormat2());
					model.addAttribute("timeFormat", timeFormat());
					model.addAttribute("email", eservice.findEmailbyId(id));
			 }
			 else {
				 return "redirect:/committees/select";
			 }
			return "/emails/editemail.jsp";
		}
		 @RequestMapping(value="/emails/edit/{id}", method=RequestMethod.POST)
		 public String UpdateEmail(@Valid @ModelAttribute("email") Emails email, BindingResult result, Model model, HttpSession session) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (result.hasErrors()) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 Long id = email.getId();
				if (committee == this.eservice.findEmailbyId(id).getCommittee()) {
					model.addAttribute("user", user);
					model.addAttribute("emails", this.eservice.findEmailbyId(id));
					model.addAttribute("committee", committee);
					model.addAttribute("dateFormat", dateFormat2());
					model.addAttribute("timeFormat", timeFormat());
					eservice.updateEmail(email);
					this.eservice.CalculateEmailData(email, committee_id);
				 }
				 else {
					 return "redirect:/committees/select";
				 }
			 return "redirect:/emails";
		 }
		 //edit and delete donations homepage
			@RequestMapping("/donations/delete/{id}")
			public String DeleteDonation(@PathVariable("id") Long id, HttpSession session, Model model) {
				 Long user_id = (Long)session.getAttribute("user_id");
				 Long committee_id = (Long)session.getAttribute("committee_id");
				 Committees committee = cservice.findbyId(committee_id);
				 model.addAttribute("committee", committee);
					if (committee == this.donservice.findDonationbyId(id).getCommittee()) {
						Donation donation = this.donservice.findDonationbyId(id);
						this.donservice.delete(id);
						Emails email = donation.getEmailDonation();
						Donor donor = donation.getDonor();
						//1
						//this.eservice.getEmailData(email, committee_id);
						this.eservice.CalculateEmailData(email, committee_id);
						this.dservice.getDonorData(donor, committee_id);
					 }
					 else {
						 return "redirect:/committees/select";
					 }
				return "redirect:/home";
			}
			@RequestMapping(value="/donations/edit/{id}")
			public String EditDonation(@PathVariable("id") long id, HttpSession session, Model model) {
				Long user_id = (Long)session.getAttribute("user_id");
				if (user_id == null) {
					return "redirect:/";
				}
				User user = uservice.findUserbyId(user_id);
				Donation donation = this.donservice.findDonationbyId(id);
				 Long committee_id = (Long)session.getAttribute("committee_id");
				 Committees committee = cservice.findbyId(committee_id);
				 model.addAttribute("committee", committee);
				if (committee == donation.getCommittee()) {
					model.addAttribute("donation", donation);
					model.addAttribute("user", user);
					model.addAttribute("donor", this.dservice.allDonors());
					model.addAttribute("email", this.eservice.allEmails());
					model.addAttribute("dateFormat", dateFormat2());
					model.addAttribute("timeFormat", timeFormat());
				 }
				 else {
					 return "redirect:/committees/select";
				 }
				return "/donations/editdonation.jsp";
			}
		 @RequestMapping(value="/donations/edit/{id}", method=RequestMethod.POST)
		 public String UpdateDonation(@Valid @ModelAttribute("donation") Donation donation, BindingResult result, Model model, HttpSession session) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 if (result.hasErrors()) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("committee", committee);
			if (committee == donation.getCommittee()) {
				model.addAttribute("committee", committee);
				 model.addAttribute("user", user);
				 model.addAttribute("donor", this.dservice.allDonors());
				 model.addAttribute("email", this.eservice.allEmails());
				 model.addAttribute("dateFormat", dateFormat2());
				 model.addAttribute("timeFormat", timeFormat());
				 Emails email = donation.getEmailDonation();
				 Donor donor = donation.getDonor();
				 System.out.println("d recurring number: " + donation.getRecurring());
				 System.out.println("d AB ID: " + donation.getActBlueId());
				 donservice.createDonation(donation);
				 //this.eservice.getEmailData(email, committee_id);
				 this.eservice.CalculateEmailData(email, committee_id);
				 this.dservice.getDonorData(donor, committee_id);
			 }
			 else {
				 return "redirect:/committees/select";
			 }
			 return "redirect:/home";
		 }
		 //edit and delete donations donor page
			@RequestMapping("/donations/delete/{id}/donor")
			public String DeleteDonationfromDonorPage(@PathVariable("id") Long id, HttpSession session, Model model) {
				Donor donor = this.donservice.findDonationbyId(id).getDonor();
				long donorid = donor.getId();
				this.donservice.delete(id);
				return "redirect:/donors/" + donorid;
			}
		 //sorting homepage
		 @RequestMapping(value="/home/sortdown")
		 public String sortdownPost(Model model, HttpSession session, @ModelAttribute("donations")Donation donation,
				 @RequestParam("startdate") @DateTimeFormat(iso = ISO.DATE) String startdate, 
				 @RequestParam("enddate") @DateTimeFormat(iso = ISO.DATE) String enddate, @RequestParam("field") String field) throws ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("dateFormat", dateFormat());
			 model.addAttribute("startdate", startdate);
			 model.addAttribute("enddate", enddate);
			 model.addAttribute("field",field);
			 List<Donation> donations = null;
			 enddate = enddate + " 23:59:00";
			 if (field.equals("amount")) {
				 donations = this.donservice.orderAmounts2(startdate, enddate, committee_id);
			 }
			 if (field.equals("datetime")) {
				 donations = this.donservice.DonTest(startdate, enddate, committee_id);
			 }
			 model.addAttribute("donations", donations);
			 return "home.jsp";
		 }
		 @RequestMapping(value="/home/sortup")
		 public String sortUpPost(Model model, HttpSession session, @ModelAttribute("donations")Donation donation,
				 @RequestParam("startdate") @DateTimeFormat(iso = ISO.DATE) String startdate, 
				 @RequestParam("enddate") @DateTimeFormat(iso = ISO.DATE) String enddate, @RequestParam("field") String field) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 model.addAttribute("dateFormat", dateFormat());
			 model.addAttribute("startdate", startdate);
			 model.addAttribute("enddate", enddate);
			 model.addAttribute("field",field);
			 List<Donation> donations = null;
			 enddate = enddate + " 23:59:00";
			 if (field.equals("amount")) {
				 donations = this.donservice.orderAmounts(startdate, enddate, committee_id);
			 }
			 if (field.equals("datetime")) {
				 donations = this.donservice.DonTestAsc(startdate, enddate, committee_id);
			 }
			 model.addAttribute("donations", donations);
			 return "home.jsp";
		 }
		 //sorting emails page
		 @RequestMapping(value="/emails/sortdown")
		 public String sortdownEmail(Model model, HttpSession session,
				 @RequestParam("startdateE") @DateTimeFormat(iso = ISO.DATE) String startdateE, 
				 @RequestParam("enddateE") @DateTimeFormat(iso = ISO.DATE) String enddateE, @RequestParam("field") String field, 
				 HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			 List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			 model.addAttribute("committees", committees);
			 model.addAttribute("user", user);
			 model.addAttribute("dateFormat", dateFormat());
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("field",field);
			 List<Emails> email = null;
			 if (field.equals("datetime")) {
				 email = eservice.EmailTest(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("average")) {
				 email = eservice.AvDesc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("sum")) {
				 email = eservice.SumDesc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("donationscount")) {
				 email = eservice.DonationsCountDesc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("donorcount")) {
				 email = eservice.DonorCountDesc(startdateE, enddateE, committee_id);
			 }
			 model.addAttribute("email", email);
			 return "emails.jsp";
		 }
		 @RequestMapping(value="/emails/sortup")
		 public String sortUpEmail(Model model, HttpSession session,
				 @Param("startdateE") @DateTimeFormat(iso = ISO.DATE) String startdateE, 
				 @Param("enddateE") @DateTimeFormat(iso = ISO.DATE) String enddateE, @Param("field") String field, 
				 HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			 List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			 model.addAttribute("committees", committees);
			 model.addAttribute("committee", committee);
			 model.addAttribute("user", user);
			 model.addAttribute("dateFormat", dateFormat());
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("field",field);
			 List<Emails> email = null;
			 if (field.equals("datetime")) {
				 email = this.eservice.EmailTestAsc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("average")) {
				 email = this.eservice.AverageAsc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("sum")) {
				 email = eservice.SumAsc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("donationscount")) {
				 email = eservice.DonationsCountAsc(startdateE, enddateE, committee_id);
			 }
			 if (field.equals("donorcount")) {
				 email = eservice.DonorCountAsc(startdateE, enddateE, committee_id);
			 }
			 model.addAttribute("email", email);
			 return "emails.jsp";
		 }
		 @RequestMapping(value="/donors/sortdown")
		 public String sortdownDonors(Model model, HttpSession session,
				 @RequestParam("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @RequestParam("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") String field) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 model.addAttribute("user", user);
			 model.addAttribute("dateFormat", dateFormat());
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("field", field);
			 List<Donor> donors = null;
			 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
			 if (field.equals("latestdonation")) {
				 donors = this.dservice.orderMostRecentbyDonorDesc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("donationcount")) {
				 donors = this.dservice.orderDonorCountDesc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("donoraverage")) {
				 donors = this.dservice.orderAverageDesc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("donorsum")) {
				 donors = this.dservice.orderDonorsumDesc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("mostrecentamount")) {
				 donors = this.dservice.orderMostrecentAmountDesc(startdateD, enddateD, committee_id);
			 }
			 model.addAttribute("donor", donors);
			 return "donors.jsp";
		 }
		 @RequestMapping(value="/donors/sortup")
		 public String sortUpDonors(Model model, HttpSession session,
				 @RequestParam("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @RequestParam("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") String field) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 model.addAttribute("user", user);
			 model.addAttribute("dateFormat", dateFormat());
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("field", field);
			 List<Donor> donors = null;
			 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
			 if (field.equals("latestdonation")) {
				 donors = this.dservice.orderMostRecentbyDonorAsc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("donationcount")) {
				 donors = this.dservice.orderDonorCountAsc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("donoraverage")) {
				 donors = this.dservice.orderAverageAsc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("donorsum")) {
				 donors = this.dservice.orderDonorsumAsc(startdateD, enddateD, committee_id);
			 }
			 if (field.equals("mostrecentamount")) {
				 donors = this.dservice.orderMostrecentAmountAsc(startdateD, enddateD, committee_id);
			 }
			 model.addAttribute("donor", donors);
			 return "donors.jsp";
		 }
		 //imports
		@RequestMapping(value="/import/donations")
		public String importdonations(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			model.addAttribute("user", user);
			return "import.jsp";
		}
		@PostMapping("/import/donations")
		public String readExcel(HttpSession session, MultipartFile file) throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 //excelService.saveFile(file);
			excelService.readData(user_id, committee, file);
			return "redirect:/home";
		}
		@RequestMapping(value="/import/emails")
		public String importEmails(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			model.addAttribute("user", user);
			return "importemails.jsp";
		}
		@PostMapping("/import/emails")
		public String readExcelemails(HttpSession session, MultipartFile file, Model model) throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			excelService.readEmailData(user_id, file, committee);
			return "redirect:/home";
		}
		@RequestMapping("/tester")
		public String tester(Model model, HttpSession session, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			for (int i = 0; i < committees.size(); i++) {
				System.out.println("committees: " + committees.get(i).getCommitteeName());
			}
			model.addAttribute("user", user);
			return "test.jsp";
		}
	    @RequestMapping("/export")
	    public String exportPage(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, HttpServletRequest request,   
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "What are you exporting?";
			 model.addAttribute("message", message);
			 Integer field = 4;
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("field", field);
			 model.addAttribute("user", user);
	        return "exporter.jsp";
	    } 
	    @RequestMapping("/export/query")
	    public String exportqueryPage(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, HttpServletRequest request,   
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "What are you exporting?";
			 model.addAttribute("message", message);
			 Integer field = 4;
			 Integer range = 0;
			 Integer type = 0;
			 model.addAttribute("type", type);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("field", field);
			 model.addAttribute("range", range);
			 model.addAttribute("user", user);
	        return "ExportQuery.jsp";
	    } 
	    @GetMapping("/export/select")
	    public String exportType(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, HttpServletRequest request,  
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 if(field == 4) {
				 String message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 return "exporter.jsp";
			 }
			 String message = "What are you exporting?";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
	        return "exporter.jsp";
	    } 
	    @GetMapping("/export/query/options")
	    public String exportQueryOptions(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, HttpServletRequest request,  
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "What are you exporting?";
			 if(field == 4) {
				 message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 return "ExportQuery.jsp";
			 }
			 String type = "Select";
			 Integer range = 0;
			 String typelabel = "Select";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("type", type);
			 model.addAttribute("range", range);
			 model.addAttribute("typelabel", typelabel);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
	        return "ExportQuery.jsp";
	    } 
	    @GetMapping("/export/query/options/range")
	    public String exportQueryRange(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, @RequestParam("range") Integer range, HttpServletRequest request,  
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "What is your parameter";
			 if(field == 4) {
				 message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 return "ExportQuery.jsp";
			 }
			 if(range == 0) {
				 message = "Please select range.";
			 }
			 String type = "Select";
			 String select = "Select";
			 String operator = "Select";
			 String operand = "Operand";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("type", type);
			 System.out.println("type in range: " + type);
			 model.addAttribute("Select", select);
			 model.addAttribute("range", range);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
	        return "ExportQuery.jsp";
	    } 
	  /*  @GetMapping("/export/query/options/type")
	    public String setexportoneQueryRange(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, @RequestParam("type") String type, @RequestParam("operator") String operator, 
				 @RequestParam("operand") String operand, @RequestParam("range") Integer range, HttpServletRequest request,  
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "What is your parameter";
			 if(field == 4) {
				 message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 return "ExportQuery.jsp";
			 }
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("range", range);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 model.addAttribute("type", type);
			 System.out.println("type in typerange: " + type);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
	        return "ExportQuery.jsp";
	    } */
	   /* @GetMapping("/export/query/options/typerange")
	    public String setexportQueryRange(@ModelAttribute("donor") Donor donor, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, @RequestParam("type") String type, @RequestParam("range") Integer range, HttpServletRequest request,  
				 HttpServletResponse response) throws IOException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "What is your parameter";
			 if(field == 4) {
				 message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 return "ExportQuery.jsp";
			 }
			 String operator = "Select";
			 String operand = "Operand";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("range", range);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 model.addAttribute("type", type);
			 System.out.println("type in typerange: " + type);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
	        return "ExportQuery.jsp";
	    } */
	    @GetMapping("/export/query/excel")
	    public String exportQueryToExcel(Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, 
				 HttpSession session, @RequestParam("field") Integer field, @RequestParam("range") Integer range, @RequestParam("type") String type, @RequestParam("operator") String operator, 
				 @RequestParam("operand") String operand, @RequestParam(value = "input", required = false) List<String> input, 
				 HttpServletResponse response) throws IOException {
			Long user_id = (Long)session.getAttribute("user_id");
	    	Long committee_id = (Long)session.getAttribute("committee_id");
	    	User user = uservice.findUserbyId(user_id);
	    	System.out.println("Start: " + startdateD);
	    	System.out.println("End: " + enddateD);
	    	System.out.println("Commmittee: " + committee_id);
	    	System.out.println("type: " + type);
	    	
	    	//lists
	    	List<Emails> emails = new ArrayList<Emails>();
	    	
			 if (field == 4) {
				 String message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("field", field);
				 model.addAttribute("enddateD", enddateD);
				 Committees committee = cservice.findbyId(committee_id);
				 model.addAttribute("committee", committee);
				 model.addAttribute("user", user);
				 return "exporter.jsp";
			 }
	    	if (field == 3) {
				 System.out.println("Donors");
				 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
				 List<Donor> donors = dservice.orderbyDonorDesc(startdateD, enddateD, committee_id);
				 excelService.exportToExcel(donors, response);
			 }
			 if (field == 2) {
				 System.out.println("Donations");
				 List<Donation> donations = donservice.DonTest(startdateD, enddateD, committee_id);
				 excelService.exportDonationsToExcel(donations, response);
			 }
			 //emails
			 if (field == 1) {
				 System.out.println("Emails");
				 emails = eservice.EmailListForExport(startdateD, enddateD, committee_id, type, operator, operand);
				excelService.exportEmailsToExcel(emails, input, response);
			 }
			 if (field == 0) {
				 System.out.println("Emails Groups");
				 List<EmailGroup> emailgroups = egservice.EmailGroupList(startdateD, enddateD, committee_id);
				 System.out.println("Emails Groups size " + emailgroups.size());
				 for (int i = 0; i < emailgroups.size(); i++) {
					 EmailGroup emailgroup = emailgroups.get(i);
					 this.egservice.getEmailGroupData(emailgroup.getId(), committee_id);
				 }
				 excelService.exportEmailGroupsToExcel(emailgroups, input, response);
			 }
			 if (field == 5) {
				 System.out.println("Test");
			     List<test> tests = tservice.findTestswithinRange(startdateD, enddateD, committee_id);
				 System.out.println("Tests size " + tests.size());
				 System.out.println("input " + input);
				 excelService.exportTestToExcel(tests, input, response);
			 }
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("range", range);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 model.addAttribute("type", type);
			 System.out.println("type in typerange: " + type);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
			 return "ExportQuery.jsp";
	    } 
	    @GetMapping("/export/excel")
	    public String exportToExcel(Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, 
				 HttpSession session, @RequestParam("field") Integer field, @RequestParam("type") Integer type, @RequestParam(value = "input", required = false) List<String> input, 
				 HttpServletResponse response) throws IOException {
			Long user_id = (Long)session.getAttribute("user_id");
	    	Long committee_id = (Long)session.getAttribute("committee_id");
	    	System.out.println("Start: " + startdateD);
	    	System.out.println("End: " + enddateD);
	    	System.out.println("Commmittee: " + committee_id);
	    	System.out.println("type: " + type);
			 if (field == 4) {
				 String message = "Please select a category to export.";
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("field", field);
				 model.addAttribute("enddateD", enddateD);
				 User user = uservice.findUserbyId(user_id);
				 Committees committee = cservice.findbyId(committee_id);
				 model.addAttribute("committee", committee);
				 model.addAttribute("user", user);
				 return "exporter.jsp";
			 }
	    	if (field == 3) {
				 System.out.println("Donors");
				 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
				 List<Donor> donors = dservice.orderbyDonorDesc(startdateD, enddateD, committee_id);
				 excelService.exportToExcel(donors, response);
			 }
			 if (field == 2) {
				 System.out.println("Donations");
				 List<Donation> donations = donservice.DonTest(startdateD, enddateD, committee_id);
				 excelService.exportDonationsToExcel(donations, response);
			 }
			 if (field == 1) {
				 System.out.println("Emails");
				 List<Emails> emails = eservice.EmailTest(startdateD, enddateD, committee_id);
				 excelService.exportEmailsToExcel(emails, input, response);
			 }
			 if (field == 0) {
				 System.out.println("Emails Groups");
				 List<EmailGroup> emailgroups = egservice.EmailGroupList(startdateD, enddateD, committee_id);
				 System.out.println("Emails Groups size " + emailgroups.size());
				 for (int i = 0; i < emailgroups.size(); i++) {
					 EmailGroup emailgroup = emailgroups.get(i);
					 this.egservice.getEmailGroupData(emailgroup.getId(), committee_id);
				 }
				 excelService.exportEmailGroupsToExcel(emailgroups, input, response);
			 }
			 if (field == 5) {
				 System.out.println("Test");
			     List<test> tests = tservice.findTestswithinRange(startdateD, enddateD, committee_id);
				 System.out.println("Tests size " + tests.size());
				 System.out.println("input " + input);
				 excelService.exportTestToExcel(tests, input, response);
			 }
			 String message = "What are you exporting?";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("enddateD", enddateD);
			 User user = uservice.findUserbyId(user_id);
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			 model.addAttribute("user", user);
			 return "export.jsp";
	    } 
		@RequestMapping(value="/import/test")
		public String importTests(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			model.addAttribute("user", user);
			return "importemails.jsp";
		}
		@PostMapping("/import/test")
		public String readExceltest(HttpSession session, MultipartFile file, Model model) throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			excelService.readTestData(user_id, file, committee);
			return "redirect:/home";
		}
	    @RequestMapping(value="/import/chairreport")
		public String importchair(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			model.addAttribute("user", user);
			return "import2.jsp";
		}
		@PostMapping("/import/chairreport")
		public String readchairreport(HttpSession session, MultipartFile file) throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 System.out.println("here");
			excelService.readChairData(file);
			return "redirect:/home";
		}
}