package edu.gzc.pojo;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

public class Product {

	@NotBlank(message = "商品名不能为空")
	private String name;

	@NotBlank(message = "商品描述不能为空")
	private String description;

	@Range(min = 0, max = 100, message = "价格只能在0和100之间")
	private double price;

	@Past(message = "创建日期需要是一个过去时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
