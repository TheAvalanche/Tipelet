package lv.telepit.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;

@Entity
public class ReceiptItem {

	private long id;
	private String name;
	private int count;
	private Double price;
	private int discount = 0;
	private BusinessReceipt parent;

	@Id
	@GeneratedValue(generator = "receiptitem_seq")
	@SequenceGenerator(name = "receiptitem_seq", sequenceName = "receiptitem_seq", initialValue = 1, allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	@ManyToOne(targetEntity = BusinessReceipt.class)
	public BusinessReceipt getParent() {
		return parent;
	}

	public void setParent(BusinessReceipt parent) {
		this.parent = parent;
	}

	@Transient
	public BigDecimal getTotalPrice() {
		return new BigDecimal(price, MathContext.DECIMAL64)
				.subtract(new BigDecimal(price, MathContext.DECIMAL64).multiply(new BigDecimal(discount)).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP))
				.multiply(new BigDecimal(count)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
