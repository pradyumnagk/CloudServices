<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet" href="../assets/css/bootstrap.css"/>
<title>Insert title here</title>
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
					<li><a href="../assets/product.jsp">Add Product</a></li>
					<li class="span2"><a href="SignIn.html">Sign In</a></li>
                    <li><a href="#">My Cart</a></li>
                  </ul>
                </div>
              </div>
     </div>
     
     <form class="well span6" onsubmit="return false;">
		<label>Product Name</label><input type="text" id="productName" class="span3" placeholder="Product Name"/>
		<label>Product decription</label><input type="text" id="description" class="span3" placeholder="Description" />
		<label>Quantity</label><input type="text" id="quantity" class="span3" placeholder="quantity" />
		<label>Product Price</label><input type="text" id="price" class="span3" placeholder="price" />
		<label>Category Id </label><input type="text" id="categoryId" class="span3" placeholder="Category name" />
		<button class="btn btn-primary" id="addProduct" onclick="addproducttocatalog()">Add product</button>
		<button id="clear" class="btn"> Clear </button>
	</form>
	
</div>

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>

<script type="text/javascript">
function addproducttocatalog(){
	var URL = "http://localhost:8080/CloudServices/rest/catalog/addProductToCatalog";
	alert("add product button clicked");
	
	$.ajax({
		type: "POST",
		url: URL,
		contentType: "application/json",
		dataType: 'json',
		data : formToJSON(),
			//success: function () { //success(data); }
		success: function(data, textStatus, jqXHR){
				alert(data);
			},
		error: function(textStatus, jqXHR,errorThrown){
			alert(textStatus+" "+jqXHR);
		}

	});
}

function formToJSON() {
    return JSON.stringify({
    "prodName": $('#productName').val(),
    "prodDescr": $('#description').val(),
    "price": $('#price').val(),
    "quantity": $('#quantity').val(),
    "categoryId": $('#categoryId').val(),
    
    });
}
</script>
</body>
</html>