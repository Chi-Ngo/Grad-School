<%--Chi Ngo
    cngongoc
--%>
<%@ page import="java.util.List" %>
<%@ page import="org.bson.Document" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: linhc
  Date: 4/1/2020
  Time: 1:00 PM
  To change this template use File | Settings | File Templates.
  This jsp page takes the list of documents and the map of analytics
  And display them in tabular forms
  First table is top 3 specialties
  Second table is top 3 cities
  Third table is top 3 states
  Last table is a full log of all queries sent to the server
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Dashboard</title>
</head>
<body>
<form action="getDashboard" method="GET">
<%List documents = (List) request.getAttribute("documents");%>
    <%Map analytics = (Map) request.getAttribute("analytics");%>
    <h1>Project 4 Dashboard</h1>
    <h3>Here are some interesting pieces of information</h3>
    <p>The top 3 most popular specialties searched are:</p>
    <div align="left">
        <table border="2" cellpadding="10">
            <tr>
                <th>Rank</th>
                <th>Specialty</th>
            </tr>
            <%String[] specialties = (String[]) analytics.get("specialty");
            for (int i = 0; i < specialties.length; i++) {%>
            <tr>
                <td><%=i+1%></td>
                <td><%=specialties[i]%></td>
            </tr>
            <%}%>
        </table>
    </div>
    <p>The top 3 most popular cities searched are: </p>
    <div align="left">
        <table border="2" cellpadding="10">
            <tr>
                <th>Rank</th>
                <th>City</th>
            </tr>
            <%String[] cities = (String[]) analytics.get("city");
                for (int i = 0; i < cities.length; i++) {%>
            <tr>
                <td><%=i+1%></td>
                <td><%=cities[i]%></td>
            </tr>
            <%}%>
        </table>
    </div>
    <p>The top 3 most popular states searched are:</p>
    <div align="left">
        <table border="2" cellpadding="10">
            <tr>
                <th>Rank</th>
                <th>State</th>
            </tr>
            <%String[] states = (String[]) analytics.get("state");
            for (int i = 0; i < states.length; i++) {%>
            <tr>
                <td><%=i+1%></td>
                <td><%=states[i]%></td>
            </tr>
            <%}%>
        </table>
    </div>
<div align="left">
    <table border="2" cellpadding="10">
        <caption><h2>Logs</h2></caption>
        <tr>
            <th>ID</th>
            <th>Timestamp</th>
            <th>Device Information</th>
            <th>Specialty</th>
            <th>City</th>
            <th>State</th>
            <th>Zip</th>
            <th>Response Code</th>
            <th>Number of Doctors Found</th>
        </tr>
        <%int i = 0;%>
        <%for (int j = documents.size()-1; j >= 0; j--) {%>
            <%i+=1;%>
            <%Document doc = (Document) documents.get(j);%>
        <tr>
            <td><%=i%></td>
            <td><%=(String) doc.get("timestamp")%></td>
            <td><%=(String) doc.get("device")%></td>
            <td><%=(String) doc.get("specialty")%></td>
            <td><%=(String) doc.get("city")%></td>
            <td><%=(String) doc.get("state")%></td>
            <td><%=(String) doc.get("zip")%></td>
            <td><%=(String) doc.get("code")%></td>
            <td><%=(String) doc.get("number")%></td>
        </tr>
        <%}%>
</table>
</div>
</form>
</body>
</html>
