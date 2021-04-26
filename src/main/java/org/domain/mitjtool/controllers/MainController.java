package org.domain.mitjtool.controllers;

import org.apache.commons.lang3.StringUtils;
import org.domain.mitjtool.dto.Req;
import org.domain.mitjtool.services.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class MainController {

	@Autowired
	private DomainService domainService;
	
	@GetMapping({ "/", "generate" })
	public String main(ModelMap model) {
		model.put("req", new Req());
		return "index";
	}
	
	@PostMapping("generate")
	public String generate(Req req,
			               ModelMap model) {
		if (StringUtils.isBlank(req.getCols())) return "index";
		model.put("output", domainService.generate(req));
		model.put("req", req);
		return "index";
	}
}
