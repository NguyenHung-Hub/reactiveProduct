   package dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.AggregatePublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Product;

public class ProductDao extends AbstractDao{

	private MongoCollection<Product> productCollection;

	public ProductDao(MongoClient client) {
		super(client);

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		productCollection = db.getCollection("products", Product.class).withCodecRegistry(pojoCodecRegistry);
	}
	
	public Product getProduct(long id) throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		AtomicReference<Product> product = new AtomicReference<Product>();
		
		Publisher<Product> pub = productCollection.find(Filters.eq("_id", id)).first();
		
		Subscriber<Product> sub = new Subscriber<Product>() {
			
			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
			}
			
			@Override
			public void onNext(Product t) {
				product.set(t);
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
		
		return product.get();
	}
	

	public boolean addProduct(Product p) throws InterruptedException {

		AtomicBoolean rs = new AtomicBoolean(false);

		CountDownLatch latch = new CountDownLatch(1);

		Publisher<InsertOneResult> pub = productCollection.insertOne(p);
		Subscriber<InsertOneResult> sub = new Subscriber<InsertOneResult>() {

			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
			}

			@Override
			public void onNext(InsertOneResult t) {
				System.out.println(t.getInsertedId()+ " inserted");
				rs.set(true);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("Completed");
				latch.countDown();
			}
		};


		pub.subscribe(sub);

		latch.await();

		return rs.get();
	}

	//	BikeStores> db.products.aggregate([{ $group: { _id: null, ps: { $addToSet: '$$ROOT' }, maxPrice: { $max: '$price' } } }, 
	//	{ $unwind: '$ps' }, 
	//	{ $match: { $expr: { $eq: ['$ps.price', '$maxPrice'] } } },
	//	{$replaceWith:'$ps'}])
	public List<Product> getProductsMaxPrice2() throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);

		List<Product> products = new ArrayList<Product>();

		Document group = Document.parse("{ $group: { _id: null, ps: { $addToSet: '$$ROOT' }, maxPrice: { $max: '$price' } } }");
		Document unwind = Document.parse("{ $unwind: '$ps' }");
		Document match = Document.parse("{ $match: { $expr: { $eq: ['$ps.price', '$maxPrice'] } } }");
		Document replaceWith = Document.parse("{$replaceWith:'$ps'}");

		AggregatePublisher<Product> pub = productCollection.aggregate(Arrays.asList(group, unwind, match, replaceWith));

		Subscriber<Product> sub = new Subscriber<Product>() {

			private Subscription s;

			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Product t) {
				products.add(t);
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
		};

		pub.subscribe(sub);

		latch.await();

		return products;
	}

//	 db.orders.createIndex({'order_details.product_id':1})
//	Tìm danh sách sản phẩm chưa bán được lần nào. 
//	BikeStores> db.products.aggregate([{ $lookup: { from: 'orders', localField: '_id', foreignField: 'order_details.product_id', as: 'rs' } },
//	 {$match:{$expr:{$eq:[{$size:'$rs'},0]}}},
//	 {$unset:'rs'}])
	public List<Product> getProducts_NotSoldYet() throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		List<Product> products = new ArrayList<Product>();
		
		Document lookup = Document.parse("{ $lookup: { from: 'orders', localField: '_id', foreignField: 'order_details.product_id', as: 'rs' } }");
		Document match = Document.parse("{$match:{$expr:{$eq:[{$size:'$rs'},0]}}}");
		Document unset = Document.parse("{$unset:'rs'}");
		
		productCollection.aggregate(Arrays.asList(lookup, match, unset))
		.subscribe(new Subscriber<Product>() {
			private Subscription s;

			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Product t) {
				products.add(t);
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
		
		return products;
	}

}
