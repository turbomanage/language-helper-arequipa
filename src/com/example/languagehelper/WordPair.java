package com.example.languagehelper;

public class WordPair {

	private Palabra orig, trad;

	public WordPair(Palabra orig, Palabra trad) {
		this.orig = orig;
		this.trad = trad;
	}

	public Palabra getOrig() {
		return orig;
	}

	public Palabra getTrad() {
		return trad;
	}
	
}
