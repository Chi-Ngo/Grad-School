<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/27/2020
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%= request.getAttribute("doctype") %>

<html>
    <head>
        <title>Bird Picture</title>
    </head>
    <body>
        <%--If a url can be found, display the picture, credit, and photographer--%>
        <% if (request.getAttribute("pictureURL") != null) { %>
        <h1>Bird chosen: <%= request.getAttribute("birdName")%></h1><br>
        <img src="<%= request.getAttribute("pictureURL")%>"><br><br>
        <p>Credit: https://nationalzoo.si.edu/migratory-birds</p>
        <p>Photographer: <%= request.getAttribute("author")%></p>
        <%--If a url cannot be found, display the error message to the user--%>
        <% } else { %>
        <h1>A picture of a <%= request.getAttribute("birdName")%> could not be found</h1><br>
        <% } %>
    </body>
    <%--Button to redirect user to index.jsp--%>
    <form action="redirect" method="GET">
        <input type="submit" value="Continue">
    </form>
</html>
