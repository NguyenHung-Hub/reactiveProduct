package dao;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;

public abstract class AbstractDao {
	private static final String DB_NAME = "BikeStores";
	private MongoClient client;
	protected MongoDatabase db;

	public AbstractDao(MongoClient client) {
		super();
		this.client = client;
		this.db = client.getDatabase(DB_NAME);
		
	}
	
	public MongoDatabase getDb() {
		return db;
	}
	
	public MongoClient getClient() {
		return client;
	}
	
	
}
