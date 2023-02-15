package com.zonesoft.persons.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.zonesoft.persons.data_generators.PersonRecordBuilder;
import com.zonesoft.persons.events.PersistenceEventRepository;
import com.zonesoft.persons.models.Person;
import com.zonesoft.persons.repositories.PersonRepository;
import com.zonesoft.persons.data_generators.RecordsGeneratorTemplate;

import reactor.core.publisher.Flux;

class PersonServiceTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceTest.class);
	private ReactiveMongoTemplate mockReactiveTemplate;	
	private PersonRepository mockRepository;
	private PersistenceEventRepository mockEventRepository;
	private PersonService service;
	
	@BeforeEach
	void setupBeforeEach() {
		this.mockRepository = mock(PersonRepository.class);
		this.mockReactiveTemplate = mock(ReactiveMongoTemplate.class);
		this.mockEventRepository = mock(PersistenceEventRepository.class);
		service = new PersonService(mockRepository,mockEventRepository,mockReactiveTemplate);
	}
	
	
	@AfterEach
	void cleanupAfterEach() {
		this.service = null;
		this.mockRepository = null;
		this.mockEventRepository = null;
		this.mockReactiveTemplate = null;
	}
	
	@Test
	void testInitialisationAndSetup() {
		assertNotNull(mockRepository);
		assertNotNull(service);
	}
	
	@Test
	void testFindAll_WhenRepositoryReturnsNull() {
		when(mockRepository.findAll()).thenReturn(null);
		Flux<Person> personFlux = service.findAll();
		assertNull(personFlux);
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsNull personFlux-is-null {}", Objects.isNull(personFlux));
	}

	@Test
	void testFindAll_WhenRepositoryReturnsEmpty() {
		when(mockRepository.findAll()).thenReturn(Flux.empty());
		Flux<Person> personFlux = service.findAll();
		assertNotNull(personFlux);
		boolean hasElements = personFlux.hasElements().block(); 
		assertFalse(hasElements);
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsEmpty personFlux-is-null {}", Objects.isNull(personFlux));
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsEmpty personFlux-has-elements {}", hasElements);
	}
	
	@Test
	void testFindAll_WhenRepositoryReturnsASingleResult() {
		Person person = new PersonRecordBuilder().withDefaults().build();
		when(mockRepository.findAll()).thenReturn(Flux.just(person));
		Flux<Person> personFlux = service.findAll();
		assertNotNull(personFlux);
		boolean hasElements = personFlux.hasElements().block(); 
		assertTrue(hasElements);
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsASingleResult personFlux-is-null {}", Objects.isNull(personFlux));
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsASingleResult personFlux-has-elements {}", hasElements);
		List<Person> result = personFlux
			.collectList()
			.map(l -> {LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsASingleResult: find-results={}", l); return l;})
			.block();
		assertNotNull(result);
		assertEquals(1,result.size());
		assertEquals(person, result.get(0));
	}
	
	@Test
	void testFindAll_WhenRepositoryReturnsSeveralResults() {
		RecordsGeneratorTemplate<PersonRecordBuilder, Person> generator = new RecordsGeneratorTemplate<>();
		Supplier<PersonRecordBuilder> supplier = () -> new PersonRecordBuilder().withDefaults();
		List<Person> persons = generator.generate(supplier);
		when(mockRepository.findAll()).thenReturn(Flux.fromIterable(persons));
		Flux<Person> personFlux = service.findAll();
		assertNotNull(personFlux);
		boolean hasElements = personFlux.hasElements().block(); 
		assertTrue(hasElements);
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsSeveralResults personFlux-is-null {}", Objects.isNull(personFlux));
		LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsSeveralResults personFlux-has-elements {}", hasElements);
		List<Person> result = personFlux
			.collectList()
			.map(l -> {LOGGER.debug("FROM testFindAll_WhenRepositoryReturnsSeveralResults: find-results={}", l); return l;})
			.block();
		assertNotNull(result);
		assertEquals(persons.size(),result.size());
		assertEquals(persons, result);
	}
}
