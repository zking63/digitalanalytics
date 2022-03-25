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
	<form method="post" class="input-form" action="/query">
    				<input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="operator" value="${operator}">
				    <input type="hidden" name="operand" value="${operand}">
				    <input type="hidden" name="type" value="${type}">
   	<div id="export-first-question">
        <label for="field">What are you searching?</label>
		<select onchange="this.form.submit()" id="field" name="field">
		<c:choose>
			<c:when test="${ field == 0}">
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
		        <option name="field" value="2">Links</option>
		        <option name="field" value="5">Tests</option>
			</c:when>
			<c:when test="${ field == 1}">
				<option name="field" value="1">Emails</option>
				<option name="field" value="2">Links</option>
		        <option name="field" value="5">Tests</option>
		        <option name="field" value="0">Email groups</option>
			</c:when>
			<c:when test="${ field == 2}">
				<option name="field" value="2">Links</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="0">Email groups</option>
		        <option name="field" value="5">Tests</option>
			</c:when>
			<c:when test="${ field == 3}">
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="2">Links</option>
				<option name="field" value="5">Tests</option>
			</c:when>
			<c:when test="${ field == 5}">
				<option name="field" value="5">Tests</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="2">Links</option>
			</c:when>
			<c:when test="${ field == 6}">
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="2">Links</option>
				<option name="field" value="5">Tests</option>
			</c:when>
			<c:otherwise>
				<option name="field" value="4">Select</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
		        <option name="field" value="2">Links</option>
		        <option name="field" value="5">Tests</option>
			</c:otherwise>
		</c:choose>
		</select>
    </div>
    </form>
 <div class="online-body">
<form method="get" action="/query/search" id="input-form">
				    <input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="field" value="${field}">
<div class="online-body-operators">
    					    	<div id="online-parameter-choices">
    					    	<c:if test="${ field != 4 }">
						    	
				
						  		    <div  id="date-choice">
						  		    	
										<input type="date" value="${startdateE}" name="startdateE"/>
				
									</div>
									
									<div  id="date-choice">
								
										<input type="date" value="${enddateE}" name="enddateE"/>
									</div>
								
					    	</c:if>
					    	</div>
			<c:choose>
			<c:when test="${field == 1}">
							        <div style="display:inline-block;max-width:400px;margin-bottom: 10px;margin-top: 0px;margin-left: 0px;"id="online-parameter-choices">
					       <p style="margin: 0px;vertical-align: top;"> <label for="operator">Select category:</label></p>
					        <div style="display:inline-block;max-width:200px; max-length:20px; margin: 3px; margin-top: 0px; vertical-align: top;">
				        				<input type="checkbox" id="input" name="category" value="Fundraiser">
										<label for="input">Fundraiser</label><br>
										<input type="checkbox" id="input" name="category" value="Survey">
										<label for="input">Survey</label><br>
										</div>
										<div style="display:inline-block;max-width:200px; max-length:10px; margin: 3px; margin-top: 0px; vertical-align: top;">
										<input type="checkbox" id="input" name="category" value="Petition">
										<label for="input">Petition</label><br>
										<input type="checkbox" id="input" name="category" value="Other">
										<label for="input">Other</label><br>
										</div>
			
				        </div>
					<div id="online-parameter-choices" style="vertical-align: top;">
				       <table style="display:inline;vertical-align: top;">
<tbody>
				        <tr>
				        <td>
				        <p style="display:inline;">
				        <label for="type">Select search factor:</label>
				        <select id="type" name="type">
											<option value="${type}">${type}</option>
											<option value="Refcode 1">Refcode 1</option>
											<option value="Refcode 2">Refcode 2</option>
											<option value="Title">Title</option>
											<option value="Subject">Subject line</option>
											<option value="Sender">Sender</option>
											<option value="Testing">Testing</option>
											<option value="Link">Link</option>
											<option value="Content">Content</option>
											<option value="All emails">All emails</option>
						</select>
						</p>
						</td>
						</tr>
							        <tr>
				        <td>
						<p style="display:inline;">
					        <label for="operator">Select operator:</label>
							<select id="operator" name="operator">
							  		<option value="${operator }">${operator }</option>
									<option value="Equals">Equals</option>
									<option value="Contains">Contains</option>
									<option value="Is blank">Is blank</option>
							</select>
						</p>
							</td>
						</tr>
						</tbody>
						</table>
					</div>
				        <div id="online-parameter-choices">
								    <table style="display:inline;vertical-align: top;width:200px;margin:0px;">
						
						<tbody>
				        <tr>
						<td>
					        <label for="operand"></label>
									<textarea name="operand" placeholder="${operand }"></textarea>
</td>
</tr>
<tr>
<td>
<p style="font-size: 11px;">hi</p>
   </tr>
						</tr>

						</tbody>
						</table>
						</div>
				        </div>
				
				</c:when>
				<c:when test="${field == 0}">
							        <div style="display:inline-block;max-width:400px;margin-bottom: 10px;margin-top: 0px;margin-left: 0px;"id="online-parameter-choices">
					       <p style="margin: 0px;vertical-align: top;"> <label for="operator">Select category:</label></p>
					        <div style="display:inline-block;max-width:200px; max-length:20px; margin: 3px; margin-top: 0px; vertical-align: top;">
				        				<input type="checkbox" id="input" name="category" value="Fundraiser">
										<label for="input">Fundraiser</label><br>
										<input type="checkbox" id="input" name="category" value="Survey">
										<label for="input">Survey</label><br>
										</div>
										<div style="display:inline-block;max-width:200px; max-length:10px; margin: 3px; margin-top: 0px; vertical-align: top;">
										<input type="checkbox" id="input" name="category" value="Petition">
										<label for="input">Petition</label><br>
										<input type="checkbox" id="input" name="category" value="Other">
										<label for="input">Other</label><br>
										</div>
			
				        </div>
					<div id="online-parameter-choices" style="vertical-align: top;">
				       <table style="display:inline;vertical-align: top;">
<tbody>
				        <tr>
				        <td>
				        <p style="display:inline;">
				        <label for="type">Select search factor:</label>
				        <select id="type" name="type">
											<option value="${type}">${type}</option>
											<option value="Refcode 1">Refcode 1</option>
											<option value="Refcode 2">Refcode 2</option>
											<option value="Title">Title</option>
											<option value="Subject">Subject line</option>
											<option value="Sender">Sender</option>
											<option value="Testing">Testing</option>
											<option value="Link">Link</option>
											<option value="Content">Content</option>
											<option value="All emails">All emails</option>
						</select>
						</p>
						</td>
						</tr>
							        <tr>
				        <td>
						<p style="display:inline;">
					        <label for="operator">Select operator:</label>
							<select id="operator" name="operator">
							  		<option value="${operator }">${operator }</option>
									<option value="Equals">Equals</option>
									<option value="Contains">Contains</option>
									<option value="Is blank">Is blank</option>
							</select>
						</p>
							</td>
						</tr>
						</tbody>
						</table>
					</div>
				        <div id="online-parameter-choices">
								    <table style="display:inline;vertical-align: top;width:200px;margin:0px;">
						
						<tbody>
				        <tr>
						<td>
					        <label for="operand"></label>
									<textarea name="operand" placeholder="${operand }"></textarea>
</td>
</tr>
<tr>
<td>
<p style="font-size: 11px;">hi</p>
   </tr>
						</tr>

						</tbody>
						</table>
						</div>
				        </div>
				</c:when>
			<c:when test="${ field == 2}">
			    <input type="checkbox" id="input" name="input" value="Amount">
				<label for="input"> Amount</label><br>
				<input type="checkbox" id="input" name="input" value="Date">
				<label for="input"> Date</label><br>
			</c:when>
			<c:when test="${ field == 3}">
			    <input type="checkbox" id="input" name="input" value="FirstName">
				<label for="input"> Name</label><br>
				<input type="checkbox" id="input" name="input" value="LastName">
				<label for="input"> Last</label><br>
			</c:when>
			<c:when test="${ field == 5}">
						<div id="online-parameter-choices">
				        <label for="type">Select search factor:</label>
				        <select id="type" name="type">
											<option value="${type}">${type}</option>
											<option value="Testing category">Testing category</option>
											<option value="Specific test">Specific test</option>
											<option value="All Tests">All tests</option>
						</select>
				        </div>
				        <div id="online-parameter-choices">
					        <label for="operator">Select operator:</label>
							<select id="operator" name="operator">
							  		<option value="${operator }">${operator }</option>
									<option value="Equals">Equals</option>
									<option value="Contains">Contains</option>
									<option value="Is blank">Is blank</option>
							</select>
				        </div>
				        <div id="online-parameter-choices">
					        <label for="operand"></label>
									<textarea name="operand" placeholder="${operand }"></textarea>
						</div>
				        </div>

			</c:when>
				<c:when test="${ field == 6}">
				    <input type="hidden" name="operator" value="${operator}">
				    <input type="hidden" name="operand" value="${operand}">
				    <input type="hidden" name="type" value="${type}">
				 </c:when>
		</c:choose>
			<button>Set</button>
	</form>
							        </div>
</div>
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