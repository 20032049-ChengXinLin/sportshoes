/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Feb-06 3:17:41 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author 20032049
 *
 */
@Service
@Transactional
public class ShoesMemberServices {
	@Autowired
    private ShoesMemberRepository memberRepo;
     
 
    public void updateResetPasswordToken(String token, String email) throws AccountNotFoundException {
        ShoesMember member = memberRepo.findByEmail(email);
        if (member != null) {
        	member.setResetPasswordToken(token);
        	memberRepo.save(member);
        } else {
            throw new AccountNotFoundException("Could not find any member with the email " + email);
        }
    }
     
    public ShoesMember getByResetPasswordToken(String token) {
        return memberRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(ShoesMember member, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);
         
        member.setResetPasswordToken(null);
        memberRepo.save(member);
    }
}
