package com.eventmanagement.exception;

import static com.eventmanagement.testutils.TestUtils.currentTest;
import static com.eventmanagement.testutils.TestUtils.exceptionTestFile;
import static com.eventmanagement.testutils.TestUtils.testReport;
import static com.eventmanagement.testutils.TestUtils.yakshaAssert;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.eventmanagement.controller.EventController;
import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.exception.EventNotFoundException;
import com.eventmanagement.model.exception.ExceptionResponse;
import com.eventmanagement.service.EventService;
import com.eventmanagement.testutils.MasterData;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
public class EventExceptionTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventService eventService;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateEventInvalidDataException() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		EventDTO savedEventDTO = MasterData.getEventDto();

		savedEventDTO.setId(1L);
		eventDTO.setName("Ab");

		when(this.eventService.createEvent(eventDTO)).thenReturn(savedEventDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/events")
				.content(MasterData.asJsonString(eventDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value() ? "true" : "false"),
				exceptionTestFile);

	}

	@Test
	public void testUpdateEventInvalidDataException() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		EventDTO savedEventDTO = MasterData.getEventDto();

		savedEventDTO.setId(1L);
		eventDTO.setName("Ab");

		when(this.eventService.updateEvent(eventDTO.getId(), eventDTO)).thenReturn(savedEventDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/events/" + eventDTO.getId())
				.content(MasterData.asJsonString(eventDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value() ? "true" : "false"),
				exceptionTestFile);

	}

	@Test
	public void testGetEventByIdEventNotFoundException() throws Exception {
		ExceptionResponse exResponse = new ExceptionResponse("Event with Id - 1 not Found!", System.currentTimeMillis(),
				HttpStatus.NOT_FOUND.value());

		when(this.eventService.getEventById(1L)).thenThrow(new EventNotFoundException("Event with Id - 1 not Found!"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contains(exResponse.getMessage()) ? "true" : "false"),
				exceptionTestFile);

	}

	@Test
	public void testDeleteEventByIdEventNotFoundException() throws Exception {
		ExceptionResponse exResponse = new ExceptionResponse("Event with Id - 1 not Found!", System.currentTimeMillis(),
				HttpStatus.NOT_FOUND.value());
		when(this.eventService.deleteEvent(1L)).thenThrow(new EventNotFoundException("Event with Id - 1 not Found!"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/events/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contains(exResponse.getMessage()) ? "true" : "false"),
				exceptionTestFile);

	}
}
