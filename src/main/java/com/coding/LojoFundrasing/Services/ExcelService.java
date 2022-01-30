package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.Donor;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Util.ExcelUtil;

@Service
public class ExcelService {
	@Autowired
	private ExcelUtil excelUtil;
	
	public String excelUrl = "D:\\excel\\";
	public void readData(Long user_id, Committees committee, MultipartFile multipartFile) throws IOException, EncryptedDocumentException, InvalidFormatException, ParseException {

			String filepath = excelUrl + multipartFile.getOriginalFilename();
			byte[] bytes = multipartFile.getBytes();
			java.nio.file.Path path = Paths.get(filepath);
			Files.write(path, bytes);
			
			excelUtil.getSheetDetails(filepath);
			
		 excelUtil.readExcelSheet(filepath, user_id, committee);
		 System.out.println("made it through read excel!!!");
		 Files.delete(path);
	}
	public void readEmailData(Long user_id, MultipartFile multipartFile, Committees committee) throws IOException, EncryptedDocumentException, InvalidFormatException, ParseException {

		String filepath = excelUrl + multipartFile.getOriginalFilename();

		byte[] bytes = multipartFile.getBytes();
		java.nio.file.Path path = Paths.get(excelUrl + multipartFile.getOriginalFilename());
		Files.write(path, bytes);


		excelUtil.getSheetDetails(filepath);
		System.out.println("made it past get sheet details");
	 /*response=*/	excelUtil.readExcelSheetEmails(filepath, user_id, committee);
	 System.out.println("made it through read excel!!!");
	 Files.delete(path);
  }
	public void readTestData(Long user_id, MultipartFile multipartFile, Committees committee) throws IOException, EncryptedDocumentException, InvalidFormatException, ParseException {

		String filepath = excelUrl + multipartFile.getOriginalFilename();

		byte[] bytes = multipartFile.getBytes();
		java.nio.file.Path path = Paths.get(excelUrl + multipartFile.getOriginalFilename());
		Files.write(path, bytes);


		excelUtil.getSheetDetails(filepath);
		System.out.println("made it past get sheet details");
	 /*response=*/	//excelUtil.readExcelSheetTest(filepath, user_id, committee);
	 System.out.println("made it through read excel!!!");
	 Files.delete(path);
  }
    public void exportToExcel(List<Donor> donors, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=donors_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        excelUtil.exporter(donors, response);
    } 
    public void exportEmailsToExcel(List<Emails> emails, List<String> input, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=emails_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        excelUtil.Emailexporter(emails, input, response);
    } 
    public void exportEmailGroupsToExcel(List<EmailGroup> emailgroups, List<String> input, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=emailgroups_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        excelUtil.EmailGroupexporter(emailgroups, input, response);
    } 
    public void exportDonationsToExcel(List<Donation> donations, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=donations_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        excelUtil.Donationexporter(donations, response);
    } 
    public void exportTestToExcel(List<test> tests, List<String> input, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tests_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        excelUtil.Testexporter(tests, input, response);
    } 
	public void readChairData(MultipartFile multipartFile) throws IOException, EncryptedDocumentException, InvalidFormatException, ParseException {

		String filepath = excelUrl + multipartFile.getOriginalFilename();

		byte[] bytes = multipartFile.getBytes();
		java.nio.file.Path path = Paths.get(excelUrl + multipartFile.getOriginalFilename());
		Files.write(path, bytes);


		excelUtil.getSheetDetails(filepath);
		System.out.println("made it past get sheet details");
	 /*response=*/	excelUtil.readChairReport(filepath);
	 System.out.println("made it through read excel!!!");
	 Files.delete(path);
  }
}
