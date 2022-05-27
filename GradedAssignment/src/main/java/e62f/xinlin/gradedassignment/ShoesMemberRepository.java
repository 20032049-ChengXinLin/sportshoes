/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-06 10:33:19 am 
 * 
 */
package e62f.xinlin.gradedassignment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 20032049
 *
 */
public interface ShoesMemberRepository extends JpaRepository<ShoesMember, Integer> {

	public ShoesMember findByUsername(String username);

	@Query("SELECT m FROM ShoesMember m WHERE m.email = ?1")
	public ShoesMember findByEmail(String email);

	public ShoesMember findByResetPasswordToken(String token);
}
