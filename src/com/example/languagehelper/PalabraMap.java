package com.example.languagehelper;

public class PalabraMap {

	String orig, trad;
	private long id;
	private boolean favorite;

	public PalabraMap(long id, String orig, String trad, boolean isFavorite) {
		this.setId(id);
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
	
}
