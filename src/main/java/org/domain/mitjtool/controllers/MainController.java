package org.domain.mitjtool.controllers;

import org.apache.commons.lang3.StringUtils;
import org.domain.mitjtool.services.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/")
@Controller
public class MainController {

	@Autowired
	private DomainService domainService;
	
	@GetMapping({ "/", "generate" })
	public String main() {
		return "index";
	}
	
	@PostMapping("generate")
	public String generate(@RequestParam("tableName") String tableName,
			               @RequestParam("cols") String cols,
			               ModelMap model) {
		if (StringUtils.isBlank(cols)) return "index";
		model.put("output", domainService.generate(tableName, cols));
		model.put("cols", cols);
		model.put("tableName", tableName);
		return "index";
	}
}
