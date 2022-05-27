/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-06 10:32:09 am 
 * 
 */
package e62f.xinlin.gradedassignment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author 20032049
 *
 */
@Entity
public class ShoesMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotEmpty(message = "Member name cannot be empty!")
	@Size(min = 3, max = 20, message = "Member Name length must be between 3 and 20 characters!")
	private String name;

	@NotNull
	@NotEmpty(message = "Member username cannot be empty!")
	@Size(min = 3, max = 10, message = "Member username length must be between 3 and 10 characters!")
	private String username;

	@NotNull
	@NotEmpty(message = "Phone cannot be empty!")
	@Pattern(regexp = "[689]\\d{7}", message = "Phone must start with 6,8, 9 and must have 8 digits")
	private String phone;

	@NotNull
	@NotEmpty(message = "Member password cannot be empty!")
	@Size(min = 5, message = "Password must consists of at least 5 characters")
	private String password;

	@Email
	@NotNull
	@NotEmpty(message = "Email cannot be empty!")
	private String email;
	private String role;
	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

}
