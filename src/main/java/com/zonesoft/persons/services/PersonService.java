package com.zonesoft.persons.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import com.zonesoft.persons.events.PersistenceEvent;
import com.zonesoft.persons.events.PersistenceEvent.PersistenceEventType;
import com.zonesoft.persons.events.PersistenceEventRepository;
import com.zonesoft.persons.models.Person;
import com.zonesoft.persons.repositories.PersonRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

@Service
public class PersonService {
	
	private final PersonRepository personRepository;
	private final PersistenceEventRepository eventRepository;
	private final ReactiveMongoTemplate reactiveTemplate;
	
	@Autowired
	public PersonService(PersonRepository personRepository, PersistenceEventRepository eventRepository, ReactiveMongoTemplate reactiveTemplate) {
		super();
		this.personRepository = personRepository;
		this.eventRepository = eventRepository;
		this.reactiveTemplate = reactiveTemplate;
	}
	
	private void writeEvent(PersistenceEventType eventType, List<Person> persons) {
		PersistenceEvent event = new PersistenceEvent(eventType,persons);
		Mono<PersistenceEvent> returnResult = eventRepository.insert(event);
		returnResult.subscribe();
	}
	
	private Consumer<SignalType> getEventWriterConsumer(PersistenceEventType eventType, Person person){
		List<Person> persons = new ArrayList<>();
		persons.add(person);
		return getEventWriterConsumer(eventType, persons);
	}
	
	private Consumer<SignalType> getEventWriterConsumer(PersistenceEventType eventType, List<Person> persons){
		return (s -> { if (s == SignalType.ON_COMPLETE) {writeEvent(eventType, persons);};});
	}
	
	public Mono<Person> insert(Person person){
		Mono<Person> returnResult = personRepository.insert(person);
		return returnResult.doFinally(getEventWriterConsumer(PersistenceEventType.SAVE, person));
	}
	
	public Mono<Person> update(Person person){
		Mono<Person> returnResult = personRepository.save(person);
		return returnResult.doFinally(getEventWriterConsumer(PersistenceEventType.SAVE, person));
	}
	
	public Flux<Person> saveAll(List<Person> persons){
		 Flux<Person> returnResult =  personRepository.saveAll(persons);
		 return returnResult.doFinally(getEventWriterConsumer(PersistenceEventType.SAVE, persons));
    }
    
	public Mono<Person> findById(String id){
    	return personRepository.findById(id);
    }
    
	public Flux<Person> findByListOfIds(List<String> listOfId){
    	return personRepository.findByIdIn(listOfId);
    }
	
	public Flux<Person> findAll(){
    	return personRepository.findAll();
    }
    
	public Mono<Void> deleteAll(){		
		return findAll()
				.collectList()
				.map((l) -> getEventWriterConsumer(PersistenceEventType.DELETE_ALL, l))
				.map((w) -> {w.accept(SignalType.ON_COMPLETE); return true;})
				.then(personRepository.deleteAll());
    }
    
	public Mono<Void> deleteById(String id){
		return findById(id)
			.map((p) -> getEventWriterConsumer(PersistenceEventType.DELETE, p))
			.map((w) -> {w.accept(SignalType.ON_COMPLETE); return true;})
			.then(personRepository.deleteById(id));
    }
	
	public Flux<Person> findByMoniker(String moniker){
    	return personRepository.findByMoniker(moniker);
    }

	public Flux<PersistenceEvent> streamAllEvents() {
        ChangeStreamOptions options = ChangeStreamOptions.builder()
                                                         .returnFullDocumentOnUpdate()
                                                         .build();

        return reactiveTemplate.changeStream("persistenceEvents",options,PersistenceEvent.class)
                               .map(ChangeStreamEvent::getBody);
    }
	
}
