package edu.sjsu.cmpe282.dto;

import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class CategryDao {
	private static AmazonDynamoDBClient client;
	private static DynamoDBMapper dbMapper;
	
	public static void main(String[] args){
		init();
	}
	
	private static void init() {
		AWSCredentials credentials;
		try {
			credentials = new PropertiesCredentials(
					CategryDao.class
							.getResourceAsStream("AwsCredentials.properties"));

			client = new AmazonDynamoDBClient(credentials);
			client.setRegion(Region.getRegion(Regions.US_WEST_1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbMapper = new DynamoDBMapper(client);
	}
}
