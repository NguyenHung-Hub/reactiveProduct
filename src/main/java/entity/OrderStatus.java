package entity;

public enum OrderStatus {
	
	NEW ("new"), 
	IN_PROGRESS ("in process"), 
	COMPLETED ("completed"), 
	PARTIALLY_SHIPPED ("partially shipped"), 
	ON_HOLD("on hold"), 
	CANCELLED("cancelled"), 
	AWAITING_EXCHANGE("awaiting exchange");
	
	private final String status;

	private OrderStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
