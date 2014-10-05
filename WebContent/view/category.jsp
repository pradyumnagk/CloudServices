<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.sjsu.cmpe282.domain.Product" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet" href="http://localhost:8080/CloudServices/assets/css/bootstrap.css"/>
</head>

<body>

<div class="container">
	<h1><a href="#">CMPE 282 Cloud Services</a></h1>
	
	<div class="navbar">
              <div class="navbar-inner">
                <div class="container">
                  <ul class="nav">
                    <li class="active"><a href="../index.html">CMPE282.com</a></li>
					<li class="active span6"></li>
					<li><a href="/view/product.jsp">Add Product</a></li>
					<li class="span2"><a href="SignIn.html">Sign In</a></li>
                    <li><a href="/view/cart.jsp">My Cart</a></li>
                  </ul>
                </div>
              </div>
     </div>
     <div class="row">
			<div class="span4">
				<ul class="nav nav-list">
					<li class="nav-header">Categories</li>
					<%ArrayList<String> categories = (ArrayList<String>)request.getAttribute("catalogs"); 
						for(String category : categories){
					%>
					<li><a href="http://localhost:8080/CloudServices/rest/catalog/<%=category%>" onclick="getProducts()"><%=category%></a></li>
					<%} %>
				</ul>
			</div>
	</div>
	
	<%if("showProducts".equals(request.getAttribute("mode"))){
		ArrayList<Product> products = (ArrayList<Product>)request.getAttribute("products");%>
	<form>
	<table>
		<tr>
			<th></th><th>PRODUCT NAME</th><th>PRODUCT DESCRIPTION</th><th>PRICE</th><th>QUANTITY</th><th>YOUR QNTY</th>
		</tr>
	<% for(Product product : products) {%>
		<TR>
			<td><input type="radio" name="product" value="<%=product.getProductId()%>"></input></td>
			<td><%=product.getProdName() %></td><td><%=product.getProdDescr() %></td><td><%=product.getPrice() %></td>
			<td><%=product.getQuantity()%></td>
			<td><input type="text" id="<%=product.getProductId()%>" ></input></td>
		</TR>
		
	<%} %>
		<tr>
			<td><input type="button" id="addtocart" value="Add to Cart" onclick="addProdToCart()"></input></td>
		</tr>
	</table>
	</form>
	<%} %>
</div>
<script type="text/javascript">
function addProdToCart(){
	var selectedProductid = $('input[name=product]:checked').val();
	var qtyId = "#"+selectedProductid;
	var qty= $(qtyId).val();
	alert(selectedProductid+""+qty);
	var URL = "http://localhost:8080/CloudServices/rest/catalog/addtocart";
	$.ajax({
		type: "POST",
		url: URL,
		contentType: "application/json",
		data : JSON.stringify({userId:"prad@gmail.com",productId:selectedProductid,userQty:qty}),
		success: function(data, textStatus, jqXHR){
				alert("you are logged in");
			},
		error: function(textStatus, jqXHR,errorThrown){
			alert(textStatus+" "+jqXHR);
		}

	});
}
</script>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript" src="http://localhost:8080/CloudServices/assets/js/jquery.cookie.js"></script>
</body>
</html>
