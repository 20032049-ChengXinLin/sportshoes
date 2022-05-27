/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-13 10:05:47 am 
 * 
 */
package e62f.xinlin.gradedassignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author 20032049
 *
 */
public class ShoesMemberDetailsService implements UserDetailsService{

	
	@Autowired
	private ShoesMemberRepository ShoesMemberRepository;
	
	@Override
	public ShoesMemberDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ShoesMember ShoesMember = ShoesMemberRepository.findByUsername(username);
		
		if (ShoesMember == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		return new ShoesMemberDetails(ShoesMember);
	}
}
