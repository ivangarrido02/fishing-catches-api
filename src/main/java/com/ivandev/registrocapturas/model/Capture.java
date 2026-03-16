package com.ivandev.registrocapturas.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain entity representing a fishing capture.
 * 
 * This entity is persisted in "captures" table and contains information about
 * each recorded catch.
 */
@Entity
@Table(name = "captures")
@Getter
@Setter
@NoArgsConstructor
public class Capture {

	/** Unique identifier for the capture. Generated automatically. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Name of the capture, cannot be null. */
	@Column(nullable = false)
	private String name;

	/** Total weight of the captures, cannot be null. */
	@Column(nullable = false)
	private Double weight;

	/** Location where the capture was caught, cannot be null */
	@Column(nullable = false)
	private String location;

	/** Local date where the capture was catch, cannot be null */
	@Column(nullable = false)
	private LocalDate date;

	/** Quantity of same type captures. */
	@Column(nullable = false)
	private Integer quantity;

	public Capture(String name, Double weight, String location, LocalDate date, Integer quantity) {
		this.name = name;
		this.weight = weight;
		this.location = location;
		this.date = date;
		this.quantity = quantity;
	}
}
