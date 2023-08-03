package com.eventmanagement.exception;

public class EventNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EventNotFoundException() {
		super();
	}

	public EventNotFoundException(String message) {
		super(message);
	}

}
