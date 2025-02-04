package com.eventmanagement.registration.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.eventmanagement.registration.entity.Registration;
import com.eventmanagement.registration.repository.RegistrationRepository;
import com.eventmanagement.registration.service.RegistrationService;

@Component
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private RegistrationRepository registrationRepo;

	@Override
	public Registration registerForEvent(@Valid Registration registration) {

		if (isRegistrationExistsAlready(registration.getUserName(), registration.getEventId())) {
			return null;
		} else {
			registration.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
			registrationRepo.save(registration);
		}
		return registration;

	}

	private boolean isRegistrationExistsAlready(String userName, String eventId) {
		List<Registration> userRegistered = registrationRepo.findByUserName(userName);
		if (!CollectionUtils.isEmpty(userRegistered)
				&& userRegistered.stream().anyMatch(e -> e.getEventId().equalsIgnoreCase(eventId))) {
			return true;
		}
		return false;
	}

	@Override
	public List<Registration> getRegistrationDetails() {
		return registrationRepo.findAll();
	}

	@Override
	public List<Registration> getRegistrationByEventId(String eventId) {
		return registrationRepo.findByEventId(eventId);
	}

	@Override
	public List<Registration> getRegistrationByUser(String userName) {
		return registrationRepo.findByUserName(userName);
	}

}
