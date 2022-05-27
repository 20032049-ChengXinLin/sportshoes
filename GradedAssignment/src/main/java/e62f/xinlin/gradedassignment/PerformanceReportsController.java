/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Feb-10 2:49:51 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.util.List;
/**
 * @author 20032049
 *
 */
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerformanceReportsController {

	@Autowired
	ProductsRepository productsRepo;

	@GetMapping("/performance")
	public String getPieChart(Model model) {
		Map<String, Integer> graphData = new TreeMap<>();
		List<Products> products = productsRepo.findAll();
		for (int i = 0; i < products.size(); i++) {
			int productsSold = products.get(i).getTotalProductsSold();
			String productName = products.get(i).getName();
			int productId = products.get(i).getId();
			String name = productId + ") " + productName;
			graphData.put(name, productsSold);
		}

		model.addAttribute("chartData", graphData);
		return "view_performance_reports";
	}
}