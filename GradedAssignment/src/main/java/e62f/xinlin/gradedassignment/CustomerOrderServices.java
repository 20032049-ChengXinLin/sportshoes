/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Feb-13 5:00:03 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author 20032049
 *
 */
@Service
public class CustomerOrderServices {
	@Autowired
	private CustomerOrderRepository repo;

	public Page<CustomerOrder> listAll(int pageNum, String sortField, String sortDir, String keyword) {
		int pageSize = 3;
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
				sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}
	
	public Page<CustomerOrder> listMemberAndOrders(int memberId, int pageNum, String sortField, String sortDir, String keyword) {
		int pageSize = 3;
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
				sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		if (keyword != null) {
			return repo.findByOrderAndMemberId(memberId, keyword, pageable);
		}
		return repo.findByMemberId(memberId, pageable);
	}
	
	
	public Page<CustomerOrder> listHistory(int memberId, int pageNum, String sortField, String sortDir) {
		int pageSize = 3;
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
				sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

		return repo.findByMemberId(memberId, pageable);
	}
	
}
