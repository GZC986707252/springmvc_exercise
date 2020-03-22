package edu.gzc.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.gzc.pojo.Product;

@Component
public class ProductValidator implements Validator {
	@Override
	public boolean supports(Class<?> aClass) {
		// 要验证的model,返回值为false则不验证
		// 判断参数aClass是否是Product本身或者子类
		return Product.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object object, Errors errors) {
		Product product = (Product) object; // object要验证的对象
		// 往Errors对象存入错误消息
		ValidationUtils.rejectIfEmpty(errors, "name", "商品名称不能为空");
		ValidationUtils.rejectIfEmpty(errors, "description", "商品描述不能为空");
		if (product.getPrice() > 100 || product.getPrice() < 0) {
			errors.rejectValue("price", "价格在0和100之间");
		}
		Date productDate = product.getDate();
		// 在系统时间之后
		if (productDate != null && productDate.after(new Date())) {
			errors.rejectValue("date", "创建日期不能是将来时间");
		}
	}
}
