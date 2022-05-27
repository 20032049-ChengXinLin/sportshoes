/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Nov-11 1:30:54 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 20032049
 *
 */
public interface ProductsRepository
		extends JpaRepository<Products, Integer>{

	@Query("SELECT p FROM Products p WHERE p.name LIKE %?1%" + " OR p.brand.name LIKE %?1%"
			+ " OR CONCAT(p.price, '') LIKE %?1%" + " OR p.gender LIKE %?1%" + " OR p.size LIKE %?1%"
			+ " OR p.price LIKE %?1%" + " OR p.quantity LIKE %?1%" + " OR p.type.name LIKE %?1%"
			+ " OR p.colour LIKE %?1%" + " OR p.productCode LIKE %?1%" + " OR p.description LIKE %?1%")
	public Page<Products> findAll(String keyword, Pageable pageable);

}
