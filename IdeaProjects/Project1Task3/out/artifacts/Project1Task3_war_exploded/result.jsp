<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/29/2020
  Time: 1:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%= request.getAttribute("doctype") %>

<html>
<head>
    <title>Survey Results</title>
</head>
<body>
<form action="getResults" method="GET">
    <h1>Distributed Systems Class Clicker</h1>
    <%--get the counts of each choice--%>
    <%int[] results = (int[]) request.getAttribute("freqArray");%>
    <%String[] choices = {"A","B","C","D"}; %>
    <%--display only those that are not zero--%>
    <%if (results[0] != 0 || results[1] != 0 || results[2] != 0 || results[3] != 0) { %>
    <p>The results from the survey are as follows:</p>
    <%for (int i = 0; i < results.length; i++) {%>
    <%if (results[i] != 0) { %>
    <p><%=choices[i]%> : <%= results[i]%></p>
    <% } %>
    <% } %>
    <p>These results have now been cleared</p>
    <%--if all answers are zeros, show this message--%>
    <% } else { %>
    <p>There are currently no results</p>
    <% } %>
</form>
</body>
</html>
