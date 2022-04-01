package com.coding.LojoFundrasing.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController  {

	private static final String PATH = "/error";

	@RequestMapping(value = PATH )
	public String myerror() {
		return "error.jsp";
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}
	
}
