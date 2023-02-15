package com.zonesoft.persons.repositories;


import java.util.List;

//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import org.springframework.data.mongodb.repository.Tailable;
//import org.springframework.stereotype.Repository;

import com.zonesoft.persons.models.Person;

import reactor.core.publisher.Flux;

//@Repository
public interface PersonRepository extends ReactiveMongoRepository<Person, String> {
	
	public Flux<Person> findByMoniker(String moniker);
	
	public Flux<Person> findByIdIn(List<String> ids);
	
//	  @Tailable
//	  @Query("{}")
//	  Flux<Person> streamAll();

}
