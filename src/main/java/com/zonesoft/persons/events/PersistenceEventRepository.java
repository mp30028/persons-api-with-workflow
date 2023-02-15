package com.zonesoft.persons.events;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface PersistenceEventRepository extends ReactiveMongoRepository<PersistenceEvent, String> {

	  @Tailable
	  @Query("{}")
	  Flux<PersistenceEvent> streamAll();
}
