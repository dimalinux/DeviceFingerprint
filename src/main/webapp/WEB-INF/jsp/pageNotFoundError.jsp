<%@page pageEncoding="UTF-8" %>
<%@page session="false"%>
<%@page isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><!doctype html>
<html lang="en">
    <head>
        <meta http-equiv="refresh" content="10; url=${homePage}">
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=yes">
        <title>404 - Document Not Found</title>
    </head>
    <body>
        <p>Sorry, the requested URL <c:out value="${requestedPage}"/> was not found.</p>
        <p>Click <a href="${homePage}">here</a> for our home page.</p>
    </body>
</html>
