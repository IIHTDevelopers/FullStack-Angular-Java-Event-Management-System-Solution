package com.eventmanagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventmanagement.entity.Event;

public interface EventDAO extends JpaRepository<Event, Long> {
	// Add custom query methods for search operations
	List<Event> findByName(String name);

	List<Event> findByStatus(String status);
}
