/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Dec-03 8:53:54 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * @author 20032049
 *
 */
@Entity
public class ShoppingCart {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String shoesize;
	
	private int quantity;
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Products product;
	
	//Many member can have cart item
	@ManyToOne
	@JoinColumn(name="member_id")
	private ShoesMember member;
	
	@Transient
	private double subTotal;

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public ShoesMember getMember() {
		return member;
	}

	public void setMember(ShoesMember member) {
		this.member = member;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
