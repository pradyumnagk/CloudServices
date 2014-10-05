package edu.sjsu.cmpe282.api.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.view.Viewable;

import edu.sjsu.cmpe282.domain.Product;
import edu.sjsu.cmpe282.dto.CatalogDao;

@Path("/catalog")
public class CatalogResource {
	private CatalogDao catalogDao = new CatalogDao();
	JSONParser parser=new JSONParser();
	
	@GET
	@Path("/categories")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCatalogData(@Context HttpServletRequest request) throws ClassNotFoundException {
		request.setAttribute("catalogs", catalogDao.getAllCatalogs());
		return Response.ok(new Viewable("/view/category.jsp", null)).build();//catalogDao.getAllProductsOfCatalog(catalogName);
	}
	
	@POST
	@Path("/addProductToCatalog")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addProductToCatalog(Product product){
		return catalogDao.addNewProductToCatalog(product);
	}
	
	@GET
	@Path("{categoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductsOfaCategory(@PathParam("categoryId") String categoryId,@Context HttpServletRequest request) throws ClassNotFoundException {
		request.setAttribute("catalogs", catalogDao.getAllCatalogs());
		request.setAttribute("products", catalogDao.getProductsOfCategory(categoryId));
		request.setAttribute("mode", "showProducts");
		return Response.ok(new Viewable("/view/category.jsp", null)).build();//catalogDao.getAllProductsOfCatalog(catalogName);
	}
	
	@POST
	@Path("/addtocart/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addProductToCart(String data) throws ParseException{
		JSONObject obj = (JSONObject)parser.parse(data);
		
		String productId = obj.get("productId").toString();
		String userQty = obj.get("userQty").toString();
		String userId = obj.get("userId").toString();
		
		Product product = catalogDao.getProductForId(productId);
		product.setQuantity(Integer.parseInt(userQty));
		catalogDao.addToCart(product, userId);		
	}
	
	@POST
	@Path("/deleteItem/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteItemFromCart(String data) throws ParseException{
		JSONObject obj = (JSONObject)parser.parse(data);
		
		String productId = obj.get("productId").toString();
		String userId = obj.get("userId").toString();
		
		catalogDao.deleteProductfromCart(userId, productId);	
	}
	
	@GET
	@Path("/showcart/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showCart(@PathParam("userId") String userId,@Context HttpServletRequest request) throws ParseException{
		request.setAttribute("cartItems", catalogDao.getAllCartDataForUser(userId));
		return Response.ok(new Viewable("/view/cart.jsp", null)).build();
	}
}
