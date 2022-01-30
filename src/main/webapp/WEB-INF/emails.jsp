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
	<title>Emails</title>
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> 
<link rel="stylesheet" href="/css/main.css"/>
	<script type="text/javascript">
		$(document).ready(function(){
		  $("#input-form").change(function() {
		     $("#field").submit();
		  });
		});
	</script>
</head>

<body>
     <div class="navbar">
     <h1 class="titles"><a href="/home">LoJo Fundraising</a></h1>
	    <form:form method="POST" action="/committees/select" class="p-4">
	    <input type="hidden" name="page" value="${page}">
	        <p>
		        <label for="committee"></label>
				<select onchange="this.form.submit()" id="committee" name="committee">
					<option class="currentcommittee" value="${ committee.id }">${ committee.getCommitteeName() }</option>
				  	<c:forEach items="${ committees }" var="e">
			        	<option value="${ e.id }">${ e.getCommitteeName() }</option>
			        </c:forEach>
				</select>
	        </p>
	    </form:form>
        <ul class="navbarmenu">
            <li>
            <button class="btn btn-secondary main">
			<a href="/home">Home</a>
			</button>
            </li>
            <li>
           		<div class="dropdown">
				  <button class="btn btn-secondary dropdown-toggle" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    Donations
				  </button>
				  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				  	<a class="dropdown-item" href="/home">Donations</a>
				    <a class="dropdown-item" href="/newdonation">New donation</a>
				    <a class="dropdown-item" href="/import/donations">Import donations</a>
				    <a class="dropdown-item" href="/export">Export donations</a>
				 	</div>
				</div>
            </li>
            <li>
           		<div class="dropdown">
				  <button class="btn btn-secondary dropdown-toggle" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    Donors
				  </button>
				  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				  	<a class="dropdown-item" href="/donors">Donors page</a>
				    <a class="dropdown-item" href="/newdonor">New donor</a>
				    <a class="dropdown-item" href="/import/donations">Import donors by donations</a>
				    <a class="dropdown-item" href="/export">Export donors</a>
				 	</div>
				</div>
            </li>
            <li>
           		<div class="dropdown">
				  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    Emails
				  </button>
				  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				 	<a class="dropdown-item" href="/emails">Emails</a>
				    <a class="dropdown-item" href="/newemail">New email</a>
				    <a class="dropdown-item" href="/import/emails">Import emails</a>
				    <a class="dropdown-item" href="/export">Export</a>
				 	</div>
				</div>
            </li>
            <li>
           		<div class="dropdown">
				  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    <img src="/images/usericon.png" alt="User">
				  </button>
				  <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
				  	<p>${ user.firstName } ${ user.lastName }</p>
					<form:form method="POST" action="/committees/select" class="dropdown-item formclass">
				    <input type="hidden" name="page" value="${page}">
				        <p>
					        <label for="committee"></label>
							<select onchange="this.form.submit()" id="committee" name="committee">
								<option class="currentcommittee" value="${ committee.id }">${ committee.getCommitteeName() }</option>
							  	<c:forEach items="${ committees }" var="e">
						        	<option value="${ e.id }">${ e.getCommitteeName() }</option>
						        </c:forEach>
							</select>
				        </p>
				    </form:form>
				    <div class="dropdown-divider"></div>
				    <a class="dropdown-item" href="/logout">Logout</a>
				 </div>
				</div>
            </li>
        </ul>
        </div>
	<h2>Emails</h2>
	<form method="post" class="date-form" action="/emails">
		<input type="date" value="${startdateE}" name="startdateE"/>
		<input type="date" value="${enddateE}" name="enddateE"/>
		<button>Set</button>
	</form>
	<table class="table table-hover">
	    <thead>
	        <tr>
	            <th>Name</th>
	            <th>Send date</br> 		
		            <form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="datetime">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="datetimeup">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>v</button>
					</form>
				</th>
	            <th>Refcode</th>
	            <th>Refcode 2</th>
	            <th>Total revenue</br> 
	            	 <form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="sum">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="sumup">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>v</button>
					</form>
	            </th>
	            <th>Average Donation</br> 
	            	<form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="average">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="averageup">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>v</button>
					</form>
	            </th>
	            <th>Number of donations</br> 
	                <form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="donationscount">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="donationscountup">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>v</button>
					</form>
	            </th>
	            <th>Number of donors</br>
	               <form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="donorcount">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/emails">
						<input type="hidden" name="field" value="donorcountup">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<button>v</button>
					</form>
	            </th>
	            <th>Recipient list</th>
	            <th>Excluded list</th>
	            <th>Recipients</th>
	            <th>Opens</th>
	            <th>Clicks</th>
	            <th>Bounces</th>
	            <th>Unsubscribers</th>
	            <th>Action</th>
	        </tr>
	    </thead>
		<tbody>
			<c:forEach items="${ email }" var="e">
				<tr>
					<td><a href="/emails/${e.id}">${ e.emailName }</a></td>
					<td>${e.getEmailDateFormatted()}</td>
					<td>${e.emailRefcode1}</td>
					<td>${e.emailRefcode2}</td>
					<td>$${e.emaildonationsum}</td>
					<td>$${e.getEmailAverageFormatted()}</td>
					<td>${e.emaildonationcount}</td>
					<td>${e.emaildonorcount}</td>
					<td>${e.getList()}</td>
					<td>${e.getExcludedList()}</td>
					<td>${e.getRecipients()}</td>
					<td>${e.getOpeners()}</td>
					<td>${e.getClicks()}</td>
					<td>${e.getBounces()}</td>
					<td>${e.getUnsubscribers()}</td>
					<td>
						<p><a href="/emails/edit/${e.id}">Edit</a></p>
						<p><a href="/emails/delete/${e.id}">Delete</a></p>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>