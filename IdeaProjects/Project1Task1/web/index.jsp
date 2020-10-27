<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/26/2020
  Time: 10:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
  <head>
    <title>Hash Computer</title>
  </head>
  <body>
    <form action="getATextInput" method="GET">
      <h1>Hash Computer</h1>
      <label for="letter">Type the text:</label>
      <input type="text" name="input"><br>
      <p></p>
      <%--Create 2 radio buttons for MD5 and SHA-256. Create 1 submit button--%>
      <input type="radio" name="type" value="MD5" checked> MD5<br>
      <input type="radio" name="type" value="SHA-256"> SHA-256<br>
      <p></p>
      <input type="submit" value="Submit">
    </form>
  </body>
</html>
