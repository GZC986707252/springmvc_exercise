package edu.gzc.pojo;

import org.springframework.web.multipart.MultipartFile;

public class SingleFile {
	private String description;
	private MultipartFile myfile;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getMyfile() {
		return myfile;
	}

	public void setMyfile(MultipartFile myfile) {
		this.myfile = myfile;
	}

}
