package com.example.languagehelper;

public class WordPair {

	String orig, trad;
	private long id, groupId;
	private boolean favorite;

	public WordPair(long id, long groupId, String orig, String trad, boolean isFavorite) {
		this.setId(id);
		this.setGroupId(groupId);
		this.orig = orig;
		this.trad = trad;
		this.favorite = isFavorite;
	}

	public String getOrig() {
		return orig;
	}

	public void setOrig(String orig) {
		this.orig = orig;
	}

	public String getTrad() {
		return trad;
	}

	public void setTrad(String trad) {
		this.trad = trad;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setGroupId(long groupid) {
		this.groupId = groupid;
	}
	
}
