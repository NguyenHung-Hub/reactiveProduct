package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	
	private String orderID;
	private Date orderDate;
	private Date shippedDate;
	private double orderTotal;
	
	private List<OrderDetail> orderDetails;
	private Staff staff;
	private Customer customer;
	private OrderStatus orderStatus;
	private Address shippingAddress;
	
	
	public Order() {
		this(new Date(), OrderStatus.NEW);
	}

	public Order(Date orderDate, OrderStatus orderStatus) {
		super();
		this.orderDate = new Date();
		this.orderStatus = orderStatus;
		orderDetails = new ArrayList<>();
		this.orderTotal = getOrderTotal();
	}

	public void addOrderDetail(Product product, Order order, int quantity, String color, double price, double discount) {
		OrderDetail od = new OrderDetail(product, quantity, color, price, discount);
		orderDetails.add(od);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public double getOrderTotal() {
		
		double total = 0.0;
		
		for(OrderDetail od : orderDetails) {
			total += od.getLineTotal();
		}
		
		return total;
		
//		return orderDetails
//				.stream()
//				.mapToDouble(or -> or.getLineTotal())
//				.sum();
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
				
		return "Order [orderID=" + orderID + ", orderDate=" + df.format(orderDate) 		
		+ ", orderStatus=" + orderStatus
		+ ", ShippedDate=" + shippedDate + ", customer=" + customer + ", staff=" + staff + ", orderDetails="
		+ orderDetails + ", orderTotal=" + orderTotal + ", shippingAddress=" + shippingAddress + "]";
	}
	
}


