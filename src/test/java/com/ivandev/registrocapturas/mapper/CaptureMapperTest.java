package com.ivandev.registrocapturas.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ivandev.registrocapturas.dto.CapturePatchDTO;
import com.ivandev.registrocapturas.dto.CaptureRequestDTO;
import com.ivandev.registrocapturas.dto.CaptureResponseDTO;
import com.ivandev.registrocapturas.model.Capture;

public class CaptureMapperTest {

	private final CaptureMapper mapper = Mappers.getMapper(CaptureMapper.class);
	
	private Capture createValidCapture() {	
		Capture capture = new Capture(
				"Sargo", 
				1.2, 
				"El puertito", 
				LocalDate.of(2024, 1, 1), 
				2
		);
		capture.setId(1L);
		
		return capture;
	}
	
	private CaptureRequestDTO createValidRequest() {
		return new CaptureRequestDTO(
				"Peto", 
				10.0, 
				"Radazul", 
				LocalDate.of(2024, 2, 2), 
				1
		);
	}
	
	@Test
	void toResponseDTO_validEntity_returnsResponseDTO() {
		
		// Arrange
		Capture capture = createValidCapture();
		
		// Act
		CaptureResponseDTO response = mapper.toDTO(capture);
		
		// Assert
		assertEquals(response.getId(), capture.getId());
		assertEquals(response.getName(), capture.getName());
		assertEquals(response.getWeight(), capture.getWeight(), 0.0001);
		assertEquals(response.getLocation(), capture.getLocation());
		assertEquals(response.getDate(), capture.getDate());
		assertEquals(response.getQuantity(), capture.getQuantity());
	}
	
	@Test
	void toEntity_validResponseDTO_returnsEntity() {
		
		// Arrange
		CaptureRequestDTO request = createValidRequest();
		
		// Act
		Capture capture = mapper.toEntity(request);
		
		// Assert
		assertNull(capture.getId());
		assertEquals(request.getName(), capture.getName());
		assertEquals(request.getWeight(), capture.getWeight(), 0.0001);
		assertEquals(request.getLocation(), capture.getLocation());
		assertEquals(request.getDate(), capture.getDate());
		assertEquals(request.getQuantity(), capture.getQuantity());
	}
	
	@Test
	void updateFromRequestDTO_validRequest_updatesAllFields() {
		
		// Arrange
		Capture capture = createValidCapture();
		capture.setId(1L);
		CaptureRequestDTO request = createValidRequest();
		
		// Act
		mapper.updateFromRequestDTO(request, capture);
		
		// Assert
		assertEquals(request.getName(), capture.getName());
		assertEquals(request.getWeight(), capture.getWeight(), 0.0001);
		assertEquals(request.getLocation(), capture.getLocation());
		assertEquals(request.getDate(), capture.getDate());
		assertEquals(request.getQuantity(), capture.getQuantity());
	}
	
	@Test
	void updateFromPatchDTO_partialFields_updatesOnlyProvidedFields() {
		
		// Arrange
		Capture capture = createValidCapture();
		capture.setId(1L);
		
		CapturePatchDTO request = new CapturePatchDTO();
		request.setQuantity(5);
		request.setWeight(5.5);
		
		// Saves original values
		String oldName = capture.getName();
		String oldLocation = capture.getLocation();
		LocalDate oldDate = capture.getDate();
		
		// Act
		mapper.updateFromPatchDTO(request, capture);
		
		//Assert
		assertEquals(capture.getWeight(), request.getWeight(), 0.0001);
		assertEquals(capture.getQuantity(), request.getQuantity());

		assertEquals(capture.getName(), oldName);
		assertEquals(capture.getLocation(), oldLocation);
		assertEquals(capture.getDate(), oldDate);
	}
}
