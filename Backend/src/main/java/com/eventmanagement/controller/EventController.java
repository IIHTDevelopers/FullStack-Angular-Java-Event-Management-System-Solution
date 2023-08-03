package com.eventmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.service.EventService;

@CrossOrigin
@RestController
@RequestMapping("/events")
public class EventController {
	private final EventService eventService;

	@Autowired
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PostMapping
	public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
		EventDTO createdEvent = eventService.createEvent(eventDTO);
		return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
		EventDTO eventDTO = eventService.getEventById(id);
		if (eventDTO != null) {
			return new ResponseEntity<>(eventDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping
	public ResponseEntity<List<EventDTO>> getAllEvents() {
		List<EventDTO> events = eventService.getAllEvents();
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/searchByName")
	public ResponseEntity<List<EventDTO>> searchEventsByName(@RequestParam String name) {
		List<EventDTO> events = eventService.searchEventsByName(name);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/searchByStatus")
	public ResponseEntity<List<EventDTO>> searchEventsByStatus(@RequestParam String status) {
		List<EventDTO> events = eventService.searchEventsByStatus(status);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
		EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
		if (updatedEvent != null) {
			return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
		eventService.deleteEvent(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
