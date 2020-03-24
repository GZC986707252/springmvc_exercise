package edu.gzc.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.gzc.exception.MyException;
import edu.gzc.pojo.Product;
import edu.gzc.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

	// 获得日志记录对象
	private static final Log logger = LogFactory.getLog(ProductController.class);

	@Autowired
	private ProductService productService;

//	@Resource
//	private Validator validator;

	@RequestMapping("/input")
	public String input() {
		return "addProduct";
	}

	@RequestMapping("/add")
	// BindingResult对象用于接收校验返回的错误消息，必须紧跟在验证对象的后面(两者相邻)
	public String addProduct(@Valid Product product, BindingResult result, Model model) {

		// 使用Spring自带验证器
		// this.validator.validate(product, result);

		if (result.hasErrors()) { // 如果校验错误
			for (FieldError error : result.getFieldErrors()) {
				System.out.println(error.getField() + ":" + error.getDefaultMessage());
			}
			model.addAttribute("errors", result.getFieldErrors());
			return "addProduct";
		}
		if (!productService.add(product)) {
			return "addProduct";
		}
		logger.info("添加成功");
		model.addAttribute("products", productService.getProducts());
		return "productList";
	}

	@RequestMapping("/ex2/{id}")
	public String ex2(@PathVariable int id) throws Exception {
		if (id == 1) {
			throw new MyException("自定义异常");
		} else if (id == 2) {
			throw new SQLException("数据库异常！");
		} else if (id == 3) {
			throw new Exception("其他异常！");
		}
		return "addProduct";
	}

}
