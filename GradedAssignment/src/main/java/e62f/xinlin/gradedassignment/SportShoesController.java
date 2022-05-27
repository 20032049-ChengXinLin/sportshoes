/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-27 2:32:15 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 20032049
 *
 */
@Controller
public class SportShoesController {

	@Autowired
	private ProductsRepository ProductsRepository;

	@GetMapping("/pagetwo/{id}")
	public String viewSingleProduct(@PathVariable("id") Integer productsId, Model model) {
		Products listProducts = ProductsRepository.getById(productsId);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("shoppingcart", new ShoppingCart());
		return "pagetwo";
	}

	@GetMapping("/sizechart")
	public String sizeChart() {
		return "size_chart";
	}
	@GetMapping("/contactus")
	public String contactUs() {
		return "contact_us";
	}

}
