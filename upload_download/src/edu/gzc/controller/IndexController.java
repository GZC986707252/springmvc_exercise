package edu.gzc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/singleFileUpload")
	public String toSingleFileUpload() {
		return "oneFileUpload";
	}

	@RequestMapping("/multipartFileUpload")
	public String toMultipartFileUpload() {
		return "multipartFileUpload";
	}
}
