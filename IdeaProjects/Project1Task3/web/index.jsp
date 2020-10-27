<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/28/2020
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%= request.getAttribute("doctype") %>

<html>
  <head>
    <title>DS Clicker</title>
  </head>
  <body>
  <%--method is POST as we are changing the state of the resource by increasing the count--%>
  <form action="submit" method="POST">
    <h1>Distributed Systems Class Clicker</h1>
    <p>Submit your answer to the current question:</p>
    <%--display 4 buttons A, B, C, D and a submit button--%>
    <input type="radio" name="choice" value="A"> A<br>
    <input type="radio" name="choice" value="B"> B<br>
    <input type="radio" name="choice" value="C"> C<br>
    <input type="radio" name="choice" value="D"> D<br>
    <p></p>
    <input type="submit" value="Submit">
  </form>
  </body>
</html>
