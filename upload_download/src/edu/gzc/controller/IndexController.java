package edu.gzc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/singleFileUpload")
	public String toSingleFileUpload() {
		System.out.println("test");
		return "oneFileUpload";
	}
}
