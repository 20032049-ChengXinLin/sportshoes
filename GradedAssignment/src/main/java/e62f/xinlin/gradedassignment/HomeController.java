/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Nov-20 9:30:34 pm 
 * 
 */
package e62f.xinlin.gradedassignment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@Autowired
	private ProductsRepository ProductsRepository;
	

	@GetMapping("/")
	public String home(Model model) {
		List<Products> listProducts = ProductsRepository.findAll();
		model.addAttribute("listProducts", listProducts);
		return "index";
	}

	@GetMapping("/merchant")
	public String merchant() {
		return "merchant";
	}

	@GetMapping("/403")
	public String error403() {
		return "403";
	}
}
