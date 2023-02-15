package com.zonesoft.persons.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import com.zonesoft.persons.utils.ToStringBuilder;
import static com.zonesoft.persons.utils.ToStringBuilder.*;

@Document(collection = "persons")
public class Person {
	
	@Id private String id;
	private String moniker;
	private String firstname;
	private String lastname;
	private List<OtherName> otherNames = new ArrayList<>();
	
	public Person(String id, String moniker, String firstname, String lastname) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.moniker = moniker;
	}
	
	public Person(String moniker, String firstname, String lastname) {
		super();
		this.id = null;
		this.firstname = firstname;
		this.lastname = lastname;
		this.moniker = moniker;
	}
	
	public Person() {
		super();
		this.id = null;
		this.firstname = null;
		this.lastname = null;
		this.moniker = null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMoniker() {
		return moniker;
	}
	
	public void setMoniker(String moniker) {
		this.moniker = moniker;
	}
	
	public List<OtherName> otherNames(){
		return this.otherNames;
	}
	
	public List<OtherName> getOtherNames(){
		return this.otherNames;
	}
	
	@Override
	public String toString() {		
		return new ToStringBuilder()
		.build(
				lBrace, newline,
					indent, key("id"), value(this.id), comma, newline,
					indent, key("moniker"), value(this.moniker), comma, newline,
					indent, key("firstname"), value(this.firstname), comma, newline,
					indent, key("lastname"), value(this.lastname), comma, newline,
					indent, key("other-names"), objectValue(this.otherNames), newline,
				rBrace
		);
	}

}
