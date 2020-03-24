package edu.gzc.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.gzc.exception.MyException;

@Controller
public class ExceptionController {

	@RequestMapping("/ex1/{id}")
	public String ex1(@PathVariable int id) throws Exception {

		if (id == 1) {
			throw new MyException("自定义异常");
		} else if (id == 2) {
			throw new SQLException("数据库异常！");
		} else if (id == 3) {
			throw new Exception("其他异常！");
		}
		return "addProduct";
	}

//	@ExceptionHandler({ MyException.class, SQLException.class })
//	public String exceptionHandler(Exception e, Model model) {
//		model.addAttribute("error", e);
//		if (e instanceof MyException) {
//			return "my-error";
//		} else if (e instanceof SQLException) {
//			return "sql-error";
//		} else {
//			return "error";
//		}
//	}

}
