package com.zonesoft.persons.controllers;

import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.zonesoft.persons.data_generators.PersonRecordBuilder;
import com.zonesoft.persons.models.Person;
import com.zonesoft.persons.services.PersonService;
import com.zonesoft.persons.data_generators.RecordsGeneratorTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.zonesoft.persons.data_generators.Generator.*;

class PersonControllerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonControllerTest.class);
	
	private static final int MIN_PERSONS = 2;
	private static final int MAX_PERSONS = 10;
	private PersonService service;
	private WebTestClient client;
	private static Person PERSON_1;
	private static Person PERSON_2;
	private static List<Person> PERSONS;

	private static void createTestData() {
		PersonRecordBuilder personGenerator = new PersonRecordBuilder();
		PERSON_1 = personGenerator.id().moniker().firstname().lastname().otherNames().build();
		PERSON_2 = personGenerator.withDefaults().build();
		RecordsGeneratorTemplate<PersonRecordBuilder, Person> generator = new RecordsGeneratorTemplate<>();
		Supplier<PersonRecordBuilder> supplier = () -> new PersonRecordBuilder().withDefaults();
		PERSONS = generator.minRecords(MIN_PERSONS).maxRecords(MAX_PERSONS).generate(supplier);
		PERSONS.add(0,PERSON_1);
		PERSONS.add(1,PERSON_2);
	}
	
	private int selectARandomPersonsIndex() {
		return generateRandomInt(0, PERSONS.size()-1);
	}

	@BeforeAll
	static void setUpBeforeAll() {
		createTestData();
	}

	@AfterAll
	static void cleanUpAfterAll() {
		PERSONS = null;
		PERSON_1 = null;
		PERSON_2 = null;
	}

	@BeforeEach
	void setUpBeforeEach() {
		service = mock(PersonService.class);
		PersonController controller = new PersonController(service);
		client = WebTestClient.bindToController(controller).build();
	}
	
	@AfterEach
	void cleanUpAfterEach() {
		client = null;
		service = null;
	}

	@Test
	@Disabled
	void test_get_persons_returnsOK_withPersonsList() {
		when(service.findAll()).thenReturn(Flux.fromIterable(PERSONS));
		LOGGER.debug("test_getPersons_returnsOK_withPersonsList: PERSONS={}",PERSONS);
		client
			.get()
			.uri(uriBuilder -> uriBuilder.path("/api/persons").build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBodyList(Person.class)
			.consumeWith(list -> LOGGER.debug("getPersons: list of persons in response = {}",list));
	}
	
	@Test
	void test_get_personById_returnsOK_withPerson() {
		int selectedPersonIndex = selectARandomPersonsIndex();
		Person selectedPerson = PERSONS.get(selectedPersonIndex);
		LOGGER.debug("selectedPerson = {}", selectedPerson);
		when(service.findById(selectedPerson.getId())).thenReturn(Mono.just(selectedPerson));
		client
			.get()
			.uri(uriBuilder -> uriBuilder.path("/api/persons/{id}").build(selectedPerson.getId()))
			.header("Content-Type", "application/json")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.jsonPath("$.id")
			.isEqualTo(selectedPerson.getId())
			.consumeWith(r -> LOGGER.debug("get_personById: response = {}",new String(r.getResponseBody(), StandardCharsets.UTF_8)));
	}
	
	@Test
	void test_post_persons_returnsOK_withNewPerson() {
		Person newPerson =  new Person(generateUUID(), generateNickname(), generateFirstName(generateGender()),generateLastName());
		LOGGER.debug("newPerson = {}", newPerson);
		when(service.insert(any())).thenReturn(Mono.just(newPerson));
		client
			.post()
			.uri(uriBuilder -> uriBuilder.path("/api/persons").build())
			.header("Content-Type", "application/json")
			.body(Mono.just(newPerson),Person.class)
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody()
			.jsonPath("$.id")
			.isEqualTo(newPerson.getId())
			.consumeWith(r -> LOGGER.debug("post_persons: response = {}",new String(r.getResponseBody(), StandardCharsets.UTF_8)));
	}
	
	@Test
	void test_put_person_returnsOK_withUpdatedPerson() {
		int selectedPersonIndex = selectARandomPersonsIndex();
		Person selectedPerson = PERSONS.get(selectedPersonIndex);
		LOGGER.debug("FROM putPerson: selectedPerson = {}", selectedPerson);
		String newMoniker = generateNickname();
		selectedPerson.setMoniker(newMoniker);
		when(service.update(any())).thenReturn(Mono.just(selectedPerson));
		client
			.put()
			.uri(uriBuilder -> uriBuilder.path("/api/persons/{id}").build(selectedPerson.getId()))
			.header("Content-Type", "application/json")
			.body(Mono.just(selectedPerson),Person.class)
			.exchange()
			.expectStatus()
			.isAccepted()
			.expectBody()
			.consumeWith(r -> LOGGER.debug("put_person: response = {}",new String(r.getResponseBody(), StandardCharsets.UTF_8)))
			.jsonPath("$.id").isEqualTo(selectedPerson.getId())
			.jsonPath("$.moniker").isEqualTo(newMoniker);
	}
	
	
	@Test
	void test_delete_personById_returnsOK() {
		int selectedPersonIndex = selectARandomPersonsIndex();
		Person selectedPerson = PERSONS.get(selectedPersonIndex);
		LOGGER.debug("FROM deletePerson: selectedPerson = {}", selectedPerson);
		Mono<Void> voidReturn  = Mono.empty();
		when(service.deleteById(selectedPerson.getId())).thenReturn(voidReturn);
		client
			.delete()
			.uri(uriBuilder -> uriBuilder.path("/api/persons/{id}").build(selectedPerson.getId()))
			.header("Content-Type", "application/json")
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	@Test
	void test_get_personByMoniker_returnsOK_withFoundPersons() {
		int selectedPersonIndex = selectARandomPersonsIndex();
		Person selectedPerson = PERSONS.get(selectedPersonIndex);
		LOGGER.debug("selectedPerson = {}", selectedPerson);
		Flux<Person> personFlux = Flux.just(selectedPerson);
		when(service.findByMoniker(anyString())).thenReturn(personFlux);
		client
			.get()
			.uri(uriBuilder -> uriBuilder.path("/api/persons").queryParam("moniker", selectedPerson.getMoniker()).build())
			.header("Content-Type", "application/json")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.consumeWith(b -> {LOGGER.debug("body={}", b);})
			.jsonPath("$[0].id").isEqualTo(selectedPerson.getId())
			.jsonPath("$.length()").isEqualTo(1)
			.consumeWith(r -> LOGGER.debug("get_persons: response = {}",new String(r.getResponseBody(), StandardCharsets.UTF_8)));
	}
	
	
	@Test
	void test_getPersonByMoniker_returnsNoContent_() {
		String SOME_RANDOM_STRING = "acDBKddeJ939I2--";
		when(service.findByMoniker(anyString())).thenReturn(null);
		client
			.get()
			.uri(uriBuilder -> uriBuilder.path("/api/persons").queryParam("moniker", SOME_RANDOM_STRING).build())
			.header("Content-Type", "application/json")
			.exchange()
			.expectStatus()
			.isNoContent();
	}
	
	
	@Test
	void test_get_personByListOfId_returnsOK_withFoundPersons() {
		int sizeOfList = generateRandomInt(MIN_PERSONS, PERSONS.size());
		List<String> listOfId = new ArrayList<>();
		List<Person> selectedPersons = new ArrayList<>();
		for (int j=0; j < sizeOfList; j++) {
			int selectedPersonIndex = generateRandomInt(0,sizeOfList-1);
			Person selectedPerson = PERSONS.get(selectedPersonIndex); 
			listOfId.add(selectedPerson.getId());
			selectedPersons.add(selectedPerson);
		}
		Flux<Person> personFlux = Flux.fromIterable(selectedPersons);
		when(service.findByListOfIds(listOfId)).thenReturn(personFlux);
		client
			.get()
			.uri(uriBuilder -> uriBuilder.path("/api/persons").queryParam("id", listOfId).build())
			.header("Content-Type", "application/json")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.consumeWith(b -> {LOGGER.debug("body={}", b);})
			.jsonPath("$[0].id").isEqualTo(selectedPersons.get(0).getId())
			.jsonPath("$.length()").isEqualTo(selectedPersons.size())
			.consumeWith(r -> LOGGER.debug("get_persons: response = {}",new String(r.getResponseBody(), StandardCharsets.UTF_8)));
	}
}

