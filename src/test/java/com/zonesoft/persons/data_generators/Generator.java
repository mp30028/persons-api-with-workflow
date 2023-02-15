package com.zonesoft.persons.data_generators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
	
	public enum Gender {
		MALE,
		FEMALE
	}
	
	private static final String[] FEMALE_FIRSTNAMES = { "Nicola", "Karen", "Fiona", "Susan", "Claire", "Sharon",
			"Angela", "Gillian", "Julie", "Michelle", "Jacqueline", "Amanda", "Tracy", "Louise", "Jennifer", "Alison",
			"Sarah", "Donna", "Caroline", "Elaine", "Lynn", "Margaret", "Elizabeth", "Lesley", "Deborah", "Pauline",
			"Lorraine", "Laura", "Lisa", "Tracey", "Carol" };
	
	private static final String[] MALE_FIRSTNAMES = { "David", "John", "Paul", "Mark", "James", "Andrew", "Scott",
			"Steven", "Robert", "Stephen", "William", "Craig", "Michael", "Stuart", "Christopher", "Alan", "Colin",
			"Brian", "Kevin", "Gary", "Richard", "Derek", "Martin", "Thomas", "Neil", "Barry", "Ian", "Jason", "Iain",
			"Gordon", "Alexander", };

	
	private static final String[] SURNAMES = { "SMITH", "BROWN", "WILSON", "THOMSON", "STEWART", "ROBERTSON",
			"CAMPBELL", "ANDERSON", "SCOTT", "TAYLOR", "MACDONALD", "CLARK", "MURRAY", "REID", "MORRISON", "YOUNG",
			"WATSON", "WALKER", "MITCHELL", "PATERSON", "ROSS", "GRAHAM", "MARTIN", "MILLER", "KERR", "JOHNSTON",
			"DAVIDSON", "HENDERSON", "HUNTER", "MCDONALD", "BELL", "FRASER", "HAMILTON", "GRAY", "DUNCAN", "FERGUSON",
			"KELLY", "CAMERON", "MACKENZIE", "SIMPSON", "MACLEOD", "ALLAN", "GRANT", "MCLEAN", "BLACK", "RUSSELL",
			"WALLACE", "MACKAY", "WRIGHT", "GIBSON" };
	
	private static final String[] MALE_MIDDLE_NAMES = {
			"Liam", "Noah", "Oliver", "Elijah", "Benjamin", "Lucas", "Henry", "Mason", "Ethan", "Jacob", "Logan", 
			"Jackson", "Levi", "Sebastian", "Mateo", "Jack", "Owen", "Theodore", "Aiden", "Samuel", "Joseph", "Wyatt", 
			"Matthew", "Luke", "Asher", "Carter", "Julian", "Grayson", "Leo", "Jayden", "Gabriel", "Isaac", "Lincoln", 
			"Anderson", "Ashton", "Avery", "Beckett", "Bond", "Brook", "Cooper", "Davis", "Ford", "Harper", "Harvey", 
			"Huxley", "Jarvis", "Keegan", "Lennox", "Morgan", "Percy", "Quinn", "Riley", "Sawyer", "Sullivan", "Watkins", 
			"Wilder", "Charles", "Kai", "Dean", "Flynn", "Grey", "Reed", "Fred", "Chance", "Felix", "King", "Mark", 
			"Rhys", "Shea", "Leonardo", "Casey", "Gilles", "Ryder", "Tristan", "Myles", "Jude", "Oscar", "Xavier", "Max", 
			"Nico", "Lloyd", "Seth", "Claude", "George", "Ray", "Francis", "Lawrence", "James", "John", "William", "Thomas", 
			"David", "Robert", "Edward", "Peter", "Lee", "Christopher", "Alexander", "Michael", "Daniel", "Anthony", "Allen"	
	};

	private static final String[] FEMALE_MIDDLE_NAMES = {
			"Olivia", "Emma", "Ava", "Sophia", "Isabella", "Charlotte", "Amelia", "Mia", "Harper", "Evelyn", "Abigail", 
			"Emily", "Ella", "Elizabeth", "Camila", "Luna", "Sofia", "Avery", "Mila", "Aria", "Anastasia", "Clothilde", 
			"Martina", "Eva", "Sara", "Anja", "Fernanda", "Luda", "Akari", "Mette", "Terese", "Mariam", "Basma", "Asha", 
			"Addison", "Amber", "Amy", "Bea", "Belle", "Beth", "Blaire", "Blake", "Blythe", "Bree", "Britt", "Camille", 
			"Cassandra", "Christina", "Claire", "Clarissa", "Dawn", "Drew", "Elle", "Eve", "Faith", "Francesca", "Grace", 
			"Hope", "Imogen", "Ingrid", "Jane", "Jess", "Jill", "Jordana", "June", "Kate", "Laura", "Lillian", "Megan", 
			"Meredith", "Michelle", "Nell", "Paige", "Paloma", "Pam", "Rae", "Rose", "Ruth", "Sabrina", "Susanna", 
			"Tabitha", "Tatiana", "Ursula", "Willow", "Louise", "Anne", "May/Mae", "Marie", "Mary", "Catherine", 
			"Victoria", "Lynn", "Renee"
	};
	
	
	private static final String[] NICKNAMES = {
			"Hippy", "Tickle", "Bossy", "Giggles", "Rude", "Bunko", "Clyde", "Creepy", "Crusty", "Dillydale", "Dragoo",
			"Fairies", "Fango", "Featherhead", "Buzzard", "Carp", "Shrimp", "Spangler", "Mitty", "Jotterbox", "Cous", 
			"Daredevil", "Helppy", "Magic", "Noughts", "Scara", "Drongo", "Hoops", "Mountie", "Bounce", "Funny", "Fussy", 
			"Jelly", "Lazy", "Messy", "Metal", "Noisy", "Nosey", "Quiet", "Scatterbrain", "Smalls", "Strongo", "Stubby", 
			"Tall", "Mummy", "Mustard", "Lope", "Monster", "Snowdrop", "Spotty", "Honk", "Turtle", "Swampy", "Gorro", 
			"Froggy", "Snake", "Tiny", "Speckle"
	};
	
	private static final String[] YEARS = {
			"1937", "1938", "1939", "1940", "1941", "1942", "1943", "1944", "1945", "1946", 
			"1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", 
			"1957", "1958", "1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", 
			"1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", 
			"1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", 
			"1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", 
			"1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", 
			"2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", 
			"2017", "2018", "2019", "2020"
	};
	
	private static final String[] MONTHS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
	
	private static final String[] DAYS = {
			"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", 
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
	};
	
	private static final String[] HOURS = {
			"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", 
			"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "00"
	};
	
	private static final String[] MINUTES_OR_SECONDS = {
			"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", 
			"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", 
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", 
			"46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "00"
	};
	
	private static final String[] PHRASES = {
			"To be on cloud nine – To be extremely happy", 
			"One-trick pony – A person with only one talent or area of expertise", 
			"Wouldn’t hurt a fly – A person that is inoffensive and harmless", 
			"Like a fish out of the water – Very uncomfortable", 
			"Fit as a fiddle – Very healthy and strong", 
			"To have your head in the clouds – To be daydreaming and/or lacking concentration", 
			"To be under the weather – To feel sick", 
			"To be as right as rain – To feel healthy or well again", 
			"Eager beaver – A person who is enthusiastic and wanting to do something very much", 
			"Teacher’s pet – A person who is considered the teacher’s favorite. This can be used in a positive or negative way depending on the context", 
			"Gold digger – A person who is pursuing a relationship with another for the sole purpose of benefiting from their wealth", 
			"Party pooper – Someone who tends to suck the fun out of situations by either not participating or adding negativity", 
			"Like two peas in a pod – Two people who are always together", 
			"To give someone the cold shoulder – To intentionally ignore someone", 
			"To cut somebody some slack – To stop being so critical of them", 
			"To give someone the benefit of the doubt – To justify or excuse someone’s actions, and not assume malice", 
			"To let someone off the hook – To not hold someone responsible for something he/she has done wrong", 
			"To rain on someone’s parade – To ruin one’s plans or temper one’s excitement", 
			"To get off on the wrong foot – To make a bad first impression with someone", 
			"To keep someone at arm’s distance – To keep your distance and not get too involved with someone", 
			"To rub someone the wrong way – To irritate or get on someone’s nerves", 
			"To bend over backward for someone – To go out of your way to do something for them", 
			"To burn a bridge – To ruin a relationship to the point that it cannot be repaired", 
			"To break the ice – To get the conversation going", 
			"To let the cat out of the bag – To reveal a secret", 
			"To spill the beans – To reveal a secret", 
			"To beat around the bush – To avoid talking about what is important", 
			"To pull someone’s leg – To say something that is not true as a way of joking", 
			"To get wind of something – To hear a rumor about something", 
			"To wrap your head around something – To understand something complicated", 
			"A penny for your thoughts – Tell me what you are thinking", 
			"To play the devil’s advocate – To argue against an idea for the sake of debate", 
			"To see which way the wind is blowing – To try to discover information about a situation before taking action", 
			"To hear something straight from the horse’s mouth – To hear from someone who personally observed a certain event", 
			"The elephant in the room – An obvious problem that people do not want to talk about", 
			"Comparing apples to oranges – Comparing two things that cannot be compared", 
			"To get your wires crossed – To misunderstand another person particularly because you thought that they were talking about one thing when they were actually talking about another thing", 
			"To be left in the dark – When someone doesn’t receive all the appropriate information that tells the whole story", 
			"To go around in circles – When you repeat the same things over again in a conversation without coming to a conclusion or resolution", 
			"A blessing in disguise – A good thing that seemed bad at first", 
			"The best of both worlds – Benefiting from two different opportunities at once", 
			"A perfect storm – The worst possible situation", 
			"To be on thin ice – To be in a risky situation", 
			"A snowball effect – A situation that becomes more serious and potentially dangerous over time", 
			"When it rains it pours – Everything is going wrong at once", 
			"To get out of hand – To lose control in a situation", 
			"To get a taste of your own medicine – To be treated the way you’ve treated others", 
			"To throw caution to the wind – To do something without worrying about the risk", 
			"To bite the bullet – To force yourself to do something unpleasant or difficult", 
			"Barking up the wrong tree – To pursue the wrong course of action", 
			"To go down in flames – To fail miserably at something", 
			"Best thing since sliced bread – To praise something for being especially great", 
			"Safe bet – Something that is sure to succeed", 
			"In full swing – Something that is currently in process and moving efficiently along", 
			"Up in the air  – Something that is uncertain or still undecided", 
			"Hold your horses – Wait a moment; slow down", 
			"To do something at the drop of a hat – To do something at once, without any delay", 
			"Once in a blue moon – Rarely", 
			"To take a rain check – To postpone a plan", 
			"To have bigger fish to fry – To have more important things to do with your time", 
			"To miss the boat – To miss an opportunity", 
			"Call it a day – It’s time to stop working on something", 
			"Round-the-clock – Something that is ongoing for 24-hours a day", 
			"Kill time  – To do something for the sake of passing the time while you’re waiting for another thing to occur", 
			"Time flies  – To express that time passes quickly", 
			"Better late than never  – It’s better to do something late than not doing it at all", 
			"At the eleventh hour  – When you complete something at the very last minute before it’s too late", 
			"Third time’s a charm  – To describe that the first two times did not work, but it will work on your third try", 
			"It’s raining cats and dogs – It’s raining very hard", 
			"A dime a dozen – Something is very common, or of no particular value", 
			"By the skin of one’s teeth – Narrowly or barely escaping a disaster", 
			"Come rain or shine – No matter the circumstances, something will get done", 
			"It costs an arm and a leg – It’s very expensive", 
			"It went to the dogs – Something is no longer as good as it was in the past", 
			"To run like the wind – To run very fast", 
			"Go on a wild goose chase – Go on a futile search or pursuit", 
			"A cloud on the horizon – Something that threatens to cause problems in the future", 
			"Hit the nail on the head – To do something exactly right", 
			"Piece of cake – An especially easy task", 
			"Steal one’s thunder – To take credit for someone else’s work or achievements", 
			"Through thick and thin – To experience both the good and bad times", 
			"Better late than never – It is better to be late than never to arrive or complete a task", 
			"Time flies when you’re having fun – Time seems to move faster when you’re enjoying something", 
			"Actions speak louder than words – What someone does means more than what they say they will do", 
			"Don’t count your chickens before they hatch – Don’t make plans that depend on something good happening before you know that it has actually happened", 
			"Every cloud has a silver lining – Difficult situations usually have at least one positive aspect", 
			"Don’t put all your eggs in one basket – Don’t risk everything on the success of one venture", 
			"Good things come to those who wait – Be patient", 
			"Kill two birds with one stone – Achieve two goals at once", 
			"There are other fish in the sea – There will be other opportunities for romance", 
			"You can’t judge a book by its cover – You shouldn’t determine the value of something by its outward appearance", 
			"Curiosity killed the cat – Being inquisitive may get you into trouble", 
			"Birds of a feather flock together – Similar people usually become friends", 
			"Absence makes the heart grow fonder – When the people we love are not with us, we grow even more in love", 
			"It takes two to tango – Both parties involved in a situation are equally responsible for it", 
			"The ship has sailed – It’s too late", 
			"Two wrongs don’t make a right – If someone has done something bad to you, there’s no justification to act in a similar way", 
			"When in Rome, do as the Romans do – When you are visiting another place, you should follow the customs of the people in that place", 
			"The early bird catches the worm – The one who takes the earliest opportunity to do something will have an advantage over others", 
			"Save up for a rainy day – Put some money aside for whenever it may be needed", 
			"An apple a day keeps the doctor away – Apples are good for your health", 
			"Your guess is as good as mine – I’m unsure of the answer or solution to a problem", 
			"It takes one to know one – Someone must have a bad quality themselves if they can recognize it in other people", 
			"Look before you leap – Take calculated risks", 
			"Don’t cry over spilled milk – Stop worrying about things in the past because they cannot be changed", 
			"You can lead a horse to water, but you can’t make him drink – You can’t force someone to make the right decision, even after guidance is given", 
			"A bird in the hand is worth two in the bush – The things you already have are more valuable than those you hope to get", 
			"You can catch more flies with honey than you can with vinegar – You can get what you want by being nice", 
			"All good things come to an end  – The good times won’t last forever", 
			"A watched pot never boils – Constantly checking on something won’t make it happen faster. Give it time and trust the process", 
			"Beggars can’t be choosers – If you are in a bind, you can’t be picky when someone offers you some help"
	};
	
	public static int generateRandomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	private static String adjustDayForLeapYear(String yearString) {
	      int year = Integer.parseInt(yearString) ;
	      if (((year % 4 == 0) && (year % 100!= 0)) || (year%400 == 0))
	         return "29";
	      else
	         return "28";
	}
	
	private static String adjustDayForMonth(String yearString, String monthString, String dayString) {
		if (monthString.equals("02")) {
			if (dayString.equals("29") || dayString.equals("30") || dayString.equals("31")) {
				return adjustDayForLeapYear(yearString);
			}else {
				return dayString;
			}
		}else if (monthString.equals("04") || monthString.equals("06") ||monthString.equals("09") ||monthString.equals("11")) {
			if (dayString.equals("31")) {
				return "30";
			}else {
				return dayString;
			}
		}else {
			return dayString;
		}
	}
	
	public static Gender generateGender() {
		int outcome = generateRandomInt(1, 2);
		Gender gender = null;
		switch(outcome){
			case 1: gender = Gender.MALE;break;
			case 2: gender = Gender.FEMALE;
		}
		return gender;
	}
	
	public static String generateFirstName(Gender gender) {
		String[] lookupValues;
		if (gender == Gender.MALE) {
			lookupValues = MALE_FIRSTNAMES;
		}else {
			lookupValues = FEMALE_FIRSTNAMES;
		}
		return lookupValues[generateRandomInt(0, lookupValues.length-1)];
	}
	
	public static String generateMiddleName(Gender gender) {
		String[] lookupValues;
		if (gender == Gender.MALE) {
			lookupValues = MALE_MIDDLE_NAMES;
		}else {
			lookupValues = FEMALE_MIDDLE_NAMES;
		}
		return lookupValues[generateRandomInt(0, lookupValues.length-1)];
	}
	
	public static String[] generateMiddleNames(Gender gender) {
		final int MAX_NUMBER_OF_NAMES = 5;
		final int numberOfNames = generateRandomInt(0, MAX_NUMBER_OF_NAMES -1);
		String[] names = new String[numberOfNames];
		for (int j=0; j < numberOfNames; j++) {
			names[j]= generateMiddleName(gender);
		}
		return names;
	}
	
	public static String generateLastName() {
		return SURNAMES[generateRandomInt(0, SURNAMES.length-1)];
	}
	
	public static String generateNickname() {
		return NICKNAMES[generateRandomInt(0, NICKNAMES.length-1)];
	}
	
	public static OffsetDateTime generateDate() {
		String yearString = YEARS[generateRandomInt(0, YEARS.length-1)];
		String monthString = MONTHS[generateRandomInt(0, MONTHS.length-1)]; 
		String dayString = adjustDayForMonth(yearString, monthString, DAYS[generateRandomInt(0, DAYS.length-1)]);
		//text to parse example "2007-12-03T10:15:30+01:00" 
		return OffsetDateTime.parse(yearString + "-" + monthString + "-" + dayString + "T00:00:00+00:00");
	}
	
	public static OffsetDateTime generateDateTime() {
		String yearString = YEARS[generateRandomInt(0, YEARS.length-1)];
		String monthString = MONTHS[generateRandomInt(0, MONTHS.length-1)]; 
		String dayString = adjustDayForMonth(yearString, monthString, DAYS[generateRandomInt(0, DAYS.length-1)]);
		String hourString =  HOURS[generateRandomInt(0, HOURS.length-1)];
		String minutesString =  MINUTES_OR_SECONDS[generateRandomInt(0, MINUTES_OR_SECONDS.length-1)];
		String secondsString =  MINUTES_OR_SECONDS[generateRandomInt(0, MINUTES_OR_SECONDS.length-1)];

		return OffsetDateTime.parse(yearString + "-" + monthString + "-" + dayString + "T" + hourString + ":" + minutesString + ":" + secondsString + "+00:00");
	}
	
	public static String generatePhrase() {
		 return PHRASES[generateRandomInt(0, PHRASES.length-1)];
	}
	
	public static long generateLongID() {
		long min = 99999L;
		long max = 1000000L;
		return ThreadLocalRandom.current().nextLong(min, max);
	}
	
	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
	
	public static <T> List<T> randomSubset(List<T> fullList, int minSize, int maxSize) {
		int subsetSize = generateRandomInt(minSize, maxSize);
		List<T> subset = new ArrayList<>();
		boolean[] scoreCard = new boolean[fullList.size()];
		for(int j=0; j < subsetSize;j++) {
			int selectedIndex = unusedIndex(scoreCard);
			subset.add(fullList.get(selectedIndex));
		}
		return subset;
	}

	private static int unusedIndex(boolean[] scoreCard) {
		int selectedIndex = generateRandomInt(0, scoreCard.length-1);
		while (scoreCard[selectedIndex]) {
			selectedIndex++;
			if (selectedIndex >= scoreCard.length) selectedIndex=0;
		}
		scoreCard[selectedIndex]=true;
		return selectedIndex;
	}
	
	public static String inputStreamToString(InputStream inputStream) throws IOException {
	    try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = inputStream.read(buffer)) != -1) {
	            result.write(buffer, 0, length);
	        }
	        return result.toString();
	    }
	}
	
}
