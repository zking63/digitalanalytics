package com.coding.LojoFundrasing.Repos;

import java.util.List;


import com.coding.LojoFundrasing.Models.Emails;

public interface EmailRepositoryCustom {
	List<Emails> findEmailByName(List<String> names);

	List<Emails> CustomEmailListForExport(Long committee_id, String type, String operator, String operand);
}
