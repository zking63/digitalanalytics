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
    <title>Email page</title>
</head>
<body>
     <div class="navbar">
     	<h1 class="titles"><a href="/home">LoJo Fundraising</a></h1>
        <ul class="navbarmenu">
            <li class="main"><a href="/home">Home</a>
            </li>
            <li><a href="/donors">Donors</a></li>
            <li><a href="/emails">Emails</a></li>
            <li><a href="/logout">Logout</a></li>
        </ul>
    </div>
    <div class="buttons1">
    <button><a href="/newdonor">Upload a new donor</a></button>
	<button><a href="/newdonation">Upload a new donation</a></button>
	<button><a href="/newemail">Upload a new email</a></button>
	</div>
	<div class="wrapper">
		<div class="table table-hover">
			<h1><b>Email:</b> ${ emailgroup.emailgroupName }.</h1>
			<h2>Properties</h2>
			<table class="table table-hover">
			    <thead>
			        <tr>
			            <th>Send date</th>
			            <th>Subject line</th>
			            <th>Sender</th>
			            <th>Testing</th>
			        </tr>
			    </thead>
				<tbody>
					<tr>
						<td>${ emailgroup.getDateFormatted() }</td>
						<td><c:choose>
				        				<c:when test="${emailgroup.getGroupTest() == 'SUBJECT'}">
				        					<p><b>${ emailgroup.fullsendvariant }</b></p>
				        					<c:choose>
				        					<c:when test="${emailgroup.variantA == emailgroup.fullsendvariant}">
				        						<p>${ emailgroup.variantB}</p>
				        					</c:when>
				        					<c:otherwise>
				        						<p>${ emailgroup.variantB}</p>
				        					</c:otherwise>
				        					</c:choose>
				        				</c:when>
				        				<c:otherwise>
											<p>emailgroup.getEmails().get(0).getSubjectLine()</p>
				        				</c:otherwise>
				        				</c:choose>
				        </td>
						<td><c:choose>
				        				<c:when test="${emailgroup.getGroupTest() == 'SENDER'}">
				        					<p><b>${ emailgroup.fullsendvariant }</b></p>
				        					<c:choose>
				        					<c:when test="${emailgroup.variantA == emailgroup.fullsendvariant}">
				        						<p>${ emailgroup.variantB}</p>
				        					</c:when>
				        					<c:otherwise>
				        						<p>${ emailgroup.variantB}</p>
				        					</c:otherwise>
				        					</c:choose>
				        				</c:when>
				        				<c:otherwise>
											<p>${ emailgroup.getEmails().get(0).getSender()}</p>
				        				</c:otherwise>
				        				</c:choose>
				        </td>
						<td>${ emailgroup.groupTest}</td>
					</tr>
				</tbody>
			</table>
			<h2>Fundraising data</h2>
			<table class="table table-hover">
			    <thead>
			        <tr>
			            <th>Total number of donations</th>
			            <th>Total Revenue</th>
			            <th>Average donation</th>
			        </tr>
			    </thead>
				<tbody>
					<tr>
						<td>${emailgroup.groupdonationcount}</td>
						<td>$${ emailgroup.getGroupSumFormatted() }</td>
						<td>$${ emailgroup.getAverageFormatted()}</td>
					</tr>
				</tbody>
			</table>
			<h2>Performance data</h2>
			<table class="table table-hover">
			    <thead>
			        <tr>
			            <th>Total number of donations</th>
			            <th>Total Revenue</th>
			            <th>Average donation</th>
			        </tr>
			    </thead>
				<tbody>
					<tr>
						<td>${emailgroup.groupdonationcount}</td>
						<td>$${ emailgroup.getGroupSumFormatted() }</td>
						<td>$${ emailgroup.getAverageFormatted()}</td>
					</tr>
				</tbody>
			</table>
		</div>
					<table><tbody><tr><td style="text-align:center;margin-left:300px;"> 
			${emailgroup.getEmails().get(0).content} 
			</td></tr></tbody></table>
	</div> 
</body>
</html>