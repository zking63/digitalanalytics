<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
    
<!DOCTYPE html>
${ html }.
<script type="text/javascript">

html2canvas(document.getElementsByClassName("capture")[0], {
    Scale: 2, // scale, default is 1
    Allowtaint: false, // allow cross domain images to contaminate the canvas
    Usecors: true, // do you want to use CORS to load images from the server
    Width: '500', // width of canvas
    Height: '500', // height of canvas
    BackgroundColor: '× 000000', // the background color of the canvas, which is transparent by default
}).then((canvas) => {
    //Convert canvas to Base64 format
    var imgUri = canvas.toDataURL("image/png");
});
</script>