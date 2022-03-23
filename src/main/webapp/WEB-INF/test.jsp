<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="jquery.datatables.model.*"%>
<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
	<title>DataTables example - DOM positioning</title>
	<link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="http://www.datatables.net/rss.xml">
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/main.js"></script>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js">
	<script type="text/javascript" async="" src="https://ssl.google-analytics.com/ga.js"></script>
	<script type="text/javascript" language="javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
	<script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript">
$(document).ready(function () {
	$.noConflict();
	$('#example').DataTable();
} );
	</script>
</head>

  <body>
    <h1>Hello, world!</h1>
<table id="example" class="display dataTable" style="width: 100%;" aria-describedby="example_info">
						<thead>
							<tr>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Name: activate to sort column ascending" style="width: 136px;">Name</th>
							<th class="sorting sorting_asc" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Position: activate to sort column descending" style="width: 89px;">Position</th>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Office: activate to sort column ascending" style="width: 68px;">Office</th>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Age: activate to sort column ascending" style="width: 27px;">Age</th>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Start date: activate to sort column ascending" style="width: 65px;">Start date</th>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Salary: activate to sort column ascending" style="width: 57px;">Salary</th>
							</tr>
						</thead>
						<tbody>
						<tr class="odd">
								<td class="sorting_asc">Airi Satou</td>
								<td>Accountant</td>
								<td>Tokyo</td>
								<td>33</td>
								<td>2008/11/28</td>
								<td>$162,700</td>
							</tr><tr class="even">
								<td class="sorting_1">Angelica Ramos</td>
								<td>Chief Executive Officer (CEO)</td>
								<td>London</td>
								<td>47</td>
								<td>2009/10/09</td>
								<td>$1,200,000</td>
							</tr><tr class="odd">
								<td class="sorting_1">Ashton Cox</td>
								<td>Junior Technical Author</td>
								<td>San Francisco</td>
								<td>66</td>
								<td>2009/01/12</td>
								<td>$86,000</td>
							</tr><tr class="even">
								<td class="sorting_1">Bradley Greer</td>
								<td>Software Engineer</td>
								<td>London</td>
								<td>41</td>
								<td>2012/10/13</td>
								<td>$132,000</td>
							</tr><tr class="odd">
								<td class="sorting_1">Brenden Wagner</td>
								<td>Software Engineer</td>
								<td>San Francisco</td>
								<td>28</td>
								<td>2011/06/07</td>
								<td>$206,850</td>
							</tr><tr class="even">
								<td class="sorting_1">Brielle Williamson</td>
								<td>Integration Specialist</td>
								<td>New York</td>
								<td>61</td>
								<td>2012/12/02</td>
								<td>$372,000</td>
							</tr><tr class="odd">
								<td class="sorting_1">Bruno Nash</td>
								<td>Software Engineer</td>
								<td>London</td>
								<td>38</td>
								<td>2011/05/03</td>
								<td>$163,500</td>
							</tr><tr class="even">
								<td class="sorting_1">Caesar Vance</td>
								<td>Pre-Sales Support</td>
								<td>New York</td>
								<td>21</td>
								<td>2011/12/12</td>
								<td>$106,450</td>
							</tr><tr class="odd">
								<td class="sorting_1">Cara Stevens</td>
								<td>Sales Assistant</td>
								<td>New York</td>
								<td>46</td>
								<td>2011/12/06</td>
								<td>$145,600</td>
							</tr><tr class="even">
								<td class="sorting_1">Cedric Kelly</td>
								<td>Senior Javascript Developer</td>
								<td>Edinburgh</td>
								<td>22</td>
								<td>2012/03/29</td>
								<td>$433,060</td>
							</tr></tbody>
						<tfoot>
							<tr><th rowspan="1" colspan="1">Name</th><th rowspan="1" colspan="1">Position</th><th rowspan="1" colspan="1">Office</th><th rowspan="1" colspan="1">Age</th><th rowspan="1" colspan="1">Start date</th><th rowspan="1" colspan="1">Salary</th></tr>
						</tfoot>
					</table>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <script type="text/javascript" src="js/jquery.js"></script>
      <script type="text/javascript" src="js/main.js"></script>
  </body>
</html>