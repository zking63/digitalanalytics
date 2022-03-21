package com.coding.LojoFundrasing.Repos;

import java.text.ParseException;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.coding.LojoFundrasing.Models.Committees;
import com.coding.LojoFundrasing.Models.EmailGroup;

public interface EmailGroupRepositoryCustom {

	//List<EmailGroup> CustomEmailGroupListForExport(@Param("startdateD") @DateTimeFormat(iso = ISO.DATE) String startdateD, 
		//	 @Param("enddateD") @DateTimeFormat(iso = ISO.DATE) String enddateD, Committees committee, String type, String operator, List<String> operands) throws ParseException;

	List<EmailGroup> PredPlugin(List<Predicate> predicates);
}
