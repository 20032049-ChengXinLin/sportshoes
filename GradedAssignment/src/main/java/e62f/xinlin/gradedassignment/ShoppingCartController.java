/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-27 2:28:04 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class ShoppingCartController {
	@Autowired
	private ShoppingCartRepository ShoppingCartRepository;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private ShoesMemberRepository memberRepository;

	@Autowired
	private CustomerOrderRepository customerOrderRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private CustomerOrderServices service;

	@GetMapping("/shoppingcart")
	public String viewShoppingCart(Model model, Principal principal) {
		// Get currently logged in user
		ShoesMemberDetails loggedInMember = (ShoesMemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getShoesMember().getId();

		// Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository
		List<ShoppingCart> cartList = ShoppingCartRepository.findByMemberId(loggedInMemberId);

		// Add the shopping cart items to the model
		model.addAttribute("cartList", cartList);

		// Calculate the total cost of all items in the shopping cart
		double cartTotal = 0.0;
		for (int i = 0; i < cartList.size(); i++) {
			double subTotal = cartList.get(i).getProduct().getPrice();
			int quantity = cartList.get(i).getQuantity();
			double total = subTotal * quantity;
			cartList.get(i).setSubTotal(total);
			if (cartList.get(i).getProduct().getQuantity() > 0) {
				cartTotal += total;
			}

		}
		double delivery = 0.0;
		if (cartTotal != 0.0) {
			delivery = 5 * cartList.size();
		}

		cartTotal += delivery;
		// Add the shopping cart total to the model
		model.addAttribute("deliveryfees", delivery);
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("memberId", loggedInMemberId);

		return "shopping_cart";
	}

	@PostMapping("/shoppingcart/process_order")
	public String processOrder(Model model, @RequestParam("cartTotal") double cartTotal,
			@RequestParam("memberId") int memberId, @RequestParam("orderId") String orderId,
			@RequestParam("transactionId") String transactionId, @RequestParam("address") String address,
			@RequestParam("country") String country, @RequestParam("postalCode") String postalCode) {

		// Retrieve shopping cart purchased
		List<ShoppingCart> cartList = ShoppingCartRepository.findByMemberId(memberId);

		// Get member object
		ShoesMember member = memberRepository.getById(memberId);

		// Get Shoes size
		String shoesSize = "";

		// Local Date and Delivery Date
		LocalDate localDate = null;
		LocalDate deliverydate = null;
		
		//Delivery fees
		double delivery = 5.0;
		
		// Loop to iterate through all cart items
		for (int i = 0; i < cartList.size(); i++) {
			// Get total for each purchase items
			double subTotal = cartList.get(i).getProduct().getPrice();
			int quantity = cartList.get(i).getQuantity();
			double total = subTotal * quantity;
			cartList.get(i).setSubTotal(total+delivery);
			// Retrieve details about current shopping cart
			ShoppingCart currentShoppingCart = cartList.get(i);
			Products productsToUpdate = currentShoppingCart.getProduct();
			int quantityOfItemPurchased = currentShoppingCart.getQuantity();
			shoesSize = currentShoppingCart.getShoesize();
			int productsToUpdateId = productsToUpdate.getId();
			System.out.println("Item: " + productsToUpdate.getDescription());

			// Update products table
			Products inventoryProducts = productsRepository.getById(productsToUpdateId);
			int currentInventoryQuantity = inventoryProducts.getQuantity();
			System.out.println("Current inventory quantity: " + inventoryProducts.getQuantity());
			inventoryProducts.setQuantity(currentInventoryQuantity - quantityOfItemPurchased);

			// Add Total No of Quantity sold to the products table
			int TotalSold = cartList.get(i).getProduct().getTotalProductsSold();
			inventoryProducts.setTotalProductsSold(TotalSold + quantityOfItemPurchased);

			productsRepository.save(inventoryProducts);

			// Date Order and Estimated Delivery Date
			localDate = LocalDate.now();
			deliverydate = localDate.plusDays(3);

			// Add item to Customer Order table
			CustomerOrder orderProducts = new CustomerOrder();
			orderProducts.setOrderId(orderId);
			orderProducts.setTransactionId(transactionId);
			orderProducts.setProduct(productsToUpdate);
			orderProducts.setMember(member);
			orderProducts.setShoesize(shoesSize.trim());
			orderProducts.setStatus("pending");
			orderProducts.setAddress(address + ' ' + country);
			orderProducts.setPostalCode(postalCode);
			orderProducts.setQuantity(quantityOfItemPurchased);
			orderProducts.setDateorder(localDate);
			orderProducts.setDatedelivery(deliverydate);

			orderProducts.setTotal(total + delivery);
			customerOrderRepository.save(orderProducts);

			// clear cart items belonging to member
			ShoppingCartRepository.deleteById(currentShoppingCart.getId());
		}
		// Pass info to view to display success page
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("cartList", cartList);
		model.addAttribute("member", member);
		model.addAttribute("email", member.getEmail());
		model.addAttribute("orderId", orderId);
		model.addAttribute("shoeSize", shoesSize);
		model.addAttribute("transactionId", transactionId);
		model.addAttribute("Deliverydate", deliverydate);
		model.addAttribute("name", member.getName());
		model.addAttribute("address", address + " " + country);
		// Send email
		String subject = "Sport Shoes Order is confirmed!";
		String body = "Thank you for your order!\n" + "We will be preparing to deliver your shoes in 3 days time. \n"
				+ "Order ID: " + orderId + "\n" + "Transaction ID: " + transactionId + "\n" + "Shoes Size: " + shoesSize
				+ "\n" + "Order Date: " + localDate + "\n" + "Estimated Delivery Date: " + deliverydate + "\n"
				+ "Total Amount: $" + cartTotal + "\n" + "Address: " + address + " " + country + " " + postalCode;
		String to = member.getEmail();
		sendEmail(to, subject, body);
		return "transaction_successful";
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);
		System.out.println("Sending");
		javaMailSender.send(msg);
		System.out.println("Sent");
	}
	
	@PostMapping("/shoppingcart/add/{productId}")
	public String addToCart(@PathVariable("productId") int productId,
			@RequestParam(value = "shoesize", required = true, defaultValue = "0") String shoesize,
			@RequestParam("quantity") int quantity, Principal principal, RedirectAttributes redirectAttributes) {

		if (shoesize.equals("0")) {
			redirectAttributes.addFlashAttribute("message", "Please choose a Shoes Size");
			redirectAttributes.addFlashAttribute("border", "border-danger");
			return "redirect:/pagetwo/{productId}";
		}
		// Get currently logged in user
		ShoesMemberDetails loggedInMember = (ShoesMemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getShoesMember().getId();

		// Check in the shoppingChartRepo if product was previously added by user.
		// *Hint: we will need to write a new method in the shoppingChartRepository
		ShoppingCart cart = ShoppingCartRepository.findByMemberIdAndProductId(loggedInMemberId, productId);

		// if the products was previously added, then we get the quantity that was
		// previously added and increase that

		if (cart != null) {
			cart.setQuantity(quantity + cart.getQuantity());
			cart.setShoesize(shoesize);
			ShoppingCartRepository.save(cart);

		} else {
			// if the products was NOT previously added,
			// then prepare the products and member objects
			Products products = productsRepository.getById(productId);
			ShoesMember member = memberRepository.getById(loggedInMemberId);

			// Create a new ShoppingCart object
			ShoppingCart newcart = new ShoppingCart();

			// Set the item and member as well as the new quantity in the new CartItem
			// object
			newcart.setProduct(products);
			newcart.setMember(member);
			newcart.setQuantity(quantity);
			newcart.setShoesize(shoesize);

			// Save the new CartItem object to the repository
			ShoppingCartRepository.save(newcart);
		}

		return "redirect:/shoppingcart";
	}


	@PostMapping("/shoppingcart/edit/{id}")
	public String saveCart(@PathVariable("id") int cartId, @RequestParam("shoesize") String shoesize,
			@RequestParam("qty") int qty) {

		// Get cart object by cart's id
		ShoppingCart cart = ShoppingCartRepository.getById(cartId);
		// Set the quantity of the car object retrieved
		cart.setQuantity(qty);
		cart.setShoesize(shoesize);
		// Save the cartItem back to the cartItemRepo
		ShoppingCartRepository.save(cart);
		return "redirect:/shoppingcart";
	}

	@GetMapping("/shoppingcart/delete/{id}")
	public String deleteCart(@PathVariable("id") int cartId) {

		ShoppingCartRepository.deleteById(cartId);

		return "redirect:/shoppingcart";
	}

	@GetMapping("/purchasehistory")
	public String viewPurchaseHistory(Model model, Principal principal) {
		// Get currently logged in user
//		ShoesMemberDetails loggedInMember = (ShoesMemberDetails) SecurityContextHolder.getContext().getAuthentication()
//				.getPrincipal();
//		int loggedInMemberId = loggedInMember.getShoesMember().getId();
//
//		List<CustomerOrder> orderList = customerOrderRepository.findByMemberId(loggedInMemberId);
//		// Add the shopping cart items to the model
//		model.addAttribute("orderList", orderList);

		return viewPage(model, 1, "id", "desc");

	}
	@GetMapping("/purchasehistory/page/{pageNum}")
	public String viewPage(Model model, @PathVariable(name = "pageNum") int pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir) {
		ShoesMemberDetails loggedInMember = (ShoesMemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getShoesMember().getId();

		Page<CustomerOrder> page = service.listHistory(loggedInMemberId, pageNum, sortField, sortDir);

		List<CustomerOrder> orderList = page.getContent();
		// List<Products> listProducts = ProductsRepository.findAll();
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("orderList", orderList);

		return "view_purchase_history";
	}

}
