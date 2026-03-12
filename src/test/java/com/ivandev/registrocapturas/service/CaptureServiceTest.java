package com.ivandev.registrocapturas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ivandev.registrocapturas.dto.CapturePatchDTO;
import com.ivandev.registrocapturas.dto.CaptureRequestDTO;
import com.ivandev.registrocapturas.dto.CaptureResponseDTO;
import com.ivandev.registrocapturas.exception.CaptureNotFoundException;
import com.ivandev.registrocapturas.mapper.CaptureMapper;
import com.ivandev.registrocapturas.model.Capture;
import com.ivandev.registrocapturas.repository.CaptureRepository;

@ExtendWith(MockitoExtension.class)
public class CaptureServiceTest {

	@InjectMocks
	private CaptureService service;

	@Mock
	private CaptureRepository repository;
	
	@Mock
	private CaptureMapper mapper;
	
	
	private Capture createCapture() {	
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
	
	private CaptureRequestDTO createRequest() {
		return new CaptureRequestDTO(
				"Sargo", 
				1.2, 
				"El puertito", 
				LocalDate.of(2024, 1, 1), 
				2
		);
	}
		
		private CaptureResponseDTO createValidResponse() {
			return new CaptureResponseDTO(
					1L,
					"Sargo", 
					1.2, 
					"El puertito", 
					LocalDate.of(2024, 1, 1), 
					2
			);
		}

	@Test
	void getCaptureById_existingId_returnsCapture() {

		// Arrange
		Capture capture = createCapture();

		CaptureResponseDTO response = createValidResponse();

		when(repository.findById(1L)).thenReturn(Optional.of(capture));
		when(mapper.toDTO(capture)).thenReturn(response);

		// Act
		CaptureResponseDTO result = service.getCaptureById(1L);

		// Assert
		assertNotNull(result);
		assertEquals("Sargo", result.getName());

		verify(repository).findById(1L);
		verify(mapper).toDTO(capture);
	}

	@Test
	void getCaptureById_nonExistingId_throwsException() {
		
		//Arrange
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		//Act + assert
		assertThrows(CaptureNotFoundException.class, ()-> service.getCaptureById(1L));
		
		verify(repository).findById(1L);
	}

	@Test
	void createCapture_validRequest_returnsCapture() {

	    // Arrange
	    CaptureRequestDTO request = createRequest();
	    Capture entity = createCapture();
	    Capture savedCapture = createCapture();

	    CaptureResponseDTO response = createValidResponse();

	    when(mapper.toEntity(request)).thenReturn(entity);
	    when(repository.save(entity)).thenReturn(savedCapture);
	    when(mapper.toDTO(savedCapture)).thenReturn(response);

	    // Act
	    CaptureResponseDTO result = service.createCapture(request);

	    // Assert
	    assertNotNull(result);
	    assertEquals("Sargo", result.getName());

	    verify(mapper).toEntity(request);
	    verify(repository).save(entity);
	    verify(mapper).toDTO(savedCapture);
	}
	
	@Test
	void updateCaptureById_existingId_updateAllFields() {

	    // Arrange
	    Capture savedCapture = createCapture();
	    CaptureRequestDTO request = createRequest();

	    CaptureResponseDTO response = new CaptureResponseDTO(
	            1L,
	            request.getName(),
	            request.getWeight(),
	            request.getLocation(),
	            request.getDate(),
	            request.getQuantity()
	    );

	    when(repository.findById(1L)).thenReturn(Optional.of(savedCapture));
	    when(mapper.toDTO(savedCapture)).thenReturn(response);

	    // Act
	    CaptureResponseDTO result = service.updateById(1L, request);

	    // Assert
	    assertNotNull(result);
	    assertEquals(request.getName(), result.getName());

	    verify(repository).findById(1L);
	    verify(mapper).updateFromRequestDTO(request, savedCapture);
	    verify(mapper).toDTO(savedCapture);
	}
	
	@Test
	void updateCaptureById_nonExistingId_throwsException() {
	    // Arrange
	    CaptureRequestDTO request = createRequest();
	    when(repository.findById(1L)).thenReturn(Optional.empty());

	    // Act & Assert
	    assertThrows(CaptureNotFoundException.class,
	                 () -> service.updateById(1L, request));
	    verify(repository).findById(1L);
	}
	
	@Test
	void deleteCaptureById_existingId_deleteSuccessfully() {

	    // Arrange
	    Capture savedCapture = createCapture();

	    when(repository.findById(1L)).thenReturn(Optional.of(savedCapture));

	    // Act
	    service.deleteById(1L);

	    // Assert
	    verify(repository).findById(1L);
	    verify(repository).delete(savedCapture);
	}
	
	@Test
	void deleteCaptureById_nonExistingId_throwsException() {
		// Arrange
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		// Act + Assert
		assertThrows(CaptureNotFoundException.class, 
				() -> service.deleteById(1L));
		
		verify(repository).findById(1L);
		verify(repository,never()).delete(any(Capture.class));
	}
	
	@Test
	void partialUpdateCapture_existingId_returnsCapture() {

	    // Arrange
	    Capture savedCapture = createCapture();

	    CapturePatchDTO request = new CapturePatchDTO();
	    request.setQuantity(5);

	    CaptureResponseDTO response = new CaptureResponseDTO(
	            1L,
	            savedCapture.getName(),
	            savedCapture.getWeight(),
	            savedCapture.getLocation(),
	            savedCapture.getDate(),
	            5
	    );

	    when(repository.findById(1L)).thenReturn(Optional.of(savedCapture));
	    when(mapper.toDTO(savedCapture)).thenReturn(response);

	    // Act
	    CaptureResponseDTO result = service.partialUpdateById(1L, request);

	    // Assert
	    assertNotNull(result);
	    assertEquals(5, result.getQuantity());

	    verify(repository).findById(1L);
	    verify(mapper).updateFromPatchDTO(request, savedCapture);
	    verify(mapper).toDTO(savedCapture);
	}
	
	@Test
	void partialUpdateCapture_nonExistingId_returns404(){
		// Arrange
		CapturePatchDTO request = new CapturePatchDTO();
		request.setQuantity(5);
		
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		// Act + Assert
		assertThrows(CaptureNotFoundException.class, 
				()-> service.partialUpdateById(1L, request));
	}
}
