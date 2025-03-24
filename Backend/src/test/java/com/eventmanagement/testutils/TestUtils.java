package com.eventmanagement.testutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// boiler-plate code

public class TestUtils {

	public static final String TEXT_RESET = "\033[0m";
	public static final String RED_BOLD_BRIGHT = "\033[1;91m"; // RED
	public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
	public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
	public static final String BLUE_BOLD_BRIGHT = "\033[1;94m"; // BLUE

	public static String testResult;

	public static int total;
	public static int passed;
	public static int failed;

	public static File businessTestFile;
	public static File boundaryTestFile;
	public static File exceptionTestFile;
	public static File xmlFile;

	public static final String GUID = "6ed39465-d6d3-4ec4-b27d-1dcb870b2992";
	public static String customData;
	public static final String URL =  "https://compiler.techademy.com/v1/mfa-results/push";

	static {
		total = 0;
		passed = 0;
		failed = 0;

		testResult = "";

		businessTestFile = new File("./output_revised.txt");
		businessTestFile.delete();

		boundaryTestFile = new File("./output_boundary_revised.txt");
		boundaryTestFile.delete();

		exceptionTestFile = new File("./output_exception_revised.txt");
		exceptionTestFile.delete();
	}

	private static String readData(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return contentBuilder.toString();
	}

	public static void yakshaAssert(String testName, Object result, File file) throws IOException {
		TestResults testResults = new TestResults();
		Map<String, TestCaseResultDto> testCaseResults = new HashMap<String, TestCaseResultDto>();

		customData = readData("../custom.ih");
		String resultStatus = "Failed";
		int resultScore = 0;
		if (result.toString().equals("true")) {
			resultScore = 1;
			resultStatus = "Passed";
		}
		try {
			String testType = "functional";
			if (file.getName().contains("boundary"))
				testType = "boundary";
			if (file.getName().contains("exception"))
				testType = "exception";
			testCaseResults.put(GUID,
					new TestCaseResultDto(testName, testType, 1, resultScore, resultStatus, true, ""));
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		String hostName = System.getenv("HOSTNAME");
		String AttemptId = System.getenv("ATTEMPT_ID");

		testResults.setTestCaseResults(asJsonString(testCaseResults));
		testResults.setCustomData(customData);
		testResults.setHosttName(hostName);
		testResults.setAttemptId(AttemptId);

		int length = 0;
		if(customData != null) {length = customData.length(); }


		try {

			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			//	String input = "{\"qty\":100,\"name\":\"iPad 4\"}";
			String input = asJsonString(testResults);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			os.close();

			int responseCode = conn.getResponseCode();
			if (!(responseCode == HttpURLConnection.HTTP_OK  || responseCode == HttpURLConnection.HTTP_CREATED)) { 
				System.out.println(RED_BOLD_BRIGHT + "⚠️ Unable to push test cases,please try again! [" + responseCode +"|" + hostName +"|" + AttemptId + "|" + length + "]" + TEXT_RESET);
			}



			// BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// // String output;
			// // while ((output = br.readLine()) != null) {
			// // 	System.out.println(output);
			// // }

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		total++;
		String[] r = testName.split("(?=\\p{Upper})");
		System.out.print("\n" + BLUE_BOLD_BRIGHT + "=>");

		System.out.print(YELLOW_BOLD_BRIGHT + "Test For : ");

		for (int i = 1; i < r.length; i++) {
			System.out.print(YELLOW_BOLD_BRIGHT + r[i] + " ");

		}
		System.out.print(" : ");

		if (result.toString().equals("true")) {
			System.out.println(GREEN_BOLD_BRIGHT + "PASSED" + TEXT_RESET);
			passed++;
		} else {
			System.out.println(RED_BOLD_BRIGHT + "FAILED" + TEXT_RESET);
			failed++;
		}
	}

	public static void testReport() {

		System.out.print("\n" + BLUE_BOLD_BRIGHT + "TEST CASES EVALUATED : " + total + TEXT_RESET);
		System.out.print("\n" + GREEN_BOLD_BRIGHT + "PASSED : " + passed + TEXT_RESET);
		System.out.println("\n" + RED_BOLD_BRIGHT + "FAILED : " + failed + TEXT_RESET);

	}

	public static String currentTest() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	// convert object into JSON
	public static String asJsonString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(obj);
//			System.out.println("jsonString");
//			System.out.println(jsonString);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return jsonString;

	}

}
