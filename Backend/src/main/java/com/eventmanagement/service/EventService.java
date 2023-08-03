package com.eventmanagement.service;

import java.util.List;

import com.eventmanagement.dto.EventDTO;

public interface EventService {
	EventDTO createEvent(EventDTO eventDTO);

	EventDTO getEventById(Long id);

	List<EventDTO> getAllEvents();

	List<EventDTO> searchEventsByName(String name);

	List<EventDTO> searchEventsByStatus(String status);

	EventDTO updateEvent(Long id, EventDTO eventDTO);

	boolean deleteEvent(Long id);
}
