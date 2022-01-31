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
        <style type="text/css">/* standard resets */

div, p, a, li, td {
  -webkit-text-size-adjust: none;
  -ms-text-size-adjust: none;
}
body {
  margin: 0;
  padding: 0;
}
table td {
  border-collapse: collapse;
}
table th {
  margin: 0 !important;
  padding: 0 !important;
  vertical-align: top;
  font-weight: normal;
}
div[style*="margin: 16px 0"] {
  margin: 0 !important;
}
.ExternalClass {
  width: 100%;
}
.ExternalClass * {
  line-height: 110%
}
img {
  -ms-interpolation-mode: bicubic;
}
.appleLinksgrey a {
  color: #9ca299 !important;
  text-decoration: none;
}

/* control text changed to links by phones */

@media only screen {
body {
  width: 100% !important;
  min-width: 100% !important;
}
a[x-apple-data-detectors] {
  color: inherit !important;
  text-decoration: none !important;
  font-size: inherit !important;
  font-family: inherit !important;
  font-weight: inherit !important;
  line-height: inherit !important;
}
}

/* Yahoo and apple mail reset on desktop */

@media screen and (min-width: 600px) {
.wrapto680px {
  width: 670px !important;
  height: auto !important;
}
}

@media screen and (max-device-width: 640px), screen and (max-width: 640px) {
/* responsive rules */
.wrapto100pc {
  width: 100% !important;
  height: auto !important;
}
.wrapto100pc-allowheight {
  width: 100% !important;
}
*[class=wrapto100pc-pt20] {
  width: 100% !important;
  height: auto !important;
  margin-top: 20px !important;
}
*[class=colsplit] {
  width: 100% !important;
  height: auto !important;
  display: block !important;
  float: left !important;
}
*[class=hero-image] {
  width: 100% !important;
  height: auto !important;
  padding-top: 20px !important;
  padding-bottom: 20px !important;
}
*[class=wrapto100pc-max480] {
  width: 100% !important;
  height: auto !important;
  max-width: 480px !important;
}
*[class=wrapto100pc-max320] {
  width: 100% !important;
  height: auto !important;
  max-width: 320px !important;
}
*[class=height-auto] {
  height: auto !important;
}
*[class=nomob] {
  display: none !important;
  width: 0px !important;
  height: 0px !important;
}
th[class=footer-button] {
  float: left !important;
  display: block !important;
  width: 100% !important;
  padding-top: 10px !important;
  padding-bottom: 10px !important;
}
/* fonts */
*[class=center-text] {
  text-align: center !important;
}
*[class=fontsize15] {
  font-size: 15px !important;
  line-height: 20px !important;
}
/* reverse stacking */
*[class=table] {
  display: table !important;
  width: 100% !important;
}
*[class=foot] {
  display: table-footer-group!important;
  width: 100% !important;
}
*[class=head] {
  display: table-header-group!important;
  width: 100% !important;
}
}
</style>
<table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%">
	
		<tr>
			<td align="center"><!--[if (gte mso 9)|(IE)]><table border="0" cellspacing="0" cellpadding="0" width="680" role="presentation"><tr><td align="center"><![endif]-->
			<table bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" class="wrapto680px-off" role="presentation" style="max-width:680px;" width="100%">
				
					<tr>
						<td width="20"><img alt="" border="0" height="1"  src="https://als-totem.s3.amazonaws.com/core/spacer.gif" style="display: block;" width="20" /></td>
						<td align="center" valign="top">
						<table cellpadding="0" cellspacing="0" id="email-contents" role="presentation" style="max-width: 100%; font-size: 21px; text-align: center; font-family: Arial,sans-serif; line-height: 1.2; " width="100%">
							
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="text-align: left;">
									<center><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX1%%" name="donate_actblue_com" ><img alt="&quot;Chip in to elect Democratic governors across the country.&quot; - Chair Roy Cooper" border="0"    src='https://contentz.mkt10784.com/ra/2022/41528/01/20127817/20211216%20Roy%20Cooper%20Chip%20In%20300x300.gif' style="max-width: 100% !important; border: 0; display: block;" title="&quot;Chip in to elect Democratic governors across the country.&quot; - Chair Roy Cooper"  /></a></center>
									</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;text-align:left;font-size:20px;text-align:left;">Hi, %%DC::FN LOWER::FIRSTNAME TEAM LOWER%%. I&rsquo;m Roy Cooper, governor of North Carolina, and I have a critical update on what&rsquo;s happening in the fight against voter suppression.</p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;text-align:left;font-size:20px;text-align:left;">Republicans have been executing a coordinated attack on voting rights ever since Joe Biden was elected in 2020. In 2021 alone, Republicans supported more than 440 bills that would make it harder to vote &ndash; especially for people of color. And just this month, voting reform legislation was blocked after every single Republican senator voted no.</p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0px; font-size: 20px;text-align:left;">But there is hope. This battle is now being waged in the states, where Democratic governors are working to expand access to the ballot box and protect voting rights. <strong>We must protect our Democratic firewall to save our democracy. <a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX2%%" name="donate_actblue_com_1" >Will you chip in $%%HPCX2%%, or whatever you can, before midnight tonight to defeat Republican voter suppression nationwide? All donations up to our $50,000 end-of-month goal will be matched.</a></strong></p>
									</td>
								</tr>
								<tr>
									<td>
									<center>
									<table border="0" cellpadding="0" cellspacing="0" role="presentation">
										
											<tr>
												<td style="text-align: center;" width="100%">
												<div class="wrapto100pc" style="display: inline-block;">
												<center>
												<table border="0" cellpadding="10" cellspacing="0" class="wrapto100pc" role="presentation" width="100%">
													
														<tr>
															<td>
															<center>
															<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																
																	<tr>
																		<td align="center" bgcolor="#eb3218" class="email-button" style="padding:8px;border-width:0px;border-radius:6px;border-color:#8c1919;color:#ffffff;font-size:20px;font-family:Arial,sans-serif;border-radius:6px; border-bottom: 4px solid #8c1919;" width="300"><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX1%%" name="secure_actblue_com_contri_2" style="line-height:39px; display: inline-block; width: 100%; height: 100%; text-decoration: none; font-weight: bold;color:#ffffff;" ><span style="font-size:22px;">DONATE $%%HPCX1%% (and have it DOUBLED)</span></a></td>
																	</tr>
																
															</table>
															</center>
															</td>
														</tr>
													
												</table>
												</center>
												</div>
												</td>
											</tr>
										
									</table>
									</center>
									</td>
								</tr>
								<tr>
									<td bgcolor="#FFFFFF" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;font-size:20px;text-align:left;">While Congress fails to act or the Supreme Court leaves unjust laws on the books, Democratic governors are the last line of defense to protect our democracy and our best chance for progress.</p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#FFFFFF" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;font-size:20px;text-align:left;">In states like Michigan, Nevada, and Wisconsin, Democratic governors have taken action, issued vetoes, and stopped reckless voter suppression laws. Here in North Carolina, my vetoes have stopped Republican schemes to impose bad voter laws, and our state courts struck down a GOP voter ID law citing &ldquo;persuasive evidence&rdquo; that it made it harder for black voters to cast ballots.</p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#FFFFFF" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;font-size:20px;text-align:left;"><b>I&rsquo;m proud of our efforts to stop this shameful attack on our democracy, but the fight is far from over. And I need backup. </b></p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#FFFFFF" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;font-size:20px;text-align:left;"><b>So please, %%DC::FN LOWER::FIRSTNAME TEAM LOWER%%, act now. <a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX2%%" name="secure_actblue_com_contri_1" >Will you invest $%%HPCX2%% before tomorrow&rsquo;s midnight deadline &ndash; while all donations are being DOUBLED up to our goal &ndash; to to protect our progress, elect Democratic governors, and defeat extremist Republicans nationwide? </a></b></p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="">
									<center>
									<p style="font-size: 16px; margin: 1em auto;"><em edit="block.disclaimer">If you&#39;ve saved payment information with ActBlue Express, your donation will go through immediately:</em></p>
									</center>
									</td>
								</tr>
								<tr>
									<td bgcolor="#FFFFFF" style="">
									<center>
									<table border="0" cellpadding="0" cellspacing="0" role="presentation">
										
											<tr>
												<td style="text-align: center;" width="100%">
												<div class="wrapto100pc" style="display: inline-block;">
												<center>
												<table border="0" cellpadding="7" cellspacing="0" class="wrapto100pc" role="presentation" width="100%">
													
														<tr>
															<td>
															<center>
															<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																
																	<tr>
																		<td align="center" bgcolor="#236092" class="email-button" style="padding:8px;border-width:0px;border-radius:6px;border-color:#2d3038;color:#ffffff;font-size:20px;font-family:Arial,sans-serif;border-radius:6px; border-bottom: 4px solid #2d3038;" width="350"><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX1%%&express_lane=true" name="secure_actblue_com_contribute_page__2" style="line-height:49px; display: inline-block; width: 100%; height: 100%; text-decoration: none; font-weight: bold;color:#ffffff;" ><span style="font-size:20px;">Donate $%%HPCX1%% (becomes $%%HPCX2MATCH%%)</span></a></td>
																	</tr>
																
															</table>
															</center>
															</td>
														</tr>
													
												</table>
												</center>
												</div>
												</td>
											</tr>
											<tr>
												<td style="text-align: center;" width="100%">
												<div class="wrapto100pc" style="display: inline-block;">
												<center>
												<table border="0" cellpadding="7" cellspacing="0" class="wrapto100pc" role="presentation" width="100%">
													
														<tr>
															<td>
															<center>
															<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																
																	<tr>
																		<td align="center" bgcolor="#eb3218" class="email-button" style="padding:8px;border-width:0px;border-radius:6px;border-color:#8c1919;color:#ffffff;font-size:20px;font-family:Arial,sans-serif;border-radius:6px; border-bottom: 4px solid #8c1919;" width="350"><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX2%%&express_lane=true" name="secure_actblue_com_contri_3" style="line-height:49px; display: inline-block; width: 100%; height: 100%; text-decoration: none; font-weight: bold;color:#ffffff;" ><span style="font-size:20px;">Donate $%%HPCX2%% (becomes $%%HPCX4MATCH%%)</span></a></td>
																	</tr>
																
															</table>
															</center>
															</td>
														</tr>
													
												</table>
												</center>
												</div>
												</td>
											</tr>
											<tr>
												<td style="text-align: center;" width="100%">
												<div class="wrapto100pc" style="display: inline-block;">
												<center>
												<table border="0" cellpadding="7" cellspacing="0" class="wrapto100pc" role="presentation" width="100%">
													
														<tr>
															<td>
															<center>
															<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																
																	<tr>
																		<td align="center" bgcolor="#236092" class="email-button" style="padding:8px;border-width:0px;border-radius:6px;border-color:#2d3038;color:#ffffff;font-size:20px;font-family:Arial,sans-serif;border-radius:6px; border-bottom: 4px solid #2d3038;" width="350"><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX4%%&express_lane=true" name="secure_actblue_com_contribute_page__4" style="line-height:49px; display: inline-block; width: 100%; height: 100%; text-decoration: none; font-weight: bold;color:#ffffff;" ><span style="font-size:20px;">Donate $%%HPCX4%% (becomes $%%HPCX8MATCH%%)</span></a></td>
																	</tr>
																
															</table>
															</center>
															</td>
														</tr>
													
												</table>
												</center>
												</div>
												</td>
											</tr>
											<tr>
												<td style="text-align: center;" width="100%">
												<div class="wrapto100pc" style="display: inline-block;">
												<center>
												<table border="0" cellpadding="7" cellspacing="0" class="wrapto100pc" role="presentation" width="100%">
													
														<tr>
															<td>
															<center>
															<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																
																	<tr>
																		<td align="center" bgcolor="#236092" class="email-button" style="padding:8px;border-width:0px;border-radius:6px;border-color:#2d3038;color:#ffffff;font-size:20px;font-family:Arial,sans-serif;border-radius:6px; border-bottom: 4px solid #2d3038;" width="350"><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807" name="secure_actblue_com_contribute_page__5" style="line-height:49px; display: inline-block; width: 100%; height: 100%; text-decoration: none; font-weight: bold;color:#ffffff;" ><span style="font-size:20px;">Or donate other amount</span></a></td>
																	</tr>
																
															</table>
															</center>
															</td>
														</tr>
													
												</table>
												</center>
												</div>
												</td>
											</tr>
										
									</table>
									</center>
									</td>
								</tr>
								<tr>
									<td bgcolor="#FFFFFF" style="padding-top:10px;padding-right:5px;padding-bottom:10px;padding-left:5px;">
									<p style="margin: 0;font-size:20px;text-align:left;">Thank you for everything you do,<br />
									<br />
									Roy Cooper<br />
									Governor of North Carolina<br />
									Chair, Democratic Governors Association</p>
									</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="padding-top:20px;padding-right:5%;padding-bottom:20px;padding-left:5%;">
									<table background="" border="0" cellpadding="0" cellspacing="0" role="presentation" style="border-radius: 0px; min-height:50px;border-style:solid;border-width:5px;border-color:#000000;padding:5px;font-family: Arial,sans-serif; line-height: 1.3;" width="100%">
										
											<tr>
												<td>
												<div class="block-parent box" collapsed="false" expand-on-hover="false">
												<table border="0" cellpadding="0" cellspacing="0" role="presentation" style="max-width: 100%; font-size: 21px; text-align: center; font-family: Arial,sans-serif; line-height: 1.2;" width="100%">
													
														<tr>
															<td bgcolor="" style="padding-top:10px;padding-right:3%;padding-bottom:10px;padding-left:3%;">
															<p style="margin: 0;text-align:left;font-size:20px;font-size:20px;text-align:left;">Roy Cooper was reelected to a second term as governor because he&rsquo;s building a North Carolina where people are better educated, healthier, and have more money in their pockets. He&rsquo;s the kind of commonsense leader we can count on to protect democracy from Republican attacks. This year, he&rsquo;s leading the DGA to reelect Democratic governors and flip red states blue all over the country.</p>
															</td>
														</tr>
														<tr>
															<td bgcolor="" style="padding-top:10px;padding-right:3%;padding-bottom:10px;padding-left:3%;">
															<p style="margin: 0;text-align:left;font-size:20px;font-size:20px;text-align:left;">Will you answer his call to action?</p>
															</td>
														</tr>
														<tr>
															<td>
															<center>
															<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																
																	<tr>
																		<td style="text-align: center;" width="100%">
																		<div class="wrapto100pc" style="display: inline-block;">
																		<center>
																		<table border="0" cellpadding="10" cellspacing="0" class="wrapto100pc" role="presentation" width="100%">
																			
																				<tr>
																					<td>
																					<center>
																					<table border="0" cellpadding="0" cellspacing="0" role="presentation">
																						
																							<tr>
																								<td align="center" bgcolor="#eb3218" class="email-button" style="padding:8px;border-width:0px;border-radius:6px;border-color:#8c1919;color:#ffffff;font-size:20px;font-family:Arial,sans-serif;border-radius:6px; border-bottom: 4px solid #8c1919;" width="200"><a href="https://secure.actblue.com/contribute/page/%%ENTITYDESIGNATION%%-digital-2022q1?refcode2=jtk8807&amount=%%HPCX1%%" name="secure_actblue_com_contri_2" style="line-height:39px; display: inline-block; width: 100%; height: 100%; text-decoration: none; font-weight: bold;color:#ffffff;" ><span style="font-size:22px;">DONATE </span></a></td>
																							</tr>
																						
																					</table>
																					</center>
																					</td>
																				</tr>
																			
																		</table>
																		</center>
																		</div>
																		</td>
																	</tr>
																
															</table>
															</center>
															</td>
														</tr>
													
												</table>
												</div>
												</td>
											</tr>
										
									</table>
									</td>
								</tr>
							
						</table>
						</td>
						<td width="20"><img alt="" border="0" height="1"  src="https://als-totem.s3.amazonaws.com/core/spacer.gif" style="display: block;" width="20" /></td>
					</tr>
					<tr>
						<td width="20"><img alt="" border="0" height="1"  src="https://als-totem.s3.amazonaws.com/core/spacer.gif" style="display: block;" width="20" /></td>
						<td>
						<div id="footer" style="margin: 4em auto 2em auto; font-size: 12px; font-family: sans-serif;">
						<center>
						<p style="margin: 1em auto;"><br />
						<br />
						<br />
						<br />
						<br />
						<br />
						<br />
						<br />
						<br />
						<br />
						<br />
						This email was sent to %%EMAIL%%.<br />
						To unsubscribe from the DGA email list, <a href="https://action.democraticgovernors.org/unsubscribe?m=%%MAILING_ID%%&r=%%RECIPIENT_ID%%&j=%%JOB_ID_CODE%%&l=%%LIST_ID%%&e=%%EMAIL%%" id="unsubscribe" name="optout" target="_blank" >click here</a>.</p>

						<p style="margin: 1em auto;text-align: center;">&nbsp;</p>

						<table border="0" cellpadding="0" cellspacing="0" style="max-width: 380px;" width="100%">
							
								<tr>
									<td style="padding: 10px">
									<table cellpadding="8" cellspacing="0" style="border: 1px solid black; text-align: center; max-width: 360px; font-family: Arial,sans-serif; font-size: 12px; line-height: 1em;" width="100%">
										
											<tr>
												<td id="disclaimer">%%DC::ENTITYDESIGNATION::DISCLAIMER%%</td>
											</tr>
										
									</table>
									</td>
								</tr>
							
						</table>

						<p id="post-disclaimer" style="margin: 1em auto;text-align: center;">Contributions or gifts to %%DC::ENTITYDESIGNATION::FOOTER%% are not tax deductible.</p>

						<p style="margin: 1em auto;text-align: center;"><span id="email-specific-post-disclaimer"></span></p>
						</center>
						</div>
						</td>
						<td width="20"><img alt="" border="0" height="1"  src="https://als-totem.s3.amazonaws.com/core/spacer.gif" style="display: block;" width="20" /></td>
					</tr>
				
			</table>
			<!--[if (gte mso 9)|(IE)]></td></tr></table><![endif]--></td>
		</tr>
	
</table>
<grammarly-desktop-integration data-grammarly-shadow-root="true"></grammarly-desktop-integration>
</body>
</html>