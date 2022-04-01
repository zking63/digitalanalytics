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
    
 <div class="online-body">
<form method="get" action="/emails" id="input-form">
				    <input type="hidden" name="page" value="${page}">
				    <input type="hidden" name="sort" value="${ sort}">
					<input type="hidden" name="direction" value="${ direction}">
				    
<div class="online-body-operators">
         	<c:if test="${ message != null }">
				<p style="margin-left:470px;width:500px;text-align:center;"><b><span style="color:#FF0000;"><c:out value="${ message }" /></span></b></p>					
	</c:if>
	<c:if test="${ operandsList != null }">
				<p style="margin-left:470px;width:500px;text-align:center;"><b>Not what you were looking for? Make sure your operand syntax was correct. Here are your operands: <c:out value="${ operandsList }" /></b></p>					
	</c:if>
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
											<option value="All">All emails</option>
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
				        <div id="online-parameter-choices" style="vertical-align: top;margin-top:0px;">
								    <table style="display:inline;vertical-align: top;width:200px;margin:0px;">
						
						<tbody>
				        <tr>
						<td>
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
			
</td>
</tr>
<tr>
<td style="width:50px;vertical-align: top;">
									<p style="font-size: 9px;margin-bottom:2px;">Place each operand in single quote marks. To include operands where multiple must be true, separate them with a plus sign. 
									To include operands where any may be true separate them by /. To include both put the OR clause in parenthesis.</p>
   									<p style="font-size: 9px;margin:0px;">To search emails that include "Biden" and either "approve" or 
									"support" you'd write 'Biden' + ('approve'/'support')</p>
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
	            <th>Date </br> 		
		            <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="date">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
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
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
				</th>
				<th>Email <br />
				<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="emailgroupName">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="emailgroupName">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
				</th>
				<th>Donations/opens </br>
				<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupdonationsOpens">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupdonationsOpens">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
				</th>
				<th>Donations/clicks </br>
				<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupdonationsClicks">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupdonationsClicks">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
				</th>
				<th>Donations </br> 
	                <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupdonationcount">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupdonationcount">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
	            </th>
	            <th>Revenue </br> 
	            	 <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupsum">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupsum">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
	            </th>
	            <th>Average donation </br> 
	            	<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupaverage">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupaverage">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
	            </th>
	            <th>Open rate <br />
	            <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupopenRate">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupopenRate">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
				</th>
	            <th>Clicks/opens <br />
	            <form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupclicksOpens">
						<input type="hidden" name="direction" value="asc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>^</button>
					</form>
					<form class="pointer" method="GET" action="/emails">
						<input type="hidden" name="sort" value="groupclicksOpens">
						<input type="hidden" name="direction" value="desc">
						<input type="hidden" name="type" value="${ type}">
						<input type="hidden" name="operator" value="${ operator}">
						<input type="hidden" name="operand" value="${ operand}">
						<input type="hidden" name="startdateE" value="${ startdateE}">
						<input type="hidden" name="enddateE" value="${ enddateE}">
						<input type="hidden" name="category" value="${ category}">
						<input type="hidden" name="fundraiser" value="${ fundraiser}">
						<input type="hidden" name="survey" value="${ survey}">
						<input type="hidden" name="petition" value="${ petition}">
						<input type="hidden" name="other" value="${ other}">
						<button>v</button>
					</form>
					</th>
	    </thead>
		<tbody>
			<c:forEach items="${ email }" var="e">
				<tr>
					<td>${e.getDateFormatted()}</td>
					<td><a href="/emails/${e.id}">${ e.emailgroupName }</a></td>
					<td>${e.getdonationsOpensFormatted()}</td>
					<td>${e.getdonationsClicksFormatted()}</td>
					<td>${e.groupdonationcount}</td>
					<td>$${e.getGroupSumFormatted()}</td>
					<td>$${e.getAverageFormatted()}</td>
					<td>${e.getOpenRateFormatted()}</td>
					<td>${e.getclicksOpensFormatted()}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>