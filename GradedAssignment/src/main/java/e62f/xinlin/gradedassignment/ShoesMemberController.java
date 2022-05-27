/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-06 10:33:42 am 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 20032049
 *
 */
@Controller
public class ShoesMemberController {
	@Autowired
	private ShoesMemberRepository ShoesMemberRepository;

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	@GetMapping("/member")
	public String viewMember(Model model) {
		List<ShoesMember> listMember = ShoesMemberRepository.findAll();

		model.addAttribute("listMember", listMember);
		return "view_member";
	}

	@GetMapping("/member/{id}")
	public String getMemberByCustomerOrder(@PathVariable("id") Integer id, Model model) {
		ShoesMember listMember = ShoesMemberRepository.getById(id);

		model.addAttribute("listMember", listMember);
		return "view_member";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("shoesMember", new ShoesMember());
		List<ShoesMember> listMember = ShoesMemberRepository.findAll();
		model.addAttribute("listMember", listMember);
		return "register_account";
	}

	@PostMapping("/register/save")
	public String saveMember(@Valid ShoesMember shoesmember, BindingResult bindingResult,
			@RequestParam("member_password") String password, RedirectAttributes redirectAttribute) {
		if (bindingResult.hasErrors()) {
			return "register_account";
		}
		List<ShoesMember> listMember = ShoesMemberRepository.findAll();
		String username = "";
		String email = "";
		for (int i = 0; i < listMember.size(); i++) {
			if (listMember.get(i).getUsername().equals(shoesmember.getUsername())) {
				username = "Username has already been taken. Please choose another one. ";
			}
			if (listMember.get(i).getEmail().equals(shoesmember.getEmail())) {
				email = "Email has already been taken";
			}
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String matchpassword = "";

		if (password.equals(shoesmember.getPassword())) {
			String encodedPassword = passwordEncoder.encode(shoesmember.getPassword());
			shoesmember.setPassword(encodedPassword);
			shoesmember.setRole("ROLE_CUSTOMER");
		} else {
			matchpassword += "Password do not match \n";
		}

		if (username != "" || matchpassword != "" || email != "") {
			redirectAttribute.addFlashAttribute("username", username);
			redirectAttribute.addFlashAttribute("matchpassword", matchpassword);
			redirectAttribute.addFlashAttribute("email", email);
			return "redirect:/register";
		}
		ShoesMemberRepository.save(shoesmember);
		redirectAttribute.addFlashAttribute("success", "Member registered!");
		return "redirect:/register";
	}

	@GetMapping("/account")
	public String viewAccount(Model model, Principal principal) {
		// Get currently logged in user
		ShoesMemberDetails loggedInMember = (ShoesMemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getShoesMember().getId();
		ShoesMember member = ShoesMemberRepository.getById(loggedInMemberId);
		model.addAttribute("member", member);
		return "view_account";
	}

	@GetMapping("/member/edit/{id}")
	public String editMember(@PathVariable("id") Integer id, Model model) {

		ShoesMember shoesMember = ShoesMemberRepository.getById(id);
		model.addAttribute("shoesMember", shoesMember);
		
		String password = shoesMember.getPassword();
		model.addAttribute("password", password);

		return "edit_member";
	}

	@PostMapping("/member/edit/{id}")
	public String saveUpdatedMember(@PathVariable("id") Integer id, @Valid ShoesMember shoesMember,
			BindingResult bindingResult, @RequestParam("oldpassword") String oldPassword,
			@RequestParam("confirmpassword") String confirmPassword, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "edit_member";
		}
		ShoesMember member = ShoesMemberRepository.getById(id);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				
		String errorOld = "";
		String errorConfirm = "";
		
		if (passwordEncoder.matches(oldPassword, member.getPassword()) == true) {
			if (confirmPassword.equals(shoesMember.getPassword())) {
				String encodedNewPassword = passwordEncoder.encode(shoesMember.getPassword());
				shoesMember.setPassword(encodedNewPassword);
				shoesMember.setRole("ROLE_CUSTOMER");
			} else {
				errorConfirm += "Password does not match";
			}

		} else {
			errorOld += "Wrong Password";
		}

		if (errorOld != "" || errorConfirm != "") {
			redirectAttributes.addFlashAttribute("errorOld", errorOld);
			redirectAttributes.addFlashAttribute("errorConfirm", errorConfirm);
			return "redirect:/member/edit/{id}";
		}
		ShoesMemberRepository.save(shoesMember);
		redirectAttributes.addFlashAttribute("success", "Member Updated!");
		return "redirect:/account";
	}

	@GetMapping("/member/delete/{id}")
	public String deleteMember(@PathVariable("id") Integer id,  RedirectAttributes redirectAttributes) {
		List<ShoppingCart> cart = shoppingCartRepository.findAll();
		for (int i = 0; i < cart.size(); i ++) {
			if (cart.get(i).getMember().getId()== id) {	
				System.out.println(cart.get(i).getMember().getId());
				shoppingCartRepository.deleteById(cart.get(i).getId());
			}
		}
		
		ShoesMemberRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "Successfully deleted your account!");
		return "redirect:/login";
	}
}
