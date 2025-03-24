package com.eventmanagement.testutils;

public class TestResults {

	private String testCaseResults;
	private String customData;
	private String hostName;
	private String attemptId;

	public String getTestCaseResults() {
		return testCaseResults;
	}

	public void setTestCaseResults(String testCaseResults) {
		this.testCaseResults = testCaseResults;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}
	public String getHostName() {
		return hostName;
	}

	public void setHosttName(String hostName) {
		this.hostName = hostName;
	}
	public String getAttemptId() {
		return attemptId;
	}

	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

}