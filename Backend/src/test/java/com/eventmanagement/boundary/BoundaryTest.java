package com.eventmanagement.boundary;

import static com.eventmanagement.testutils.TestUtils.boundaryTestFile;
import static com.eventmanagement.testutils.TestUtils.currentTest;
import static com.eventmanagement.testutils.TestUtils.testReport;
import static com.eventmanagement.testutils.TestUtils.yakshaAssert;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.testutils.MasterData;

@ExtendWith(SpringExtension.class)
public class BoundaryTest {

	private static Validator validator;

	// ----------------------------------------------------------------------------------------------
	@BeforeAll
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testNameNotNull() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		eventDTO.setName(null);
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

	@Test
	public void testNameMinThree() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		eventDTO.setName("AB");
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

	@Test
	public void testNameMaxTwenty() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		String name = "";
		for (int i = 0; i < 30; i++) {
			name.concat("A");
		}
		eventDTO.setName(name);
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

	@Test
	public void testDescriptionNotNull() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		eventDTO.setDescription(null);
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

	@Test
	public void testDescriptionMinThree() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		eventDTO.setDescription("AB");
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

	@Test
	public void testDescriptionMaxTwenty() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		String description = "";
		for (int i = 0; i < 210; i++) {
			description.concat("A");
		}
		eventDTO.setDescription(description);
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

	@Test
	public void testStatusNotNull() throws Exception {
		EventDTO eventDTO = MasterData.getEventDto();
		eventDTO.setStatus(null);
		Set<ConstraintViolation<EventDTO>> violations = validator.validate(eventDTO);
		yakshaAssert(currentTest(), !violations.isEmpty() ? true : false, boundaryTestFile);
	}

}
