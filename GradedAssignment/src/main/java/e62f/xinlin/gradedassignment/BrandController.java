/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-14 8:09:04 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 20032049
 *
 */
@Controller
public class BrandController {

	@Autowired
	private ProductsRepository ProductsRepository;

	@Autowired
	private BrandRepository BrandRepository;

	@GetMapping("/shoesbrand")
	public String viewShoesBrand(Model model) {
		List<Brand> listBrand = BrandRepository.findAll();
		model.addAttribute("listBrand", listBrand);
		return "view_shoes_brand";
	}

	@GetMapping("/shoesbrand/add")
	public String AddShoesBrand(Model model) {
		model.addAttribute("brand", new Brand());
		
		// Javascript validation
		List<Brand> listBrand = BrandRepository.findAll();
		model.addAttribute("listBrand", listBrand);
		
		return "add_shoes_brand";
	}

	@PostMapping("/shoesbrand/save")
	public String saveShoesBrand(@Valid Brand brand, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "add_shoes_brand";
		}
		
		String message = "";
		List<Brand> listBrand = BrandRepository.findAll();
		for (int i = 0; i < listBrand.size(); i++) {
			if (brand.getName().equalsIgnoreCase(listBrand.get(i).getName())){
				message = "Shoes brand name cannot be duplicated.";
			}
		}
		if (!message.equals("")) {
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			return "redirect:/shoesbrand/add";
		}
		BrandRepository.save(brand);

		// Adding danger alert message to show add failed
		redirectAttributes.addFlashAttribute("message", "Brand " + brand.getId() + "added failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		// Adding success alert message to show add success
		redirectAttributes.addFlashAttribute("message", "Brand " + brand.getId() + " Successfully Added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/shoesbrand";
	}

	@GetMapping("/shoesbrand/edit/{id}")
	public String EditShoesBrand(@PathVariable("id") Integer id, Model model) {
		Brand brand = BrandRepository.getById(id);
		model.addAttribute("brand", brand);
		return "edit_shoes_brand";
	}

	@PostMapping("/shoesbrand/edit/{id}")
	public String SaveUpdatedShoesBrand(@Valid Brand brand, BindingResult result, @PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			return "edit_shoes_brand";
		}

		// Adding danger alert message to show edit failed
		redirectAttributes.addFlashAttribute("message", "Brand " + brand.getId() + "update failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		BrandRepository.save(brand);
		// Adding success alert message to show edit success
		redirectAttributes.addFlashAttribute("message", "Brand " + brand.getId() + " Successfully Updated");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/shoesbrand";
	}

	@GetMapping("/shoesbrand/delete/{id}")
	public String deleteShoesBrand(@PathVariable("id") Integer id, Brand brand, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		List<Products> listProducts = ProductsRepository.findAll();
		for (int i = 0; i < listProducts.size(); i++) {
			if (brand.getId() == listProducts.get(i).getBrand().getId()) {
				redirectAttributes.addFlashAttribute("message",
						"Brand cannot be deleted. Foreign key must deleted first.");
				redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
				return "redirect:/shoesbrand";
			}
		}

		BrandRepository.deleteById(id);

		// Adding danger alert message to show delete failed
		redirectAttributes.addFlashAttribute("message", "Brand ID " + brand.getId() + " Deleted failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		if (result.hasErrors()) {
			return "redirect:/shoesbrand";
		}
		// Adding success alert message to show delete success
		redirectAttributes.addFlashAttribute("message", "Brand ID " + brand.getId() + " Successfully Deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/shoesbrand";
	}
}
