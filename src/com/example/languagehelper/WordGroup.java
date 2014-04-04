package com.example.languagehelper;

import java.util.ArrayList;
import java.util.List;

public class WordGroup {

	private String name;
	private List<PalabraMap> words;

	public WordGroup(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PalabraMap> getWords() {
		return words;
	}

	public void setWords(List<PalabraMap> list) {
		this.words = list;
	}

}