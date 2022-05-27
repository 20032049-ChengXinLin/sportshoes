/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Dec-03 9:11:00 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 20032049
 *
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer>{
	//Select all the cart items that belongs to a member
		public List<ShoppingCart> findByMemberId(int id);
		
		public ShoppingCart findByMemberIdAndProductId(int memberId, int Productid);
}
