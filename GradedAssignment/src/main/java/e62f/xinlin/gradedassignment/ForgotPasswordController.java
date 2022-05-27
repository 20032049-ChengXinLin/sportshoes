/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Feb-06 3:22:36 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.bytebuddy.utility.RandomString;

/**
 * @author 20032049
 *
 */
@Controller
public class ForgotPasswordController {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ShoesMemberServices memberService;

	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		return "forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);
		if (email.equals("")) {
			redirectAttributes.addFlashAttribute("email", "Please enter your email");
			return "redirect:/forgot_password";
		}
		try {
			memberService.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			sendEmail(email, resetPasswordLink);
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		} catch (AccountNotFoundException ex) {
			model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error while sending email");
		}

		return "forgot_password_form";
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("rpsportshoes@gmail.com", "Sport Shoes Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link
				+ "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);

	}

	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		ShoesMember member = memberService.getByResetPasswordToken(token);
		model.addAttribute("token", token);

		if (member == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		}

		return "reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		 String token = request.getParameter("token");
		    String password = request.getParameter("password");
		    String confirmpassword = request.getParameter("confirmpassword");
		    
		    ShoesMember member = memberService.getByResetPasswordToken(token);
		    model.addAttribute("title", "Reset your password");
		     
		    if (member == null) {
		        model.addAttribute("message", "Invalid Token");
		        return "message";
		    } else { 
		    	String errorpassword = "";
		    	String errorconfirmpassword = "";
		    	if (password.equals("")) {
		    		errorpassword = "Please enter a new password";
		    	}
		    	if (confirmpassword.equals("")) {
		    		errorconfirmpassword = "Please confirm password";
		    	}
		    	if (!errorpassword.equals("")|!errorconfirmpassword.equals("")) {
		    		redirectAttributes.addFlashAttribute("password", errorpassword);
		    		redirectAttributes.addFlashAttribute("confirmpassword", errorconfirmpassword);
		    		return "redirect:/reset_password?token="+token;
		    	}
		    	if (!password.equals(confirmpassword)) {
		    		System.out.println("hello");
		    		redirectAttributes.addFlashAttribute("matchpassword", "Password do not match");
		    		return "redirect:/reset_password?token="+token;
		    	}
		        memberService.updatePassword(member, password);
		         
		        redirectAttributes.addFlashAttribute("message", "You have successfully changed your password.");
		    }
		     
		    return "redirect:/login";
	}
}
