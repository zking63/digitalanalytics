<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page trimDirectiveWhitespaces="true" %>
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
    <form method="get" id="input-form" action="/export/query">
    				<input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="operator" value="${operator}">
				    <input type="hidden" name="operand" value="${operand}">
				    <input type="hidden" name="type" value="${type}">
				    <input type="hidden" name="startdateD" value="${startdateD}">
				    <input type="hidden" name="enddateD" value="${enddateD}">
     	<c:if test="${ message != null }">
				<p style="margin-left:470px;width:500px;text-align:center;"><b><span style="color:#FF0000;"><c:out value="${ message }" /></span></b></p>					
	</c:if>
	     	<c:if test="${ operandsList != null }">
				<p style="margin-left:470px;width:500px;text-align:center;"><b><span style="color:#FF0000;"><c:out value="${ operandsList }" /></span></b></p>					
	</c:if>
   	<div id="export-first-question">
        <label for="field">What are you exporting?</label>
		<select onchange="this.form.submit()" id="field" name="field">
		<c:choose>
			<c:when test="${ field == 0}">
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
		        <option name="field" value="6">Top 10 Report</option>
			</c:when>
			<c:when test="${ field == 1}">
				<option name="field" value="1">Emails</option>
		        <option name="field" value="6">Top 10 Report</option>
		        <option name="field" value="0">Email groups</option>
			</c:when>
			<c:when test="${ field == 6}">
				<option name="field" value="6">Top 10 Report</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
			</c:when>
			<c:otherwise>
				<option name="field" value="4">Select</option>
				<option name="field" value="0">Email groups</option>
				<option name="field" value="1">Emails</option>
		        <option name="field" value="6">Top 10 Report</option>
			</c:otherwise>
		</c:choose>
		</select>
    </div>
    </form>
 <div class="export-body">
<form method="get" action="/export/query/excel" id="input-form">
				    <input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="field" value="${field}">
				 
    					    	<div id="export-first-question" style="margin-left:455px;" >
    					    	<c:if test="${ field != 4 }">
						    	
				
						  		    <div  id="date-choice">
						  		    	
										<input type="date" value="${startdateD}" name="startdateD"/>
				
									</div>
									
									<div  id="date-choice">
								
										<input type="date" value="${enddateD}" name="enddateD"/>
									</div>
								
					    	</c:if>
					    	</div>
			<div class="export-body-operators">
			<c:choose>
			<c:when test="${field == 1}">
							        <div style="display:inline-block;max-width:400px;margin-bottom: 10px;margin-top: 0px;margin-left: 200px;"id="parameter-choices">
					       <p style="margin-bottom: 0px;"> <label for="operator">Select category:</label></p>
					        <div style="display:inline-block;max-width:200px; max-length:20px; margin: 3px; margin-top: 0px; vertical-align: top;">
				        				<c:choose>
				        				<c:when test="${fundraiser == 1}">
				        					<input type="checkbox" id="input" name="category" value="Fundraiser" checked = "checked">
											<label for="input">Fundraiser</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Fundraiser">
											<label for="input">Fundraiser</label><br>
				        				</c:otherwise>
				        				</c:choose>
				        				<c:choose>
				        				<c:when test="${survey== 1}">
				        					<input type="checkbox" id="input" name="category" value="Survey" checked = "checked">
											<label for="input">Survey</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Survey">
											<label for="input">Survey</label><br>
				        				</c:otherwise>
				        				</c:choose>
				        				</div>
				        				<div style="display:inline-block;max-width:200px; max-length:10px; margin: 3px; margin-top: 0px; vertical-align: top;">
				        				<c:choose>
				        				<c:when test="${petition== 1}">
				        					<input type="checkbox" id="input" name="category" value="Petition" checked = "checked">
											<label for="input">Petition</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Petition">
											<label for="input">Petition</label><br>
				        				</c:otherwise>
				        				</c:choose>
				        				
				        				<c:choose>
				        				<c:when test="${other== 1}">
				        					<input type="checkbox" id="input" name="category" value="Other" checked = "checked">
											<label for="input">Other</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Other">
											<label for="input">Other</label><br>
				        				</c:otherwise>
				        				</c:choose>
										</div>
			
				        </div>
					<div id="parameter-choices">
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
											<option value="All">All</option>
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
						<c:choose>
				        <c:when test="${operand != NULL}">
					        <label for="operand"></label>
									<textarea name="operand">${operand }</textarea>
						</c:when>
						<c:otherwise>
					        <label for="operand"></label>
									<textarea name="operand" placeholder="Operand"></textarea>
						</c:otherwise>
						</c:choose>
									<p style="font-size: 11px;">Place each operand in single quote marks. To include operands where multiple must be true, separate them with a plus sign. To include operands where any may be true separate them by /. To include both put the OR clause in parenthesis.</p>
									<p style="font-size: 11px;">To search emails that include "Biden" and either "approve" or "support" you'd write 'Biden' + ('approve'/'support')</p>
						</div>
				        </div>
				        <div id="export-choices-box">
				        <p>
				        <label for="input">Select data fields:</label>
				        </p>
				        			<div id="export-choices">
				        				<input type="checkbox" id="input" name="input" value="Refcode 1">
										<label for="input"> Refcode 1</label><br>
										<input type="checkbox" id="input" name="input" value="List">
										<label for="input"> List</label><br>
				        				<input type="checkbox" id="input" name="input" value="category">
										<label for="input"> Category</label><br>
										<input type="checkbox" id="input" name="input" value="link">
										<label for="input"> Link</label><br>
										<input type="checkbox" id="input" name="input" value="sender">
										<label for="input"> Sender</label><br>
										<input type="checkbox" id="input" name="input" value="subject">
										<label for="input"> Subject Line</label><br>
										<input type="checkbox" id="input" name="input" value="Recipients">
										<label for="input"> Recipients</label><br>
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
									</div>
									<div id="export-choices">
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
										<input type="checkbox" id="input" name="input" value="Donations/opens">	
										<label for="input"> Donations per open</label><br>
										<input type="checkbox" id="input" name="input" value="Donations/clicks">	
										<label for="input"> Donations per click</label><br>
										<input type="checkbox" id="input" name="input" value="Average donation">
										<label for="input"> Average donation</label><br>	
										<input type="checkbox" id="input" name="input" value="Tandem revenue">
										<label for="input"> Tandem revenue</label><br>
										<input type="checkbox" id="input" name="input" value="Tandem donations">
										<label for="input"> Tandem donations</label><br>
										<input type="checkbox" id="input" name="input" value="Total revenue">
										<label for="input"> Total revenue</label><br>	
										<input type="checkbox" id="input" name="input" value="Recurring donations">
										<label for="input"> Recurring donations</label><br>
										<input type="checkbox" id="input" name="input" value="Recurring revenue">
										<label for="input"> Recurring revenue</label><br>
										<input type="checkbox" id="input" name="input" value="testing">
										<label for="input"> Testing</label><br>
										<input type="checkbox" id="input" name="input" value="variant">
										<label for="input"> Variant</label><br>
										<input type="checkbox" id="input" name="input" value="parentid">
										<label for="input"> Parent id</label><br>
									</div>
								</div>
				</c:when>
				<c:when test="${field == 0}">
								        <div style="display:inline-block;max-width:400px;margin-bottom: 10px;margin-top: 0px;margin-left: 200px;"id="parameter-choices">
					       <p style="margin-bottom: 0px;"> <label for="operator">Select category:</label></p>
					        <div style="display:inline-block;max-width:200px; max-length:20px; margin: 3px; margin-top: 0px; vertical-align: top;">
				        				<c:choose>
				        				<c:when test="${fundraiser == 1}">
				        					<input type="checkbox" id="input" name="category" value="Fundraiser" checked = "checked">
											<label for="input">Fundraiser</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Fundraiser">
											<label for="input">Fundraiser</label><br>
				        				</c:otherwise>
				        				</c:choose>
				        				<c:choose>
				        				<c:when test="${survey== 1}">
				        					<input type="checkbox" id="input" name="category" value="Survey" checked = "checked">
											<label for="input">Survey</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Survey">
											<label for="input">Survey</label><br>
				        				</c:otherwise>
				        				</c:choose>
				        				</div>
				        				<div style="display:inline-block;max-width:200px; max-length:10px; margin: 3px; margin-top: 0px; vertical-align: top;">
				        				<c:choose>
				        				<c:when test="${petition== 1}">
				        					<input type="checkbox" id="input" name="category" value="Petition" checked = "checked">
											<label for="input">Petition</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Petition">
											<label for="input">Petition</label><br>
				        				</c:otherwise>
				        				</c:choose>
				        				
				        				<c:choose>
				        				<c:when test="${other== 1}">
				        					<input type="checkbox" id="input" name="category" value="Other" checked = "checked">
											<label for="input">Other</label><br>
				        				</c:when>
				        				<c:otherwise>
				        					<input type="checkbox" id="input" name="category" value="Other">
											<label for="input">Other</label><br>
				        				</c:otherwise>
				        				</c:choose>
										</div>
			
				        </div>
					<div id="parameter-choices">
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
											<option value="All">All</option>
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
						<c:choose>
				        <c:when test="${operand != NULL}">
					        <label for="operand"></label>
									<textarea name="operand">${operand }</textarea>
						</c:when>
						<c:otherwise>
					        <label for="operand"></label>
									<textarea name="operand" placeholder="Operand"></textarea>
						</c:otherwise>
						</c:choose>
									<p style="font-size: 11px;">Place each operand in single quote marks. To include operands where multiple must be true, separate them with a plus sign. To include operands where any may be true separate them by /. To include both put the OR clause in parenthesis.</p>
									<p style="font-size: 11px;">To search emails that include "Biden" and either "approve" or "support" you'd write 'Biden' + ('approve'/'support')</p>
						</div>
				        </div>
				        <div id="export-choices-box" style="margin-bottom-20px;">
				        <p>
				        <label for="input">Select data fields:</label>
				        </p>
				        			<div id="export-choices">
										<input type="checkbox" id="input" name="input" value="Recipients">
										<label for="input"> Recipients</label><br>
										<input type="checkbox" id="input" name="input" value="Category">
										<label for="input"> Category</label><br>
										<input type="checkbox" id="input" name="input" value="link">
										<label for="input"> Link</label><br>
										<input type="checkbox" id="input" name="input" value="sender">
										<label for="input"> Sender</label><br>
										<input type="checkbox" id="input" name="input" value="subject">
										<label for="input"> Subject Line</label><br>
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
									</div>
									<div id="export-choices">
										<input type="checkbox" id="input" name="input" value="Clicks/opens">	
										<label for="input"> Clicks per open</label><br>	
										<input type="checkbox" id="input" name="input" value="Revenue">
										<label for="input"> Revenue</label><br>	
										<input type="checkbox" id="input" name="input" value="Donations">	
										<label for="input"> Donations</label><br>	
										<input type="checkbox" id="input" name="input" value="Donations/opens">	
										<label for="input"> Donations per open</label><br>
										<input type="checkbox" id="input" name="input" value="Donations/clicks">	
										<label for="input"> Donations per click</label><br>
										<input type="checkbox" id="input" name="input" value="Average donation">
										<label for="input"> Average donation</label><br>
										<input type="checkbox" id="input" name="input" value="Tandem revenue">
										<label for="input"> Tandem revenue</label><br>
										<input type="checkbox" id="input" name="input" value="Tandem donations">
										<label for="input"> Tandem donations</label><br>
										<input type="checkbox" id="input" name="input" value="Total revenue">
										<label for="input"> Total revenue</label><br>	
										<input type="checkbox" id="input" name="input" value="Recurring donations">
										<label for="input"> Recurring donations</label><br>
										<input type="checkbox" id="input" name="input" value="Recurring revenue">
										<label for="input"> Recurring revenue</label><br>
										<input type="checkbox" id="input" name="input" value="testing">
										<label for="input"> Testing</label><br>
										<input type="checkbox" id="input" name="input" value="fullsendvariant">
										<label for="input"> Full send variant</label><br>
										<input type="checkbox" id="input" name="input" value="varianta">
										<label for="input"> Variant A</label><br>
										<input type="checkbox" id="input" name="input" value="variantb">
										<label for="input"> Variant B</label><br>
										<input type="checkbox" id="input" name="input" value="parentid">
										<label for="input"> Parent id</label><br>
									</div>
								</div>
				</c:when>
				<c:when test="${ field == 6}">
				    <input type="hidden" name="operator" value="${operator}">
				    <input type="hidden" name="operand" value="${operand}">
				    <input type="hidden" name="type" value="${type}">
				 </c:when>
		</c:choose>
		<c:if test="${ field != 4 }">
			<button style="display:inline-block;margin-top:110px;">Download</button>					
		</c:if>
			
	</form>
							        </div>
</div>
</body>
</html>