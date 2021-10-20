package entity;

public class Phone {
	private String type;
	private String number;
	/**
	 * @param type
	 * @param number
	 */
	public Phone(String type, String number) {
		this.type = type;
		this.number = number;
	}
	/**
	 * 
	 */
	public Phone() {
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "Phone [type=" + type + ", number=" + number + "]";
	}
	
	
}
