package com.ivandev.registrocapturas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ivandev.registrocapturas.model.Capture;

/**
 * Repository interface for managing {@link Capture} entities.
 * 
 * Provides CRUD operations and database access for fishing captures.
 */
public interface CaptureRepository extends JpaRepository<Capture, Long>, JpaSpecificationExecutor<Capture> {
	
}