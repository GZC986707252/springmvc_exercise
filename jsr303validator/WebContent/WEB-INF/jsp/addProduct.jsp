<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath }/product/add" method="post">
		<fieldset>
			<legend>添加商品</legend>
			<p>
				<label>商品名：</label>
				<input type="text" id="name" name="name">
			</p>
			<p>
				<label>商品描述：</label>
				<textarea rows="3" id="description" name="description"></textarea>
			</p>
			<p>
				<label>商品价格：</label>
				<input type="text" id="price" name="price">
			</p>
			<p>
				<label>创建日期：</label>
				<input type="text" id="date" name="date">(yyyy-MM-dd)
			</p>
			<p>
				<input type="reset" value="重置"/>
				<input type="submit" value="提交"/>
			</p>
			<c:forEach var="error" items="${ errors }">
				<p>${ error.defaultMessage }</p>
			</c:forEach>
		</fieldset>
		
	</form>
</body>
</html>