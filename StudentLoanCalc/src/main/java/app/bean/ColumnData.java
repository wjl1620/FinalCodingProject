package app.bean;

public class ColumnData {
	private Integer id;
	private String date;
	private String payment;
	private String addpayment;
	private String interest;
	private String principle;
	private String balance;
	
	public ColumnData(String balance) {
		super();
		this.balance = balance;
	}

	public ColumnData(Integer id, String date, String payment, String addpayment, String interest, String principle,
			String balance) {
		super();
		this.id = id;
		this.date = date;
		this.payment = payment;
		this.addpayment = addpayment;
		this.interest = interest;
		this.principle = principle;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getAddpayment() {
		return addpayment;
	}

	public void setAddpayment(String addpayment) {
		this.addpayment = addpayment;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getPrinciple() {
		return principle;
	}

	public void setPrinciple(String principle) {
		this.principle = principle;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
}
