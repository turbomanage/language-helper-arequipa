package com.example.languagehelper;

import com.turbomanage.storm.api.Entity;

@Entity
public class Page {

	private long id;
	private String locale;
	private String title;
	private int pageNum;
	
	public Page() {
		// storm-gen
	}
	
	public Page(int pageNum, String locale, String title) {
		this.setPageNum(pageNum);
		this.locale = locale;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}
