package com.example.languagehelper;

import com.turbomanage.storm.api.Entity;

@Entity
public class Palabra {
	
	public enum Classification {CONNECTOR, PREPOSITION, VERB};
	
	private long id;
	int ord; // ordinal of the word in the file
	String word;
	String locale; // en
	Classification type;
	
	public Palabra() {
		// TODO Auto-generated constructor stub
	}
	
	public Palabra(int ord, String p, String locale, Classification type) {
		// TODO Auto-generated constructor stub
		this.ord = ord;
		this.word = p;
		this.locale = locale;
		this.type = type;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Classification getType() {
		return type;
	}
	public void setType(Classification type) {
		this.type = type;
	}
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}
	
}
