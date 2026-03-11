package com.ivandev.registrocapturas.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ivandev.registrocapturas.dto.CapturePatchDTO;
import com.ivandev.registrocapturas.dto.CaptureRequestDTO;
import com.ivandev.registrocapturas.dto.CaptureResponseDTO;
import com.ivandev.registrocapturas.model.Capture;

/**
 * Mapper interface used by the service to convert {@link Captures} entities 
 * in DTOs and vice versa. 
 */
@Mapper(componentModel = "spring")
public interface CaptureMapper {
	
	/**
	 * Converts {@link Capture} entities into a {@link CaptureResponseDTO}.
	 * 
	 * @param capture the capture entity.
	 * @return the corresponding CaptureResponseDTO 
	 */
	CaptureResponseDTO toDTO(Capture capture);
	
	/**
	 * Converts {@link CaptureRequestDTO} into a {@link Capture} entity. 
	 * 
	 * @param dto the DTO to convert.
	 * @return the corresponding Capture.
	 */
	Capture toEntity(CaptureRequestDTO dto);
	
	/**
	 * Updates an existing {@link Capture} with the values from a {@link CaptureRequestDTO}.
	 * 
	 * @param dto the DTO containing the updated capture data.   
	 * @param capture the existing capture to update.
	 */
	void updateFromRequestDTO (CaptureRequestDTO dto, @MappingTarget Capture capture);
	
	/**
	 * Partially updates an existing {@link Capture} with the values from a 
	 * {@link CapturePatchDTO}, ignoring null values. 
	 * 
	 * @param dto the DTO containing the fields and capture data.
	 * @param capture the existing capture to update. 
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateFromPatchDTO (CapturePatchDTO dto, @MappingTarget Capture capture);

}

