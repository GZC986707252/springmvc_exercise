<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		<p>文件描述：${ singleFile.description }</p>
	</fieldset>
	<fieldset>
		<legend>多文件上传结果显示</legend>
	</fieldset>
</body>
</html>