<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>   
<head>
	<meta charset="ISO-8859-1">
    <%@include file="header.jsp" %>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" 
		integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" 
		crossorigin="anonymous">
	<link rel="stylesheet" href="/css/main.css"/>
    <title>Email page</title>
</head>
<body>
	<div class="wrapper">
		<div class="table table-hover">
			<h1 style="text-align:center;">${ emailgroup.emailgroupName }</h1>
			<c:if test="${ nofullsend != NULL}">
				<p>Didn't fully send</p>
			</c:if>
			<h2>Properties</h2>
			<table class="table table-hover">
			    <thead>
			        <tr>
			            <th>Send date </th>
			            <th>Subject line </th>
			            <th>Sender </th>
			            <th>Category </th>
			            <th>Test </th>
			            <th style="max-width:250px;">Link </th>
			        </tr>
			    </thead>
				<tbody>
					<tr>
						<td>${ emailgroup.getDateFormatted() }</td>
						<td><c:choose>
				        				<c:when test="${emailgroup.getGroupTest() == 'SUBJECT' && nofullsend == NULL}">
				        					<p><b>${ winningsubject }</b></p>
				        					
				        					<c:choose>
				        					<c:when test="${prospectsubject != NULL}">
				        						<p><b>${ prospectsubject}</b></p>
				        					</c:when>
				        					<c:otherwise>
				        						<p>${ losingsubject}</p>
				        					</c:otherwise>
				        					</c:choose>
				        				</c:when>
				        				<c:otherwise>
											<p>${ winningsubject} </p>
											<c:if test="${ nofullsend != NULL}">
												<p>${losingsubject}</p>
											</c:if>
				        				</c:otherwise>
				        				</c:choose>
				        </td>
						<td><c:choose>
				        				<c:when test="${emailgroup.getGroupTest() == 'SENDER' && nofullsend == NULL}">
				        					<p><b>${ winningsender }</b></p>
				        					<c:choose>
				        					<c:when test="${prospectsender != NULL}">
				        						<p><b>${ prospectsender}</b></p>
				        					</c:when>
				        					<c:otherwise>
				        						<p>${ losingsender}</p>
				        					</c:otherwise>
				        					</c:choose>
				        				</c:when>
				        				<c:otherwise>
											<p>${ winningsender} </p>
											<c:if test="${ nofullsend != NULL}">
												<p>${losingsender}</p>
											</c:if>
				        				</c:otherwise>
				        				</c:choose>
				        </td>
				        <td>${ emailgroup.groupCategory}</td>
						<td>${ emailgroup.groupTest}</td>
						<td style="max-width:250px;">${ emailgroup.link}</td>
					</tr>
				</tbody>
			</table>
			<h2>Fundraising data</h2>
			<table class="table table-hover">
			    <thead>
			        <tr>
			        	<th>Donations/opens </th>
			        	<th>Donations/clicks </th>
			            <th>Donations </th>
			            <th>Revenue </th>
			            <th>Average donation </th>
			            <c:if test="${ emailgroup.getTandemrevenue() != 0.0}">
								<th>Tandem revenue</th>
						</c:if>
						<c:if test="${ emailgroup.getTandemdonations() != 0}">
								<th>Tandem donations</th>
						</c:if>
			        </tr>
			    </thead>
				<tbody>
					<tr>
						<td>${emailgroup.getdonationsOpensFormatted()}</td>
						<td>${emailgroup.getdonationsClicksFormatted()}</td>
						<td>${emailgroup.groupdonationcount}</td>
						<td>$${emailgroup.getGroupSumFormatted()}</td>
						<td>$${emailgroup.getAverageFormatted()}</td>
						<c:if test="${ emailgroup.getTandemrevenue() != 0.0}">
								<td>${emailgroup.getTandemRevenueFormatted()}</td>
						</c:if>
						<c:if test="${ emailgroup.getTandemdonations() != 0}">
								<td>${emailgroup.getTandemdonations() }</td>
						</c:if>
					</tr>
				</tbody>
			</table>
			<h2>Performance data</h2>
			<table class="table table-hover">
			    <thead>
			        <tr>
			            <th>Open rate </th>
			            <th>Clicks/opens </th>
			            <th>Unsubscribe rate </th>
			            <th>Bounce rate </th>
			            <th>Opens </th>
			            <th>Clicks </th>
			        </tr>
			    </thead>
				<tbody>
					<tr>
						<td>${emailgroup.getOpenRateFormatted()}</td>
						<td>$${emailgroup.getclicksOpensFormatted()}</td>
						<td>$${emailgroup.getunsubscribeRateFormatted()}</td>
						<td>${emailgroup.getbounceRateFormatted()}</td>
						<td>${emailgroup.getGroupOpenersFormatted() }</td>
						<td>${emailgroup.getGroupClicksFormatted()}</td>
					</tr>
				</tbody>
			</table>
		</div>
					<table style="text-align:center;margin-left:225px;"><tbody><tr><td style="text-align:center;margin-left:300px;"> 
			${emailgroup.getEmails().get(0).content} 
			</td></tr></tbody></table>
	</div> 
</body>
</html>