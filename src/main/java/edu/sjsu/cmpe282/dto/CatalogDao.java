package edu.sjsu.cmpe282.dto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import edu.sjsu.cmpe282.cache.CustomCache;
import edu.sjsu.cmpe282.domain.Product;



public class CatalogDao {
	private final String PRODUCT_TABLE = "ProductCatalog";
	private final String CATEGORY_TABLE = "Category";
	private final String CART_TABLE = "Cart";
	private final String id = "id";
	private final String PRODUCT_DESC = "productDesc";
	private final String PRODUCT_ID = "productId";
	private final String PRODUCT_NAME = "productName";
	private final String PRODUCT_PRICE = "pricePerUnit";
	private final String PRODUCT_QUANTITY = "prodQuantity";
	private final String CATEGORY_ID = "categoryId";
	private final String USER_ID = "userId";
	
	CustomCache myCache = new CustomCache();
	private AmazonDynamoDBClient client = null;

	public CatalogDao() {
		try{
			AWSCredentials credentials = new PropertiesCredentials(DynamoDBClient.class
					.getResourceAsStream("AwsCredentials.properties"));
			client = new AmazonDynamoDBClient(credentials);
		}catch(IOException ioe){
				ioe.printStackTrace();
		}
	}
	
	public String addNewProductToCatalog(Product product){
		String latestId = getLatestID();
		try {
			// Add books.
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

			item.put(PRODUCT_ID, new AttributeValue().withN(latestId));
			item.put(PRODUCT_NAME, new AttributeValue().withS(product.getProdName()));
			item.put(PRODUCT_DESC, new AttributeValue().withS(product.getProdDescr()));
			item.put(PRODUCT_QUANTITY, new AttributeValue().withN(product.getQuantity()+""));
			item.put(PRODUCT_PRICE, new AttributeValue().withS(product.getPrice()));
			item.put(CATEGORY_ID, new AttributeValue().withS(product.getCategoryId()));

			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			PutItemRequest putReq = new PutItemRequest()
			.withTableName(PRODUCT_TABLE)
			.withItem(item);
			client.putItem(putReq);
		}catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + PRODUCT_TABLE);
			ase.printStackTrace();
			return "Failed to create item in " + PRODUCT_TABLE;
		}

		updatePropertiesID(Integer.parseInt(latestId));
		return "success";
	}
	
	public void addToCart(Product product,String userId) {
		try {
			// Add books.
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			
			item.put(USER_ID, new AttributeValue().withS(userId));
			item.put(PRODUCT_ID, new AttributeValue().withN(product.getProductId()+""));
			item.put(PRODUCT_NAME, new AttributeValue().withS(product.getProdName()));
			item.put(PRODUCT_DESC, new AttributeValue().withS(product.getProdDescr()));
			item.put(PRODUCT_QUANTITY, new AttributeValue().withN(product.getQuantity()+""));
			item.put(PRODUCT_PRICE, new AttributeValue().withS(product.getPrice()));
			item.put(CATEGORY_ID, new AttributeValue().withS(product.getCategoryId()));

			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			PutItemRequest putReq = new PutItemRequest()
			.withTableName(CART_TABLE)
			.withItem(item);
			client.putItem(putReq);
		}catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + CART_TABLE);
			ase.printStackTrace();
		}
	}

	public ArrayList<String> getAllCatalogs() {
		if(!myCache.getDataCache().isEmpty()){
			return myCache.getDataCache().get(CATEGORY_TABLE);
		}
		ArrayList<String> categories = new ArrayList<String>();
		ScanRequest scanRequest = new ScanRequest().withTableName(CATEGORY_TABLE);
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		ScanResult result = client.scan(scanRequest);
		
		for(Map<String, AttributeValue> item : result.getItems()){
			categories.add(item.get(CATEGORY_ID).getS());
		}
		myCache.setDataCache(CATEGORY_TABLE,categories);
		return categories;
	}

	public ArrayList<Product> getProductsOfCategory(String categoryId) {
		ArrayList<Product> products = new ArrayList<Product>();
		ScanRequest scanRequest = new ScanRequest().withTableName(PRODUCT_TABLE);
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		ScanResult result = client.scan(scanRequest);

		for(Map<String, AttributeValue> item : result.getItems()){
			AttributeValue attributeValue = item.get(CATEGORY_ID);
			if(categoryId.equals(attributeValue.getS())){
				Product product = new Product();
				product.setProductId(Integer.parseInt(item.get(PRODUCT_ID).getN()));
				product.setProdName(item.get(PRODUCT_NAME).getS());
				product.setProdDescr(item.get(PRODUCT_DESC).getS());
				product.setQuantity(Integer.parseInt(item.get(PRODUCT_QUANTITY).getN()));
				product.setPrice(item.get(PRODUCT_PRICE).getS());
				product.setCategoryId(item.get(CATEGORY_ID).getS());
				products.add(product);
			}
		}
		return products;
	}
	
	public ArrayList<Product> getAllCartDataForUser(String userId){
		ArrayList<Product> products = new ArrayList<Product>();
		ScanRequest scanRequest = new ScanRequest().withTableName(CART_TABLE);
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		ScanResult result = client.scan(scanRequest);
		
		for(Map<String, AttributeValue> item : result.getItems()){
			AttributeValue attributeValue = item.get(USER_ID);
			if(userId.equals(attributeValue.getS())){
				Product product = new Product();
				product.setProductId(Integer.parseInt(item.get(PRODUCT_ID).getN()));
				product.setProdName(item.get(PRODUCT_NAME).getS());
				product.setProdDescr(item.get(PRODUCT_DESC).getS());
				product.setQuantity(Integer.parseInt(item.get(PRODUCT_QUANTITY).getN()));
				product.setPrice(item.get(PRODUCT_PRICE).getS());
				product.setCategoryId(item.get(CATEGORY_ID).getS());
				products.add(product);
			}
		}
		
		return products;
	}
	
	public Product getProductForId(String productId) {
		Product product = new Product();
		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put(PRODUCT_ID, new AttributeValue().withN(productId));

		GetItemRequest getItemRequest = new GetItemRequest()
		    .withTableName(PRODUCT_TABLE)
		    .withKey(key);
		
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		
		GetItemResult result = client.getItem(getItemRequest);
		Map<String, AttributeValue> item = result.getItem();
		product.setProductId(Integer.parseInt(item.get(PRODUCT_ID).getN()));
		product.setProdName(item.get(PRODUCT_NAME).getS());
		product.setProdDescr(item.get(PRODUCT_DESC).getS());
		product.setPrice(item.get(PRODUCT_PRICE).getS());
		product.setCategoryId(item.get(CATEGORY_ID).getS());
		return product;
	}
	
	public void deleteProductfromCart(String userId, String productId){
		try {
			Map<String, ExpectedAttributeValue> expectedValues = new HashMap<String, ExpectedAttributeValue>();
			HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put(USER_ID, new AttributeValue().withS(userId));
			key.put(PRODUCT_ID, new AttributeValue().withN(productId));
			
			expectedValues.put(PRODUCT_ID,
					new ExpectedAttributeValue()
			.withComparisonOperator("EQ")
			.withAttributeValueList(new AttributeValue().withN(productId))); // Boolean stored as 0 or 1.

			ReturnValue returnValues = ReturnValue.ALL_OLD;

			DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
			.withTableName(CART_TABLE)
			.withKey(key)
			.withExpected(expectedValues)
			.withReturnValues(returnValues);
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			DeleteItemResult result = client.deleteItem(deleteItemRequest);

			// Check the response.
			System.out.println("Printing item that was deleted...");
		}  catch (AmazonServiceException ase) {
			System.err.println("Failed to get item after deletion " + CART_TABLE);
			ase.printStackTrace();
		} 
	}
	
	private void updatePropertiesID(int latestId) {
		Properties prop = new Properties();
		FileOutputStream output = null;
		try {
			FileInputStream in = new FileInputStream("Ids.properties");
			prop.load(in);
			in.close();
			
			output = new FileOutputStream("Ids.properties");
			latestId+=1;
			prop.setProperty("id", latestId+"");
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.flush();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
	}
	
	private String getLatestID(){
		Properties props = new Properties();
		InputStream is = null;

		try {
			if ( is == null ) {
				is = getClass().getResourceAsStream("Ids.properties");
			}

			// Try loading properties from the file (if found)
			props.load( is );
		}
		catch ( Exception e ) { }
		return props.getProperty(id);
	}

}
