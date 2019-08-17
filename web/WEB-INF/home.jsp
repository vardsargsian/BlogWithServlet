<%@ page import="model.Post" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Karen
  Date: 13.02.2019
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
<% List<Post> posts = (List<Post>) request.getAttribute("posts"); %>
Posts:
<%
    for (Post post : posts) { %>
<div>
    <h2><%=post.getTitle()%>
    </h2>
    <div>
        <img src="/getImage?picName=<%=post.getPicUrl()%>" width="100"/>
    </div>
    <p><%=post.getShortText()%>
    </p>
</div>
<hr>
<%
    }
%>
<br>


</body>
</html>
