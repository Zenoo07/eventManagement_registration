package com.eventmanagement.registration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventmanagement.registration.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

	public List<Registration> findByUserName(String userName);

	public List<Registration> findByEventId(String eventId);

}
