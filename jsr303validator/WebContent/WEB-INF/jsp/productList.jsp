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
	<fieldset>
		<legend>商品列表</legend>
		<table>
			<tr>
				<td>商品名</td>
				<td>商品描述</td>
				<td>商品价格</td>
				<td>创建日期</td>
			</tr>
			<c:forEach var="product" items="${ products }">
				<tr>
					<td>${ product.name }</td>
					<td>${ product.description }</td>
					<td>${ product.price }</td>
					<td>${ product.date }</td>
				</tr>
			</c:forEach>
		</table>
	</fieldset>
</body>
</html>