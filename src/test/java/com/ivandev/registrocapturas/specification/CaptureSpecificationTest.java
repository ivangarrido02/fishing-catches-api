package com.ivandev.registrocapturas.specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ivandev.registrocapturas.dto.CaptureFilterDTO;
import com.ivandev.registrocapturas.model.Capture;
import com.ivandev.registrocapturas.repository.CaptureRepository;

import jakarta.transaction.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class CaptureSpecificationTest {

	@Autowired
	private CaptureRepository repository;
	
	private Capture c1;
	private Capture c2;
	private Capture c3;
	
	@BeforeEach
	void setUp() {
		repository.deleteAll();
		
		c1 = repository.save(new Capture("Sargo", 1.2, "El puertito", LocalDate.of(2024, 1, 1), 2));
		c2 = repository.save(new Capture("Bacalao", 5.0, "La playa", LocalDate.of(2024, 1, 2), 1));
	    c3 = repository.save(new Capture("Sargo", 2.5, "La playa", LocalDate.of(2024, 1, 3), 4));
	}
	
	@Test
	void findCaptureByName_returnsMatchingCaptures() {
		
		// Arrange
		CaptureFilterDTO filter = new CaptureFilterDTO();
		filter.setName("Sargo");
		
		// Act
		List<Capture> result = repository.findAll(CaptureSpecification.buildCaptureFilter(filter));
		
		// Assert
		assertEquals(2, result.size());
		assertTrue(result.contains(c1));
		assertTrue(result.contains(c3));
	}
	
	@Test
	void findCaptureByNameLocationAndQuantity_returnsMatchingCaptures() {
		
		// Arrange
		CaptureFilterDTO filter = new CaptureFilterDTO();
		filter.setName("Sargo");
		filter.setLocation("El puertito");
		filter.setQuantity(2);
		
		// Act
		List<Capture> result = repository.findAll(CaptureSpecification.buildCaptureFilter(filter));
		
		// Assert
		assertEquals(1, result.size());
		assertTrue(result.contains(c1));
	}
}
