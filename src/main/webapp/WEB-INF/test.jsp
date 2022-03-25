<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="jquery.datatables.model.*"%>
    <%@ page import="java.lang.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.google.gson.*"%>
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
	     $('#example').DataTable({ 
	    	 var email = { "emailName": emailName, "openers": openers };
	    	    $.ajax({
	    	        url: "/tabletest",   //  This call gets into the controller
	    	        type: "GET",
	    	        data: email,
	    	        dataType: "json",
	    	        success: function (result) {
	    	            var look = result;
	    	        }   
	    	    });
	     }); 
	 });
  </script>
</head>

  <body>
    <h1>Hello, world!</h1>
<table id="example" class="display dataTable" style="width: 100%;" aria-describedby="example_info">
						<thead>
							<tr>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Name: activate to sort column ascending" style="width: 136px;">Name</th>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Name: activate to sort column ascending" style="width: 136px;">openers</th>
							<th class="sorting" tabindex="0" aria-controls="example" rowspan="1" colspan="1" aria-label="Name: activate to sort column ascending" style="width: 136px;">list</th>
							</tr>
						</thead>
					</table>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
      <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <script type="text/javascript" src="js/jquery.js"></script>
      <script type="text/javascript" src="js/main.js"></script>
<script type="text/javascript">
  $(document).ready(function () {

	     $('#example').DataTable({ 
	         'destroy' : true,
	         'serverSide' : true,
	         'sAjaxSource': '/tabletest',
	         'contentType': "application/json; charset=utf-8",
	         'sAjaxDataProp': '',
	         'order': [ [ 0, 'asc' ] ],
	         'columns': 
	         [ 
	            {  'data': 'emailName',
	                'render': function(data, type, row, meta){ 
	                    if(type === 'display'){ 
	                        data = '<a href="javascript:openPersonDetail(&apos;'+ row.openers +'&apos;);">' + data + '</a>' 
	                    }  
	                    return data; 
	                } 
	            } ,
	            { 'data': 'emailName'} ,
	            { 'data': 'openers'} ,
	            { 'data': 'list'} 
	         ],
	         'scrollY' : 300,
	         'scrollCollapse' : true,
	         'paging' : true,
	         'autoWidth' : true,
	         'ordering' : true,
	         'searching' : false,
	         'pageLength' : 20,
	         'lengthChange' : false,
	         'pagingType' : 'full_numbers',
	         'dom' : '<"top"ip>rt<"bottom"fl><"clear">' 
	     }); 
	 });
  </script>
  </body>
</html>