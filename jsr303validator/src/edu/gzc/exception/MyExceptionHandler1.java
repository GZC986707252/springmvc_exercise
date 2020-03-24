package edu.gzc.exception;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MyExceptionHandler1 {

	@ExceptionHandler
	public ModelAndView exceptionHandler(Exception e) {
		// 由于自定义的类并不是Controller, 参数不能传入model,map等模型，这里只能使用 ModelAndView
		System.out.println(e);
		ModelAndView mv = new ModelAndView();
		mv.addObject("error", e);
		if (e instanceof MyException) {
			mv.setViewName("my-error");
		} else if (e instanceof SQLException) {
			mv.setViewName("sql-error");
		} else {
			mv.setViewName("error");
		}
		return mv;
	}
}
