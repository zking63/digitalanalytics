package com.coding.LojoFundrasing.Repos;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.Emails;

public interface EmailRepositoryCustom {
	List<Emails> findEmailByName(List<String> names);

	List<Emails> CustomEmailListForExport(@Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
			 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, Committees committee, String type, String operator, List<String> operands) throws ParseException;
}
