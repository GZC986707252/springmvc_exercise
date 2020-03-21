<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<fieldset>
		<legend>单文件上传结果显示</legend>
		<p>文件名：${ singleFile.myfile.originalFilename }</p>
		<p>文件大小(Byte):${ singleFile.myfile.size }</p>
		<p>文件描述：${ singleFile.description }</p>

	</fieldset>
	<fieldset>
		<legend>多文件上传结果显示</legend>
		<table>
			<tr>
				<td>文件名</td>
				<td>文件大小(Byte)</td>
				<td>文件描述</td>
			</tr>
			<!-- 同时取两个数组的元素 -->
			<c:forEach var="myfile" items="${multipleFiles.myfiles}" varStatus="loop">
				<tr>
					<td>${ myfile.originalFilename }</td>
					<td>${ myfile.size }</td>
					<td>${ multipleFiles.description[loop.index] }</td>
				</tr>
			</c:forEach>
		</table>
	</fieldset>
</body>
</html>