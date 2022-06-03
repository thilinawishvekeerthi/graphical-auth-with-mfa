package com.graphicauth.authservice;

import com.graphicauth.authservice.service.IGraphicAuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceApplicationTests {

	private final Long NUMBER_OF_POINTS = 4L;
	private final Long TOLERANCE = 1L;

	@Autowired
	IGraphicAuthService graphicAuthService;

	@Test
	@Order(1)
	public void testIfWrongSelectedPassPointDoesNotAuthenticates(){
		String passPoints = "1,1|2,2|3,3|4,4";
		String selectedPassPoints = "1,19|2,2|3,3|4,4";
		Boolean authenticated = graphicAuthService.authenticate(passPoints, selectedPassPoints, NUMBER_OF_POINTS, TOLERANCE);
		Assertions.assertTrue(authenticated == false, "wrong selected passPoints authenticates test fail");
	}

	@Test
	@Order(2)
	public void testIfCorrectSelectedPassPointAuthenticates(){
		String passPoints = "1,1|2,2|3,3|4,4";
		String selectedPassPoints = "1,1|2,2|3,3|4,4";
		Boolean authenticated = graphicAuthService.authenticate(passPoints, selectedPassPoints, NUMBER_OF_POINTS, TOLERANCE);
		Assertions.assertTrue(authenticated == true, "Correct passPoints does not authenticate test fail");
	}

	@Test
	@Order(3)
	public void testIfSelectedPassPointExceedsCorrectPassPointCountDoNotAuthenticate(){
		String passPoints = "1,1|2,2|3,3|4,4";
		String selectedPassPoints = "1,1|2,2|3,3|4,4|5,5";
		Boolean authenticated = graphicAuthService.authenticate(passPoints, selectedPassPoints, NUMBER_OF_POINTS, TOLERANCE);
		Assertions.assertTrue(authenticated == false, "Exceeded selected passPoints authenticates test fail");
	}

	@Test
	@Order(4)
	public void testIfCorrectSelectedPassWithInToleranceRatioAuthenticates(){
		String passPoints = "1,1|2,2|3,3|4,4";
		String selectedPassPoints = "1.5,1.5|2.5,2.5|3.5,3.5|4.5,4.5";
		Boolean authenticated = graphicAuthService.authenticate(passPoints, selectedPassPoints, NUMBER_OF_POINTS, TOLERANCE);
		Assertions.assertTrue(authenticated == true, "PassPoints with in the tolerance ratio does not authenticate test fail");
	}

	@Test
	@Order(5)
	public void testIfSelectedPassNotWithInToleranceRatioDoNotAuthenticate(){
		String passPoints = "1,1|2,2|3,3|4,4";
		String selectedPassPoints = "1.5,2|2.5,3|3.5,3.5|4.5,4.5";
		Boolean authenticated = graphicAuthService.authenticate(passPoints, selectedPassPoints, NUMBER_OF_POINTS, TOLERANCE);
		Assertions.assertTrue(authenticated == false, "PassPoints not with in the tolerance ratio authenticates test fail");
	}



}
