package dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Customer;

public class OrderDao extends AbstractDao{
	
	private CustomerDao customerDao;
	
	private MongoCollection<Document> orderCollection;

	public OrderDao(MongoClient client, CustomerDao customerDao) {
		super(client);
		this.customerDao = customerDao;
		orderCollection = db.getCollection("orders");
	}
	
//	db.orders.aggregate([])
//	+ getOrdersByCustomers() : Map<Customer, Integer> 

	public Map<Customer, Integer> getOrdersByCustomers() throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Map<Customer, Integer> map = new HashMap<Customer, Integer>();
		
//		orderCollection.aggregate(Arrays.asList(
//					Document.parse("{$group:{_id:'$customer.customer_id', orders:{$sum:1}}}")
//				));
		orderCollection
		.aggregate(Arrays.asList(Aggregates.group("$customer.customer_id", Accumulators.sum("orders",1))))
		.subscribe(new Subscriber<Document>() {
			private Subscription s;
			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Document t) {
				try {
					Customer customer = customerDao.getCustomer(t.getString("_id"));
					map.put(customer, t.getInteger("orders"));
					this.s.request(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				latch.countDown();
			}
		});
		
		latch.await();
		
		return map;
	}

}
