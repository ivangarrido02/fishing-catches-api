package com.ivandev.registrocapturas.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivandev.registrocapturas.dto.CapturePatchDTO;
import com.ivandev.registrocapturas.dto.CaptureRequestDTO;
import com.ivandev.registrocapturas.dto.CaptureResponseDTO;
import com.ivandev.registrocapturas.exception.CaptureNotFoundException;
import com.ivandev.registrocapturas.exception.ErrorHandler;
import com.ivandev.registrocapturas.service.CaptureService;

@WebMvcTest(CaptureController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ErrorHandler.class)
public class CaptureControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CaptureService service;

	@Autowired
	private ObjectMapper objectMapper;
	
	private static final LocalDate DEFAULT_DATE = LocalDate.of(2024, 1, 1);

	private CaptureResponseDTO validCaptureResponse() {
		return new CaptureResponseDTO(
				1L, 
				"Sargo", 
				1.2, 
				"El puertito", 
				DEFAULT_DATE, 
				2
		);
	}
	
	private CaptureRequestDTO validCaptureRequest() {
		return new CaptureRequestDTO(
				"Sargo", 
				1.2, 
				"El puertito", 
				DEFAULT_DATE, 
				2
		);
	}
	
	private CaptureRequestDTO invalidCaptureRequest() {
		return new CaptureRequestDTO(
				"Sargo", 
				1.2, 
				"El puertito", 
				DEFAULT_DATE, 
				0
		);
	}
	
	@Test
	void getCaptureById_existingId_returnsCapture() throws Exception {
		
		// Arrange
		CaptureResponseDTO response = validCaptureResponse();
		
		when(service.getCaptureById(1L)).thenReturn(response);
		
		// Act + Assert
		   mockMvc.perform(get("/api/v1/captures/1"))
           .andExpect(status().isOk())
           .andExpect(content().json(objectMapper.writeValueAsString(response)));
	}
	
	@Test
	void getCaptureById_nonExistingId_returns404() throws Exception{
		
		// Arrange
		when(service.getCaptureById(1L)).thenThrow(new CaptureNotFoundException(1L));
		
		// Act + Assert
		mockMvc.perform(get("/api/v1/captures/1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void createCapture_validRequest_createsCapture() throws Exception {
		
		// Arrange
		CaptureRequestDTO request = validCaptureRequest();
		CaptureResponseDTO response = validCaptureResponse();
		
		when(service.createCapture(any(CaptureRequestDTO.class))).thenReturn(response);
		
		// Act + Assert
		mockMvc.perform(post("/api/v1/captures")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isCreated())
		.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}
	@Test
	void createCapture_invalidRequest_returns400() throws Exception {
		
		// Arrange
		CaptureRequestDTO request = invalidCaptureRequest();
		
		// Act + Assert
		mockMvc.perform(post("/api/v1/captures")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCapture_validRequest_returnsCapture() throws Exception{
		
		// Arrange
		CaptureRequestDTO request = validCaptureRequest();
		CaptureResponseDTO response = validCaptureResponse();
		
		when(service.updateById(eq(1L), any(CaptureRequestDTO.class)))
				.thenReturn(response);
		
		// Act + Assert
		mockMvc.perform(put("/api/v1/captures/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isOk())
		.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(service).updateById(eq(1L), any(CaptureRequestDTO.class));
	}
	
	@Test
	void updateCapture_nonExistingId_returns404() throws Exception {
		// Arrange
		CaptureRequestDTO request = validCaptureRequest();
		
		doThrow(new CaptureNotFoundException(1L))
				.when(service).updateById(eq(1L), any(CaptureRequestDTO.class));
		
		// Act + Assert
		mockMvc.perform(put("/api/v1/captures/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void updateCapture_invalidRequest_returns400() throws Exception {
		// Arrange
		CaptureRequestDTO request = invalidCaptureRequest();
		
		// Act + Assert
		mockMvc.perform(put("/api/v1/captures/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).updateById(anyLong(), any(CaptureRequestDTO.class));
		
	}
	
	@Test
	void partialUpdateCapture_existingId_returnsCapture() throws Exception {
		// Arrange
		CapturePatchDTO request = new CapturePatchDTO();
		request.setWeight(5.0);
		request.setQuantity(3);
		
		CaptureResponseDTO response = new CaptureResponseDTO(
				1L, 
				"Sargo", 
				5.0, 
				"El puertito", 
				DEFAULT_DATE, 
				3
		);
		
		when(service.partialUpdateById(eq(1L), any(CapturePatchDTO.class)))
				.thenReturn(response);
		
		// Act + Assert
		  mockMvc.perform(patch("/api/v1/captures/1")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(request)))
		 .andExpect(status().isOk())
		 .andExpect(content().json(objectMapper.writeValueAsString(response)));
		  
		  verify(service).partialUpdateById(eq(1L), any(CapturePatchDTO.class));
	}
	
	@Test
	void partialUpdateCapture_nonExistingId_returns404() throws Exception{
		// Arrange
		CapturePatchDTO request = new CapturePatchDTO();
		request.setWeight(5.0);
		request.setQuantity(3);
		
		doThrow(new CaptureNotFoundException(1L))
		.when(service).partialUpdateById(eq(1L), any(CapturePatchDTO.class));
		
		// Act + Assert
		mockMvc.perform(patch("/api/v1/captures/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void partialUpdateCapture_invalidRequest_returns400() throws Exception{
		// Arrange
		CapturePatchDTO request = new CapturePatchDTO();
		request.setQuantity(0);
		
		// Act + Assert
		mockMvc.perform(patch("/api/v1/captures/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).partialUpdateById(anyLong(), any(CapturePatchDTO.class));		
	}
	
	
}
