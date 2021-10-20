package app;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import dao.CustomerDao;
import dao.OrderDao;
import dao.ProductDao;
import entity.Customer;
import entity.Product;
import service.ProductService;

public class App {
	public static void main(String[] args) throws InterruptedException {
		
		MongoClient client = MongoClients.create();
		ProductDao productDao = new ProductDao(client );
		ProductService productService = new ProductService(productDao);
		
		CustomerDao customerDao = new CustomerDao(client);
		OrderDao orderDao = new OrderDao(client, customerDao);
		
		orderDao
		.getOrdersByCustomers()
		.entrySet()
		.iterator()
		.forEachRemaining(entry -> {
			System.out.println("Customer name: " + entry.getKey().getFirstName() + "  " + entry.getKey().getLastName());
			System.out.println("Number of orders: " + entry.getValue());
			System.out.println("=================");
		});
		
//		customerDao
//		.getCustomersNew()
//		.forEach(cus -> System.out.println(cus));
		
//		Customer cus = customerDao.getCustomer("CHAR5");
//		System.out.println(cus);
		
//		productService
//		.getTotalProduct()
//		.entrySet()
//		.iterator()
//		.forEachRemaining(entry -> {
//			System.out.println("Product: " + entry.getKey().getName());
//			System.out.println("Quantity in total: " + entry.getValue());
//			System.out.println("====================");
//		});
		
		
//		productDao.getProducts_NotSoldYet()
//		.forEach(p -> System.out.println(p));
		
//		Product p = productDao.getProduct(3l);
//		System.out.println(p);
		
//		Product p = new Product(40001l, "abc", "xyz", "p name", Arrays.asList("blue"), 2020, 11999.99);
//		boolean rs = productDao.addProduct(p );
//		System.out.println(rs);
		
//		productDao.getProductsMaxPrice2()
//		.forEach(p-> System.out.println(p));
		
	}
}
