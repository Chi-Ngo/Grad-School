<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 1/27/2020
  Time: 2:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%= request.getAttribute("doctype") %>

<html>
    <head>
        <title>Bird Picture</title>
    </head>
    <body>
        <%--get the list of birds to populate the drop down menu
        if the list has some birds in it
        make the dropdown--%>
        <%ArrayList birds = (ArrayList) request.getAttribute("birdsList");%>
        <% if (birds.size() != 0) { %>
        <form action="getABird" method="GET">
            <p>Choose a bird from the dropdown</p>
            <%--create the dropdown menu dynamically based on the value in list of birds--%>
            <select name="bird">
                <%for (Object s: birds) {%>
                <option value="<%=s%>"><%=s%></option>
                <%}%>
            </select>
            <p></p>
            <input type="submit" value="Submit">
        </form>
        <%--if there is no bird found with the input string--%>
        <%} else {%>
            <%--output the error and let the user search for another bird--%>
            <form action="getAName" method="GET">
                <h2>This bird could not be found</h2>
                <p>Enter the name of another bird</p>
                <input type="text" name="name"><br>
                <p></p>
                <input type="submit" value="Submit">
            </form>
            <%}%>
    </body>
</html>
