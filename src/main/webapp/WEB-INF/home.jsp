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
	<title>Donors</title>
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
	<div class="wrapper">
	<h1>Recent Donations</h1>
		<form method="post" class="date-form" action="/home">
			<input type="date" value="${startdate}" name="startdate"/>
			<input type="date" value="${enddate}" name="enddate"/>
			<button>Set</button>
		</form>
	<table class="table table-hover">
	    <thead>
	        <tr>
	            <th>Donor</th>
	            <p><th>Amount 		
		            <form class="pointer" method="post" action="/home/sortdown">
						<input type="hidden" name="field" value="amount">
						<input type="hidden" name="startdate" value="${ startdate}">
						<input type="hidden" name="enddate" value="${ enddate}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/home/sortup">
						<input type="hidden" name="field" value="amount">
						<input type="hidden" name="startdate" value="${ startdate}">
						<input type="hidden" name="enddate" value="${ enddate}">
						<button>v</button>
					</form>
				</th></p>
	            <th>Email given to</th>
	            <p><th>Date/Time 		
		            <form class="pointer" method="post" action="/home/sortdown">
						<input type="hidden" name="field" value="datetime">
						<input type="hidden" name="startdate" value="${ startdate}">
						<input type="hidden" name="enddate" value="${ enddate}">
						<button>^</button>
					</form>
					<form class="pointer" method="post" action="/home/sortup">
						<input type="hidden" name="field" value="datetime">
						<input type="hidden" name="startdate" value="${ startdate}">
						<input type="hidden" name="enddate" value="${ enddate}">
						<button>v</button>
					</form>
				</th></p>
		</div>
	            <th>Action</th>
	        </tr>
	    </thead>
		<tbody>
			<c:forEach items="${ donations }" var="d">
				<tr>
					<td><a href="/donors/${d.donor.id}">${ d.donor.donorFirstName } ${d.donor.donorLastName}</a></td>
					<td>$${d.amount}</td>
					<td><a href="/emails/${d.emailDonation.id}">${d.emailDonation.emailName}</a></td>
					<td>${d.getDonationDateFormatted()}</td>
					<td>
						<p><a href="/donations/edit/${d.id}">Edit</a></p>
						<p><a href="/donations/delete/${d.id}">Delete</a></p>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>