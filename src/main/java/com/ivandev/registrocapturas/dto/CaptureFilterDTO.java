package com.ivandev.registrocapturas.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/**
 * DTO used to pass filtering criteria when querying captures.
 * 
 * Used by {@link CaptureSpecification} to build dynamic filters.
 */
public class CaptureFilterDTO {
	private String name;
	private String location;
	@PastOrPresent
	private LocalDate date;
	@Positive
	private Integer quantity;
}
