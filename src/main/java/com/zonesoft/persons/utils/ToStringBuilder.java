package com.zonesoft.persons.utils;

import java.util.Objects;

public class ToStringBuilder {
	
	private StringBuilder stringBuilder;
	
	public ToStringBuilder() {
		super();
		this.stringBuilder = new StringBuilder();
	}
	
	public String build(String ... args) {
		for(String arg: args) {
			stringBuilder.append(arg);
		}
		return stringBuilder.toString();
	}
	
	public static final String comma = ",";
	public static final String lBrace = "{";
	public static final String rBrace = "}";
	public static final String indent = "\t";
	public static final String newline = "\n";
	public static final String quote = "\"";

	
	public static String key(String k) {
		if(k.isBlank()) { 
			//if string is null, empty, or only whitespace
			return "\"key_invalid\": ";
		}else{ 
			return "\"" + k + "\": ";
		}
	}
	
	public static String objectValue(Object in) {
		if(Objects.isNull(in)) {
			return "null";
		}else{ 
			return in.toString();
		}
	};
	
	public static String value(Object in) {
		if(Objects.isNull(in)) {
			return "null";
		}else{ 
			return "\"" + in.toString() + ("\"") ;
		}
	}
}
