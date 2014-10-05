function showCart(){
	alert("going to cart page");
	userId = "prad@gmail.com";
	window.location.href="http://localhost:8080/CloudServices/rest/catalog/showcart/"+userId;
}

function deleteItem(){
	var selectedProductid = $('input[name=product]:checked').val();
	alert(selectedProductid);
	var URL = "http://localhost:8080/CloudServices/rest/catalog/deleteItem";
	$.ajax({
		type: "POST",
		url: URL,
		contentType: "application/json",
		data : JSON.stringify({userId:"prad@gmail.com",productId:selectedProductid}),
		success: function(data, textStatus, jqXHR){
				alert("you are logged in");
			},
		error: function(textStatus, jqXHR,errorThrown){
			alert(textStatus+" "+jqXHR);
		}

	});
}