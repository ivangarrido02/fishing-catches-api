package com.ivandev.registrocapturas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ivandev.registrocapturas.dto.CaptureFilterDTO;
import com.ivandev.registrocapturas.dto.CapturePatchDTO;
import com.ivandev.registrocapturas.dto.CaptureRequestDTO;
import com.ivandev.registrocapturas.dto.CaptureResponseDTO;
import com.ivandev.registrocapturas.exception.CaptureNotFoundException;
import com.ivandev.registrocapturas.service.CaptureService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/** 
 * REST Controller responsible for managing fishing captures.
 * 
 * Provide endpoints to create, retrieve, update and delete captures.  
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/captures")
public class CaptureController {
	private final CaptureService service;

	public CaptureController(CaptureService service) {
		this.service = service;
	}

	/**
	 * Retrieves a paginated list of captures. 
	 * 
	 * Support optional filters through {@link CaptureFilterDTO}.
	 * 
	 * @param filter filtering criteria for captures.
	 * @param pageable pagination information.
	 * @return a page of CaptureResponseDTO
	 * 
	 */
	@GetMapping
	public Page<CaptureResponseDTO> getAll(Pageable pageable, CaptureFilterDTO filter) {
	    log.debug("event=get_all_captures status=received filter={} pageable={}",
	    		filter, pageable);
	    return service.getAll(pageable, filter);
	}

	/**
	 * Retrieves a capture by its identifier.
	 * 
	 * @param id capture identifier. 
	 * @return Capture data. 
	 * @throws CaptureNotFoundException if the capture does not exist. 
	 */
	@GetMapping("/{id}")
	public CaptureResponseDTO getCaptureById(@PathVariable Long id) {
		log.debug("event=get_capture status=received captureId={}", 
				id);
		return service.getCaptureById(id);
	}

	/**
	 * Creates a new capture.
	 * 
	 *  The RequestBody must contain valid capture data.
	 *  
	 *  @param dto capture data.
	 *  @return the created capture as CaptureResponseDTO.
	 *  @throws MethodArgumentNotValidException if validations fails.
	 */
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public CaptureResponseDTO createCapture(@Valid @RequestBody CaptureRequestDTO dto) {
		log.info("event=create_capture status=received name={} date={} quantity={}", 
				dto.getName(), dto.getDate(), dto.getQuantity());
		return service.createCapture(dto);
	}

	/**
	 * Deletes an existing capture.
	 * 
	 * @param id capture identifier.
	 * @throws CaptureNotFoundException if the capture does not exist.
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) {
		log.info("event=delete_capture status=received captureId={}",
				id);
		service.deleteById(id);
	}

	/**
	 * Fully updates an existing capture.
	 * 
	 * @param id capture identifier.
	 * @param dto updated capture data. 
	 * @return the updated capture.
	 * @throws CaptureNotFoundException if the capture does not exist (404). 
	 * @throws MethodArgumentInvalidException if validation fails.
	 */
	@PutMapping("/{id}")
	public CaptureResponseDTO updateById(@PathVariable Long id, @Valid @RequestBody CaptureRequestDTO dto) {
		log.info("event=update_capture status=received captureId={}", 
				id);
		return service.updateById(id, dto);
	}

	/**
	 * Partially updates an existing capture.
	 * 
	 * @param id capture identifier.
	 * @param dto fields to update. 
	 * @return the updated capture.
	 * @throws CaptureNotFoundException if the capture does not exist (404). 
	 * @throws MethodArgumentNotValidException if validation fails.
	 */
	@PatchMapping("/{id}")
	public CaptureResponseDTO partialUpdateById(@PathVariable Long id, @Valid @RequestBody CapturePatchDTO dto) {
		log.info("event=partial_update_capture status=received captureId={}",
				id);
		return service.partialUpdateById(id, dto);
	}
}
