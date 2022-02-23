package com.coding.LojoFundrasing.Util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

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
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Donation;
import com.coding.LojoFundrasing.Models.Donor;
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.Emails;
import com.coding.LojoFundrasing.Models.HtmlImageGenerator;
import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Services.CommitteeService;
import com.coding.LojoFundrasing.Services.DonationService;
import com.coding.LojoFundrasing.Services.DonorService;
import com.coding.LojoFundrasing.Services.EmailService;
import com.coding.LojoFundrasing.Services.TestService;
import com.coding.LojoFundrasing.Services.UserService;

@Component
public class WordUtil {
	public String getRateFormatted(Double number) {
		if (number == null) {
			number = 0.0;
		}
		number = number*100;
		double number1 = (double) number;
		DecimalFormat df = new DecimalFormat("0.000");
		String numberfinal = df.format(number1) + "%"; 
		return numberfinal;
	}
	public String getAverageFormatted(Double number) {
		if (number == null) {
			number = 0.0;
		}
		double number1 = (double) number;
		DecimalFormat df = new DecimalFormat("0.00000");
		String numberfinal = df.format(number1); 
		return numberfinal;
	}
	public String getRevenueFormatted(Double number) {
		if (number == null) {
			number = 0.0;
		}
		double number1 = (double) number;
		DecimalFormat df = new DecimalFormat("0.00");
		String numberfinal = "$" + df.format(number1); 

		return numberfinal;
	}
	
	public void MonthlyTop10Bottom102(List<EmailGroup> emailgroups, HttpServletResponse response) 
			throws IOException, InvalidFormatException{
		XWPFDocument document = new XWPFDocument();
		String url = "http://localhost:8080/render/emails/9172";
		String html = emailgroups.get(0).getEmails().get(0).getContent();
		 HtmlImageGenerator imageGenerator = new HtmlImageGenerator(); 
		// imageGenerator.loadHtml(emailgroups.get(i).getEmails().get(0).getContent()); 
		// File file = new File("email");
		 
		// System.out.println(frame);
		 //BufferedImage image = imageGenerator.saveAsImage(emailgroups.get(i).getEmails().get(0).getContent()); 
		// imageGenerator.saveAsHtmlWithMap("html.html", "html.png");
		// File image = new File("html.jpeg");
		
		 //imageGenerator.renderHTML(html, image);
		// BufferedImage img = ImageIO.read(image);
		// System.out.println("img" + img);
	
		// imageGenerator.loadHtml(html);
		 imageGenerator.loadUrl(url);
		
		
		File image = imageGenerator.saveAsImage(html);
		imageGenerator.saveAsHtmlWithMap(image, url);
	       FileInputStream imageData = new FileInputStream(image);
	       // System.out.println("data " + imageData.read());
		 int imageType = XWPFDocument.PICTURE_TYPE_JPEG;
	        String imageFileName = "html.jpeg";
			//ServletOutputStream out = response.getOutputStream();
			//FileOutputStream out = new FileOutputStream(output);
	       // File outputfile = new File("D:\\Sample.png");
	       // FileOutputStream out = new FileOutputStream("html.jpeg");
	       
			XWPFParagraph para2 = document.createParagraph();
			XWPFRun para2Run = para2.createRun();
			para2Run.addPicture(imageData, imageType, imageFileName, Units.toEMU(600), Units.toEMU(12000));
			para2Run.setText(html);
			 ServletOutputStream out = response.getOutputStream();
				document.write(out);
				out.close();
				document.close();
	}

	public void MonthlyTop10Bottom10(List<EmailGroup> emailgroups, HttpServletResponse response) 
			throws IOException, InvalidFormatException{
		System.out.println("word util");
		XWPFDocument document = new XWPFDocument();
		String output = "rest-with-spring.docx";
		
		XWPFParagraph title = document.createParagraph();
		title.setAlignment(ParagraphAlignment.CENTER);
		//XWPFRun titleRun = title.createRun();
		Integer rowcount = 0;
		Integer cellcount = 0;
		for (int i = 0; i <emailgroups.size(); i++) {
			//XWPFRun titleRun = title.createRun();
			XWPFParagraph paragraph = document.createParagraph();
			XWPFRun titleRun = paragraph.createRun();
			titleRun.setText(emailgroups.get(i).getEmailgroupName());
			XWPFTable table = document.createTable(2, 7);
			//set title paragraphs
			XWPFParagraph sender = table.getRow(0).getCell(0).getParagraphs().get(0);
			XWPFParagraph subject = table.getRow(0).getCell(1).getParagraphs().get(0);
			XWPFParagraph date = table.getRow(0).getCell(2).getParagraphs().get(0);
			XWPFParagraph revenue = table.getRow(0).getCell(3).getParagraphs().get(0);
			XWPFParagraph donations = table.getRow(0).getCell(4).getParagraphs().get(0);
			XWPFParagraph donationsopens = table.getRow(0).getCell(5).getParagraphs().get(0);
			XWPFParagraph average = table.getRow(0).getCell(6).getParagraphs().get(0);
			
			//align title paragraphs
			sender.setAlignment(ParagraphAlignment.CENTER);
			subject.setAlignment(ParagraphAlignment.CENTER);
			date.setAlignment(ParagraphAlignment.CENTER);
			revenue.setAlignment(ParagraphAlignment.CENTER);
			donations.setAlignment(ParagraphAlignment.CENTER);
			donationsopens.setAlignment(ParagraphAlignment.CENTER);
			average.setAlignment(ParagraphAlignment.CENTER);
	
			
			//set title text
			XWPFRun senderrun = sender.createRun();
			XWPFRun subjectrun = subject.createRun();
			XWPFRun daterun = date.createRun();
			XWPFRun revenuerun = revenue.createRun();
			XWPFRun donationsrun = donations.createRun();
			XWPFRun donationsopensrun = donationsopens.createRun();
			XWPFRun averagerun = average.createRun();
			
			//bold title text
			subjectrun.setBold(true);
			senderrun.setBold(true);
			daterun.setBold(true);
			revenuerun.setBold(true);
			donationsrun.setBold(true);
			donationsopensrun.setBold(true);
			averagerun.setBold(true);
			
			
			//write title text
			senderrun.setText("Sender");
			subjectrun.setText("Subject line");
			daterun.setText("Date");
			revenuerun.setText("Revenue");
			donationsrun.setText("Donations");
			donationsopensrun.setText("g/o");
			averagerun.setText("Average");
			

			//set body paragraphs
			XWPFParagraph emailsender = table.getRow(1).getCell(0).getParagraphs().get(0);
			XWPFParagraph emailsubject = table.getRow(1).getCell(1).getParagraphs().get(0);
			XWPFParagraph emaildate = table.getRow(1).getCell(2).getParagraphs().get(0);
			XWPFParagraph emailrevenue = table.getRow(1).getCell(3).getParagraphs().get(0);
			XWPFParagraph emaildonations = table.getRow(1).getCell(4).getParagraphs().get(0);
			XWPFParagraph emaildonationsopens = table.getRow(1).getCell(5).getParagraphs().get(0);
			XWPFParagraph emailaverage = table.getRow(1).getCell(6).getParagraphs().get(0);
			
			//align title paragraphs
			emailsender.setAlignment(ParagraphAlignment.CENTER);
			emailsubject.setAlignment(ParagraphAlignment.CENTER);
			emaildate.setAlignment(ParagraphAlignment.CENTER);
			emailrevenue.setAlignment(ParagraphAlignment.CENTER);
			emaildonations.setAlignment(ParagraphAlignment.CENTER);
			emaildonationsopens.setAlignment(ParagraphAlignment.CENTER);
			emailaverage.setAlignment(ParagraphAlignment.CENTER);
	
			
			//set title text
			XWPFRun emailsenderrun = emailsender.createRun();
			XWPFRun emailsubjectrun = emailsubject.createRun();
			XWPFRun emaildaterun = emaildate.createRun();
			XWPFRun emailrevenuerun = emailrevenue.createRun();
			XWPFRun emaildonationsrun = emaildonations.createRun();
			XWPFRun emaildonationsopensrun = emaildonationsopens.createRun();
			XWPFRun emailaveragerun = emailaverage.createRun();
			
			String sendertext = null;
			String subjecttext = null;
			
        		if (emailgroups.get(i).getGroupTest() != null && emailgroups.get(i).getGroupTest().contentEquals("SENDER")) {
        			System.out.println("tested sender");
                	if (emailgroups.get(i).getFullsendvariant() == null) {
                		if (emailgroups.get(i).getFullsendvariantdonors() != null) {
                			if (emailgroups.get(i).getFullsendvariantprospects() != null) {
                				sendertext = "Donors: " + emailgroups.get(i).getFullsendvariantdonors() + "\n" + "Prospects: " + emailgroups.get(i).getFullsendvariantprospects();
                				
                			}
                			else {
                				sendertext = emailgroups.get(i).getFullsendvariantdonors();
                			}
                			
                		}
                		else { 
                			sendertext = "A (didn't full send): " + emailgroups.get(i).getVariantA() + "\n" + "B (didn't full send): " + emailgroups.get(i).getVariantB();
                		}
                	}
                	else {
                		if (emailgroups.get(i).getVariantA().contentEquals(emailgroups.get(i).getFullsendvariant())) {
                			sendertext = "Winning sender: " + emailgroups.get(i).getFullsendvariant() + "Losing sender: " + emailgroups.get(i).getVariantB(); 
                		}
                		else {
                			sendertext = "Winning sender: " + emailgroups.get(i).getFullsendvariant() + "Losing sender: " + emailgroups.get(i).getVariantA(); 
                		}
                	}
        		}
        		else {
        			sendertext = emailgroups.get(i).getEmails().get(0).getSender();
        		}
        		if (emailgroups.get(i).getGroupTest() != null && emailgroups.get(i).getGroupTest().contentEquals("SUBJECT")) {
        			System.out.println("tested subject");
                	if (emailgroups.get(i).getFullsendvariant() == null) {
                		if (emailgroups.get(i).getFullsendvariantdonors() != null) {
                			if (emailgroups.get(i).getFullsendvariantprospects() != null) {
                				subjecttext = "Donors: " + emailgroups.get(i).getFullsendvariantdonors() + "\n" + "Prospects: " + emailgroups.get(i).getFullsendvariantprospects();
                				
                			}
                			else {
                				subjecttext = emailgroups.get(i).getFullsendvariantdonors();
                			}
                			
                		}
                		else { 
                			subjecttext = "A (didn't full send): " + emailgroups.get(i).getVariantA() + "\n" + "B (didn't full send): " + emailgroups.get(i).getVariantB();
                		}
                	}
                	else {
                		if (emailgroups.get(i).getVariantA().contentEquals(emailgroups.get(i).getFullsendvariant())) {
                			subjecttext = "Winning SL: " + emailgroups.get(i).getFullsendvariant() + "Losing SL: " + emailgroups.get(i).getVariantB(); 
                		}
                		else {
                			subjecttext = "Winning SL: " + emailgroups.get(i).getFullsendvariant() + "Losing SL: " + emailgroups.get(i).getVariantA(); 
                		}
                	}
        		}
        		else {
        			subjecttext = emailgroups.get(i).getEmails().get(0).getSubjectLine();
        		}
			//write title text
			emailsenderrun.setText(sendertext);
			emailsubjectrun.setText(subjecttext);
			emaildaterun.setText(emailgroups.get(i).getDateFormatted());
			emailrevenuerun.setText(getRevenueFormatted(emailgroups.get(i).getGroupsum()));
			//donationsrun.setText(emailgroups.get(i).getGroupdonationcount());
			emaildonationsopensrun.setText(getRateFormatted(emailgroups.get(i).getGroupdonationsOpens()));
			emailaveragerun.setText(getAverageFormatted(emailgroups.get(i).getGroupaverage()));

			 rowcount++;
			XWPFParagraph para2 = document.createParagraph();
			XWPFRun para2Run = para2.createRun();
	
		}
		
        //export
		ServletOutputStream out = response.getOutputStream();
		//FileOutputStream out = new FileOutputStream(output);
		document.write(out);
		out.close();
		document.close();
	}
}
