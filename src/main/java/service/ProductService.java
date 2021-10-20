package service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.reactivestreams.client.MongoCollection;

import dao.ProductDao;
import entity.Product;

public class ProductService {
	
	private ProductDao productDao;
	
	private MongoCollection<Document> ordersCollection;

	public ProductService(ProductDao productDao) {
		super();
		this.productDao = productDao;
		
		ordersCollection = productDao.getDb().getCollection("orders");
	}
	
//	Tính tổng số lượng của từng sản phẩm đã bán ra.
//	BikeStores> db.orders.aggregate([{$unwind:'$order_details'},
//	 {$group:{_id:'$order_details.product_id', 'quantity in total':{$sum:'$order_details.quantity'}}}])  
//	 + getTotalProduct(): Map<Product, Integer> 
	public Map<Product, Integer> getTotalProduct() throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		 Map<Product, Integer> map = new HashMap<Product, Integer>();
		 
		 ordersCollection.aggregate(Arrays.asList(
				 	Document.parse("{$unwind:'$order_details'}"),
				 	Document.parse("{$group:{_id:'$order_details.product_id', 'quantity in total':{$sum:'$order_details.quantity'}}}")
				 )).subscribe(new Subscriber<Document>() {
					 	private Subscription s;
					 
					@Override
					public void onSubscribe(Subscription s) {
						this.s = s;
						this.s.request(1);
					}

					@Override
					public void onNext(Document t) {
						try {
							Product p = productDao.getProduct(t.getLong("_id"));
							map.put(p, t.getInteger("quantity in total"));
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
