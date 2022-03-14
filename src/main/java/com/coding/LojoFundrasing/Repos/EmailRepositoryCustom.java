package com.coding.LojoFundrasing.Repos;

import java.util.List;


import com.coding.LojoFundrasing.Models.Emails;

public interface EmailRepositoryCustom {
	List<Emails> findEmailById(List<Long> id);
}
