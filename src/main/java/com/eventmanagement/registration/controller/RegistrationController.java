package com.eventmanagement.registration.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.eventmanagement.registration.entity.Registration;
import com.eventmanagement.registration.service.RegistrationService;

@RestController
@RequestMapping("/api/eventmanagement/registration")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@GetMapping("healthcheck")
	public ResponseEntity<String> healthcheck() {
		return new ResponseEntity<>("Registration Service is Running", HttpStatus.OK);
	}

	@GetMapping("getRegistrationDetails")
	public ResponseEntity<List<Registration>> getRegistrationDetails() {
		return ResponseEntity.ok(registrationService.getRegistrationDetails());
	}

	@GetMapping("getRegistrationByEvent/{eventId}")
	public ResponseEntity<List<Registration>> getRegistrationByEvent(
			@PathVariable(value = "eventId", required = true) String eventId) {
		List<Registration> registrations = null;
		if (null != eventId && !eventId.isBlank()) {
			registrations = registrationService.getRegistrationByEventId(eventId);
		} else {
			return new ResponseEntity<List<Registration>>(new ArrayList<Registration>(), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("getRegistrationByUser/{userName}")
	public ResponseEntity<List<Registration>> getRegistrationByUser(
			@PathVariable(value = "userName", required = true) String userName) {
		List<Registration> registrations = null;
		if (null != userName && !userName.isBlank()) {
			registrations = registrationService.getRegistrationByUser(userName);
		} else {
			return new ResponseEntity<List<Registration>>(new ArrayList<Registration>(), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(registrations);
	}

	@PostMapping("register")
	public ResponseEntity<String> addUser(@Valid @RequestBody Registration registration) {

		if (isEventIdValid(registration.getEventId())
				&& isUserDetailsValid(registration.getUserName(), registration.getUserEmail())) {
			Registration registeredOnEvent = registrationService.registerForEvent(registration);
			if (null != registeredOnEvent) {
				return ResponseEntity.ok("Registration Successfull!! Great Choice!");
			} else {
				return new ResponseEntity<String>("Duplicate Registration", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Invalid Data - Validate Your Data", HttpStatus.BAD_REQUEST);
		}

	}

	private boolean isEventIdValid(String eventId) {
		ResponseEntity<Boolean> isEventIdValid = new RestTemplate().getForEntity(
				"http://2044739eventservicelb-863318823.us-west-2.elb.amazonaws.com/api/eventmanagement/event/isEventIdValid/{eventId}", Boolean.class, eventId);
		return isEventIdValid.getBody();
	}

	private boolean isUserDetailsValid(String userName, String userEmail) {
		ResponseEntity<Boolean> isUserDetailsValid = new RestTemplate().getForEntity(
				"http://2044739userServiceLB-1566141824.us-west-2.elb.amazonaws.com/api/eventmanagement/user/validateUser/{userName}/{userEmail}", Boolean.class,
				userName, userEmail);
		return isUserDetailsValid.getBody();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationException(MethodArgumentNotValidException mex) {

		Map<String, String> validationError = new HashMap<String, String>();

		mex.getBindingResult().getAllErrors().forEach(error -> {
			validationError.put(error.getObjectName(), error.getDefaultMessage());
		});

		return validationError;

	}

}
