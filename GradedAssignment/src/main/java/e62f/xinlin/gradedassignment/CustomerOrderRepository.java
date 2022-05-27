/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Feb-03 10:00:37 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author 20032049
 *
 */
public interface CustomerOrderRepository
		extends JpaRepository<CustomerOrder, Integer>, PagingAndSortingRepository<CustomerOrder, Integer> {
	// Select all the cart items that belongs to a member
	public List<CustomerOrder> findByMemberId(int id);

	public CustomerOrder findByMemberIdAndProductId(int memberId, int Productid);

	@Query("SELECT c FROM CustomerOrder c WHERE c.member.username LIKE %?1%" + " OR CONCAT(c.total, '') LIKE %?1%"
			+ " OR c.orderId LIKE %?1%" + " OR c.transactionId LIKE %?1%" + " OR c.quantity LIKE %?1%"
			+ " OR c.address LIKE %?1%" + " OR c.product.productCode LIKE %?1%" + " OR c.shoesize LIKE %?1%"
			+ " OR c.dateorder LIKE %?1%" + " OR c.datedelivery LIKE %?1%" + " OR c.status LIKE %?1%" + " OR c.postalCode LIKE %?1%")
	public Page<CustomerOrder> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT c FROM CustomerOrder c WHERE c.member.username LIKE %?1%" + " OR CONCAT(c.total, '') LIKE %?1%"
			+ " OR c.orderId LIKE %?1%" + " OR c.transactionId LIKE %?1%" + " OR c.quantity LIKE %?1%"
			+ " OR c.address LIKE %?1%" + " OR c.product.productCode LIKE %?1%" + " OR c.shoesize LIKE %?1%"
			+ " OR c.dateorder LIKE %?1%" + " OR c.datedelivery LIKE %?1%" + " OR c.status LIKE %?1%" + " OR c.postalCode LIKE %?1%")
	public Page<CustomerOrder> findByOrderAndMemberId(int memberId, String keyword, Pageable pageable);
	
	public Page<CustomerOrder> findByMemberId(int memberId, Pageable pageable);
	
}
