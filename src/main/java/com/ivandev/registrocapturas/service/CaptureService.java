package com.ivandev.registrocapturas.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivandev.registrocapturas.dto.CaptureFilterDTO;
import com.ivandev.registrocapturas.dto.CapturePatchDTO;
import com.ivandev.registrocapturas.dto.CaptureRequestDTO;
import com.ivandev.registrocapturas.dto.CaptureResponseDTO;
import com.ivandev.registrocapturas.exception.CaptureNotFoundException;
import com.ivandev.registrocapturas.mapper.CaptureMapper;
import com.ivandev.registrocapturas.model.Capture;
import com.ivandev.registrocapturas.repository.CaptureRepository;
import com.ivandev.registrocapturas.specification.CaptureSpecification;

import lombok.extern.slf4j.Slf4j;

/**
 * Service layer that provides business logic for managing fishing captures. 
 * 
 * Receives request from {@link Controller} and coordinates persistence operations
 * through {@link CaptureRepository}.
 * Handles the main CRUD operations for {@link Capture} entities. 
 */

@Slf4j
@Service
public class CaptureService {
	private final CaptureRepository repository;
	private final CaptureMapper mapper;

	public CaptureService(CaptureRepository repository, CaptureMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Retrieves a paginated list of captures applying optional filters. 
	 * 
	 * @param pageable pagination and sorting information. 
	 * @param filter filtering criteria used no narrow the results.  
	 * @return a page of {@link CaptureResponseDTO} matching the given filters. 
	 */
	@Transactional(readOnly = true)
	public Page<CaptureResponseDTO> getAll(Pageable pageable, CaptureFilterDTO filter) {
		
		Specification<Capture> spec = CaptureSpecification.buildCaptureFilter(filter);
		Page<Capture> captures = repository.findAll(spec, pageable);
		return captures.map(capture -> mapper.toDTO(capture));

	}

	/**
	 * Retrieves a capture by its id. 
	 * 
	 * @param id the unique identifier of the capture. 
	 * @return a {@link CaptureResponseDTO} containing the capture data. 
	 */
	@Transactional(readOnly = true)
	public CaptureResponseDTO getCaptureById(Long id) {
		log.debug("Recovering capture id = {} ", id);
		Capture capture = getOrThrow(id);
		return mapper.toDTO(capture);
	}

	/**
	 * Creates a new {@link Capture} in the database using the data provided by the client. 
	 * 
	 * @param dto a {@link CaptureRequestDTO} containing the capture data. 
	 * @return the {@link CaptureResponseDTO} representing the created capture. 
	 */
	@Transactional
	public CaptureResponseDTO createCapture(CaptureRequestDTO dto) {
		Capture capture = mapper.toEntity(dto);
		Capture saved = repository.save(capture);
		
		return mapper.toDTO(saved);
	}

	/**
	 * Deletes a {@link Capture} by its id.
	 * 
	 * @param id the unique identifier of the capture to delete.
	 */
	@Transactional
	public void deleteById(Long id) {
		Capture capture = getOrThrow(id);
		log.info("Deleting capture id = {}", 
				id);
		repository.delete(capture);
		log.info("Capture id = {}, deleted successfully", 
				id);
	}

	/**
	 * Updates totally an existing {@link Capture} with the data provided by the client. 
	 * 
	 * @param id the unique identifier of the capture to update. 
	 * @param dto a {@link CaptureRequestDTO} containing the new capture data. 
	 * @return the {@link CaptureResponseDTO} representing the updated capture. 
	 */
	@Transactional
	public CaptureResponseDTO updateById(Long id, CaptureRequestDTO dto) {
		log.info("Updating capture id = {}, with new values name = {}, quantity = {} and date = {}", id,
				dto.getName(), dto.getQuantity(), dto.getDate());
		
		Capture capture = getOrThrow(id);
		mapper.updateFromRequestDTO(dto, capture);

		log.info("Capture updated successfully with id = {}", capture.getId());
		
		return mapper.toDTO(capture);
	}

	/**
	 * Updates partially an existing {@link Capture} with the data provided by the client. 
	 * Only the field presents in the {@link CapturePatchDTO} will be updated.
	 * 
	 * @param id the unique identifier of the capture to update. 
	 * @param dto the {@link CapturePatchDTO} containing optional capture data. 
	 * @return a {@link CaptureResponseDTO} representing the updated capture. 
	 */
	@Transactional
	public CaptureResponseDTO partialUpdateById(Long id, CapturePatchDTO dto) {
		Capture capture = getOrThrow(id);
		mapper.updateFromPatchDTO(dto, capture);
		
		return mapper.toDTO(capture);
	}

	/**
	 * Retrieves a {@link Capture} by its identifier.
	 * 
	 * @param id the unique identifier of the capture.
	 * @return the {@link Capture} entity. 
	 * @throws {@link CaptureNotFoundException} if no capture exists with the given id. 
	 */
	@Transactional(readOnly = true)
	private Capture getOrThrow(Long id) {
		return repository.findById(id).orElseThrow(() -> new CaptureNotFoundException(id));
	}
}

