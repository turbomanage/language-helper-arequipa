package com.example.languagehelper;

import com.example.languagehelper.Palabra.Classification;

public interface LanguageModel {
	public String getSelectedLocale();
	public Classification getClassificationForTab(int ord);
	public int getNumTabs();
	public String getTitleForTab(int ord);
}