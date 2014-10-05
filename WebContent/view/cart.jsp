<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.sjsu.cmpe282.domain.Product" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<link type="text/css" rel="stylesheet" href="/CloudServices/assets/css/bootstrap.css"/>
</head>

<body>
<div class="navbar">
              <div class="navbar-inner">
                <div class="container">
                  <ul class="nav">
                    <li class="active"><a href="../index.html">CMPE282.com</a></li>
					<li class="active span6"></li>
					<li><a href="/view/product.jsp">Add Product</a></li>
					<li class="span2"><a href="SignIn.html">Sign In</a></li>
                    <li><a href="#" onclick="showCart()">My Cart</a></li>
                  </ul>
                </div>
              </div>
</div>

<h2>YOUR CART </h2>
<% if(request.getAttribute("cartItems")!=null){
ArrayList<Product> cartItems = (ArrayList<Product>)request.getAttribute("cartItems"); %>
<form>
<table class="table table-striped table-bordered table-condensed">
	<tr><th></th><th>PRODUCT NAME</th><th>PRODUCT DESCRIPTION</th><th>PRICE</th><th>YOUR QNTY</th></tr>
	<%for(Product product : cartItems){ %>
		<tr>
			<td><input type="radio" name="product" value="<%=product.getProductId()%>"></input></td>
			<td><%=product.getProdName() %></td><td><%=product.getProdDescr() %></td><td><%=product.getPrice() %></td>
			<td><%=product.getQuantity()%></td>
		</tr>
	<%} %>
	<tr>
			<td><input type="button" id="addtocart" value="Delete Item" onclick="deleteItem()"></input></td>
	</tr>
</table>
</form>
<%}else{ %>
	<p>The cart is empty</p>
<%} %>

<script src="/CloudServices/assets/js/bootstrap.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script  src="/CloudServices/assets/js/cart.js"></script>
</body>
</html>