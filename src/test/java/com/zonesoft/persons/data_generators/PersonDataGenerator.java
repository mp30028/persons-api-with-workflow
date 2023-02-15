package com.zonesoft.persons.data_generators;

import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.zonesoft.persons.models.Person;
import com.zonesoft.persons.repositories.PersonRepository;



@SpringBootTest
public class PersonDataGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonDataGenerator.class);
	
	private final PersonRepository repository;
    
	@Value("${com.zonesoft.persons.regenerate-data}")
    private boolean isDataToBeRegenerated = false;
    
    @Autowired
    public PersonDataGenerator(PersonRepository repository) {
    	super();
		this.repository = repository;
    }
    
	@Test
	void checkDataGeneratorFlag() {
		assertFalse("WARNING: Data store was cleaned out and replaced with regenerated dummy data", isDataToBeRegenerated); 
	}
    
	@Test
	void deleteAndCreateDummyData() {
		if (isDataToBeRegenerated){
			deleteAllPersonInDb();
			savePersonsToDb(generatePersons());
		}
		List<Person> fetchedPersons = fetchPersonsFromDb();
		LOGGER.debug("isDataToBeRegenerated={}, current-data={}", isDataToBeRegenerated, fetchedPersons);
	}

	private List<Person> generatePersons() {
		RecordsGeneratorTemplate<PersonRecordBuilder, Person> generator = new RecordsGeneratorTemplate<>();
		Supplier<PersonRecordBuilder> supplier = () -> new PersonRecordBuilder().withDefaults();
		return generator.generate(supplier);
	}

	private List<Person> fetchPersonsFromDb() {
		List<Person> persons = repository.findAll().collectList().block();
		return persons;
	}

	private void savePersonsToDb(List<Person> persons) {
		persons = repository.insert(persons).collectList().block();
		LOGGER.debug("savePersonsToDb completed: persons saved ={}",persons);
	}

	private void deleteAllPersonInDb() {
		repository.deleteAll().block();
		LOGGER.debug("deleteAllPersonsInDb completed");
	}
}
