<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/28/2020
  Time: 4:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%= request.getAttribute("doctype") %>

<html>
<head>
    <title>DS Clicker</title>
</head>
<body>
<form action="submit" method="POST">
    <h1>Distributed Systems Class Clicker</h1>
    <%--Display the previous response to the user and prompt them to answer the next question--%>
    <p>Your "<%= request.getAttribute("answer")%>" response has been registered</p>
    <p>Submit your answer to the next question:</p>
    <input type="radio" name="choice" value="A"> A<br>
    <input type="radio" name="choice" value="B"> B<br>
    <input type="radio" name="choice" value="C"> C<br>
    <input type="radio" name="choice" value="D"> D<br>
    <p></p>
    <input type="submit" value="Submit">
</form>
</body>
</html>

