package com.example.languagehelper;

import java.util.ArrayList;
import java.util.List;

import com.turbomanage.storm.api.Entity;

@Entity
public class WordGroup {

	private long id;
	private String name;
	transient private List<WordPair> words = new ArrayList<WordPair>();
	private long pageId;
	
	public WordGroup() {
		// for storm-gen
	}
	
	public WordGroup(String name, long pageId) {
		this.name = name;
		this.setPageId(pageId);
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

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

}