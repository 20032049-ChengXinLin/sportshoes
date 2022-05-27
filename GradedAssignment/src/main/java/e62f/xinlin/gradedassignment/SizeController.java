/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-14 8:09:20 pm 
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
public class SizeController {

	@Autowired
	private SizeRepository SizeRepository;

	@GetMapping("/availablesize")
	public String viewAvailableSize(Model model) {
		List<Size> listSize = SizeRepository.findAll();
		model.addAttribute("listSize", listSize);
		return "view_available_size";
	}

	@GetMapping("/availablesize/add")
	public String addAvailableSize(Model model) {
		model.addAttribute("size", new Size());
		List<Size> listSize = SizeRepository.findAll();
		model.addAttribute("listSize", listSize);
		return "add_shoes_size";
	}

	@PostMapping("/availablesize/save")
	public String saveAvailableSize(@Valid Size size, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			return "add_shoes_size";
		}
		
		String message = "";
		List<Size> listSize = SizeRepository.findAll();
		for (int i = 0; i < listSize.size(); i++) {
			if (size.getName().equalsIgnoreCase(listSize.get(i).getName())) {
				message = "Shoes Size cannot be duplicated. \n";
			}
		}
		if (!message.equals("")) {
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			return "redirect:/availablesize/add";
		}
		SizeRepository.save(size); 
		// Adding danger alert message to show add failed
		redirectAttributes.addFlashAttribute("message", "Size " + size.getId() + "added failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");


		
		// Adding success alert message to show add success
		redirectAttributes.addFlashAttribute("message", "Size " + size.getId() + " Successfully Added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/availablesize";
	}

	@GetMapping("/availablesize/edit/{id}")
	public String editAvailableSize(@PathVariable("id") Integer id, Model model) {
		Size size = SizeRepository.getById(id);
		model.addAttribute("size", size);

		return "edit_shoes_size";
	}

	@PostMapping("/availablesize/edit/{id}")
	public String saveAvailableSize(@Valid Size size, BindingResult result, @PathVariable("id") Integer id, 
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "edit_shoes_size";
		}
		SizeRepository.save(size);

		// Adding danger alert message to show edit failed
		redirectAttributes.addFlashAttribute("message", "Size " + size.getId() + "update failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		// Adding success alert message to show edit success
		redirectAttributes.addFlashAttribute("message", "Size " + size.getId() + " Successfully Updated");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/availablesize";
	}

	@GetMapping("/availablesize/delete/{id}")
	public String deleteAvailableSize(@PathVariable("id") Integer id, Size size, BindingResult result,
			RedirectAttributes redirectAttributes) {
		SizeRepository.deleteById(id);

		// Adding danger alert message to show delete failed
		redirectAttributes.addFlashAttribute("message", "Size ID " + size.getId() + " Deleted failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		if (result.hasErrors()) {
			return "redirect:/availablesize";
		}
		// Adding success alert message to show delete success
		redirectAttributes.addFlashAttribute("message", "Size ID " + size.getId() + " Successfully Deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/availablesize";
	}
}
