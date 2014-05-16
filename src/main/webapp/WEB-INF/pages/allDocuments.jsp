<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>All documents</title>
</head>
<body>
<h1>Counted: ${count} documents!</h1>
<c:forEach items="${documents}" var="item">
    <p>DocumentId:${item.id}<br>Document first symbols:${item.firstSymbols}...</p>
    <br>
</c:forEach>
<h1></h1>
</body>
</html>
