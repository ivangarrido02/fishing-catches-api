package com.ivandev.registrocapturas.dto;

import java.time.LocalDate;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO containing data required to create a capture. Used by
 * {@link CaptureController} to receive capture data from the client.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaptureRequestDTO {
	/**Name of the capture, cannot be blank.*/
	@NotBlank
	private String name;

	/**Total weight of the capture/s in kilograms, cannot be null and must be greater than 0.0.*/
	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	private Double weight;

	/**Location where the capture was catch, cannot be blank.*/
	@NotBlank
	private String location;

	/**Date of the capture. Cannot be null, and must be in the past or present.*/
	@NotNull
	@PastOrPresent
	private LocalDate date;

	/**Total quantity of captures of same species, must be at least 1.*/
	@NotNull
	@Min(1)
	private Integer quantity;

}
