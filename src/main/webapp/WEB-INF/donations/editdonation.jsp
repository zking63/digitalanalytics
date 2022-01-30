<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>   
<head>
	<meta charset="ISO-8859-1">
	 <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" 
		integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" 
		crossorigin="anonymous">
	<link rel="stylesheet" href="/css/main.css"/>
	<title>Update donation</title>
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
	<div class="user-form">
	<h2>Edit donation</h2>
	    <form:form method="POST" action="/donations/edit/${donation.id}" modelAttribute="donation">
	    	<form:hidden value="${ user.id }" path="donation_uploader"/>
	    	<form:hidden value="${ committee.id }" path="committee"/>
	    	<p>
	            <form:label path="amount">Amount:</form:label>
	            <form:errors path="amount"></form:errors>
	            <form:input type="long" path="amount"/>
	        </p>
	        <p>
	            <form:label path="Dondate">Date:</form:label>
	            <form:errors path="Dondate"></form:errors>
	            <form:input type="Dondate" path="Dondate"/>
	        </p>
	        <p>
	            <form:label path="recurring">Recurring:</form:label>
	            <form:errors path="recurring"></form:errors>
	            <form:input type="recurring" path="recurring"/>
	        </p>
	        <p>
	            <form:label path="recurrenceNumber">Recurrence Number:</form:label>
	            <form:errors path="recurrenceNumber"></form:errors>
	            <form:input type="recurrenceNumber" path="recurrenceNumber"/>
	        </p>
	        <p>
	            <form:label path="ActBlueId">ActBlue Recipient Id:</form:label>
	            <form:errors path="ActBlueId"></form:errors>
	            <form:input type="ActBlueId" path="ActBlueId"/>
	        </p>
	        <p>
		        <label for="donor">Assign a donor:</label>
				<select id="donor" name="donor">
				  	<c:forEach items="${ committee.getDonors() }" var="p">
			        	<option value="${ p.id }">${ p.donorEmail }</option>
			        </c:forEach>
				</select>
	        </p>
	        <p>
		        <label for="emailDonation">Assign an email:</label>
				<select id="emailDonation" name="emailDonation">
				  	<c:forEach items="${ committee.getEmails() }" var="e">
			        	<option value="${ e.id }">${ e.emailRefcode }</option>
			        </c:forEach>
				</select>
	        </p>
	        <input type="submit" value="Submit"/>
	    </form:form>
	</div> 
</body>
</html>