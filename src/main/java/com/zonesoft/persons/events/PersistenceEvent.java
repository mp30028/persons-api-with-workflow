package com.zonesoft.persons.events;



import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zonesoft.persons.models.Person;
import com.zonesoft.persons.utils.ToStringBuilder;

import static com.zonesoft.persons.utils.ToStringBuilder.*;


@Document(collection = "persistenceEvents")
public class PersistenceEvent {
	@Id private String id;
	private List<Person>  persons;
	private PersistenceEventType eventType;
	
	public enum PersistenceEventType {
		SAVE,
		SAVE_ALL,
		DELETE,
		DELETE_ALL,
		OTHER
	}
	
	public PersistenceEvent(PersistenceEventType eventType, List<Person>  persons) {
		this.persons = persons;
		this.eventType = eventType;
	}
	
	public PersistenceEvent() {
		this.persons = null;
		this.eventType = PersistenceEventType.OTHER ;
	}
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public PersistenceEventType getEventType() {
		return eventType;
	}

	public void setEventType(PersistenceEventType eventType) {
		this.eventType = eventType;
	}

	public List<Person>  getPersons() {
		return persons;
	}
	
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {		
		return new ToStringBuilder()
		.build(
				lBrace, newline,
					indent, key("persons"), objectValue(this.persons), comma, newline,
					indent, key("persistence-event-type"), value(this.eventType), newline,
				rBrace
		);
	}
}


