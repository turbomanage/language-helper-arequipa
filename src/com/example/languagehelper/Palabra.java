package com.example.languagehelper;

import com.turbomanage.storm.api.Entity;

@Entity
public class Palabra {
	
	public enum Classification {CONNECTOR, PREPOSITION, VERB};
	
	private long id;
	int ord; // ordinal of the word in the file
	private String word;
	private String category;
	private boolean favorite;
	private long groupId;
	
	public Palabra() {
		// TODO Auto-generated constructor stub
	}
	
	public Palabra(int ord, String p, long groupId) {
		// TODO Auto-generated constructor stub
		this.ord = ord;
		this.word = p;
		this.setGroupId(groupId);
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
}
