package com.coding.LojoFundrasing.Util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.coding.LojoFundrasing.Services.EmailService;
import com.coding.LojoFundrasing.Services.TestService;
import com.coding.LojoFundrasing.Services.UserService;

@Component
public class ExcelUtil {
	@Autowired
	private DonorService dservice;
	@Autowired
	private EmailService eservice;
	@Autowired
	private DonationService donservice;
	@Autowired
	private UserService uservice;
	@Autowired
	private TestService tservice;
	@Autowired 
	private CommitteeService cservice;
	
	public Double getRateFormatted(Double number) {
		if (number == null) {
			number = 0.0;
		}
		double number1 = (double) number;
		DecimalFormat df = new DecimalFormat("0.00000");
		String numberfinal = df.format(number1); 
		number = Double.valueOf(numberfinal);
		return number;
	}
	
	private String dateFormat() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}
	private void setUpDonation(String address, String city, String state,
			String Zipcode, String country, String phone, String emailValue, String nameValue,
			String LNValue, Double amount, String refcode, String refcode2, String ActBlueId, String Recurring,
			Integer Recurrence, Date date, User uploader, Committees committee, 
			int rowNumber) throws ParseException {
		
			Emails email = null;
			Donor donor = null;
			Donation donation = null;
			if (ActBlueId == null || amount == null || emailValue == null) {
				rowNumber = rowNumber +1;
				System.out.println("*****NOTHING IN THIS ROW " + rowNumber);
				return;
			}
        	
        	//set donor committee
    		donor = dservice.setUpDonorThroughUpload(nameValue, LNValue, emailValue, address, city, country, 
    				phone, Zipcode, state, committee, uploader);
        	
        	//email
        	email = eservice.setEmailThroughDonation(refcode, refcode2, committee, uploader);
        	
        	//create donation
        	Long id = donor.getId();
        	System.out.println("CREATE DONATION 1: " + ActBlueId);
        	donation = donservice.createDonationfromUpload(ActBlueId, Recurring, Recurrence, date, committee, 
        			dservice.findDonorByIdandCommittee(id, committee.getId()), amount, uploader, email, 
        			refcode, refcode2);
        	System.out.println("CREATE DONATION 2: ");
        	
        	//double saving everything
        	cservice.createCommittee(committee);
        	eservice.updateEmail(email);
        	dservice.updateDonor(donor);
        	
        	//update data
    		email = donation.getEmailDonation();
    		donor = donation.getDonor();
    		dservice.getDonorData(donor, committee.getId());
    		eservice.CalculateEmailData(email, committee.getId());
			System.out.println("NEW Id: " + donor.getId() + " Person: " + donor.getDonorFirstName() + " Email: " + donor.getDonorEmail());
	}
	
	public void getSheetDetails(String excelPath)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(excelPath));

		// Retrieving the number of sheets in the Workbook
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		System.out.println("Retrieving Sheets using for-each loop");
		
		for (Sheet sheet : workbook) {
			System.out.println("=> " + sheet.getSheetName());
		}
	}
	
	public void readExcelSheet(String excelPath, Long user_id, Committees committee)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		List<String> list = new ArrayList<String>();

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		System.out.println("workbook created");

		int x = workbook.getNumberOfSheets();
		
		System.out.println("number of sheets " + x);

		int noOfColumns = 0;
		List<Cell> headers = new ArrayList<Cell>();
		Cell header = null;
		
		// Getting the Sheet at index zero
		for (int i = 0; i < x; i++) {

			Sheet sheet1 = workbook.getSheetAt(i);
			
			System.out.println("sheet1 " + sheet1);
			Iterator<Row> rowIterator = sheet1.iterator();

			noOfColumns = sheet1.getRow(i).getLastCellNum();
			
			System.out.println("number of columns " + noOfColumns);
			

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();
			int NameColumn = 0;
			int EmailColumn = 0;
			int LastNameColumn = 0;
			int AmountColumn = 0;
			int RefcodeColumn = 0;
			int Refcode2Column = 0;
			int DateColumn = 0;
			int AbIdColumn = 0;
			int RecurringColumn = 0;
			int RecurrenceColumn = 0;
			int addressColumn = 0;
			int cityColumn = 0;
			int zipColumn = 0;
			int stateColumn = 0; 
			int countryColumn = 0;
			int phoneColumn = 0;
			User uploader = uservice.findUserbyId(user_id);
			String address = null;
			String city = null;
			String state = null;
			String Zipcode = null;
			String country = null;
			String phone = null;
			String emailValue = null;
			String nameValue = null;
			String LNValue = null;
			Double amount = null;
			String refcode = null;
			String refcode2 = null;
			String ActBlueId = null;
			String Recurring = null;
			Integer Recurrence = null;
			Date date = null;
			//Donation donation  = null;
			System.out.println("The sheet number is " + i + 1);
			// 2. Or you can use a for-each loop to iterate over the rows and columns
			System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	             Iterator<Cell> cellIterator = row.cellIterator();
	                while(cellIterator.hasNext()) {

	                   
	                	Cell cell = cellIterator.next();
						if (row.getRowNum() == 0) {
							header = cell;
							//System.out.println("Header: " + header);
							headers.add(header);
							
							String headerValue = dataFormatter.formatCellValue(header).toUpperCase();
							if (headerValue.contains("FIRST NAME")) {
								NameColumn = header.getColumnIndex();
							}
							if (headerValue.contains("LAST NAME")) {
								LastNameColumn = header.getColumnIndex();
							}
							if (headerValue.contains("EMAIL")) {
								EmailColumn = header.getColumnIndex();
								System.out.println("donor email: " + headerValue);
							}
							if (headerValue.contains("REFERENCE CODE")) {
								System.out.println("refcode: " + headerValue);
								if (headerValue.contains("REFERENCE CODE 2")) {
									Refcode2Column = header.getColumnIndex();
								}
								else {
									RefcodeColumn = header.getColumnIndex();
								}
								System.out.println("RefcodeColumn 1: " + RefcodeColumn);
								System.out.println("RefcodeColumn 2: " + Refcode2Column);
							}
							if (headerValue.contains("AMOUNT")) {
								AmountColumn = header.getColumnIndex();
							}
							if (headerValue.contains("DATE")) {
								DateColumn = header.getColumnIndex();
								System.out.println("DateColumn: " + DateColumn);
							}
							if (headerValue.contains("RECEIPT")) {
								AbIdColumn = header.getColumnIndex();
							}
							if (headerValue.contains("RECURRING")) {
								RecurringColumn = header.getColumnIndex();
							}
							if (headerValue.contains("RECURRENCE")) {
								RecurrenceColumn = header.getColumnIndex();
							}
							if (headerValue.contains("ADDR")) {
								addressColumn = header.getColumnIndex();
							}
							if (headerValue.contains("CITY")) {
								cityColumn = header.getColumnIndex();
							}
							if (headerValue.contains("STATE")) {
								stateColumn = header.getColumnIndex();
							}
							if (headerValue.contains("ZIP")) {
								zipColumn = header.getColumnIndex();
							}
							if (headerValue.contains("COUNTRY")) {
								countryColumn = header.getColumnIndex();
							}
							if (headerValue.contains("PHONE")) {
								phoneColumn = header.getColumnIndex();
							}
							//System.out.println("Headers: " + headers);
						}
						else if (row.getRowNum() > 0){
									if (cell.getColumnIndex() == EmailColumn) {
										emailValue = dataFormatter.formatCellValue(cell);
										System.out.println("Email:" + emailValue);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == NameColumn) {
										//System.out.println("Values: " + values);
										//System.out.println("NameColumn TWO: " + NameColumn);
										nameValue = dataFormatter.formatCellValue(cell);
										System.out.println(nameValue);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == LastNameColumn) {
										LNValue = dataFormatter.formatCellValue(cell);
										System.out.println(LNValue);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == AbIdColumn) {
										ActBlueId = dataFormatter.formatCellValue(cell);
										System.out.println("ActBlue Id: " + ActBlueId);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == AmountColumn) {
										String amount1 = dataFormatter.formatCellValue(cell);
										amount = Double.parseDouble(amount1); 
										System.out.println(amount);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == DateColumn) {
										//System.out.println("hit date column");
										String dateValue1 = dataFormatter.formatCellValue(cell);
										//System.out.println("date value " + dateValue1);
										if (dateValue1.contains("/")) {
											date = new SimpleDateFormat("MM/dd/yy HH:mm").parse(dateValue1);
											DateTimeFormatter formatterNew = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
											SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											String strDate = dt.format(date);
											date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
											System.out.println("Simple date: " + date);
										}
										else if(dateValue1.contains("-")) {
											date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateValue1);
										}
										//date = new SimpleDateFormat("MM/dd/YY").parse(dateValue1);
										//System.out.println("Simple date: " + date);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == RecurringColumn) {
										Recurring = dataFormatter.formatCellValue(cell);
										System.out.println("Recurring: " + Recurring);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == RecurrenceColumn) {
										Recurrence = Integer.parseInt(dataFormatter.formatCellValue(cell));
										System.out.println("Recurrence: " + Recurrence);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == addressColumn) {
										address = dataFormatter.formatCellValue(cell);
										System.out.println("Address: " + address);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == cityColumn) {
										city = dataFormatter.formatCellValue(cell);
										System.out.println("City: " + city);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == stateColumn) {
										state = dataFormatter.formatCellValue(cell);
										System.out.println("State: " + state);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == zipColumn) {
										Zipcode = dataFormatter.formatCellValue(cell);
										System.out.println("Zip: " + Zipcode);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == countryColumn) {
										country = dataFormatter.formatCellValue(cell);
										System.out.println("Country: " + country);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == phoneColumn) {
										phone = dataFormatter.formatCellValue(cell);
										System.out.println("Phone: " + phone);
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
										
									}
									else if (cell.getColumnIndex() == RefcodeColumn) {
										refcode = dataFormatter.formatCellValue(cell);
										System.out.println("Refcode: " + refcode);
										System.out.println("Column 1: " + cell.getColumnIndex());
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									else if (cell.getColumnIndex() == Refcode2Column) {
										refcode2 = dataFormatter.formatCellValue(cell);
										System.out.println("Refcode 2: " + refcode);
										System.out.println("Column 1: " + cell.getColumnIndex());
										if (cell.getColumnIndex() == noOfColumns -1) { 
											System.out.println("Column: " + noOfColumns);
											setUpDonation(address, city, state,
													Zipcode, country, phone, emailValue, nameValue,
													LNValue, amount, refcode, refcode2, ActBlueId, Recurring,
													Recurrence, date, uploader, committee, 
													row.getRowNum());
											address = null;
											state = null;
											city = null;
											Zipcode = null;
											country = null;
											phone = null;
											emailValue = null;
											nameValue = null;
											LNValue = null;
											amount = null;
											refcode = null;
											refcode2 = null;
											ActBlueId = null;
											Recurring = null;
											Recurrence = null;
											date = null;
										}
									}
									
									//row iteration end below
							}
		    	        }

	            }
		}
	}
	public void readExcelSheetEmails(String excelPath, Long user_id, Committees committee)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		List<String> list = new ArrayList<String>();

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		System.out.println("workbook created");

		int x = workbook.getNumberOfSheets();
		
		System.out.println("number of sheets " + x);

		int noOfColumns = 0;
		List<Cell> headers = new ArrayList<Cell>();
		Cell header = null;
		//Cell value = null;
		List<Cell> values = new ArrayList<Cell>();
		
		// Getting the Sheet at index zero
		for (int i = 0; i < x; i++) {

			Sheet sheet1 = workbook.getSheetAt(i);
			
			System.out.println("sheet1 " + sheet1);
			Iterator<Row> rowIterator = sheet1.iterator();

			noOfColumns = sheet1.getRow(i).getLastCellNum();
			
			System.out.println("number of columns " + noOfColumns);
			

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();
			int NameColumn = 0;
			int RefcodeColumn = 0;
			int Refcode2Column = 0;
			int DateColumn = 0;
			int listColumn = 0;
			int excludeColumn = 0;
			int openersColumn = 0;
			int bouncesColumn = 0;
			int unsubscribersColumn = 0;
			int clicksColumn = 0;
			int recipientsColumn = 0;
			int senderColumn = 0;
			int subjectColumn = 0;
			int testingColumn = 0;
			int linkColumn = 0;
			int variantColumn = 0; 
			int categoryColumn = 0;
			String sender = null;
			String subject = null;
			String testing = null;
			String link = null;
			String variant = null;
			String category = null;
			String recipientList = null;
			String excludedList = null;
			Long openers = null;
			Long bounces = null;
			Long unsubscribers = null;
			Long clicks = null;
			Long recipients = null;
			User uploader = uservice.findUserbyId(user_id);
			String nameValue = null;
			String refcode = null;
			String refcode2 = null;
			Date date = null;
			System.out.println("The sheet number is " + i + 1);
			// 2. Or you can use a for-each loop to iterate over the rows and columns
			System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	             Iterator<Cell> cellIterator = row.cellIterator();
	                while(cellIterator.hasNext()) {

	                   
	                	Cell cell = cellIterator.next();
	                	//System.out.println("CELL: " + cell.getAddress());
						if (row.getRowNum() == 0) {
							//header = cell.getAddress();
							header = cell;
							//System.out.println("Header: " + header);
							headers.add(header);
							//System.out.println("Header column: " + header.getColumn());
							
							String headerValue = dataFormatter.formatCellValue(header).toUpperCase();
							if (headerValue.contains("NAME")) {
								NameColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("DATE")) {
								DateColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("REFERENCE CODE")) {
								System.out.println("refcode: " + headerValue);
								if (headerValue.contains("REFERENCE CODE 2")) {
									Refcode2Column = header.getColumnIndex();
								}
								else {
									RefcodeColumn = header.getColumnIndex();
								}
							}
							if (headerValue.contains("RECIPIENT LIST")) {
								listColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("EXCLUDED LIST")) {
								excludeColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("OPEN")) {
								openersColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("UNSUBSCRIBE")) {
								unsubscribersColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("CLICK")) {
								clicksColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("RECIPIENTS")) {
								recipientsColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("BOUNCE")) {
								bouncesColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("LINK")) {
								linkColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("SENDER")) {
								senderColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("SUBJECT")) {
								subjectColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("TEST")) {
								testingColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("VARIANT")) {
								variantColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("CATEGORY")) {
								categoryColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
						}
						else if (row.getRowNum() > 0) {
									//Cell value = cell;
									//value = cell;
							if (cell.getColumnIndex() == NameColumn) {
								//System.out.println("Values: " + values);
								//userMap.put(headerValue, valValue);
								//System.out.println("NameColumn TWO: " + NameColumn);
								nameValue = dataFormatter.formatCellValue(cell);
								System.out.println(nameValue);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == clicksColumn) {
								String amount1 = dataFormatter.formatCellValue(cell);
								clicks = Long.parseLong(amount1); 
								System.out.println("clicks col: " + clicks);
								//System.out.println(clicks);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == recipientsColumn ) {
								String amount1 = dataFormatter.formatCellValue(cell);
								recipients = Long.parseLong(amount1); 
								//System.out.println(recipients);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == unsubscribersColumn) {
								String amount1 = dataFormatter.formatCellValue(cell);
								unsubscribers = Long.parseLong(amount1); 
								System.out.println("unsub col: " + unsubscribers);
								//System.out.println(unsubscribers);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == openersColumn) {
								String amount1 = dataFormatter.formatCellValue(cell);
								openers = Long.parseLong(amount1); 
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == bouncesColumn) {
								String amount1 = dataFormatter.formatCellValue(cell);
								bounces = Long.parseLong(amount1); 
								System.out.println("bounces col: " + bounces);
								//System.out.println(bounces);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == excludeColumn) {
								//System.out.println("Values: " + values);
								//userMap.put(headerValue, valValue);
								excludedList = dataFormatter.formatCellValue(cell);
								System.out.println(excludedList);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == listColumn) {
								recipientList = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							//new start 
							else if (cell.getColumnIndex() == categoryColumn) {
								category = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == senderColumn) {
								sender = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == subjectColumn) {
								subject = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == testingColumn) {
								testing = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == linkColumn) {
								link = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == variantColumn) {
								variant = dataFormatter.formatCellValue(cell);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == DateColumn) {
								String dateValue1 = dataFormatter.formatCellValue(cell);
								System.out.println("dateValue1: " + dateValue1);
								if (dateValue1.isEmpty()) {
									System.out.println("dateValue1: " + dateValue1);
									date = null;
									System.out.println("date: " + date);
									dateValue1 = null;
								}
								if (dateValue1 != null) {
									System.out.println("dateValue1 not null: " + dateValue1);
									date = new SimpleDateFormat("MM/dd/yy").parse(dateValue1);
									System.out.println("date: " + date);
								}
								//System.out.println("Simple date: " + date);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
							}
							else if (cell.getColumnIndex() == RefcodeColumn) {
								refcode = dataFormatter.formatCellValue(cell);
								System.out.println("refcode in reader " + refcode);
								System.out.println("col " + cell.getColumnIndex());
								System.out.println("last col " + noOfColumns);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
						}
							else if (cell.getColumnIndex() == Refcode2Column) {
								refcode2 = dataFormatter.formatCellValue(cell);
								System.out.println("refcode2 in reader " + refcode2);
								if (cell.getColumnIndex() == noOfColumns - 1) {
									eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
											clicks, recipients, uploader, nameValue, refcode, refcode2, 
											date, committee, sender, subject, category, 
											testing, variant, link, row.getRowNum());
									recipientList = null;
									excludedList = null;
									openers = null;
									bounces = null;
									unsubscribers = null;
									clicks = null;
									recipients = null;
									nameValue = null;
									refcode = null;
									refcode2 = null;
									date = null;
									sender = null;
									subject = null;
									category = null;
									testing = null;
									variant =  null;
									link = null;
								}
					}
							else if (cell.getColumnIndex() == noOfColumns - 1) {
								eservice.setUpEmailsfromUpload(recipientList, excludedList, openers, bounces, unsubscribers, 
										clicks, recipients, uploader, nameValue, refcode, refcode2, 
										date, committee, sender, subject, category, 
										testing, variant, link, row.getRowNum());
								recipientList = null;
								excludedList = null;
								openers = null;
								bounces = null;
								unsubscribers = null;
								clicks = null;
								recipients = null;
								nameValue = null;
								refcode = null;
								refcode2 = null;
								date = null;
								sender = null;
								subject = null;
								category = null;
								testing = null;
								variant =  null;
								link = null;
							}
						}
		    	        }
	                }
		}    
  }
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<Donor> donors = null;
    List<Emails> emails = null;
    List<Donation> donations = null;
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } 
        else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    
    public void exporter(List<Donor> donors, HttpServletResponse response) throws IOException{
        this.donors = donors;
        workbook = new XSSFWorkbook();
        DataFormatter dataFormatter = new DataFormatter();
        
        //write header lines
        sheet = workbook.createSheet("Donors");
        
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Id", style); 
        createCell(row, 1, "Donor email", style); 
        createCell(row, 2, "First Name", style); 
        createCell(row, 3, "Last Name", style); 
        createCell(row, 4, "Most recent donation date", style); 
        createCell(row, 5, "Most recent donation amount", style); 
        createCell(row, 6, "Average", style);
        createCell(row, 7, "Highest previous contribution", style);
        createCell(row, 8, "Donations", style); 
        createCell(row, 9, "Total given", style); 
        createCell(row, 10, "Most recent donation in range date", style); 
        createCell(row, 11, "Most recent donation in range amount", style);
        createCell(row, 12, "Average within range", style); 
        createCell(row, 13, "HPC within range", style);
        createCell(row, 14, "Count within range", style);
        createCell(row, 15, "Total given within range", style);
        
        //write data lines
        int rowCount = 1;
        CellStyle bodyStyle = workbook.createCellStyle();
        XSSFFont bodyfont = workbook.createFont();
        bodyfont.setBold(false);
        bodyfont.setFontHeight(14);
        bodyStyle.setFont(bodyfont);
                 
        for (int i = 0; i < donors.size(); i++) {
            row = sheet.createRow(rowCount++);
            int columnCount = 0;
            
            createCell(row, columnCount++, String.valueOf(donors.get(i).getId()), bodyStyle);
            createCell(row, columnCount++, donors.get(i).getDonorEmail(), bodyStyle);
            createCell(row, columnCount++, donors.get(i).getDonorFirstName(), bodyStyle);
            createCell(row, columnCount++, donors.get(i).getDonorLastName(), bodyStyle);
            createCell(row, columnCount++, donors.get(i).getRecentDateFormatted(), bodyStyle);
            createCell(row, columnCount++, donors.get(i).getDonorRecentAmountFormatted(), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getDonordata().getDonoraverage()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getDonordata().getHpc()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getDonordata().getDonor_contributioncount()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getDonordata().getDonorsum()), bodyStyle);
            createCell(row, columnCount++, donors.get(i).getRecentDateinRangeFormatted(), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getMostrecentInrangeAmount()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getAveragewithinrange()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getHpcwithinrange()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getCountwithinrange()), bodyStyle);
            createCell(row, columnCount++, String.valueOf(donors.get(i).getSumwithinrange()), bodyStyle);
        }
        //export
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
	}
    public void Emailexporter(List<Emails> emails, List<String> input, HttpServletResponse response) throws IOException{
        //column values
    	int ClickCol = 0;
    	int OpenCol = 0;
    	int BounceCol = 0;
    	int UnsubCol = 0;
    	int ClickrateCol = 0;
    	int OpenrateCol = 0;
    	int UnsubrateCol = 0;
    	int BouncerateCol = 0;
    	int ClickOpenCol = 0;
    	int RevCol = 0;
    	int DonationsCol = 0;
    	int DonorsCol =0;
    	int AvCol = 0;
    	int DonOpenCol = 0;
    	int DonClickCol = 0;
    	int DonorsOpenCol =0;
    	int DonorsClickCol = 0;
    	int DonRecurCol = 0;
    	int DonorsRecurCol =0;
    	int RevRecurCol = 0;
    	
    	int variantCol = 0;
    	int categoryCol =0;
    	int senderCol = 0;
    	int subjectCol = 0;
    	int testingCol =0;
    	int linkCol = 0;
    	
    	
    	this.emails = emails;
        workbook = new XSSFWorkbook();
        DataFormatter dataFormatter = new DataFormatter();
        
        //write header lines
        sheet = workbook.createSheet("Emails");
        
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Id", style); 
        createCell(row, 1, "Email", style); 
        createCell(row, 2, "Refcode", style); 
        createCell(row, 3, "Refcode 2", style); 
        createCell(row, 4, "Send Date", style); 
        createCell(row, 5, "List", style); 
        createCell(row, 6, "Excluded List", style); 
        createCell(row, 7, "Recipients", style);
        
        int columnCount = 8;
        Cell cell = row.createCell(columnCount);
        
        if (input == null) {
        	input = Arrays.asList("Clicks", "Opens", "Bounces","Unsubscribes","Open rate");
        }
        
            for (int i = 0; i < input.size(); i++) {
            	System.out.println("Input: " + input.get(i));
            	if (input.get(i).equals("Clicks")) {
                    ClickCol = columnCount;
                    createCell(row, columnCount++, "Clicks", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + ClickCol);
            	}
            	if (input.get(i).equals("Opens")) {
            		OpenCol = columnCount;
                    createCell(row, columnCount++, "Opens", style);
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + OpenCol);
            	}
            	if (input.get(i).equals("Bounces")) {
                    BounceCol = columnCount;
                    createCell(row, columnCount++, "Bounces", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + BounceCol);
            	}
            	if (input.get(i).equals("Unsubscribes")) {
            		UnsubCol = columnCount;
                    createCell(row, columnCount++, "Unsubscribes", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + UnsubCol);
            	}
            	if (input.get(i).equals("Open rate")) {
            		OpenrateCol = columnCount;
                    createCell(row, columnCount++, "Open rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + OpenrateCol);
            	}
            	if (input.get(i).equals("Click rate")) {
            		ClickrateCol = columnCount;
                    createCell(row, columnCount++, "Click rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + ClickrateCol);
            	}
            	if (input.get(i).equals("Unsubscribe rate")) {
            		UnsubrateCol = columnCount;
                    createCell(row, columnCount++, "Unsubscribe rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + UnsubrateCol);
            	}
            	if (input.get(i).equals("Bounce rate")) {
            		BouncerateCol = columnCount;
                    createCell(row, columnCount++, "Bounce rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + BouncerateCol);
            	}
            	if (input.get(i).equals("Clicks/opens")) {
            		ClickOpenCol = columnCount;
                    createCell(row, columnCount++, "Clicks per open", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + ClickOpenCol);
            	}
            	if (input.get(i).equals("Revenue")) {
            		RevCol = columnCount;
                    createCell(row, columnCount++, "Revenue", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + RevCol);
            	}
            	if (input.get(i).equals("Donations")) {
            		DonationsCol = columnCount;
                    createCell(row, columnCount++, "Donations", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonationsCol);
            	}
            	if (input.get(i).equals("Donors")) {
            		DonorsCol = columnCount;
                    createCell(row, columnCount++, "Donors", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsCol);
            	}
            	if (input.get(i).equals("Average donation")) {
            		AvCol = columnCount;
                    createCell(row, columnCount++, "Average donation", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + AvCol);
            	}
            	if (input.get(i).equals("Donations/open")) {
            		DonOpenCol = columnCount;
                    createCell(row, columnCount++, "Donations per open", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonOpenCol);
            	}
            	if (input.get(i).equals("Donations/click")) {
            		DonClickCol = columnCount;
                    createCell(row, columnCount++, "Donations per click", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonClickCol);
            	}
            	if (input.get(i).equals("Donors/open")) {
            		DonorsOpenCol = columnCount;
                    createCell(row, columnCount++, "Donors per open", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsOpenCol);
            	}
            	if (input.get(i).equals("Donors/click")) {
            		DonorsClickCol = columnCount;
                    createCell(row, columnCount++, "Donors per click", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsClickCol);
            	}
            	if (input.get(i).equals("Recurring donations")) {
            		DonRecurCol = columnCount;
                    createCell(row, columnCount++, "Recurring donations", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonRecurCol);
            	}
            	if (input.get(i).equals("Recurring donors")) {
            		DonorsRecurCol = columnCount;
                    createCell(row, columnCount++, "Recurring donors", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsRecurCol);
            	}
            	if (input.get(i).equals("Recurring revenue")) {
            		RevRecurCol = columnCount;
                    createCell(row, columnCount++, "Recurring revenue", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + RevRecurCol);
            	}
            	if (input.get(i).equals("variant")) {
            		variantCol = columnCount;
                    createCell(row, columnCount++, "Testing variant", style); 
            	}
            	if (input.get(i).equals("category")) {
            		categoryCol = columnCount;
                    createCell(row, columnCount++, "Category", style); 
            	}
            	if (input.get(i).equals("link")) {
            		linkCol = columnCount;
                    createCell(row, columnCount++, "Link", style); 
            	}
            	if (input.get(i).equals("sender")) {
            		senderCol = columnCount;
                    createCell(row, columnCount++, "Sender", style); 
            	}
            	if (input.get(i).equals("subject")) {
            		subjectCol = columnCount;
                    createCell(row, columnCount++, "Subject line", style); 
            	}
            	if (input.get(i).equals("testing")) {
            		testingCol = columnCount;
                    createCell(row, columnCount++, "Testing factor", style); 
            	}
            }
        
        //write data lines
        int rowCount = 1;
        CellStyle bodyStyle = workbook.createCellStyle();
        XSSFFont bodyfont = workbook.createFont();
        bodyfont.setBold(false);
        bodyfont.setFontHeight(14);
        bodyStyle.setFont(bodyfont);
                 
        for (int i = 0; i < emails.size(); i++) {
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            createCell(row, columnCount++, emails.get(i).getId(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getEmailName(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getEmailRefcode1(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getEmailRefcode2(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getEmailDateFormatted(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getList(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getExcludedList(), bodyStyle);
            createCell(row, columnCount++, emails.get(i).getRecipients(), bodyStyle);
            if (columnCount == ClickCol) {
            	createCell(row, columnCount++, emails.get(i).getClicks(), bodyStyle);
            }
            if (columnCount == OpenCol) {
            	createCell(row, columnCount++, emails.get(i).getOpeners(), bodyStyle);
            }
            if (columnCount == BounceCol) {
            	createCell(row, columnCount++, emails.get(i).getBounces(), bodyStyle);
            }
            if (columnCount == UnsubCol) {
            	createCell(row, columnCount++, emails.get(i).getUnsubscribers(), bodyStyle);
            }
            if (columnCount == OpenrateCol) {
                createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmailopenRate()), bodyStyle);
            }
            if (columnCount == ClickrateCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmailclickRate()), bodyStyle);
            }
            if (columnCount == UnsubrateCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmailunsubscribeRate()), bodyStyle);
            }
            if (columnCount == BouncerateCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getBounceRate()), bodyStyle);
            }
            if (columnCount == ClickOpenCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmailclicksOpens()), bodyStyle);
            }
            if (columnCount == RevCol) {
            	createCell(row, columnCount++, emails.get(i).getEmaildonationsum(), bodyStyle);
            }
            if (columnCount == DonationsCol) {
            	createCell(row, columnCount++, emails.get(i).getEmaildonationcount(), bodyStyle);
            }
            if (columnCount == DonorsCol) {
            	createCell(row, columnCount++, emails.get(i).getEmaildonorcount(), bodyStyle);
            }
            if (columnCount == AvCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmaildonationaverage()), bodyStyle);
            }
            if (columnCount == DonOpenCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmaildonationsOpens()), bodyStyle);
            }
            if (columnCount == DonClickCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmaildonationsClicks()), bodyStyle);
            }
            if (columnCount == DonorsOpenCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmaildonorsOpens()), bodyStyle);
            }
            if (columnCount == DonorsClickCol) {
            	createCell(row, columnCount++, getRateFormatted(emails.get(i).getEmaildonorsClicks()), bodyStyle);
            }
            if (columnCount == DonRecurCol) {
            	createCell(row, columnCount++, emails.get(i).getRecurringDonationCount(), bodyStyle);
            }
            if (columnCount == DonorsRecurCol) {
            	createCell(row, columnCount++, emails.get(i).getRecurringDonorCount(), bodyStyle);
            }
            if (columnCount == RevRecurCol) {
            	createCell(row, columnCount++, emails.get(i).getRecurringRevenue(), bodyStyle);
            }
            
            if (columnCount == variantCol) {
            	createCell(row, columnCount++, emails.get(i).getVariant(), bodyStyle);
            }
            if (columnCount == categoryCol) {
            	createCell(row, columnCount++, emails.get(i).getEmailCategory(), bodyStyle);
            }
            if (columnCount == linkCol) {
            	createCell(row, columnCount++, emails.get(i).getLink(), bodyStyle);
            }
            if (columnCount == senderCol) {
            	createCell(row, columnCount++, emails.get(i).getSender(), bodyStyle);
            }
            if (columnCount == subjectCol) {
            	createCell(row, columnCount++, emails.get(i).getSubjectLine(), bodyStyle);
            }
            if (columnCount == testingCol) {
            	createCell(row, columnCount++, emails.get(i).getTesting(), bodyStyle);
            }
        }
        //export
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
	}
    public void EmailGroupexporter(List<EmailGroup> emailgroup, List<String> input, HttpServletResponse response) throws IOException{
        //column values
    	int ClickCol = 0;
    	int OpenCol = 0;
    	int BounceCol = 0;
    	int UnsubCol = 0;
    	int ClickrateCol = 0;
    	int OpenrateCol = 0;
    	int UnsubrateCol = 0;
    	int BouncerateCol = 0;
    	int ClickOpenCol = 0;
    	int RevCol = 0;
    	int DonationsCol = 0;
    	int DonorsCol =0;
    	int AvCol = 0;
    	int DonOpenCol = 0;
    	int DonClickCol = 0;
    	int DonorsOpenCol =0;
    	int DonorsClickCol = 0;
    	int DonRecurCol = 0;
    	int DonorsRecurCol =0;
    	int RevRecurCol = 0;
    	
    	
    	//this.emailgroup = emailgroup;
        workbook = new XSSFWorkbook();
        DataFormatter dataFormatter = new DataFormatter();
        
        //write header lines
        sheet = workbook.createSheet("Email Groups");
        
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Id", style); 
        createCell(row, 1, "Email Group", style); 
        createCell(row, 2, "Recipients", style);
        
        int columnCount = 3;
        Cell cell = row.createCell(columnCount);
        
            for (int i = 0; i < input.size(); i++) {
            	System.out.println("Input: " + input.get(i));
            	if (input.get(i).equals("Clicks")) {
                    ClickCol = columnCount;
                    createCell(row, columnCount++, "Clicks", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + ClickCol);
            	}
            	if (input.get(i).equals("Opens")) {
            		OpenCol = columnCount;
                    createCell(row, columnCount++, "Opens", style);
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + OpenCol);
            	}
            	if (input.get(i).equals("Bounces")) {
                    BounceCol = columnCount;
                    createCell(row, columnCount++, "Bounces", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + BounceCol);
            	}
            	if (input.get(i).equals("Unsubscribes")) {
            		UnsubCol = columnCount;
                    createCell(row, columnCount++, "Unsubscribes", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + UnsubCol);
            	}
            	if (input.get(i).equals("Open rate")) {
            		OpenrateCol = columnCount;
                    createCell(row, columnCount++, "Open rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + OpenrateCol);
            	}
            	if (input.get(i).equals("Click rate")) {
            		ClickrateCol = columnCount;
                    createCell(row, columnCount++, "Click rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + ClickrateCol);
            	}
            	if (input.get(i).equals("Unsubscribe rate")) {
            		UnsubrateCol = columnCount;
                    createCell(row, columnCount++, "Unsubscribe rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + UnsubrateCol);
            	}
            	if (input.get(i).equals("Bounce rate")) {
            		BouncerateCol = columnCount;
                    createCell(row, columnCount++, "Bounce rate", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + BouncerateCol);
            	}
            	if (input.get(i).equals("Clicks/opens")) {
            		ClickOpenCol = columnCount;
                    createCell(row, columnCount++, "Clicks per open", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + ClickOpenCol);
            	}
            	if (input.get(i).equals("Revenue")) {
            		RevCol = columnCount;
                    createCell(row, columnCount++, "Revenue", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + RevCol);
            	}
            	if (input.get(i).equals("Donations")) {
            		DonationsCol = columnCount;
                    createCell(row, columnCount++, "Donations", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonationsCol);
            	}
            	if (input.get(i).equals("Donors")) {
            		DonorsCol = columnCount;
                    createCell(row, columnCount++, "Donors", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsCol);
            	}
            	if (input.get(i).equals("Average donation")) {
            		AvCol = columnCount;
                    createCell(row, columnCount++, "Average donation", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + AvCol);
            	}
            	if (input.get(i).equals("Donations/open")) {
            		DonOpenCol = columnCount;
                    createCell(row, columnCount++, "Donations per open", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonOpenCol);
            	}
            	if (input.get(i).equals("Donations/click")) {
            		DonClickCol = columnCount;
                    createCell(row, columnCount++, "Donations per click", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonClickCol);
            	}
            	if (input.get(i).equals("Donors/open")) {
            		DonorsOpenCol = columnCount;
                    createCell(row, columnCount++, "Donors per open", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsOpenCol);
            	}
            	if (input.get(i).equals("Donors/click")) {
            		DonorsClickCol = columnCount;
                    createCell(row, columnCount++, "Donors per click", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsClickCol);
            	}
            	if (input.get(i).equals("Recurring donations")) {
            		DonRecurCol = columnCount;
                    createCell(row, columnCount++, "Recurring donations", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonRecurCol);
            	}
            	if (input.get(i).equals("Recurring donors")) {
            		DonorsRecurCol = columnCount;
                    createCell(row, columnCount++, "Recurring donors", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + DonorsRecurCol);
            	}
            	if (input.get(i).equals("Recurring revenue")) {
            		RevRecurCol = columnCount;
                    createCell(row, columnCount++, "Recurring revenue", style); 
                    System.out.println("Input 2: " + input.get(i));
                    System.out.println("Column logged: " + RevRecurCol);
            	}
            }
        
        //write data lines
        int rowCount = 1;
        CellStyle bodyStyle = workbook.createCellStyle();
        XSSFFont bodyfont = workbook.createFont();
        bodyfont.setBold(false);
        bodyfont.setFontHeight(14);
        bodyStyle.setFont(bodyfont);
                 
        for (int i = 0; i < emailgroup.size(); i++) {
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            createCell(row, columnCount++, emailgroup.get(i).getId(), bodyStyle);
            createCell(row, columnCount++, emailgroup.get(i).getEmailgroupName(), bodyStyle);
            createCell(row, columnCount++, emailgroup.get(i).getGroupRecipients(), bodyStyle);
            if (columnCount == ClickCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupClicks(), bodyStyle);
            }
            if (columnCount == OpenCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupOpeners(), bodyStyle);
            }
            if (columnCount == BounceCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupBounces(), bodyStyle);
            }
            if (columnCount == UnsubCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupUnsubscribers(), bodyStyle);
            }
            if (columnCount == OpenrateCol) {
                createCell(row, columnCount++, getRateFormatted(emailgroup.get(i).getGroupopenRate()), bodyStyle);
            }
            if (columnCount == ClickrateCol) {
            	createCell(row, columnCount++, getRateFormatted(emailgroup.get(i).getGroupclickRate()), bodyStyle);
            }
            if (columnCount == UnsubrateCol) {
            	createCell(row, columnCount++, getRateFormatted(emailgroup.get(i).getGroupunsubscribeRate()), bodyStyle);
            }
            if (columnCount == BouncerateCol) {
            	createCell(row, columnCount++, getRateFormatted(emailgroup.get(i).getGroupbounceRate()), bodyStyle);
            }
            if (columnCount == ClickOpenCol) {
            	createCell(row, columnCount++, getRateFormatted(emailgroup.get(i).getGroupclicksOpens()), bodyStyle);
            }
            if (columnCount == RevCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupsum(), bodyStyle);
            }
            if (columnCount == DonationsCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupdonationcount(), bodyStyle);
            }
            if (columnCount == DonorsCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupdonorcount(), bodyStyle);
            }
            if (columnCount == AvCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupaverage(), bodyStyle);
            }
            if (columnCount == DonOpenCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupdonationsOpens(), bodyStyle);
            }
            if (columnCount == DonClickCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupdonationsClicks(), bodyStyle);
            }
            if (columnCount == DonorsOpenCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupdonorsOpens(), bodyStyle);
            }
            if (columnCount == DonorsClickCol) {
            	createCell(row, columnCount++, getRateFormatted(emailgroup.get(i).getGroupdonorsClicks()), bodyStyle);
            }
            if (columnCount == DonRecurCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupRecurringDonationCount(), bodyStyle);
            }
            if (columnCount == DonorsRecurCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupRecurringDonorCount(), bodyStyle);
            }
            if (columnCount == RevRecurCol) {
            	createCell(row, columnCount++, emailgroup.get(i).getGroupRecurringRevenue(), bodyStyle);
            }
        }
        //export
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
	}
    public void Donationexporter(List<Donation> donations, HttpServletResponse response) throws IOException{
        this.donations = donations;
        workbook = new XSSFWorkbook();
        DataFormatter dataFormatter = new DataFormatter();
        
        //write header lines
        sheet = workbook.createSheet("Emails");
        
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Donation Id", style); 
        createCell(row, 1, "Amount", style); 
        createCell(row, 2, "Date", style); 
        createCell(row, 3, "Refcode", style);
        createCell(row, 4, "Donor Email", style); 
        createCell(row, 5, "Donor First Name", style); 
        createCell(row, 6, "Donor Last Name", style); 
        createCell(row, 7, "Donor Id", style);
        createCell(row, 8, "Donor Address", style); 
        createCell(row, 9, "Donor City", style);
        createCell(row, 10, "Donor State", style); 
        createCell(row, 11, "Donor Zipcode", style); 
        createCell(row, 12, "Donor Country", style); 
        createCell(row, 13, "Donor Phone", style);
        
        //write data lines
        int rowCount = 1;
        CellStyle bodyStyle = workbook.createCellStyle();
        XSSFFont bodyfont = workbook.createFont();
        bodyfont.setBold(false);
        bodyfont.setFontHeight(14);
        bodyStyle.setFont(bodyfont);
                 
        for (int i = 0; i < donations.size(); i++) {
            row = sheet.createRow(rowCount++);
            int columnCount = 0;
            
            createCell(row, columnCount++, donations.get(i).getId(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getAmount(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonationDateFormatted(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getEmailDonation().getEmailRefcode1(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getDonorEmail(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getDonorFirstName(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getDonorLastName(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getId(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getAddress(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getCity(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getState(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getZipcode(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getCountry(), bodyStyle);
            createCell(row, columnCount++, donations.get(i).getDonor().getPhone(), bodyStyle);
        }
        //export
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
	}
    
	public void Testexporter(List<test> bigtest, List<String> input, HttpServletResponse response) throws IOException{
	    int testcategoryCol = 0;
    	int ClickCol = 0;
    	int OpenCol = 0;
    	int emailcountCol = 0;
    	int AvEmailCol = 0;
    	int ClickrateCol = 0;
    	int OpenrateCol = 0;
    	int ClickOpenCol = 0;
    	int RevCol = 0;
    	int DonationsCol = 0;
    	int AvCol = 0;
    	int DonOpenCol = 0;
    	int DonClickCol = 0;
    	int RecipientsCol = 0;
	    
		
		//this.bigtest = bigtest;
        workbook = new XSSFWorkbook();
        DataFormatter dataFormatter = new DataFormatter();
        
        //write header lines
        sheet = workbook.createSheet("Emails");
        
        Row row = sheet.createRow(0);
        Row rowB = sheet.createRow(1);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        row.setRowStyle(style);
         
        createCell(row, 0, "Test Id", style); 
        createCell(row, 1, "Testing factor", style); 
        createCell(row, 2, "Variant", style); 
       
        
        int columnCount = 3;
        for (int i = 0; i < input.size(); i++) {
        	System.out.println("Input: " + input.get(i));
        	if (input.get(i).equals("Clicks")) {
                ClickCol = columnCount;
                createCell(row, columnCount++, "Clicks", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + ClickCol);
        	}
        	if (input.get(i).equals("Opens")) {
        		OpenCol = columnCount;
                createCell(row, columnCount++, "Opens", style);
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + OpenCol);
        	}
        	if (input.get(i).equals("emailcount")) {
                emailcountCol = columnCount;
                createCell(row, columnCount++, "Number of emails tested", style); 
                System.out.println("Input 2: " + input.get(i));
        	}
        	if (input.get(i).equals("testcategory")) {
        		testcategoryCol = columnCount;
                createCell(row, columnCount++, "Test category", style); 
                System.out.println("Input 2: " + input.get(i));
        	}
        	if (input.get(i).equals("Open rate")) {
        		OpenrateCol = columnCount;
                createCell(row, columnCount++, "Open rate", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + OpenrateCol);
        	}
        	if (input.get(i).equals("Click rate")) {
        		ClickrateCol = columnCount;
                createCell(row, columnCount++, "Click rate", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + ClickrateCol);
        	}
        	if (input.get(i).equals("Clicks/opens")) {
        		ClickOpenCol = columnCount;
                createCell(row, columnCount++, "Clicks per open", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + ClickOpenCol);
        	}
        	if (input.get(i).equals("Revenue")) {
        		RevCol = columnCount;
                createCell(row, columnCount++, "Revenue", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + RevCol);
        	}
        	if (input.get(i).equals("Donations")) {
        		DonationsCol = columnCount;
                createCell(row, columnCount++, "Donations", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + DonationsCol);
        	}
        	if (input.get(i).equals("Average donation")) {
        		AvCol = columnCount;
                createCell(row, columnCount++, "Average donation", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + AvCol);
        	}
        	if (input.get(i).equals("Average email revenue")) {
        		AvEmailCol = columnCount;
                createCell(row, columnCount++, "Average email revenue", style); 
                System.out.println("Input 2: " + input.get(i));
        	}
        	if (input.get(i).equals("Donations/open")) {
        		DonOpenCol = columnCount;
                createCell(row, columnCount++, "Donations per open", style); 
                System.out.println("Input 2: " + input.get(i));
        	}
        	if (input.get(i).equals("Donations/click")) {
        		DonClickCol = columnCount;
                createCell(row, columnCount++, "Donations per click", style); 
                System.out.println("Input 2: " + input.get(i));
                System.out.println("Column logged: " + DonClickCol);
        	}
        	if (input.get(i).equals("Recipients")) {
        		RecipientsCol = columnCount;
                createCell(row, columnCount++, "Recipients", style); 
        	}
        }
        //write data lines
        int rowCount = 1;
        int rowBCount = 2;
        CellStyle bodyStyleA = workbook.createCellStyle();
        bodyStyleA.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        bodyStyleA.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        
        CellStyle bodyStyle = workbook.createCellStyle();
        CellStyle cellstyle = workbook.createCellStyle();
        CellStyle boldcellstyle = workbook.createCellStyle();
        XSSFFont bodyfont = workbook.createFont();
        XSSFFont boldfont = workbook.createFont();
        bodyfont.setBold(false);
        bodyfont.setFontHeight(14);
        boldfont.setBold(true);
        boldfont.setFontHeight(14);
        bodyStyle.setFont(bodyfont);
        bodyStyleA.setFont(bodyfont);
        cellstyle.setFont(bodyfont);
        boldcellstyle.setFont(boldfont);
        
        row.setRowStyle(bodyStyleA);
        rowB.setRowStyle(bodyStyleA);
        System.out.println("row: " +rowCount);
        for (int i = 0; i < bigtest.size(); i++) {
        	System.out.println("bigtest: " + bigtest.size());
            row = sheet.createRow(rowCount);
            rowB = sheet.createRow(rowBCount);
            columnCount = 0;
            System.out.println("i: " + i);
            Boolean rowStyleset = false;
            
            while (rowStyleset == false) {
                if (cellstyle == bodyStyleA) {
                	cellstyle = bodyStyle;
                	boldcellstyle = bodyStyle;
                	boldcellstyle.setFont(boldfont);
                	cellstyle.setFont(bodyfont);
                	if (rowCount == 0) {
                		row.setRowStyle(style);
                		rowB.setRowStyle(style);
                	} 
                	else {
                        row.setRowStyle(cellstyle);
                        rowB.setRowStyle(cellstyle);
                	}
                    rowStyleset = true;
                }
                else {
                	cellstyle = bodyStyleA;
                	boldcellstyle = bodyStyleA;
                	boldcellstyle.setFont(boldfont);
                	cellstyle.setFont(bodyfont);
                    row.setRowStyle(cellstyle);
                    rowB.setRowStyle(cellstyle);
                    rowStyleset = true;
                }
            }
            System.out.println("2: " + columnCount);
            createCell(row, columnCount++, bigtest.get(i).getId(), cellstyle);
            createCell(row, columnCount++, bigtest.get(i).getTestname(), cellstyle);
            createCell(row, columnCount++, "Variant A: " + bigtest.get(i).getVariantA(), boldcellstyle);
            System.out.println("column: " + columnCount);
            if (columnCount == ClickCol) {
            	System.out.println("clicks: " + bigtest.get(i).getVariantAClicks());
            	createCell(row, columnCount++, bigtest.get(i).getVariantAClicks(), cellstyle);
            }
            System.out.println("2: " + columnCount);
            if (columnCount == OpenCol) {
            	System.out.println("opens: " + bigtest.get(i).getVariantAOpens());
            	createCell(row, columnCount++, bigtest.get(i).getVariantAOpens(), cellstyle);
            }
            System.out.println("2: " + columnCount);
            if (columnCount == OpenrateCol) {
                createCell(row, columnCount++, getRateFormatted(bigtest.get(i).getVariantAOpenRate()), cellstyle);
            }
            System.out.println("2: " + columnCount);
            if (columnCount == ClickrateCol) {
            	createCell(row, columnCount++, getRateFormatted(bigtest.get(i).getVariantAClickRate()), cellstyle);
            }
            if (columnCount == ClickOpenCol) {
            	System.out.println("clicks/opens: " + bigtest.get(i).getVariantAClickOpens());
            	createCell(row, columnCount++, getRateFormatted(bigtest.get(i).getVariantAClickOpens()), cellstyle);
            }
            System.out.println("2: " + columnCount);
            if (columnCount == RevCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantARevenue(), cellstyle);
            }
            System.out.println("2: " + columnCount);
            if (columnCount == DonationsCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantADonations(), cellstyle);
            }
            if (columnCount == AvCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantAaverageDonation(), cellstyle);
            }
            if (columnCount == DonOpenCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantADonationsOpens(), cellstyle);
            }
            if (columnCount == DonClickCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantADonationsClicks(), cellstyle);
            }
            if (columnCount == AvEmailCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantAaverageRevenueperEmail(), cellstyle);
            }
            if (columnCount == emailcountCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantAemailcount(), cellstyle);
            }
            if (columnCount == testcategoryCol) {
            	createCell(row, columnCount++, bigtest.get(i).getTestcategory(), cellstyle);
            }
            if (columnCount == RecipientsCol) {
            	createCell(row, columnCount++, bigtest.get(i).getVariantARecipients(), cellstyle);
            }
            columnCount = 0;
            createCell(rowB, columnCount++, bigtest.get(i).getId(), cellstyle);
            createCell(rowB, columnCount++, bigtest.get(i).getTestname(), cellstyle);
            createCell(rowB, columnCount++, "Variant B: " + bigtest.get(i).getVariantB(), boldcellstyle);
            if (columnCount == ClickCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBClicks(), cellstyle);
            }
            if (columnCount == OpenCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBOpens(), cellstyle);
            }
            if (columnCount == OpenrateCol) {
                createCell(rowB, columnCount++, getRateFormatted(bigtest.get(i).getVariantBOpenRate()), cellstyle);
            }
            if (columnCount == ClickrateCol) {
            	createCell(rowB, columnCount++, getRateFormatted(bigtest.get(i).getVariantBClickRate()), cellstyle);
            }
            if (columnCount == ClickOpenCol) {
            	createCell(rowB, columnCount++, getRateFormatted(bigtest.get(i).getVariantBClickOpens()), cellstyle);
            }
            if (columnCount == RevCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBRevenue(), cellstyle);
            }
            if (columnCount == DonationsCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBDonations(), cellstyle);
            }
            if (columnCount == AvCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBaverageDonation(), cellstyle);
            }
            if (columnCount == DonOpenCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBDonationsOpens(), cellstyle);
            }
            if (columnCount == DonClickCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBDonationsClicks(), cellstyle);
            }
            if (columnCount == AvEmailCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBaverageRevenueperEmail(), cellstyle);
            }
            if (columnCount == emailcountCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBemailcount(), cellstyle);
            }
            if (columnCount == testcategoryCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getTestcategory(), cellstyle);
            }
            if (columnCount == RecipientsCol) {
            	createCell(rowB, columnCount++, bigtest.get(i).getVariantBRecipients(), cellstyle);
            }
            rowCount = rowCount + 2;
            rowBCount = rowBCount + 2;
        }
        //export
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
	}
	public void readChairReport(String excelPath)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		List<String> list = new ArrayList<String>();

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		System.out.println("workbook created");

		int x = workbook.getNumberOfSheets();
		
		System.out.println("number of sheets " + x);

		int noOfColumns = 0;
		List<Cell> headers = new ArrayList<Cell>();
		Cell header = null;
		Cell value = null;
		List<Cell> values = new ArrayList<Cell>();
		
		// Getting the Sheet at index zero
		for (int i = 0; i < x; i++) {

			Sheet sheet1 = workbook.getSheetAt(i);
			
			System.out.println("sheet1 " + sheet1);
			Iterator<Row> rowIterator = sheet1.iterator();

			noOfColumns = sheet1.getRow(i).getLastCellNum();
			
			System.out.println("number of columns " + noOfColumns);
			

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();
			int NameColumn = 0;
			int RevenueColumn = 0;
			int GiftsColumn = 0;
			String Name = null;
			Double Revenue = null;
			Integer Gifts = null;
			System.out.println("The sheet number is " + i + 1);
			// 2. Or you can use a for-each loop to iterate over the rows and columns
			System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	             Iterator<Cell> cellIterator = row.cellIterator();
	                while(cellIterator.hasNext()) {

	                   
	                	Cell cell = cellIterator.next();
	                	//System.out.println("CELL: " + cell.getAddress());
						if (row.getRowNum() == 0) {
							//header = cell.getAddress();
							header = cell;
							//System.out.println("Header: " + header);
							headers.add(header);
							//System.out.println("Header column: " + header.getColumn());
							
							String headerValue = dataFormatter.formatCellValue(header).toUpperCase();
							if (headerValue.contains("NAME")) {
								NameColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("REVENUE")) {
								RevenueColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							if (headerValue.contains("GIFTS")) {
								GiftsColumn = header.getColumnIndex();
								//System.out.println(headerValue);
							}
							//System.out.println("Headers: " + headers);
						}
						else if (row.getRowNum() > 0){
							//if (refcode == null) {
								//if (cell.getColumnIndex() == headers.get(j).getColumnIndex()) {
									value = cell;
									if (cell.getColumnIndex() == NameColumn) {
										//System.out.println("Values: " + values);
										//userMap.put(headerValue, valValue);
										//System.out.println("NameColumn TWO: " + NameColumn);
										Name = dataFormatter.formatCellValue(cell);
										//System.out.println(nameValue);
										if (cell.getColumnIndex() == noOfColumns - 1) {
											System.out.println(Name + " - " + "$" + Revenue + ", " + Gifts + " gifts");
											Name = null;
											Revenue = null;
											Gifts = null;
										}
									}
									else if (cell.getColumnIndex() == RevenueColumn) {
										String amount1 = dataFormatter.formatCellValue(cell);
										Revenue = Double.parseDouble(amount1); 
										//System.out.println(nameValue);
										if (cell.getColumnIndex() == noOfColumns - 1) {
											System.out.println(Name + " - " + "$" + Revenue + ", " + Gifts + " gifts");
											Name = null;
											Revenue = null;
											Gifts = null;
										}
									}
									if (cell.getColumnIndex() == GiftsColumn) {
										String amount2 = dataFormatter.formatCellValue(cell);
										Gifts = Integer.parseInt(amount2); 
										if (cell.getColumnIndex() == noOfColumns - 1) {
											System.out.println(Name + " - " + "$" + Revenue + ", " + Gifts + " gifts");
											Name = null;
											Revenue = null;
											Gifts = null;
										}
									}
		    	        }

	            }
	        }
		}
  }
}
	
