<%-- 
    Document   : index
    Created on : 6 avr. 2023, 01:04:27
    Author     : ITU
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="test.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>test:<%= request.getAttribute("test") %></h1>
        <%
            Emp e=(Emp)request.getAttribute("emp");
        %>
        <h1>nom:<%= e.getNom() %></h1>
    </body>
</html>
