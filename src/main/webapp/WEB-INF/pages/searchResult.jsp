<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Search results</title>
</head>
<body>
<c:forEach items="${annotations}" var="item">
    <p>Founded sentence: [${item.span}].
        <c:forEach items="${item.positions}" var="pos">
            <br>Document id:${pos.documentId}
            <br>Begin of substring=${pos.begin}, end of substring=${pos.end}
            <br>First symbols of text=${annotatedDocumentMap[pos.documentId].firstSymbols}
        </c:forEach>
    </p>
</c:forEach>
</body>
</html>
