<%--
  Created by IntelliJ IDEA.
  User: guo
  Date: 2020/3/15
  Time: 12:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    ${sessionScope.user.name},欢迎您！<br/>
<a href="${pageContext.request.contextPath}/logout">退出登录</a>
</body>
</html>
