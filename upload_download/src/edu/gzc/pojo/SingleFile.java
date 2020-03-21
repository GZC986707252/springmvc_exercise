package edu.gzc.pojo;

import org.springframework.web.multipart.MultipartFile;

public class SingleFile {
	private String description;
	private MultipartFile myflie;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getMyflie() {
		return myflie;
	}

	public void setMyflie(MultipartFile myflie) {
		this.myflie = myflie;
	}

}
