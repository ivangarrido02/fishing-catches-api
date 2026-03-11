package com.ivandev.registrocapturas.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used for PATCH requests to partially update a capture.
 * 
 */
@Setter
@Getter
@NoArgsConstructor
public class CapturePatchDTO {
	private String name;
	
	@DecimalMin(value = "0.0", inclusive = false)
	private Double weight;
	
	private String location;
	
	@PastOrPresent
	private LocalDate date;
	
	@Min(1)
	private Integer quantity;

}
