/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Nov-11 1:25:28 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author 20032049
 *
 */
@Entity
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotEmpty(message = "Gender cannot be empty!")
	private String gender;

	private String image;

	@NotNull
	@NotEmpty(message = "Product name cannot be empty!")
	@Size(min = 5, max = 50, message = "Product Name length must be between 3 and 30 characters!")
	private String name;

	@NotNull
	@NotEmpty(message = "Size cannot be empty!")
	private String size;

	@NotNull
	@DecimalMin(value="50.0", message = "price must be positive and more than 50!")
	private double price;

	@NotNull
	@NotEmpty(message = "Colour cannot be empty!")
	@Size(min = 5, max = 50, message = "Colour length must be between 3 and 50 characters!")
	private String colour;

	@NotNull
	@Min(value = 0, message = "Quantity must be more than 0!")
	private int quantity;

	@NotNull
	@NotEmpty(message = "ProductCode cannot be empty!")
	@Pattern(regexp = "[A-Z]{2}\\d{4}(-\\d{3})?", message = "Product Code must start with [A-Z]{2}\\d{4}(-\\d{3})?")
	private String productCode;

	@NotNull
	@NotEmpty(message = "Description cannot be empty!")
	@Size(min = 50, max = 250, message = "Description length must be between 50 and 250 characters!")
	private String description;

	@ManyToOne
	@JoinColumn(name = "type_id", nullable = false)
	@NotNull(message = "Type cannot be empty!")
	private Type type;

	private int TotalProductsSold;
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false)
	@NotNull(message = "Brand cannot be empty!")
	private Brand brand;

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@OneToMany(mappedBy = "product")
	private Set<ShoppingCart> ShoppingChart;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Pattern(regexp = "\\w+\\.(png|jpeg)", message = "Only images of type JPEG or PNG are supported.")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Set<ShoppingCart> getShoppingChart() {
		return ShoppingChart;
	}

	public void setShoppingChart(Set<ShoppingCart> shoppingChart) {
		ShoppingChart = shoppingChart;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTotalProductsSold() {
		return TotalProductsSold;
	}

	public void setTotalProductsSold(int totalProductsSold) {
		TotalProductsSold = totalProductsSold;
	}

}
