<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/login" method="post">
        用户名：<input type="text" name="name" id="name"><br>
        密&nbsp;&nbsp;码：<input type="password" name="password" id="password"><br>
        <button type="submit">登录</button>
    </form>
    <span style="color: red">${msg}</span>
</body>
</html>
