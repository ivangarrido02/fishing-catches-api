package com.ivandev.registrocapturas.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * DTO representing capture data returned by the API.
 */

@Getter
@AllArgsConstructor
public class CaptureResponseDTO {
	private final Long id;
	private final String name;
	private final Double weight;
	private final String location;
	private final LocalDate date;
	private final Integer quantity;

}
