package com.eventmanagement.testutils;

import java.util.ArrayList;
import java.util.List;

import com.eventmanagement.dto.EventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MasterData {

	public static EventDTO getEventDto() {
		EventDTO eventDTO = new EventDTO();
		eventDTO.setId(1L);
		eventDTO.setName("David");
		eventDTO.setDescription("Event By David");
		eventDTO.setStatus("ACTIVE");
		return eventDTO;
	}

	public static List<EventDTO> getEventDtoList() {
		List<EventDTO> eventDTOS = new ArrayList<>();
		EventDTO eventDTO = new EventDTO();
		eventDTO.setId(1L);
		eventDTO.setName("David");
		eventDTO.setDescription("Event By David");
		eventDTO.setStatus("ACTIVE");
		eventDTOS.add(eventDTO);

		eventDTO = new EventDTO();
		eventDTO.setId(2L);
		eventDTO.setName("Sam");
		eventDTO.setDescription("Event By Sam");
		eventDTO.setStatus("DISABLE");
		eventDTOS.add(eventDTO);

		return eventDTOS;
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			final String jsonContent = mapper.writeValueAsString(obj);

			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
