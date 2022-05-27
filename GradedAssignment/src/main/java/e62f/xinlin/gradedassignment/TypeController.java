/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-14 8:08:37 pm 
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
public class TypeController {

	@Autowired
	private ProductsRepository ProductsRepository;

	@Autowired
	private TypeRepository TypeRepository;

	@GetMapping("/shoestype")
	public String viewShoesType(Model model) {
		List<Type> listType = TypeRepository.findAll();
		model.addAttribute("listType", listType);
		return "view_shoes_type";
	}

	@GetMapping("/shoestype/add")
	public String AddShoesType(Model model) {
		model.addAttribute("type", new Type());
		List<Type> listType = TypeRepository.findAll();
		model.addAttribute("listType", listType);
		return "add_shoes_type";
	}

	@PostMapping("/shoestype/save")
	public String saveShoesType(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			return "add_shoes_type";
		}
		
		String message = "";
		List<Type> listType = TypeRepository.findAll();
		for (int i = 0; i < listType.size(); i++) {
			if (type.getName().equalsIgnoreCase(listType.get(i).getName())) {
				message = "Shoes type name cannot be duplicated. \n";
			}
		}
		if (!message.equals("")) {
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			return "redirect:/shoestype/add";
		}

		TypeRepository.save(type);

		// Adding danger alert message to show edit failed
		redirectAttributes.addFlashAttribute("message", "Type " + type.getId() + "added failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");


		
		// Adding success alert message to show edit success
		redirectAttributes.addFlashAttribute("message", "Type " + type.getId() + " Successfully Added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/shoestype";
	}

	@GetMapping("/shoestype/edit/{id}")
	public String EditShoesType(@PathVariable("id") Integer id, Model model) {
		Type type = TypeRepository.getById(id);
		model.addAttribute("type", type);
		return "edit_shoes_type";
	}

	@PostMapping("/shoestype/edit/{id}")
	public String SaveUpdatedShoesType(@Valid Type type, BindingResult result, @PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "edit_shoes_type";
		}
		TypeRepository.save(type);

		// Adding danger alert message to show edit failed
		redirectAttributes.addFlashAttribute("message", "Type " + type.getId() + "update failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		// Adding success alert message to show edit success
		redirectAttributes.addFlashAttribute("message", "Type " + type.getId() + " Successfully Updated");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/shoestype";
	}

	@GetMapping("/shoestype/delete/{id}")
	public String deleteShoesType(@PathVariable("id") Integer id, Type type, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		List<Products> listProducts = ProductsRepository.findAll();
		for (int i = 0; i < listProducts.size(); i++) {
			if (type.getId() == listProducts.get(i).getType().getId()) {
				redirectAttributes.addFlashAttribute("message",
						"Type cannot be deleted. Foreign key must deleted first.");
				redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
				return "redirect:/shoestype";
			}
		}

		TypeRepository.deleteById(id);

		// Adding danger alert message to show delete failed
		redirectAttributes.addFlashAttribute("message", "Type ID " + type.getId() + " Deleted failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		if (result.hasErrors()) {
			return "redirect:/shoestype";
		}
		// Adding success alert message to show delete success
		redirectAttributes.addFlashAttribute("message", "Type ID " + type.getId() + " Successfully Deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/shoestype";
	}

}
