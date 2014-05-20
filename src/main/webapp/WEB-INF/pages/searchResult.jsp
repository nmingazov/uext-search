<%@ page import="ru.kpfu.itis.issst.search.dto.AnnotatedDocument" %>
<%@ page import="java.util.Map" %>
<%@ page import="ru.kpfu.itis.issst.search.dto.annotation.SolrSentence" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.kpfu.itis.issst.search.dto.annotation.Position" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // todo: remove dirty solution using smart taglibs
    List<SolrSentence> annotations = (List<SolrSentence>) request.getAttribute("annotationsList");
    Map<String, AnnotatedDocument> annotatedDocumentMap = (Map<String, AnnotatedDocument>) request.getAttribute("annotatedDocumentMap");
%>
<html>
<head>
    <title>Search results</title>
</head>
<body>
<% for (SolrSentence solrSentence: annotations) { %>
    <p>Founded sentence: [<%= solrSentence.getSpan() %>].
    <% for (Position position: solrSentence.getPositions() ) { %>
            <br>Document id:<%= position.getDocumentId() %>
            <br>Begin of substring=<%= position.getBegin() %>, end of substring=<%= position.getEnd() %>
            <br>First symbols of text:<%= annotatedDocumentMap.get(position.getDocumentId()).getFirstSymbols() %>
    <% } %>
<% } %>
</body>
</html>
