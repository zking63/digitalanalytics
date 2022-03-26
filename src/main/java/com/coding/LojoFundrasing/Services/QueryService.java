package com.coding.LojoFundrasing.Services;

import org.springframework.stereotype.Service;

@Service
public class QueryService {
	public Boolean operandCheck(String operand) {
		if (!operand.contains("'")) {
			return false;
		}
		System.out.println("operand: " + operand);
		int quote = 0;
		int plus = 0;
		int parenthesis = 0;
		int dash = 0;
		for (int i = 0; i < operand.length(); i++) {
			System.out.println("operand at i: " + operand.substring(i, i+1));
			if (operand.substring(i, i+1).contentEquals("'")) {
				quote++;
			}
			if (operand.substring(i, i+1).contentEquals("+")) {
				plus++;
			}
			if (operand.substring(i, i+1).contentEquals("(") || operand.substring(i, i+1).contentEquals(")")) {
				parenthesis++;
			}
			if (operand.substring(i, i+1).contentEquals("/")) {
				dash++;
			}
		}
		System.out.println("quote: " + quote);
		System.out.println("plus: " + plus);
		System.out.println("parenthesis: " + parenthesis);
		System.out.println("dash: " + dash);
		if (operand.contains("/")) {
			if (quote < (dash+1)*2) {
				return false;
			}
			if (operand.contains("/")){
				
			}
		}
		return true;
	}
}
