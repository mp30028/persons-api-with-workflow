package com.zonesoft.persons.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zonesoft.persons.data_generators.PersonRecordBuilder;
import com.zonesoft.persons.models.PersonTest;

class PersonTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonTest.class);
	
	@Test
	void testInstantiation() {
		PersonRecordBuilder generator = new PersonRecordBuilder();
		Person person = generator.id().moniker().firstname().lastname().otherNames(2,4).build();
		LOGGER.debug("Generated Person Instantiated: person = {}", person);
		assertNotNull(person);
		assertNotNull(person.getId());
		assertNotNull(person.otherNames());
		for(OtherName otherName: person.otherNames()) {
			assertNotNull(otherName);
			assertNotNull(otherName.getId());
		}
		
		String idToUse = "1234ABCD";
		person = generator.id(idToUse).moniker().firstname().lastname().otherNames(2,4).build();
		LOGGER.debug("Generated Person Instantiated: person = {}", person);
		assertNotNull(person);
		assertNotNull(person.getId());
		assertEquals(idToUse, person.getId());
		assertNotNull(person.otherNames());
		for(OtherName otherName: person.otherNames()) {
			assertNotNull(otherName);
			assertNotNull(otherName.getId());
		}
	}

}