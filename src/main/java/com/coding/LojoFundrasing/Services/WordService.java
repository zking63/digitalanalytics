package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.coding.LojoFundrasing.Models.EmailGroup;
import com.coding.LojoFundrasing.Models.test;
import com.coding.LojoFundrasing.Util.WordUtil;

@Service
public class WordService {
	@Autowired
	private WordUtil wordutil;

	public String url = "D:\\word\\";
    public void exportWord(List<EmailGroup> emailgroups, HttpServletResponse response) throws IOException, InvalidFormatException {
    	System.out.println("word service");
        response.setContentType("application/octet-stream");
        //MediaType.APPLICATION_JSON
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tests_" + currentDateTime + ".docx";
        response.setHeader(headerKey, headerValue);
        String word = "hello world";
        
        wordutil.MonthlyTop10Bottom10(emailgroups, response);
    
    }
}
