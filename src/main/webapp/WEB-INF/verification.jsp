<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" 
		integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" 
		crossorigin="anonymous">
	<link rel="stylesheet" href="/css/main.css"/>
    <title>Verification</title>
</head>
<h2 style="margin: 500px;margin-top: 100px;">${message}</h2> 
<c:if test="${login != null }"><p><a href="/">${login}</a></p>
</c:if>

	    <form method="GET" action="/verify">
	    				<input type="hidden" name="petition" value="${code}">
						<input type="hidden" name="other" value="${email}">
	
</form>

	</div>  
</body>
</html>