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
 <div class="online-body">
<form method="get" action="/emails" id="input-form">
				    <input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="sort" value="${ sort}">
					<input type="hidden" name="direction" value="${ direction}">
				    
<div class="online-body-operators">
 <p>${category }</p>
  <p>${type }</p>
   <p>${operator }</p>
    <p>${operand }</p>
    					    	<div id="online-parameter-choices">
    					  
						    	
				
						  		    <div  id="date-choice">
						  		    	
										<input type="date" value="${startdateE}" name="startdateE"/>
				
									</div>
									
									<div  id="date-choice">
								
										<input type="date" value="${enddateE}" name="enddateE"/>
									</div>
								
				
					    	</div>

			
							        <div style="display:inline-block;max-width:400px;margin-bottom: 10px;margin-top: 0px;margin-left: 0px;"id="online-parameter-choices">
					       <p style="margin: 0px;vertical-align: top;"> <label for="category">Select category:</label></p>
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
		            <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="date">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="date">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>v</button>
					</form>
				</th>
	            <th>Total revenue</br> 
	            	 <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="revenue">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="revenue">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>v</button>
					</form>
	            </th>
	            <th>Average Donation</br> 
	            	<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="average">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="average">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>v</button>
					</form>
	            </th>
	            <th>Number of donations</br> 
	                <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="donations">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="donations">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<button>v</button>
					</form>
	            </th>
	            <th>Recipients</th>
	            <th>Opens</th>
	            <th>Clicks</th>
	            <th>Bounces</th>
	            <th>Unsubscribers</th>
	        </tr>
	    </thead>
		<tbody>
			<c:forEach items="${ email }" var="e">
				<tr>
					<td>${ e.emailgroupName }</td>
					<td>${e.date}</td>
					<td>$${e.groupsum}</td>
					<td>$${e.groupaverage}</td>
					<td>${e.groupdonationcount}</td>
					<td>${e.groupRecipients}</td>
					<td>${e.groupOpeners}</td>
					<td>${e.groupClicks}</td>
					<td>${e.groupBounces}</td>
					<td>${e.groupUnsubscribers}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>