package com.ivandev.registrocapturas.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ivandev.registrocapturas.dto.CaptureFilterDTO;
import com.ivandev.registrocapturas.model.Capture;

import jakarta.persistence.criteria.Predicate;

/**
 * Class to create criteria filters
 */
public class CaptureSpecification {

	/**
	 * Provides 
	 * 
	 * @param filter
	 * @return
	 */
	public static Specification<Capture> buildCaptureFilter(CaptureFilterDTO filter){
		return (root, query, criteriaBuilder) -> {
		List<Predicate> predicates = new ArrayList<>();
		if (filter.getName() != null && !filter.getName().isBlank()) {
			predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
		}
		if(filter.getLocation() != null && !filter.getLocation().isBlank()) {
			predicates.add(criteriaBuilder.equal(root.get("location"), filter.getLocation()));
		}
		if (filter.getDate() != null) {
			predicates.add(criteriaBuilder.equal(root.get("date"), filter.getDate()));
		}
		if(filter.getQuantity() != null) {
			predicates.add(criteriaBuilder.equal(root.get("quantity"), filter.getQuantity()));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		
	}
}
