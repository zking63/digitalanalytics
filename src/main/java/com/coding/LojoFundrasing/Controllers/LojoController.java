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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.Donor;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.Link;
import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Services.CommitteeService;
import com.coding.LojoFundrasing.Services.DonationService;
import com.coding.LojoFundrasing.Services.DonorService;
import com.coding.LojoFundrasing.Services.EmailGroupService;
import com.coding.LojoFundrasing.Services.EmailService;
import com.coding.LojoFundrasing.Services.ExcelService;
import com.coding.LojoFundrasing.Services.LinkService;
import com.coding.LojoFundrasing.Services.QueryService;
import com.coding.LojoFundrasing.Services.TestService;
import com.coding.LojoFundrasing.Services.UserService;
import com.coding.LojoFundrasing.Services.WordService;
import com.coding.LojoFundrasing.Validation.DonorValidation;
import com.coding.LojoFundrasing.Validation.UserValidation;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

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
	
	@Autowired
	private WordService wservice;
	
	@Autowired
	private LinkService lservice;
	
	@Autowired
	private QueryService qservice;
	
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
			 page = "http://localhost:8080/emails";
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
		return "emails.jsp";
	}
	///query pages
    @GetMapping("/emails")
    public String EmailQuery(@RequestParam(value = "category", required = false) ArrayList<String> categories, Model model, @Param("startdateE") @DateTimeFormat(iso = ISO.DATE) String startdateE, 
			 @Param("enddateE") @DateTimeFormat(iso = ISO.DATE) String enddateE, 
			 HttpSession session, @Param("type") String type, @Param("operator") String operator, 
			 @Param("operand") String operand, @Param("sort") String sort, @Param("direction") String direction, Integer fundraiser, Integer survey, Integer petition, Integer other, 
			 HttpServletResponse response, HttpServletRequest request) throws IOException, InvalidFormatException, ParseException {
		Long user_id = (Long)session.getAttribute("user_id");
		 if (user_id == null) {
			 return "redirect:/";
		 }
		Long committee_id = (Long)session.getAttribute("committee_id");
    	Committees committee = cservice.findbyId(committee_id);
    	User user = uservice.findUserbyId(user_id);
		 String pagename = request.getRequestURL().toString();
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
		 System.out.println("page in query: " + pagename);
    	System.out.println("Start: " + startdateE);
    	System.out.println("End: " + enddateE);
    	System.out.println("Commmittee: " + committee_id);
    	System.out.println("type: " + type);
    	System.out.println("operator: " + operator);
    	System.out.println("operand: " + operand);
    	System.out.println("*************categories 1: " + categories);
    	 List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
    	
    	List<Predicate> predicates = new ArrayList<Predicate>();
    	List<String> operands = new ArrayList<String>();		  
    	
		 if (type == null) {
			 type = "Select";
		 }
		 if (operator == null) {
			 operator = "Select";
		 }
		 if (type.contentEquals("All")) {
			 type = "All";
			 operator = "Select";
			 operand = null;
		 }
		 if (operand == null || operand.isEmpty()) {
			 operand = null;
		 }
		 if (sort == null) {
			 sort = "date";
		 }
		 if (direction == null) {
			 direction = "desc";
		 }
		 if (fundraiser == null) {
			 fundraiser = 0;
		 }
		 if (survey == null) {
			 survey = 0;
		 }
		 if (petition == null) {
			 petition = 0;
		 }
		 if (other == null) {
			 other = 0;
		 }
		 if (categories == null || categories.isEmpty() || categories.size() == 0) {
			 categories = new ArrayList<String>();
			 if (fundraiser == 1) {
				 categories.add("Fundraiser");
			 }
			 if (survey == 1) {
				 categories.add("Survey");
			 }
			 if (petition == 1) {
				 categories.add("Petition");
			 }
			 if (other == 1) {
				 categories.add("Other");
			 }
		 }
		 if (categories != null && !categories.isEmpty() && categories.size() > 0) {
				 if (categories.contains("Fundraiser")) {
					 fundraiser = 1;
					 System.out.println("fundraiser: " + fundraiser);
				 }
				 if (categories.contains("Survey")) {
					 survey = 1;
					 System.out.println("survey: " + survey);
				 }
				 if (categories.contains("Petition")) {
					 petition = 1;
					 System.out.println("petition: " + petition);
				 }
				 if (categories.contains("Other")) {
					 other = 1;
					 System.out.println("other: " + other);
				 }
		 }
		model.addAttribute("fundraiser", fundraiser);
		model.addAttribute("survey", survey);
		model.addAttribute("petition", petition);
		model.addAttribute("other", other);

		 Integer field = 0;

		 
		 if (startdateE == null) {
			 startdateE = dateFormat7daysAgo();
		 }
		 if (enddateE == null) {
			 enddateE = dateFormat();
		 }

		 List<String> operandsList = new ArrayList<String>();

    	
    	//lists
    	
	    	System.out.println("Start: " + startdateE);
	    	System.out.println("End: " + enddateE);
	    	System.out.println("Commmittee: " + committee_id);
	    	System.out.println("type: " + type);
	    	System.out.println("operator: " + operator);
	    	System.out.println("operand: " + operand);
	    	System.out.println("*************categories 1: " + categories);
    	
		 if (operator.contentEquals("Select") && !type.contentEquals("Select")
				 && !type.contentEquals("All")) {
			 String message = "Please select an operator";
			 System.out.println("msg: " + message);
			 model.addAttribute("message", message);
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 //model.addAttribute("category", categories);
			 model.addAttribute("email", emailgroups);
			 model.addAttribute("sort", sort);
			 model.addAttribute("direction", direction);
			 return "emails.jsp";
		 }
		 System.out.println("Email Groups");
		 if (!operator.contentEquals("Select") && !type.contentEquals("Select") && !type.contentEquals("All") && !operator.contentEquals("Is blank")
				 && (operand == null || operand.isEmpty() || operand.contentEquals("Operand"))) {
			 String message = "Please select an operand";
			 System.out.println("msg: " + message);
			 model.addAttribute("message", message);
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			// model.addAttribute("category", categories);
			 model.addAttribute("email", emailgroups);
			 model.addAttribute("sort", sort);
			 model.addAttribute("direction", direction);
			 return "emails.jsp";
		 }
		 System.out.println("Email Groups");
		if (operand != null && !operand.isEmpty() && 
				 !operand.contentEquals("Operand") && (type.contentEquals("Select") || operator.contentEquals("Select") 
						 || type.contentEquals("All")) ) {
			 String message = null;
			 if (type.contentEquals("Select")) {
				 message = "Please select a search factor to find emails with operand.";
				 if (operator.contentEquals("Select") ) {
					 message = "Please select a search factor and operator to find emails with operand.";
				 }
				 System.out.println("msg: " + message);
			 }
			 else if (operator.contentEquals("Select")){
				message = "Please select an operator";
				System.out.println("msg: " + message);
			 }
			 model.addAttribute("message", message);
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			// model.addAttribute("category", categories);
			 model.addAttribute("email", emailgroups);
			 model.addAttribute("sort", sort);
			 model.addAttribute("direction", direction);
			 return "emails.jsp";
		 }
		System.out.println("Email Groups");
		if (!operator.contentEquals("Is blank") && operand != null && !operand.isEmpty() && 
				 !operand.contentEquals("Operand") && !operand.contains("'")){
			 String message = "Please put singular quote marks around each of your operands. E.g. 'operand'";
			 System.out.println("msg: " + message);
			 model.addAttribute("message", message);
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			// model.addAttribute("category", categories);
			 model.addAttribute("email", emailgroups);
			 model.addAttribute("sort", sort);
			 model.addAttribute("direction", direction);
			 return "emails.jsp";
			 
	 }


			 System.out.println("Email Groups");
			//egservice.SortEmailsandEmailGroupsId(startdateD, enddateD, committee_id);
			 //List<String> types = egservice.SortEmailsandEmailGroupsCategory(committee_id);
			// System.out.println("Emails Groups size " + map.size());
			// for (int i = 0; i < map.size(); i++) {
				 //System.out.println("id: " + map.get(i));
				 
			 //}

			//emailgroups = egservice.EmailGroupExporter(startdateD, enddateD, committee_id, type, operator, operand);
	    	
	    	emailgroups = egservice.PredPlugin(sort, direction, field, categories, operandsList, 
	    			predicates, startdateE, enddateE, committee, type, operator, operands, operand);
	    	System.out.println("Emailgroup size in controller " + emailgroups.size());
	    	System.out.println("operands " + operandsList);
	    	if (operandsList != null && operandsList.size() > 0) {
	    		model.addAttribute("operandsList", operandsList);
	    	}
	    	 model.addAttribute("email", emailgroups);
			 model.addAttribute("startdateE", startdateE);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateE", enddateE);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 //model.addAttribute("category", categories);
			 model.addAttribute("sort", sort);
			 model.addAttribute("direction", direction);
			 return "emails.jsp";
    } 
	
	
	
	/* @RequestMapping("/newdonor")
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
	 }*/
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
	/*@RequestMapping("/newdonation")
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
	 @RequestMapping("/query")
	 public String Querypage(Model model, HttpSession session,
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
		 return "query.jsp";
	 }
	    @GetMapping("/query/search")
	    public String Query(@RequestParam(value = "category", required = false) List<String> categories, HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, @RequestParam("operator") String operator, 
				 @RequestParam("operand") String operand, @RequestParam("type") String type, HttpServletRequest request,  
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
			 System.out.println("type: " + type);
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
				 return "query.jsp";
			 }

			 String select = "Select";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("type", type);
			 System.out.println("type in range: " + type);
			 model.addAttribute("Select", select);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
	        return "query.jsp";
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
		 
		 EmailGroup emailgroup = this.egservice.findEmailGroupbyId(id, committee_id);
		 
		 HashMap<String, String> map = egservice.GroupWinnerAndLoser(emailgroup);
		 
		 System.out.println("Mapping of HashMap hm2 are : "
                 + map);
		 
			String winningsender = null;
			String winningsubject = null;
			String losingsender = null;
			String losingsubject = null;
			String prospectsender = null;
			String prospectsubject = null;
			String nofullsend = null;
		 
		 
		 model.addAttribute("user", user);
		 model.addAttribute("emailgroup", emailgroup);
		 
		 if (map.containsKey("nofullsend")) {
			 nofullsend = map.get("nofullsend");
		 }
		// if (emailgroup.getGroupTest() != null && emailgroup.getGroupTest().contentEquals("SENDER")) {
			 if (map.containsKey("winningsender")) {
				 winningsender = map.get("winningsender");
			 }
			 if (map.containsKey("losingsender")) {
				 losingsender = map.get("losingsender");
			 }
			 if (map.containsKey("prospectsender")) {
				 prospectsender = map.get("prospectsender");
			 }
		// }
		// if (emailgroup.getGroupTest() != null && emailgroup.getGroupTest().contentEquals("SUBJECT")) {
			 if (map.containsKey("winningsubject")) {
				 winningsubject= map.get("winningsubject");
			 }
			 if (map.containsKey("losingsubject")) {
				losingsubject = map.get("losingsubject");
			 }
			 if (map.containsKey("prospectsubject")) {
				 prospectsubject =  map.get("prospectsubject");
			 }
		// }
		 System.out.println("nofullsend " + nofullsend);
		 System.out.println("winningsender " + winningsender);
		 System.out.println("losingsender " + losingsender);
		 System.out.println("prospectsender " + prospectsender);
		 System.out.println("winningsubject " + winningsubject);
		 System.out.println("losingsubject " + losingsubject);
		 System.out.println("prospectsubject " + prospectsubject);
		 
		 model.addAttribute("nofullsend", nofullsend);
		 model.addAttribute("winningsender", winningsender);
		 model.addAttribute("losingsender", losingsender);
		 model.addAttribute("prospectsender", prospectsender);
		 model.addAttribute("winningsubject", winningsubject);
		 model.addAttribute("losingsubject", losingsubject);
		 model.addAttribute("prospectsubject", prospectsubject );
		 return "showemail.jsp";
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
		}*/
		@RequestMapping(value="/import/emails")
		public String importEmails(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 if (user_id != 2) {
				 return "redirect:/emails";
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
			 if (user_id != 2) {
				 return "redirect:/emails";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			excelService.readEmailData(user_id, file, committee);
			return "redirect:/emails";
		}
		/*@RequestMapping("/tester")
		public String tester(Model model, HttpSession session, HttpServletRequest request) throws ParseException {
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
			return "test.jsp";
		}
		@RequestMapping(path = "/tabletest", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List<Emails> gettable() {
			System.out.println("CALLED");
		    	List<Emails> emails = eservice.listemails();
			JsonArray recordsArray = new JsonArray();
			/*for (Emails email: emails) {
				String currentRecord;
				Gson g = new Gson(); 
				String str = g.toJson(email);
				Emails p = g.fromJson(str, Emails.class);
				
				currentRecord = email.toJson();
				currentRecord = email.toJson();
				currentRecord.add("Id", new JsonPrimitive(email.getId());
				currentRecord.add("Name", new JsonPrimitive(email.getString("emailName")));
				currentRecord.add("LastName", new JsonPrimitive(employees.getString("LastName")));
				currentRecord.add("Title", new JsonPrimitive(employees.getString("Title")));
				currentRecord.add("BirthDate", new JsonPrimitive(employees.getString("BirthDate")));
				if (totalRecordsAdded == false) {
					// add the number of filtered records to the first record for client-side use
					currentRecord.add("totalRecords", new JsonPrimitive(totalRecords));
					totalRecordsAdded = true;
				}
				recordsArray.add(currentRecord);*/
		   // return emails;
		//}
	    @RequestMapping("/export")
	    public String exportqueryPage(HttpSession session, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
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
				 startdateD =dateFormat7daysAgo();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 String message = "";
			 model.addAttribute("message", message);
			 Integer field = 4;
			 String type = "Select";
			 String operator = "Select";
			 String operand = null;
			 List<String> categories = new ArrayList<>();
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 model.addAttribute("categories", categories);
	        return "ExportQuery.jsp";
	    }
	    @GetMapping("/export/query")
	    public String exportQueryRange(@RequestParam(value = "category", required = false) ArrayList<String> categories, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, 
				 HttpSession session, @Param("type") String type, @Param("operator") String operator, @Param("input") ArrayList<String> input, 
				 @Param("operand") String operand, Integer fundraiser, Integer survey, Integer petition, Integer other, 
				 HttpServletResponse response, HttpServletRequest request) throws IOException, InvalidFormatException, ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
		    	System.out.println("Start: " + startdateD);
		    	System.out.println("End: " + enddateD);
		    	System.out.println("type: " + type);
		    	System.out.println("operator: " + operator);
		    	System.out.println("operand in quuery: " + operand);
			 User user = uservice.findUserbyId(user_id);
			 model.addAttribute("user", user);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 System.out.println("type: " + type);
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			 if (startdateD == null) {
				 startdateD = dateFormat7daysAgo();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }
			 if (fundraiser == null) {
				 fundraiser = 0;
			 }
			 if (survey == null) {
				 survey = 0;
			 }
			 if (petition == null) {
				 petition = 0;
			 }
			 if (other == null) {
				 other = 0;
			 }
			 String message = "";
			 operand = null;
			 if(field == 4) {
				 message = "Please select a field to export.";
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("field", field);
				 model.addAttribute("type", type);
				 model.addAttribute("enddateD", enddateD);
				 model.addAttribute("user", user);
				 model.addAttribute("operator", operator);
				 model.addAttribute("operand", operand);
				 model.addAttribute("categories", categories);
				 return "ExportQuery.jsp";
			 }

			 String select = "Select";
			 model.addAttribute("message", message);
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("field", field);
			 model.addAttribute("type", type);
			 model.addAttribute("Select", select);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 model.addAttribute("categories", categories);
				model.addAttribute("fundraiser", fundraiser);
				model.addAttribute("survey", survey);
				model.addAttribute("petition", petition);
				model.addAttribute("other", other);
	        return "ExportQuery.jsp";
	    } 
	    @GetMapping("/export/query/excel")
	    public String exportQueryToExcel(@RequestParam(value = "category", required = false) ArrayList<String> categories, Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, @RequestParam("field") Integer field, 
				 HttpSession session, @Param("type") String type, @Param("operator") String operator, @Param("input") ArrayList<String> input, 
				 @Param("operand") String operand, Integer fundraiser, Integer survey, Integer petition, Integer other, 
				 HttpServletResponse response, HttpServletRequest request) throws IOException, InvalidFormatException, ParseException {
			Long user_id = (Long)session.getAttribute("user_id");
	    	Long committee_id = (Long)session.getAttribute("committee_id");
	    	Committees committee = cservice.findbyId(committee_id);
	    	User user = uservice.findUserbyId(user_id);
			 if (user_id == null) {
				 return "redirect:/";
			 }
	    	String pagename = request.getRequestURL().toString();
				List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
				 model.addAttribute("committee", committee);
				model.addAttribute("committees", committees);
			 System.out.println("page in query: " + pagename);
	    	System.out.println("Start: " + startdateD);
	    	System.out.println("End: " + enddateD);
	    	System.out.println("Commmittee: " + committee_id);
	    	System.out.println("type: " + type);
	    	System.out.println("operator: " + operator);
	    	System.out.println("operand: " + operand);
	    	System.out.println("*************categories 1: " + categories);
	    	 List<EmailGroup> emailgroups = new ArrayList<EmailGroup>();
	    	 List<Emails> emails = new ArrayList<Emails>();
	    	List<Predicate> predicates = new ArrayList<Predicate>();
	    	List<String> operands = new ArrayList<String>();	
	    	
	    	String sort = "date";
	    	String direction = "desc";
	    	
			 if (type == null) {
				 type = "Select";
			 }
			 if (operator == null) {
				 operator = "Select";
			 }
			 if (type.contentEquals("All")) {
				 type = "All";
				 operator = "Select";
				 operand = null;
			 }
			 if (operand == null || operand.isEmpty()) {
				 operand = null;
			 }
			 if (fundraiser == null) {
				 fundraiser = 0;
			 }
			 if (survey == null) {
				 survey = 0;
			 }
			 if (petition == null) {
				 petition = 0;
			 }
			 if (other == null) {
				 other = 0;
			 }
			 if (categories == null || categories.isEmpty() || categories.size() == 0) {
				 categories = new ArrayList<String>();
				 if (fundraiser == 1) {
					 categories.add("Fundraiser");
				 }
				 if (survey == 1) {
					 categories.add("Survey");
				 }
				 if (petition == 1) {
					 categories.add("Petition");
				 }
				 if (other == 1) {
					 categories.add("Other");
				 }
			 }
			 if (categories != null && !categories.isEmpty() && categories.size() > 0) {
					 if (categories.contains("Fundraiser")) {
						 fundraiser = 1;
						 System.out.println("fundraiser: " + fundraiser);
					 }
					 if (categories.contains("Survey")) {
						 survey = 1;
						 System.out.println("survey: " + survey);
					 }
					 if (categories.contains("Petition")) {
						 petition = 1;
						 System.out.println("petition: " + petition);
					 }
					 if (categories.contains("Other")) {
						 other = 1;
						 System.out.println("other: " + other);
					 }
			 }
			model.addAttribute("fundraiser", fundraiser);
			model.addAttribute("survey", survey);
			model.addAttribute("petition", petition);
			model.addAttribute("other", other);



			 
			 if (startdateD == null) {
				 startdateD = dateFormat7daysAgo();
			 }
			 if (enddateD == null) {
				 enddateD = dateFormat();
			 }

			 List<String> operandsList = new ArrayList<String>();

	    	
	    	//lists
	    	
		    	System.out.println("Start: " + startdateD);
		    	System.out.println("End: " + enddateD);
		    	System.out.println("Commmittee: " + committee_id);
		    	System.out.println("type: " + type);
		    	System.out.println("operator: " + operator);
		    	System.out.println("operand: " + operand);
		    	System.out.println("*************categories 1: " + categories);
	    	
			 if (operator.contentEquals("Select") && !type.contentEquals("Select")
					 && !type.contentEquals("All")) {
				 String message = "Please select an operator";
				 System.out.println("msg: " + message);
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("type", type);
				 model.addAttribute("enddateD", enddateD);
				 model.addAttribute("user", user);
				 model.addAttribute("operator", operator);
				 model.addAttribute("operand", operand);
				 //model.addAttribute("category", categories);
				 model.addAttribute("email", emailgroups);
				 model.addAttribute("sort", sort);
				 model.addAttribute("direction", direction);
				 model.addAttribute("field", field);
				 return "ExportQuery.jsp";
			 }
			
			 if (!operator.contentEquals("Select") && !type.contentEquals("Select") && !type.contentEquals("All") && !operator.contentEquals("Is blank")
					 && (operand == null || operand.isEmpty() || operand.contentEquals("Operand"))) {
				 String message = "Please select an operand";
				 System.out.println("msg: " + message);
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("type", type);
				 model.addAttribute("enddateD", enddateD);
				 model.addAttribute("user", user);
				 model.addAttribute("operator", operator);
				 model.addAttribute("operand", operand);
				 //model.addAttribute("category", categories);
				 model.addAttribute("email", emailgroups);
				 model.addAttribute("sort", sort);
				 model.addAttribute("direction", direction);
				 model.addAttribute("field", field);
				 return "ExportQuery.jsp";
			 }
	
			if (operand != null && !operand.isEmpty() && 
					 !operand.contentEquals("Operand") && (type.contentEquals("Select") || operator.contentEquals("Select") 
							 || type.contentEquals("All")) ) {
				 String message = null;
				 if (type.contentEquals("Select")) {
					 message = "Please select a search factor to find emails with operand.";
					 if (operator.contentEquals("Select") ) {
						 message = "Please select a search factor and operator to find emails with operand.";
					 }
					 System.out.println("msg: " + message);
				 }
				 else if (operator.contentEquals("Select")){
					message = "Please select an operator";
					System.out.println("msg: " + message);
				 }
				 model.addAttribute("message", message);
				 System.out.println("msg: " + message);
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("type", type);
				 model.addAttribute("enddateD", enddateD);
				 model.addAttribute("user", user);
				 model.addAttribute("operator", operator);
				 model.addAttribute("operand", operand);
				 //model.addAttribute("category", categories);
				 model.addAttribute("email", emailgroups);
				 model.addAttribute("sort", sort);
				 model.addAttribute("direction", direction);
				 model.addAttribute("field", field);
				 return "ExportQuery.jsp";
			 }
		
			if (!operator.contentEquals("Is blank") && operand != null && !operand.isEmpty() && 
					 !operand.contentEquals("Operand") && !operand.contains("'")){
				 String message = "Please put singular quote marks around each of your operands. E.g. 'operand'";
				 System.out.println("msg: " + message);
				 model.addAttribute("message", message);
				 model.addAttribute("startdateD", startdateD);
				 model.addAttribute("type", type);
				 model.addAttribute("enddateD", enddateD);
				 model.addAttribute("user", user);
				 model.addAttribute("operator", operator);
				 model.addAttribute("operand", operand);
				 //model.addAttribute("category", categories);
				 model.addAttribute("email", emailgroups);
				 model.addAttribute("sort", sort);
				 model.addAttribute("direction", direction);
				 model.addAttribute("field", field);
				 return "ExportQuery.jsp";
				 
		 }

			if (field == 0) {
				
			
				 System.out.println("Email Groups");
				//egservice.SortEmailsandEmailGroupsId(startdateD, enddateD, committee_id);
				 //List<String> types = egservice.SortEmailsandEmailGroupsCategory(committee_id);
				// System.out.println("Emails Groups size " + map.size());
				// for (int i = 0; i < map.size(); i++) {
					 //System.out.println("id: " + map.get(i));
					 
				 //}

				//emailgroups = egservice.EmailGroupExporter(startdateD, enddateD, committee_id, type, operator, operand);
		    	
		    	emailgroups = egservice.PredPlugin(sort, direction, field, categories, operandsList, 
		    			predicates, startdateD, enddateD, committee, type, operator, operands, operand);
		    	System.out.println("Emailgroup size in controller " + emailgroups.size());
		    	System.out.println("operands " + operandsList);
		    	if (operandsList != null && operandsList.size() > 0) {
		    		model.addAttribute("operandsList", operandsList);
		    	}
		    	excelService.exportEmailGroupsToExcel(emailgroups, input, response);
			}
			/* else if (field == 3) {
				 System.out.println("Donors");
				 dservice.DonorsWithinRange(startdateD, enddateD, committee_id);
				 List<Donor> donors = dservice.orderbyDonorDesc(startdateD, enddateD, committee_id);
				 excelService.exportToExcel(donors, response);
			 }
			 else if (field == 2) {
				 sort = "linkname";
				 System.out.println("Links");
				 List<Link> links = lservice.PredicateCreator(sort, direction, field, categories, operandsList, predicates, startdateD, enddateD, committee, type, operator, operandsList, operand);
				 System.out.println("Link size in controller " + links.size());
			 }*/
			 //emails
			 else if (field == 1) {
				emails = eservice.PredicateCreator(sort, direction, field, categories, operandsList, predicates, startdateD, enddateD, committee, type, operator, operands, operand);
				System.out.println("Emails size in controller " + emails.size());
				excelService.exportEmailsToExcel(emails, input, response);
			 }
			/* else if (field == 5) {
				 System.out.println("Test");
			     List<test> tests = tservice.TestExporter(startdateD, enddateD, committee_id, type, operator, operand);
				 System.out.println("Tests size " + tests.size());
				 System.out.println("input " + input);
				 excelService.exportTestToExcel(tests, input, response);
			 }*/
			 else if (field == 6) {
				 System.out.println("Report");
				 List<EmailGroup> top10revenue = egservice.top10byRevenue(startdateD, enddateD, committee_id);
				 List<EmailGroup> bottom10revenue = egservice.bottom10byRevenue(startdateD, enddateD, committee_id);
				 List<EmailGroup> top10GO = egservice.top10byGO(startdateD, enddateD, committee_id);
				 List<EmailGroup> bottom10GO= egservice.bottom10byGO(startdateD, enddateD, committee_id);
				 wservice.exportWord(top10GO, top10revenue, bottom10GO, bottom10revenue, response);
			 }
			 model.addAttribute("startdateD", startdateD);
			 model.addAttribute("type", type);
			 model.addAttribute("enddateD", enddateD);
			 model.addAttribute("user", user);
			 model.addAttribute("operator", operator);
			 model.addAttribute("operand", operand);
			 model.addAttribute("field", field);
			 //model.addAttribute("category", categories);
			 model.addAttribute("email", emailgroups);
			 model.addAttribute("sort", sort);
			 model.addAttribute("direction", direction);
			 return "ExportQuery.jsp";
	    } 
	   /* @GetMapping("/export/excel")
	    public String exportToExcel(Model model, @Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
				 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, 
				 HttpSession session, @RequestParam("field") Integer field, @RequestParam("type") Integer type, @RequestParam(value = "input", required = false) List<String> input, 
				 HttpServletResponse response) throws IOException, InvalidFormatException {
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
				 return "ExportQuery.jsp";
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
			 if (field == 6) {
				 System.out.println("Report");
				 List<EmailGroup> top10revenue = egservice.top10byRevenue(startdateD, enddateD, committee_id);
				 List<EmailGroup> bottom10revenue = egservice.bottom10byRevenue(startdateD, enddateD, committee_id);
				 List<EmailGroup> top10GO = egservice.top10byGO(startdateD, enddateD, committee_id);
				 List<EmailGroup> bottom10GO= egservice.bottom10byGO(startdateD, enddateD, committee_id);
				 wservice.exportWord(top10GO, top10revenue, bottom10GO, bottom10revenue, response);
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
	    } */
	/*	@RequestMapping(value="/import/test")
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
		}*/
	    @RequestMapping(value="/import/chairreport")
		public String importchair(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 if (user_id != 2) {
				 return "redirect:/emails";
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
			 if (user_id != 2) {
				 return "redirect:/emails";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 System.out.println("here");
			excelService.readChairData(file);
			return "redirect:/emails";
		}
	   /* @RequestMapping(value="/import/digireport")
		public String importdig(HttpSession session, Model model, HttpServletRequest request) {
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
			return "import3.jsp";
		}
		@PostMapping("/import/digireport")
		public String readdigi(HttpSession session, MultipartFile file) throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 System.out.println("here");
			excelService.reademaildata(file);
			return "redirect:/home";
		}*/
	   /* @GetMapping(value="/rundata/test")
		public void rundataontest(HttpSession session, Model model, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			List<Committees> committees = cservice.findAllexcept(committee_id, user_id);
			 model.addAttribute("committee", committee);
			model.addAttribute("committees", committees);
			model.addAttribute("user", user);
		}*/
		/*@RequestMapping("/rundata/test")
		public String runtestdata(HttpSession session) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 
			 List<test> tests = tservice.findAllTests(committee_id);
			 for (int i = 0; i < tests.size(); i++) {
				 test test = tests.get(i);
				 tservice.CalculateTestData(test, committee);
				 if (tests.get(i).getEmailcount() == 0) {
					//if( egservice.findgroupbytestid(test.getId(), committee_id).size()> 0){
					 System.out.println("delete TEST" + test.getId());
					 tests.remove(test);
					 committee.setBigtest(tests);
					 tservice.delete(test.getId());
				// }
			 }
			 }
			return "redirect:/home";
		}
		@RequestMapping("/rundata/links")
		public String runLinkdata(HttpSession session) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 
			 List<Link> links = lservice.findall(committee_id);
			 for (int i = 0; i < links.size(); i++) {
				 Link link = links.get(i);
				 lservice.CalculateLinkData(link, committee_id);
			 }
			return "redirect:/home";
		}
		/*@RequestMapping("/export/report")
		public String createReport(HttpSession session, HttpServletRequest request,   
				 HttpServletResponse response) throws IOException, InvalidFormatException {
			System.out.println("create report");
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 List<EmailGroup> emailgroups = egservice.top10byRevenue(committee_id);
			wservice.exportWord(emailgroups, response);
			return "redirect:/home";
		}*/
		/* @RequestMapping("/render/emails/{id}")
		 public String renderEmail(@PathVariable("id") long id, Model model, HttpSession session, 
				 @ModelAttribute("email")Emails email, HttpServletRequest request) {
			 Long user_id = (Long)session.getAttribute("user_id");
			 if (user_id == null) {
				 return "redirect:/";
			 }
			 User user = uservice.findUserbyId(user_id);
			 Long committee_id = (Long)session.getAttribute("committee_id");
			 Committees committee = cservice.findbyId(committee_id);
			 model.addAttribute("committee", committee);
			String html = eservice.findEmailbyId(id).getContent();
			 model.addAttribute("html", html);
			 String pagename = request.getRequestURL().toString();
			 System.out.println("page: " + pagename);
			 session.setAttribute("page", pagename);
			 if (committee == this.eservice.findEmailbyId(id).getCommittee()) {
				 model.addAttribute("user", user);
				 model.addAttribute("emails", this.eservice.findEmailbyId(id));
			 }
			 else {
				 return "redirect:/committees/select";
			 }
			 return "/emails/renderemail.jsp";
		 }*/
			/*@RequestMapping("/testquery")
			public void testquery(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
				 Long user_id = (Long)session.getAttribute("user_id");
				 if (user_id == null) {
					 response.sendRedirect("loginreg.jsp");
				 }
				 Long committee_id = (Long)session.getAttribute("committee_id");
				 Committees committee = cservice.findbyId(committee_id);
				 String type = "Content";
				 String operator = "Contains";
				 String operand = "Biden & President";
				 System.out.println("resp " + response);
				 String operand = "'Biden' + 'President' + ('approv'/'grade'/'support')";
			    	String startdateD = "2022-02-12";
			    	String enddateD = "2022-03-13";
			    	List<Predicate> predicates = new ArrayList<>();
			    	List<String> operandsList = new ArrayList<>();
			    	List<EmailGroup> emailgroups = new ArrayList<>();
			    	List<String> operands = new ArrayList<String>();
			    	List<String> categories = new ArrayList<String>();
			    	categories.add("Fundraiser");
			    	categories.add("Survey");
			    	categories.add("Petition");
			    	categories.add("Other");
			    	List<String> inputs = new ArrayList<String>();
			    	inputs.add("Recipients");
			    	System.out.println("op check: " + qservice.operandCheck(operand));
			    	emailgroups = egservice.PredPlugin(categories, operandsList, predicates, startdateD, enddateD, committee, type, operator, operands, operand);
			    	System.out.println("emailgroups: " + emailgroups);
			    	System.out.println("Emailgroup size in controller " + emailgroups.size());
			    	
			    	String operand1 = "Biden & Joe";
				 String operand2 = "approve / grade / support";
				 String operand3 = "fundraiser";
				 
				 
			    	List<String> operands1 = new ArrayList<String>();
			    	List<String> operands2 = new ArrayList<String>();
			    	List<String> operands3 = new ArrayList<String>();
			    	if (operand1.contains("&")) {
			    		operands1 = Arrays.asList(operand1.split("&", -1));
				    	if (operand1.contains("& ")) {
				    		operands1 = Arrays.asList(operand1.split("& ", -1));
				    	}
				    	else if (operand1.contains("&")) {
				    		operands1 = Arrays.asList(operand1.split("&", -1));
				    	}
				    	else {
				    		operands1.add(operand1);
				    	}
			    	}
			    	System.out.println("operands: " + operands1);
			    	String startdateD = "2022-02-12";
			    	String enddateD = "2022-03-13";
				egservice.CustomEmailListForExport(startdateD, enddateD, committee, type, operator, operands);
				eservice.CustomEmailListForExport(startdateD, enddateD, committee, type, operator, operands);
				String name = "0201, 0202";
				List<String> names = new ArrayList<String>();
		    	if (name.contains(", ")) {
		    		names = Arrays.asList(name.split(", ", -1));
		    	}
		    	else if (name.contains(",")) {
		    		names = Arrays.asList(name.split(",", -1));
		    	}
		    	else {
		    		names.add(name);
		    	}

				System.out.println("names: " + names);
				 System.out.println("names size " + names.size());
				 eservice.findEmailByName(names);
			        String page = "";
			        try {

			        } catch (Exception e) {
			          page = "home.jsp";
			        } finally {
			          page = "home.jsp";
			        }

			    RequestDispatcher dd=request.getRequestDispatcher(page);
			    try {
					dd.forward(request, response);
				} catch (ServletException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			    	
			
			}	*/ 
}