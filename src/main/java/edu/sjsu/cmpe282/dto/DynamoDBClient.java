package edu.sjsu.cmpe282.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

public class DynamoDBClient {
		private String productCatalogTable = "ProductCatalog";
		private String categoryTable = "Category";
		private String cart_table = "Cart";
		private AmazonDynamoDBClient client = null;
		
		public DynamoDBClient() throws IOException{
			AWSCredentials credentials = new PropertiesCredentials(DynamoDBClient.class
					.getResourceAsStream("AwsCredentials.properties"));
			client = new AmazonDynamoDBClient(credentials);
		}
		
		public static void main(String[] args){
			try{
			DynamoDBClient dbClient = new DynamoDBClient();
			dbClient.createProductTable();
			dbClient.createCategoriesTable();
			dbClient.createCartTable();
//			dbClient.addCategories();
//			dbClient.addProductsToCatalog();
//			dbClient.updateProductCatalog();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		private void createCartTable() {
			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("userId").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("productId").withAttributeType("N"));

			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ks.add(new KeySchemaElement().withAttributeName("userId").withKeyType(KeyType.HASH));
			ks.add(new KeySchemaElement().withAttributeName("productId").withKeyType(KeyType.RANGE));
			
			ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
			.withReadCapacityUnits(10L).withWriteCapacityUnits(10L);
			
			CreateTableRequest req = new CreateTableRequest()
			.withTableName(cart_table)
			.withAttributeDefinitions(attributeDefinitions)
			.withKeySchema(ks)
			.withProvisionedThroughput(provisionedThroughput);
			
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			CreateTableResult result = client.createTable(req);
			System.out.println("created table"+ result.getTableDescription().getTableName());
		}

		public void createCategoriesTable(){
			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("categoryId").withAttributeType("S"));

			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ks.add(new KeySchemaElement().withAttributeName("categoryId").withKeyType(KeyType.HASH));

			ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
			.withReadCapacityUnits(10L).withWriteCapacityUnits(10L);
			
			CreateTableRequest req = new CreateTableRequest()
			.withTableName(categoryTable)
			.withAttributeDefinitions(attributeDefinitions)
			.withKeySchema(ks)
			.withProvisionedThroughput(provisionedThroughput);
			
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			CreateTableResult result = client.createTable(req);
			System.out.println("created table"+ result.getTableDescription().getTableName());
		}
		
		public void addCategories(){
			try {
				// Add books.
				Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
				item.put("categoryId", new AttributeValue().withS("Electronics"));
				
				client.setRegion(Region.getRegion(Regions.US_WEST_2));
				
				PutItemRequest putReq = new PutItemRequest()
						.withTableName(categoryTable)
						.withItem(item);
				client.putItem(putReq);
				
				item.clear();
				item.put("categoryId", new AttributeValue().withS("Books"));
				putReq = new PutItemRequest()
				.withTableName(categoryTable)
				.withItem(item);
				client.putItem(putReq);
				
			}catch (AmazonServiceException ase) {
				System.err.println("Failed to create item in " + categoryTable);
				ase.printStackTrace();
			} 
		}
		public void createProductTable(){
			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("productId").withAttributeType("N"));
			
			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ks.add(new KeySchemaElement().withAttributeName("productId").withKeyType(KeyType.HASH));
			
			ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
						.withReadCapacityUnits(10L).withWriteCapacityUnits(10L);
			CreateTableRequest req = new CreateTableRequest()
						.withTableName(productCatalogTable)
						.withAttributeDefinitions(attributeDefinitions)
						.withKeySchema(ks)
						.withProvisionedThroughput(provisionedThroughput);
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			CreateTableResult result = client.createTable(req);
			System.out.println("created table"+ result.getTableDescription().getTableName());
			
		}
		
//		public void createCategoryTable(){
//			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
//			attributeDefinitions.add(new AttributeDefinition().withAttributeName("CategoryId").withAttributeType("S"));
//			
//			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
//			ks.add(new KeySchemaElement().withAttributeName("CategoryId").withKeyType(KeyType.HASH));
//			
//			ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
//						.withReadCapacityUnits(10L).withWriteCapacityUnits(10L);
//			CreateTableRequest req = new CreateTableRequest()
//						.withTableName(categoryTable)
//						.withAttributeDefinitions(attributeDefinitions)
//						.withKeySchema(ks)
//						.withProvisionedThroughput(provisionedThroughput);
//			client.setRegion(Region.getRegion(Regions.US_WEST_2));
//			CreateTableResult result = client.createTable(req);
//			System.out.println("created table"+ result.getTableDescription().getTableName());
//			
//		}
		
		public void addProductsToCatalog(){
			try {
				// Add books.
				Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
				item.put("productId", new AttributeValue().withN("1"));
				item.put("productName", new AttributeValue().withS("Book"));
				item.put("productDesc", new AttributeValue().withS("Fictional book"));
				item.put("prodQuantity", new AttributeValue().withN("10"));
				item.put("pricePerUnit", new AttributeValue().withS("54"));
				item.put("categoryId", new AttributeValue().withN("1"));
				
				client.setRegion(Region.getRegion(Regions.US_WEST_2));
				PutItemRequest putReq = new PutItemRequest()
						.withTableName(productCatalogTable)
						.withItem(item);
				client.putItem(putReq);
			}catch (AmazonServiceException ase) {
				System.err.println("Failed to create item in " + productCatalogTable);
				ase.printStackTrace();
			}         
		}
		
		public void getTableDescription(){
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			TableDescription tableDesc = client.describeTable(
					new DescribeTableRequest().withTableName(productCatalogTable)).getTable();
			System.out.println(tableDesc.getTableName());
		}
		
		public void updateProductCatalog(){
			Map<String,AttributeValueUpdate> updateItems = new HashMap<String,AttributeValueUpdate>();
			HashMap<String,AttributeValue> key = new HashMap<String,AttributeValue>();
			key.put("productId", new AttributeValue().withN("1"));
			
			updateItems.put("prodQuantity", new AttributeValueUpdate()
					.withAction(AttributeAction.PUT)
					.withValue(new AttributeValue().withN("30")));
			
			ReturnValue returnValues = ReturnValue.UPDATED_NEW;
			UpdateItemRequest upReq = new UpdateItemRequest()
					.withTableName(productCatalogTable).withKey(key)
					.withAttributeUpdates(updateItems)
					.withReturnValues(returnValues);
			
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			UpdateItemResult result = client.updateItem(upReq);
			System.out.println(result.toString());
		}
}
