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
		<p><td><a href="${ pageContext.request.contextPath }/download?filename=${ singleFile.myfile.originalFilename }">download</a></td></p>
	</fieldset>
	<br>
	<fieldset>
		<legend>多文件上传结果显示</legend>
		<table>
			<tr>
				<td>文件名</td>
				<td>文件描述</td>
				<td>文件大小(Byte)</td>
				<td>下载</td>
			</tr>
			<!-- 同时取两个数组的元素 -->
			<c:forEach var="myfile" items="${multipleFiles.myfiles}" varStatus="loop">
				<tr>
					<td>${ myfile.originalFilename }</td>
					<td>${ multipleFiles.description[loop.index] }</td>
					<td>${ myfile.size }</td>
					<td><a href="${ pageContext.request.contextPath }/download?filename=${ myfile.originalFilename }">download</a></td>
				</tr>
			</c:forEach>
		</table>
	</fieldset>
	<br>
	<br>
	<fieldset>
		<legend>文件下载</legend>
		<table>
			<tr>
				<td>文件名</td>
				<td>下载</td>
			</tr>
			<c:forEach var="fileName" items="${fileNames}">
				<tr>
					<td>${ fileName }</td>
					<td><a href="${ pageContext.request.contextPath }/download?filename=${ fileName }">download</a></td>
				</tr>
			</c:forEach>
		</table>
	</fieldset>
</body>
</html>