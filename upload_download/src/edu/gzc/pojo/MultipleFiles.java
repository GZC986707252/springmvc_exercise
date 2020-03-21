package edu.gzc.pojo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MultipleFiles {
	private List<String> description;
	private List<MultipartFile> myfiles;

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public List<MultipartFile> getMyfiles() {
		return myfiles;
	}

	public void setMyfiles(List<MultipartFile> myfiles) {
		this.myfiles = myfiles;
	}

}
