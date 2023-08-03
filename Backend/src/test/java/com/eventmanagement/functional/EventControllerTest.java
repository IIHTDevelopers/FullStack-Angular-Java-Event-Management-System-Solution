package com.eventmanagement.functional;

import static com.eventmanagement.testutils.TestUtils.businessTestFile;
import static com.eventmanagement.testutils.TestUtils.currentTest;
import static com.eventmanagement.testutils.TestUtils.testReport;
import static com.eventmanagement.testutils.TestUtils.yakshaAssert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.eventmanagement.controller.EventController;
import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.service.EventService;
import com.eventmanagement.testutils.MasterData;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
public class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventService eventService;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateEvent() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		EventDTO savedEventDTO = MasterData.getEventDto();
		savedEventDTO.setId(eventDTO.getId());

		when(eventService.createEvent(any())).thenReturn(savedEventDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/events")
				.content(MasterData.asJsonString(eventDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(savedEventDTO))
						? "true"
						: "false"),
				businessTestFile);

	}

	@Test
	public void testCreateEventIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		EventDTO eventDTO = MasterData.getEventDto();
		EventDTO savedEventDTO = MasterData.getEventDto();
		savedEventDTO.setId(eventDTO.getId());

		when(this.eventService.createEvent(any())).then(new Answer<EventDTO>() {

			@Override
			public EventDTO answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return savedEventDTO;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/events")
				.content(MasterData.asJsonString(eventDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

	@Test
	public void testGetEventById() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		when(this.eventService.getEventById(eventDTO.getId())).thenReturn(eventDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/" + eventDTO.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(eventDTO)) ? "true"
						: "false"),
				businessTestFile);

	}

	@Test
	public void testGetEventByIdIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		EventDTO eventDTO = MasterData.getEventDto();
		when(this.eventService.getEventById(eventDTO.getId())).then(new Answer<EventDTO>() {

			@Override
			public EventDTO answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return eventDTO;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/" + eventDTO.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

	@Test
	public void testGetAllEvents() throws Exception {
		List<EventDTO> eventDTOS = MasterData.getEventDtoList();

		when(this.eventService.getAllEvents()).thenReturn(eventDTOS);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(eventDTOS)) ? "true"
						: "false"),
				businessTestFile);

	}

	@Test
	public void testGetAllEventsIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		List<EventDTO> eventDTOS = MasterData.getEventDtoList();
		when(this.eventService.getAllEvents()).then(new Answer<List<EventDTO>>() {

			@Override
			public List<EventDTO> answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return eventDTOS;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

	@Test
	public void testSearchEventsByName() throws Exception {
		List<EventDTO> eventDTOS = MasterData.getEventDtoList();
		String name = "David";
		when(this.eventService.searchEventsByName(name)).thenReturn(eventDTOS);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/searchByName").queryParam("name", name)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(eventDTOS)) ? "true"
						: "false"),
				businessTestFile);

	}

	@Test
	public void testSearchEventsByNameIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		List<EventDTO> eventDTOS = MasterData.getEventDtoList();
		String name = "David";
		when(this.eventService.searchEventsByName(name)).then(new Answer<List<EventDTO>>() {

			@Override
			public List<EventDTO> answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return eventDTOS;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/searchByName").queryParam("name", name)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

	@Test
	public void testSearchEventsByStatus() throws Exception {
		List<EventDTO> eventDTOS = MasterData.getEventDtoList();
		String status = "Active";
		when(this.eventService.searchEventsByStatus(status)).thenReturn(eventDTOS);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/searchByStatus")
				.queryParam("status", status).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(eventDTOS)) ? "true"
						: "false"),
				businessTestFile);

	}

	@Test
	public void testSearchEventsByStatusIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		List<EventDTO> eventDTOS = MasterData.getEventDtoList();
		String status = "Active";
		when(this.eventService.searchEventsByStatus(status)).then(new Answer<List<EventDTO>>() {

			@Override
			public List<EventDTO> answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return eventDTOS;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/searchByStatus")
				.queryParam("status", status).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

	@Test
	public void testUpdateEvent() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		EventDTO savedEventDTO = MasterData.getEventDto();
		savedEventDTO.setId(eventDTO.getId());

		when(this.eventService.updateEvent(any(), any())).thenReturn(savedEventDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/events/" + eventDTO.getId())
				.content(MasterData.asJsonString(savedEventDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(savedEventDTO))
						? "true"
						: "false"),
				businessTestFile);

	}

	@Test
	public void testUpdateEventIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		EventDTO eventDTO = MasterData.getEventDto();
		EventDTO savedEventDTO = MasterData.getEventDto();
		savedEventDTO.setId(eventDTO.getId());

		when(this.eventService.updateEvent(any(), any())).then(new Answer<EventDTO>() {

			@Override
			public EventDTO answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return savedEventDTO;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/events/" + eventDTO.getId())
				.content(MasterData.asJsonString(savedEventDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

	@Test
	public void testDeleteEvent() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		when(this.eventService.deleteEvent(eventDTO.getId())).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/events/" + eventDTO.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), (result.getResponse().getContentAsString().contentEquals("") ? "true" : "false"),
				businessTestFile);

	}

	@Test
	public void testDeleteEventIsServiceMethodCalled() throws Exception {
		final int count[] = new int[1];
		EventDTO eventDTO = MasterData.getEventDto();
		when(this.eventService.deleteEvent(eventDTO.getId())).then(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				count[0]++;
				return true;
			}
		});
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/events/" + eventDTO.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), count[0] == 1 ? true : false, businessTestFile);

	}

}
