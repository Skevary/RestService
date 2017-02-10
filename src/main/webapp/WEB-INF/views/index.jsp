<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Index page</title>
</head>
<body>
<header>
    <h1>Title : ${title}</h1>
</header>
<c:choose>
    <c:when test="${pageContext.request.userPrincipal.name != null}">
        <h3>
            Welcome : ${pageContext.request.userPrincipal.name}
            | <a href="logout">Log out</a>
        </h3>
    </c:when>
    <c:otherwise>
        <h3>
            Welcome : Guest! | <a href="login">Log in</a>
        </h3>
    </c:otherwise>
</c:choose>
</body>
</html>
