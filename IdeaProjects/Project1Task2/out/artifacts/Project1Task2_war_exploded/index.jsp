<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/27/2020
  Time: 1:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%= request.getAttribute("doctype")%>

<html>
  <head>
    <title>Bird Picture</title>
  </head>
  <body>
      <form action="getAName" method="GET">
        <h1>Migratory Birds</h1>
        <p>Created by Chi Ngo</p>
        <p>Enter the name of a bird</p>
        <%--get the input string from the user and submit--%>
        <input type="text" name="name"><br>
        <p></p>
        <input type="submit" value="Submit">
      </form>
  </body>
</html>
