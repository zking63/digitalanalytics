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
	<title>Export</title>
	
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
    <form method="get" id="input-form" action="/export/query/options">
   	<div id="export-first-question">
        <label for="field">What are you exporting?</label>
		<select onchange="this.form.submit()" id="field" name="field">
		<c:choose>
			<c:when test="${ field == 0}">
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
		        <option name="field" value="2">Donations</option>
		        <option name="field" value="3">Donors</option>
		        <option name="field" value="5">Tests</option>
			</c:when>
			<c:when test="${ field == 1}">
				<option name="field" value="1">Emails</option>
		        <option name="field" value="2">Donations</option>
		        <option name="field" value="3">Donors</option>
		        <option name="field" value="5">Tests</option>
		        <option name="field" value="0">Email groups</option>
			</c:when>
			<c:when test="${ field == 2}">
				<option name="field" value="2">Donations</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="0">Email groups</option>
		        <option name="field" value="3">Donors</option>
		        <option name="field" value="5">Tests</option>
			</c:when>
			<c:when test="${ field == 3}">
				<option name="field" value="3">Donors</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="2">Donations</option>
				<option name="field" value="5">Tests</option>
			</c:when>
			<c:when test="${ field == 5}">
				<option name="field" value="5">Tests</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
				<option name="field" value="2">Donations</option>
				<option name="field" value="3">Donors</option>
			</c:when>
			<c:otherwise>
				<option name="field" value="4">Select</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
		        <option name="field" value="2">Donations</option>
		        <option name="field" value="3">Donors</option>
		        <option name="field" value="5">Tests</option>
			</c:otherwise>
		</c:choose>
		</select>
    </div>
    </form>
 <div class="export-body">
  <div class="export-body-operators">
    <c:choose>
    <c:when test="${ field != 4}">
					<form:form method="get" action="/export/query/options/range" id="input-form">
					<div id="parameter-choices">
				    <input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="field" value="${field}">
				        <label for="range">Time period:</label>
							<select onchange="this.form.submit()" id="range" name="range">
							<c:choose>
								<c:when test="${ range == 0}">
									<option name="range" value="0">Select</option>
							  		<option name="range" value="1">Within range</option>
							  		<option name="range" value="2">All time</option>
							  	</c:when>
							  	<c:when test="${ range == 1}">
							  		<option name="range" value="1">Within range</option>
							  		<option name="range" value="2">All time</option>
							  	</c:when>
							  	<c:otherwise>
							  		<option name="range" value="2">All time</option>
							  		<option name="range" value="1">Within range</option>
							  	</c:otherwise>
							</c:choose>
							</select>
				        </div>
				    </form:form>	
							</c:when>
			</c:choose>
<form method="get" action="/export/query/excel" id="input-form">
				    <input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="field" value="${field}">
				    <input type="hidden" name="range" value="${range}">
    					    	<c:if test="${ range == 1 }">
						    	<div id="parameter-choices">
						  		    <div id="date-choice">
										<input type="date" value="${startdateD}" name="startdateD"/>
									</div>
									<div id="date-choice">
										<input type="date" value="${enddateD}" name="enddateD"/>
									</div>
								</div>
					    	</c:if>
			<c:choose>
			<c:when test="${field == 1}">
					<div id="parameter-choices">
				        <label for="type">Select search factor:</label>
				        <select id="type" name="type">
											<option value="${type}">${type}</option>
											<option value="Refcode 1">Refcode 1</option>
											<option value="Refcode 2">Refcode 2</option>
											<option value="Title">Title</option>
											<option value="Category">Category</option>
											<option value="Subject">Subject line</option>
											<option value="Sender">Sender</option>
											<option value="Testing">Testing</option>
											<option value="Link">Link</option>
											<option value="All emails">All emails</option>
						</select>
				        </div>
				        <div id="parameter-choices">
					        <label for="operator">Select operator:</label>
							<select id="operator" name="operator">
							  		<option value="${operator }">${operator }</option>
									<option value="Equals">Equals</option>
									<option value="Contains">Contains</option>
									<option value="Is blank">Is blank</option>
							</select>
				        </div>
				        <div id="parameter-choices">
					        <label for="operand"></label>
									<textarea name="operand" placeholder="${operand }"></textarea>
						</div>
				        </div>
				        <div id="export-choices-box">
				        <p>
				        <label for="input">Select data fields:</label>
				        </p>
				        			<div id="export-choices">
										<input type="checkbox" id="input" name="input" value="Clicks">
										<label for="input"> Clicks</label><br>
										<input type="checkbox" id="input" name="input" value="Opens">
										<label for="input"> Opens</label><br>
										<input type="checkbox" id="input" name="input" value="Bounces">
										<label for="input"> Bounces</label><br>	
										<input type="checkbox" id="input" name="input" value="Unsubscribes">
										<label for="input"> Unsubscribes</label><br>	
										<input type="checkbox" id="input" name="input" value="Open rate">
										<label for="input"> Open rate</label><br>	
										<input type="checkbox" id="input" name="input" value="Click rate">
										<label for="input"> Click rate</label><br>	
										<input type="checkbox" id="input" name="input" value="Unsubscribe rate">	
										<label for="input"> Unsubscribe rate</label><br>	
										<input type="checkbox" id="input" name="input" value="Bounce rate">
										<label for="input"> Bounce rate</label><br>	
										<input type="checkbox" id="input" name="input" value="Clicks/opens">	
										<label for="input"> Clicks per open</label><br>	
										<input type="checkbox" id="input" name="input" value="Revenue">
										<label for="input"> Revenue</label><br>	
										<input type="checkbox" id="input" name="input" value="Donations">	
										<label for="input"> Donations</label><br>	
										<input type="checkbox" id="input" name="input" value="Donors">
										<label for="input"> Donors</label><br>	
										<input type="checkbox" id="input" name="input" value="Average donation">
										<label for="input"> Average donation</label><br>		
									</div>
									<div id="export-choices">
										<input type="checkbox" id="input" name="input" value="Donations/open">
										<label for="input"> Donations per open</label><br>
										<input type="checkbox" id="input" name="input" value="Donations/click">
										<label for="input"> Donations per click</label><br>
										<input type="checkbox" id="input" name="input" value="Donors/open">
										<label for="input"> Donors per open</label><br>
										<input type="checkbox" id="input" name="input" value="Donors/click">
										<label for="input"> Donors per click</label><br>
										<input type="checkbox" id="input" name="input" value="Recurring donations">
										<label for="input"> Recurring donations</label><br>
										<input type="checkbox" id="input" name="input" value="Recurring donors">
										<label for="input"> Recurring donors</label><br>
										<input type="checkbox" id="input" name="input" value="Recurring revenue">
										<label for="input"> Recurring revenue</label><br>
										<input type="checkbox" id="input" name="input" value="variant">
										<label for="input"> Variant</label><br>
										<input type="checkbox" id="input" name="input" value="category">
										<label for="input"> Category</label><br>
										<input type="checkbox" id="input" name="input" value="link">
										<label for="input"> Link</label><br>
										<input type="checkbox" id="input" name="input" value="sender">
										<label for="input"> Sender</label><br>
										<input type="checkbox" id="input" name="input" value="subject">
										<label for="input"> Subject Line</label><br>
										<input type="checkbox" id="input" name="input" value="testing">
										<label for="input"> Testing</label><br>
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
			<div id="export-choices">
				<input type="checkbox" id="input" name="input" value="Clicks">
				<label for="input"> Clicks</label><br>
				<input type="checkbox" id="input" name="input" value="Opens">
				<label for="input"> Opens</label><br>
				<input type="checkbox" id="input" name="input" value="Open rate">
				<label for="input"> Open rate</label><br>	
				<input type="checkbox" id="input" name="input" value="Click rate">
				<label for="input"> Click rate</label><br>	
				<input type="checkbox" id="input" name="input" value="Clicks/opens">	
				<label for="input"> Clicks per open</label><br>	
				<input type="checkbox" id="input" name="input" value="Revenue">
				<label for="input"> Revenue</label><br>	
				<input type="checkbox" id="input" name="input" value="Donations">	
				<label for="input"> Donations</label><br>		
				</div>
				<div id="export-choices">	
				<input type="checkbox" id="input" name="input" value="Average donation">
				<label for="input"> Average donation</label><br>
				<input type="checkbox" id="input" name="input" value="Donations/open">
				<label for="input"> Donations per open</label><br>
				<input type="checkbox" id="input" name="input" value="Donations/click">
				<label for="input"> Donations per click</label><br>
				<input type="checkbox" id="input" name="input" value="Average email revenue">
				<label for="input"> Average email revenue</label><br>
				<input type="checkbox" id="input" name="input" value="emailcount">
				<label for="input"> Number of emails tested</label><br>
				<input type="checkbox" id="input" name="input" value="testcategory">
				<label for="input"> Test category</label><br>
				<input type="checkbox" id="input" name="input" value="Recipients">
				<label for="input"> Recipients</label><br>
				</div>
			</c:when>
		</c:choose>
			<button>Download Excel</button>
	</form>
							        </div>
</div>
</body>
</html>