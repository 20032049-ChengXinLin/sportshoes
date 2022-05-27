/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Dec-02 6:00:09 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author 20032049
 *
 */


@Entity
public class CustomerOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String orderId;
	private String transactionId;
	
	private String shoesize;

	private int quantity;
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Products product;
	
	//Many member can have cart item
	@ManyToOne
	@JoinColumn(name="member_id")
	private ShoesMember member;
	
	private String address;
	private String postalCode;
	private String status;
	private LocalDate dateorder;
	private LocalDate datedelivery;
	private double total;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getShoesize() {
		return shoesize;
	}

	public void setShoesize(String shoesize) {
		this.shoesize = shoesize;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public ShoesMember getMember() {
		return member;
	}

	public void setMember(ShoesMember member) {
		this.member = member;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDateorder() {
		return dateorder;
	}

	public void setDateorder(LocalDate dateorder) {
		this.dateorder = dateorder;
	}

	public LocalDate getDatedelivery() {
		return datedelivery;
	}

	public void setDatedelivery(LocalDate datedelivery) {
		this.datedelivery = datedelivery;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
