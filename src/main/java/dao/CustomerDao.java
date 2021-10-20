package dao;



import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.AggregatePublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Customer;

public class CustomerDao extends AbstractDao{
	
	private MongoCollection<Customer> customerCollection;

	public CustomerDao(MongoClient client) {
		super(client);
		
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		customerCollection = db.getCollection("customers", Customer.class).withCodecRegistry(pojoCodecRegistry);
	}
	
	public Customer getCustomer(String id) throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		AtomicReference<Customer> customer = new AtomicReference<Customer>();
		
		Publisher<Customer> pub = customerCollection.find(Filters.eq("_id", id)).first();
		Subscriber<Customer> sub = new Subscriber<Customer>() {

			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
			}

			@Override
			public void onNext(Customer t) {
				customer.set(t);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				latch.countDown();
			}
		};
		
		pub.subscribe(sub);
		
		latch.await();
		
//		Thread.sleep(1000);
		
		return customer.get();
	}
//	BikeStores> db.customers.aggregate([{ $lookup: { from: 'orders', localField: '_id', foreignField: 'customer.customer_id', as: 'rs' } },
//	{$match:{$expr:{$eq:[{$size:'$rs'},0]}}},
//	{$unset:'rs'}])
//	Những khách hàng chưa từng mua hàng
	public List<Customer> getCustomersNew() throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		List<Customer> customers = new ArrayList<Customer>();
		
		Document lookup = Document.parse("{ $lookup: { from: 'orders', localField: '_id', foreignField: 'customer.customer_id', as: 'rs' } }");
		Document match = Document.parse("{$match:{$expr:{$eq:[{$size:'$rs'},0]}}}");
		Document unset = Document.parse("{$unset:'rs'}");
		
		customerCollection
		.aggregate(Arrays.asList(lookup, match, unset))
		.subscribe(new Subscriber<Customer>() {
			private Subscription s;
			
			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Customer t) {
				customers.add(t);
				this.s.request(1);
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
		
		return customers;
	}

}
