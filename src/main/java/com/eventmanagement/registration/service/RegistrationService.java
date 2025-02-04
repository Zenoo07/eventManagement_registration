package com.eventmanagement.registration.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.eventmanagement.registration.entity.Registration;

@Service
public interface RegistrationService {

	public Registration registerForEvent(@Valid Registration registration);

	public List<Registration> getRegistrationDetails();

	public List<Registration> getRegistrationByEventId(String eventId);

	public List<Registration> getRegistrationByUser(String userName);
}
