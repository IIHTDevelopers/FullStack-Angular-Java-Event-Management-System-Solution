package com.eventmanagement.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventDTO {

	private Long id;

	@NotNull
	@Size(min = 3, max = 20)
	private String name;

	@NotNull
	@Size(min = 3, max = 200)
	private String description;

	@NotNull
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
