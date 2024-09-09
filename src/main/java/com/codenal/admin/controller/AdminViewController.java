package com.codenal.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {
	
	// 신규 직원 등록
	@GetMapping("/join")
	public String joinPage() {
		return "admin/join";
	}

	// 직원 목록
//	@GetMapping("/employeeList")
//	public String employeeList() {
//		return "admin/employeeList";
//	}
}