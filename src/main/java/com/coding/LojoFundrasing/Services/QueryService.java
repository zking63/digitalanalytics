package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.Committees;

@Service
public class QueryService {
	@Autowired 
	private CommitteeService cservice;
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private EmailGroupService egservice;
	
	@Autowired
	private EmailService eservice;
	
	@Autowired
	private TestService tservice;
	
	@Autowired
	private WordService wservice;
	
	@Autowired
	private LinkService lservice;
	
	public void GetOperands(Integer field, List<String> categories, List<String> operandsList, List<Predicate> predicates, String startdate, String enddate, Committees committee, 
			String type, String operator, String operand) throws ParseException, IOException {
		System.out.println("IN GET OPS");
		List<String> operands = new ArrayList<String>();
		int index = 0;
		int finalindex = -1;
		String sub = operand;
		if (operand == null || operand.isEmpty() || operand.length() <= 0
				|| operand.contentEquals("()") || operand.contentEquals("(  )")) {
			System.out.println("DONE");
			if (operand == null) {
				if (field == 0) {
					egservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
					return;
				}
				else if (field == 1) {
					eservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
					return;
				}
				return;
			}
			finalindex = operand.length();
			if (operand.contains("('")) {
			    try {
			    	index = operand.indexOf("('")-1;
			      } catch (Exception e) {
			        System.out.println("error with parenthesis ");
			      }
			}
			if (operand.contains("')")) {
			    try {
			    	finalindex = operand.indexOf("')")+1;
			      } catch (Exception e) {
			        System.out.println("error with parenthesis ");
			      }
			}
			sub = operand.substring(index, finalindex);
			System.out.println("sub" + sub);
			operand = null;
			if (field == 0) {
				egservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
				return;
			}
			else if (field == 1) {
				eservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
				return;
			}
		}
		if (operand != null && (operand.contains("/") || operand.contains("+"))) {
			if (operand.contains("/")) {
				if (operand.contains("('")) {
				    try {
				    	index = operand.indexOf("('")-1;
				      } catch (Exception e) {
				        System.out.println("error with parenthesis ");
				      }
				    try {
				    	finalindex = operand.indexOf("')")+1;
				      } catch (Exception e) {
				        System.out.println("error with parenthesis ");
				      }
					sub = operand.substring(index, finalindex);
				}
				System.out.println("out of if ");
				List<String>ops = Arrays.asList(sub.split("/", -1));
				for (int i = 0; i < ops.size(); i++) {
					String op = ops.get(i);
					op = op.trim();
				    try {
				    	op = op.substring(op.indexOf("'")+1);
				      } catch (Exception e) {
				        System.out.println("sub in try /: " + op);
				      }
				    try {
				    	op = op.substring(0, op.indexOf("'"));
				      } catch (Exception e) {
				        System.out.println("sub in try2 /: " + op);
				      }
				    if (op != null && !op.isEmpty()) {
				    	System.out.println("op is not null /: " + op);
				    	operands.add(op);
				    }
					
				}
				if (sub.length() == operand.length()) {
					System.out.println("sub = operand ");
					operand = null;
					if (field == 0) {
						egservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
						return;
					}
					else if (field == 1) {
						eservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
						return;
					}
				}
				finalindex = operand.indexOf(sub);
				if (operand.indexOf(sub) != 0) {
					finalindex = operand.indexOf(sub)-1;
				}
				index = sub.length()+(operand.indexOf(sub));
				String x = operand.substring(0, finalindex);
				x = x.trim();
				System.out.println("x: " +x);
				String y = operand.substring(index+1, operand.length());
				System.out.println("y: " +y);
				y = y.trim();
				
				operand = x.concat(y);
				System.out.println("operands: " +operands);
				if (field == 0) {
					egservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
					return;
				}
				else if (field == 1) {
					eservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
					return;
				}
				//GetOperands(predicates, startdate, enddate, committee, type, operator, operand);
				
			}
			else if (operand.contains("+")) {
				finalindex = sub.indexOf("+");
				index = 0;
				if (finalindex == 0 || finalindex == 1) {
					index = finalindex +2;
					finalindex = operand.length();
				}
				System.out.println("index " + index);
				System.out.println("finalindex " + finalindex);
				if (index < 0 || finalindex < 0 
						|| index > operand.length() || finalindex > operand.length()) {
					System.out.println("index is less " + index);
					operand = null;
					GetOperands(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operand);
					return;
				}
				sub = sub.substring(index, finalindex);
				System.out.println("sub1" +sub);
				sub = sub.trim();
				if (sub.contains("+") || sub.contains("/") 
						|| sub.contains("(") || sub.contains("!")) {
					System.out.println("sub" +sub);
					operand = sub;
					//System.out.println("operand" +operand +".");
					GetOperands(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operand);
					return;
				}
			    try {
			    	sub = sub.substring(sub.indexOf("'")+1);
			      } catch (Exception e) {
			        System.out.println("sub in try: " + sub);
			      }
				System.out.println("sub after try1: " + sub);
			    try {
			    	sub = sub.substring(0, sub.indexOf("'"));
			      } catch (Exception e) {
			        System.out.println("sub in try 2: " + sub);
			      }
				
				System.out.println("sub after try2 : " + sub);
				operands.add(sub);
				System.out.println("operands: " +operands);
				operand = operand.substring(finalindex, operand.length());
				System.out.println("operand : " + operand);
				if (field == 0) {
					egservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
					return;
				}
				else if (field == 1) {
					eservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
					return;
				}
				//GetOperands(predicates, startdate, enddate, committee, type, operator, operand);

			}
		}
		else {
			sub = sub.substring(operand.indexOf("'")+1);
			System.out.println("sub after: " + sub);
			sub = sub.substring(0, sub.indexOf("'"));
			System.out.println("sub after2 : " + sub);
			operands.add(sub);
			operand = null;
			if (field == 0) {
				egservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
				return;
			}
			else if (field == 1) {
				eservice.PredicateCreator(field, categories, operandsList, predicates, startdate, enddate, committee, type, operator, operands, operand);
				return;
			}
			//System.out.println("operands: " +operands);
			//GetOperands(startdate, enddate, committee, type, operator, operand);

		}
	}
	
	
	
	
	/*public Boolean operandCheck(String operand) {
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
	}*/
}
