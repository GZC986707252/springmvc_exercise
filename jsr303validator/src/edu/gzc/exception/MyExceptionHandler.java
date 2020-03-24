package edu.gzc.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class MyExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {
		// TODO Auto-generated method stub

		// 把异常对象放入模型中，可以在异常页面获取
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", arg3);
		// 根据不同异常转向不同的页面
		if (arg3 instanceof MyException) {
			return new ModelAndView("my-error", model);
		} else if (arg3 instanceof SQLException) {
			return new ModelAndView("sql-error", model);
		} else {
			return new ModelAndView("error", model);
		}
	}

}
