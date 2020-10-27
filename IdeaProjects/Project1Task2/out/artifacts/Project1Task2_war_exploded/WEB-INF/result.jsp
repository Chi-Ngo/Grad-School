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
        <% if (request.getAttribute("pictureURL") != null) { %>
            <%--<h1>Bird chosen: <%= request.getParameter("value")%></h1><br>--%>
            <img src="<%= request.getAttribute("pictureURL")%>"><br><br>
        <% } else { %>
            <h1>A picture of a <%= request.getParameter("value")%> could not be found</h1><br>
        <% } %>
    </body>
</html>
