package com.zonesoft.persons.data_generators;

import static com.zonesoft.persons.data_generators.Generator.generateRandomInt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RecordsGeneratorTemplate<B extends IRecordBuilder<T>,T> {
	//Defaults
	private static final int MIN_RECORDS_DEFAULT = 2;
	private static final int MAX_RECORDS_DEFAULT = 10;
	
	private int minimumNumberOfRecords = MIN_RECORDS_DEFAULT;
	private int maximumNumberOfRecords = MAX_RECORDS_DEFAULT;
	
	public RecordsGeneratorTemplate<B,T> minRecords(int minimumNumberOfRecords) {
		this.minimumNumberOfRecords = minimumNumberOfRecords;
		return this;
	}
	
	public RecordsGeneratorTemplate<B, T> maxRecords(int maximumNumberOfRecords) {
		this.maximumNumberOfRecords = maximumNumberOfRecords;
		return this;
	}
	
	
	public List<T> generate(Supplier<B> supplier){
		int listSize = generateRandomInt(minimumNumberOfRecords, maximumNumberOfRecords);
		List<T> records = new ArrayList<>();		
		for(int j=0; j < listSize ; j++) {
			B builder = supplier.get();
			T record = builder.build();
			records.add(record);
		}
		return records;
	}

}
