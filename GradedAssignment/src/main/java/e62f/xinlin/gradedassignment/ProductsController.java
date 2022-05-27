/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2021-Nov-21 4:14:26 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 20032049
 *
 */
@Controller
public class ProductsController {
	@Autowired
	private ProductsRepository ProductsRepository;

	@Autowired
	private TypeRepository TypeRepository;

	@Autowired
	private BrandRepository BrandRepository;

	@Autowired
	private SizeRepository SizeRepository;

	@Autowired
	private ProductsService service;

	@GetMapping("/products/page/{pageNum}")
	public String viewPage(Model model, @PathVariable(name = "pageNum") int pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Products> page = service.listAll(pageNum, sortField, sortDir, keyword);

		List<Products> listProducts = page.getContent();

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listProducts", listProducts);

		return "view_products";
	}

	@RequestMapping("/products")
	public String viewCategories(Model model, @Param("keyword") String keyword) {

		return viewPage(model, 1, "id", "asc", keyword);
	}	
	@GetMapping("/products/add")
	public String addProducts(Model model) {
		// Javascript validation
		List<Products> listProducts = ProductsRepository.findAll();
		model.addAttribute("listProducts", listProducts);
		
		List<Type> typeList = TypeRepository.findAll();
		model.addAttribute("typeList", typeList);
		for (int i = 0; i < typeList.size(); i++) {
			System.out.println(typeList.get(i).getName());
		}
		List<Brand> brandList = BrandRepository.findAll();
		model.addAttribute("brandList", brandList);

		List<Size> sizeList = SizeRepository.findAll();
		model.addAttribute("sizeList", sizeList);
		
		model.addAttribute("products", new Products());	
		

		return "add_products";
	}

	@PostMapping("/products/save")
	public String saveProducts(@Valid Products products, BindingResult bindingResult,
			@RequestParam("itemImage") MultipartFile imgFile, Model model, RedirectAttributes redirectAttribute) {

		List<Products> listProduct = ProductsRepository.findAll();

		String name = "";
		String productCode = "";
		String file = "";

		for (int i = 0; i < listProduct.size(); i++) {
			if (products.getName().equalsIgnoreCase(listProduct.get(i).getName())) {
				name += "Product name cannot be duplicated. \n";
			}
			if (products.getProductCode().equalsIgnoreCase(listProduct.get(i).getProductCode())) {
				productCode += "Product Code must be unique. \n";
			}
		}

		if (bindingResult.hasErrors()) {
			List<Type> typeList = TypeRepository.findAll();
			model.addAttribute("typeList", typeList);

			List<Brand> brandList = BrandRepository.findAll();
			model.addAttribute("brandList", brandList);

			List<Size> sizeList = SizeRepository.findAll();
			model.addAttribute("sizeList", sizeList);
			return "add_products";
		}
		if (products.getBrand().getName().equals("Adidas")) {
			String size = "";
			String[] splitsize = products.getSize().split(",");
			for (int i = 0; i < splitsize.length; i++) {
				size += "UK " + splitsize[i] + ", ";

			}
			products.setSize(size.substring(0, size.length() - 1));
		}

		if (products.getBrand().getName().equals("Nike")) {
			String size = "";
			String[] splitsize = products.getSize().split(",");
			for (int i = 0; i < splitsize.length; i++) {
				size += "US " + splitsize[i] + ", ";

			}
			products.setSize(size.substring(0, size.length() - 1));
		}
		if (imgFile.isEmpty()) {
			file += "Please select a file to upload";
		}
		// first save the path of the file in the database
		String image = imgFile.getOriginalFilename();
		String pattern = "\\w+\\.(png|jpeg)";
		boolean isvalid = Pattern.matches(pattern, image);

		if (!isvalid) {
			file += "Only images of type JPEG or PNG are supported.";
		}

		// validation message
		if (!name.equals("") | !productCode.equals("") | !file.equals("")) {
			redirectAttribute.addFlashAttribute("name", name);
			redirectAttribute.addFlashAttribute("productCode", productCode);
			redirectAttribute.addFlashAttribute("file", file);
			redirectAttribute.addFlashAttribute("alertClass", "alert-danger");
			return "redirect:/products/add";
		}

		// Set the image name in the item object
		products.setImage(image);
		// Save the item obj to the db
		Products savedItem = ProductsRepository.save(products);

		// Uploading of the image
		try {
			// Preparing the directory path
			// Try the file operations if it fails to throw an exception
			// Setting the directory path
			String uploadDir = "uploads/products/" + savedItem.getId();
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory path: " + uploadPath);

			// Checking if the upload path exists, if not it will be created.
			// if the path does not exist create the path
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Preparing path for file
			// add the image name to the path
			Path fileToCreatePath = uploadPath.resolve(image);
			System.out.println("File path: " + fileToCreatePath);

			// Copy file to the upload location
			// Copy the file from the source to destination with option
			// to replace existing file if any
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException io) {
			io.printStackTrace();
		}

		// Adding danger alert message to show add failed
		redirectAttribute.addFlashAttribute("message", "Products " + products.getId() + "added failed");
		redirectAttribute.addFlashAttribute("alertClass", "alert-danger");

		// Adding success alert message to show add success
		redirectAttribute.addFlashAttribute("message", "Products " + products.getId() + " Successfully Added");
		redirectAttribute.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model) {

		List<Type> typeList = TypeRepository.findAll();
		model.addAttribute("typeList", typeList);

		List<Brand> brandList = BrandRepository.findAll();
		model.addAttribute("brandList", brandList);

		Products products = ProductsRepository.getById(id);
		model.addAttribute("products", products);

		List<Size> sizeList = SizeRepository.findAll();
		model.addAttribute("sizeList", sizeList);

		return "edit_products";
	}

	@PostMapping("/products/edit/{id}")
	public String saveUpdatedProduct(@Valid Products products, BindingResult bindingResult,
			@RequestParam("itemImage") MultipartFile imgFile, Model model, RedirectAttributes redirectAttribute) {

		if (bindingResult.hasErrors()) {
			List<Type> typeList = TypeRepository.findAll();
			model.addAttribute("typeList", typeList);

			List<Brand> brandList = BrandRepository.findAll();
			model.addAttribute("brandList", brandList);

			List<Size> sizeList = SizeRepository.findAll();
			model.addAttribute("sizeList", sizeList);
			return "edit_products";
		}
		if (products.getBrand().getName().equals("Adidas")) {
			String size = "";
			String[] splitsize = products.getSize().split(",");
			for (int i = 0; i < splitsize.length; i++) {
				size += "UK " + splitsize[i] + ", ";

			}
			products.setSize(size.substring(0, size.length() - 1));
		}

		if (products.getBrand().getName().equals("Nike")) {
			String size = "";
			String[] splitsize = products.getSize().split(",");
			for (int i = 0; i < splitsize.length; i++) {
				size += "US " + splitsize[i] + ", ";

			}
			products.setSize(size.substring(0, size.length() - 1));
		}

		// Total Products Sold
		List<Products> productlists = ProductsRepository.findAll();
		for (int i = 0; i < productlists.size(); i++) {
			if(productlists.get(i).getId() == products.getId()) {
				int quantity = productlists.get(i).getTotalProductsSold();
				products.setTotalProductsSold(quantity);
			}
		}
		
		// first save the path of the file in the database String image =
		// imgFile.getOriginalFilename();

		// Set the image name in the item object products.setImage(image);
		// Save the item obj to the db Products savedItem =

		// if imgFile is NOT empty, proceed to:
		// set new image name for item object, save the item object, upload new file and
		// update db
		// otherwise, this means no new file was uploaded, simply save the item object

		if (!imgFile.isEmpty()) {
			String imageName = imgFile.getOriginalFilename();
			System.out.println("Image name from imgFile: " + imageName);
			
			// Validation
			String image = imgFile.getOriginalFilename();
			String pattern = "\\w+\\.(png|jpeg)";
			boolean isvalid = Pattern.matches(pattern, image);
			String file = "";
			if (!isvalid) {
				file += "Only images of type JPEG or PNG are supported.";
			}

			if (!file.equals("")) {
				redirectAttribute.addFlashAttribute("file", file);
				redirectAttribute.addFlashAttribute("alertClass", "alert-danger");
				return "redirect:/products/edit/{id}";
			}
			// Set the image name in item object
			products.setImage(imageName);

			Products savedProducts = ProductsRepository.save(products);
			// Uploading of the image
			try {
				// Preparing the directory path
				// Try the file operations if it fails to throw an exception
				// Setting the directory path
				String uploadDir = "uploads/products/" + savedProducts.getId();
				Path uploadPath = Paths.get(uploadDir);
				System.out.println("Directory path: " + uploadPath);

				// Checking if the upload path exists, if not it will be created.
				// if the path does not exist create the path
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				// Preparing path for file
				// add the image name to the path
				Path fileToCreatePath = uploadPath.resolve(imageName);
				System.out.println("File path: " + fileToCreatePath);

				// Copy file to the upload location
				// Copy the file from the source to destination with option
				// to replace existing file if any
				Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException io) {
				io.printStackTrace();
			}
		} else { // No edit to image, save the item object as passed.
			System.out.println("Image name from item object: " + products.getImage());
			ProductsRepository.save(products);
		}

		// Adding danger alert message to show edit failed
		redirectAttribute.addFlashAttribute("message", "Products " + products.getId() + "update failed");
		redirectAttribute.addFlashAttribute("alertClass", "alert-danger");
		// Adding success alert message to show edit success
		redirectAttribute.addFlashAttribute("message", "Products " + products.getId() + " Successfully Updated");
		redirectAttribute.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProducts(@PathVariable("id") Integer id, Products products, BindingResult result,
			RedirectAttributes redirectAttributes) {

		ProductsRepository.deleteById(id);

		// Adding danger alert message to show delete failed
		redirectAttributes.addFlashAttribute("message", "Product ID " + products.getId() + " Deleted failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		if (result.hasErrors()) {
			return "redirect:/products";
		}
		// Adding success alert message to show delete success
		redirectAttributes.addFlashAttribute("message", "Product ID " + products.getId() + " Successfully Deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/products";
	}

}
