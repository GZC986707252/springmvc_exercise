<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>多文件上传</title>
</head>
<body>
	<form action="upload/multipartfile" method="post" enctype="multipart/form-data">
		<fieldset>
			<legend>上传文件1</legend>
			选择文件:<input type="file" name="myfiles"><br>
			文件描述：<textarea rows="3" name="description"></textarea><br>
		</fieldset>
		<fieldset>
			<legend>上传文件2</legend>
			选择文件:<input type="file" name="myfiles"><br>
			文件描述：<textarea rows="3" name="description"></textarea><br>
		</fieldset>
		<fieldset>
			<legend>上传文件3</legend>
			选择文件:<input type="file" name="myfiles"><br>
			文件描述：<textarea rows="3" name="description"></textarea><br>
		</fieldset>
		<input type="submit" value="提交">
	</form>
</body>
</html>