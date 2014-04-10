package com.example.languagehelper;

import java.util.ArrayList;
import java.util.List;

import com.example.languagehelper.Palabra.Classification;
import com.turbomanage.storm.api.Entity;

@Entity
public class WordGroup {

	private long id;
	private String name;
	transient private List<WordPair> words = new ArrayList<WordPair>();
	private Classification classification;
	private String locale;

	public WordGroup() {
		// for storm-gen
	}
	
	public WordGroup(String name, Classification classification, String locale) {
		this.name = name;
		this.setClassification(classification);
		this.setLocale(locale);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WordPair> getWords() {
		return words;
	}

	public void setWords(List<WordPair> list) {
		this.words = list;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}