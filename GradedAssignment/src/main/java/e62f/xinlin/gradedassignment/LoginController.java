/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-13 6:08:19 pm 
 * 
 */
package e62f.xinlin.gradedassignment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 20032049
 *
 */
@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}


}
