<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>单文件上传</title>
</head>
<body>
	<form action="upload/singlefile" method="post" enctype="multipart/form-data">
		选择文件:<input type="file" name="myfile"><br>
		文件描述：<textarea rows="3" name="description"></textarea><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>