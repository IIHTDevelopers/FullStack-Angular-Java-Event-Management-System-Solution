package com.eventmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.exception.EventNotFoundException;
import com.eventmanagement.repo.EventDAO;
import com.eventmanagement.service.EventService;

@Service
public class EventServiceImpl implements EventService {
	private final EventDAO eventDAO;
	private final ModelMapper modelMapper;

	@Autowired
	public EventServiceImpl(EventDAO eventDAO, ModelMapper modelMapper) {
		this.eventDAO = eventDAO;
		this.modelMapper = modelMapper;
	}

	@Override
	public EventDTO createEvent(EventDTO eventDTO) {
		Event event = modelMapper.map(eventDTO, Event.class);
		Event savedEvent = eventDAO.save(event);
		return modelMapper.map(savedEvent, EventDTO.class);
	}

	@Override
	public EventDTO getEventById(Long id) {
		Optional<Event> optionalEvent = eventDAO.findById(id);
		if (optionalEvent.isPresent()) {
			Event event = optionalEvent.get();
			return modelMapper.map(event, EventDTO.class);
		}
		throw new EventNotFoundException(String.format("Event with Id - %s not Found!", id));
	}

	@Override
	public List<EventDTO> getAllEvents() {
		List<Event> events = eventDAO.findAll();
		return events.stream().map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> searchEventsByName(String name) {
		List<Event> events = eventDAO.findByName(name);
		return events.stream().map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> searchEventsByStatus(String status) {
		List<Event> events = eventDAO.findByStatus(status);
		return events.stream().map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());
	}

	@Override
	public EventDTO updateEvent(Long id, EventDTO eventDTO) {
		Optional<Event> optionalEvent = eventDAO.findById(id);
		if (optionalEvent.isPresent()) {
			Event event = optionalEvent.get();
			event.setName(eventDTO.getName());
			event.setDescription(eventDTO.getDescription());
			event.setStatus(eventDTO.getStatus());
			Event updatedEvent = eventDAO.save(event);
			return modelMapper.map(updatedEvent, EventDTO.class);
		}
		throw new EventNotFoundException(String.format("Event with Id - %s not Found!", id));
	}

	@Override
	public boolean deleteEvent(Long id) {
		if (!eventDAO.findById(id).isPresent()) {
			throw new EventNotFoundException(String.format("Event with Id - %s not Found!", id));
		}
		eventDAO.deleteById(id);
		return true;
	}
}
