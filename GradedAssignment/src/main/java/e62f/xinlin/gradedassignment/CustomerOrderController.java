/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-14 8:09:54 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * @author 20032049
 *
 */
@Controller
public class CustomerOrderController {
	@Autowired
	private CustomerOrderRepository CustomerOrderRepository;
		
	@Autowired
	private CustomerOrderServices service;
	
	@GetMapping("/customerorders/page/{pageNum}")
	public String viewPage(Model model, @PathVariable(name = "pageNum") int pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<CustomerOrder> page = service.listAll(pageNum, sortField, sortDir, keyword);

		List<CustomerOrder> listCustomer = page.getContent();
		// List<Products> listProducts = ProductsRepository.findAll();

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listCustomer", listCustomer);

		return "view_customer_orders";
	}

	@GetMapping("/customerorders")
	public String viewCustomerOrders(Model model, @Param("keyword") String keyword) {
//		List<CustomerOrder> listCustomer = CustomerOrderRepository.findAll();
//		model.addAttribute("listCustomer", listCustomer);
//
//		return "view_customer_orders";
		return viewPage(model, 1, "id", "desc", keyword);
	}

	@GetMapping(path = "/customerorders/member/{id}")
	public String getOrdersbyMember(@PathVariable Integer id, Model model, @Param("keyword") String keyword) {

		List<CustomerOrder> listCustomer = CustomerOrderRepository.findByMemberId(id);

		model.addAttribute("listCustomer", listCustomer);
		return viewOrdersMemberPage(id, model, 1, "id", "desc", keyword);
	}
	@GetMapping("/customerorders/member/{id}/page/{pageNum}")
	public String viewOrdersMemberPage(@PathVariable(name = "id") Integer id, Model model, @PathVariable(name = "pageNum") int pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<CustomerOrder> page = service.listMemberAndOrders(id, pageNum, sortField, sortDir, keyword);

		List<CustomerOrder> listCustomer = page.getContent();
		// List<Products> listProducts = ProductsRepository.findAll();

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listCustomer", listCustomer);

		return "view_customer_orders";
	}

}
