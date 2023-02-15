package com.zonesoft.persons.repositories;

import static com.zonesoft.persons.data_generators.Generator.generateRandomInt;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.zonesoft.persons.data_generators.PersonRecordBuilder;
import com.zonesoft.persons.models.Person;
import com.zonesoft.persons.data_generators.RecordsGeneratorTemplate;

@Testcontainers()
@DataMongoTest
class RepositoryIntegrationTest{
	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryIntegrationTest.class);
	
	private static final String IMAGE_NAME = "mongo:6.0.2";

    private static final MongoDBContainer MONGODB_CONTAINER;
    static {
    	LOGGER.debug("From MONGODB_CONTAINER initialisation");
    	MONGODB_CONTAINER = new MongoDBContainer(DockerImageName.parse(IMAGE_NAME));
        MONGODB_CONTAINER.start();
    }

	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {		
		registry.add("spring.data.mongodb.uri", MONGODB_CONTAINER::getReplicaSetUrl);  
	}
	
	@Autowired
	private PersonRepository personRepository;

	
	private Person  createAndInsertSinglePerson() {
		PersonRecordBuilder generator = new PersonRecordBuilder();
		Person generatedPerson = generator.moniker().firstname().lastname().build();
		return personRepository.insert(generatedPerson).block();
	}
	
	private List<Person>  createAndInsertPersons() {
		RecordsGeneratorTemplate<PersonRecordBuilder, Person> generator = new RecordsGeneratorTemplate<>();
		Supplier<PersonRecordBuilder> supplier = () -> new PersonRecordBuilder().withDefaults(false);
		List<Person> generatedPersons = generator.generate(supplier);
		return personRepository.insert(generatedPersons).collectList().block();
	}
	
	@Test
	void testFindAll_returnsInsertedNumberOfPersons() {
		assertNotNull(personRepository);
		personRepository.deleteAll().block();
		List<Person> createdPersons = createAndInsertPersons();
		LOGGER.debug("createdPersons ={}", createdPersons);
		List<Person> findAllResults = personRepository.findAll().collectList().block();
		LOGGER.debug("testFindAll_returnsInsertedNumberOfPersons: results ={}", findAllResults);
		assertEquals(createdPersons.size(), findAllResults.size());
	}
	
	@Test
	void testFindAll_returnsNoResultsWhenNoRecordsInserted() {
		assertNotNull(personRepository);
		personRepository.deleteAll().block();
		List<Person> findAllResults = personRepository.findAll().collectList().block();
		LOGGER.debug("testFindAll_returnsNoResultsWhenNoRecordsInserted: results ={}", findAllResults);
		assertEquals(0, findAllResults.size());
	}
	
	@Test
	void testFindAll_returnsOneResultsWhenOneRecordsInserted() {
		assertNotNull(personRepository);
		personRepository.deleteAll().block();
		createAndInsertSinglePerson();
		List<Person> findAllResults = personRepository.findAll().collectList().block();
		LOGGER.debug("testFindAll_returnsOneResultsWhenOneRecordsInserted: results ={}", findAllResults);
		assertEquals(1, findAllResults.size());
	}
	
	@Test
	void testFindAll_returnsManyResultsWhenManyRecordsInserted() {
		assertNotNull(personRepository);
		personRepository.deleteAll().block();
		List<Person> createdPersons = createAndInsertPersons();
		List<Person> findAllResults = personRepository.findAll().collectList().block();
		LOGGER.debug("testFindAll_returnsManyResultsWhenManyRecordsInserted: results ={}", findAllResults);
		assertEquals(createdPersons.size(), findAllResults.size());
	}
	
	@Test
	void test_FindById_GivenAValidId_ReturnsSingleValidPerson() {
		Person createdPerson = createAndInsertSinglePerson();
		Person findResult = personRepository.findById(createdPerson.getId()).block();
		assertNotNull(findResult);
		assertEquals(createdPerson.toString(), findResult.toString());
	}
	
	@Test
	void test_FindByMoniker_GivenAValidMoniker_ReturnsValidPersons() {
		personRepository.deleteAll().block();
		List<Person> createdPersons = createAndInsertPersons();
		int selectedIndex = generateRandomInt(0, createdPersons.size()-1);
		String monikerToFind = createdPersons.get(selectedIndex).getMoniker();
		List<Person> searchResults = personRepository.findByMoniker(monikerToFind).collectList().block();
		LOGGER.debug("test_FindByMoniker_GivenAValidMoniker_ReturnsValidPersons: monikerToFind = {}, searchResults = {}",monikerToFind, searchResults);
	}
	
	@Test
	void test_FindByIdIn_GivenAListOfIds_ReturnsValidPersons() {
		personRepository.deleteAll().block();
		List<Person> createdPersons = createAndInsertPersons();
		List<String> idsToFind = new ArrayList<>();
		for(Person person: createdPersons) {
			idsToFind.add(person.getId());
		}
		List<Person> searchResults = personRepository.findByIdIn(idsToFind).collectList().block();
		LOGGER.debug("test_FindByIdIn_GivenAListOfId_ReturnsValidPersons: idsToFind = {}, searchResults = {}",idsToFind, searchResults);
		assertNotNull(searchResults);
		for(Person person: searchResults) {
			Optional<Person> expectedPerson = createdPersons
					.stream()
					.filter(p -> {	LOGGER.debug("p.getId()={},  person.getId()={}",p.getId(),person.getId());
									return (p.getId().equals(person.getId()));
							})
					.findFirst();
			assertTrue(expectedPerson.isPresent());
			if (expectedPerson.isPresent()) {
				Person expected = expectedPerson.get();
				assertEquals(expected.getId(), person.getId());	
				assertEquals(expected.getFirstname(), person.getFirstname());
				assertEquals(expected.getLastname(), person.getLastname());
				assertEquals(expected.getMoniker(), person.getMoniker());
				assertEquals(expected.getOtherNames().size(), person.getOtherNames().size());
			}
		}	
	}
	
}