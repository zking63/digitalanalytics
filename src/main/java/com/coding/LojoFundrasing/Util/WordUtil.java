package com.coding.LojoFundrasing.Util;

import java.io.File;
import java.io.FileOutputStream;
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

	public void MonthlyTop10Bottom10(List<EmailGroup> emailgroups, HttpServletResponse response) 
			throws IOException{
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
			XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().get(0);
			XWPFParagraph p3 = table.getRow(0).getCell(1).getParagraphs().get(0);
			p1.setAlignment(ParagraphAlignment.CENTER);
			XWPFParagraph p2 = table.getRow(1).getCell(1).getParagraphs().get(0);
			p2.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun r1 = p1.createRun();
			r1.setBold(true);
			XWPFRun r3 = p3.createRun();
			r3.setBold(true);
			//if (table.getRow(0).getCell(0)) {
				r1.setText("Sender");
			//}
			//if (cellcount == 1) {
				r3.setText("Subject line");
			//}
			XWPFRun r2 = p2.createRun();
			r2.setBold(true);
			String row = rowcount.toString();
			r2.setText(row);
			rowcount++;
			XWPFParagraph para2 = document.createParagraph();
			XWPFRun para2Run = para2.createRun();
			para2Run.setText("           ");
		}
		
        //export
		ServletOutputStream out = response.getOutputStream();
		//FileOutputStream out = new FileOutputStream(output);
		document.write(out);
		out.close();
		document.close();
	}
}
