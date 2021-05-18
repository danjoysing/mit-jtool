package org.domain.mitjtool.controllers;

import java.time.LocalDate;

import org.domain.mitjtool.services.AntiCovidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/anti-covid")
@Controller
public class AntiCovidController {

	@Autowired
	private AntiCovidService antiCovidService;
	
	@GetMapping
	public String main(ModelMap model) {
		model.put("datas", antiCovidService.cal(LocalDate.now()));
		return "work-calendar.html";
	}
}
