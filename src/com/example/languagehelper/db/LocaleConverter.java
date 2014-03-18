package com.example.languagehelper.db;

import java.util.Locale;

import com.turbomanage.storm.api.Converter;
import com.turbomanage.storm.types.TypeConverter;
import com.turbomanage.storm.types.TypeConverter.BindType;
import com.turbomanage.storm.types.TypeConverter.SqlType;

@Converter(bindType = BindType.STRING, forTypes = { Locale.class }, sqlType = SqlType.TEXT)
public class LocaleConverter extends TypeConverter<Locale, String> {

	public static final LocaleConverter GET = new LocaleConverter();
	
	@Override
	public String toSql(Locale javaValue) {
		return javaValue.getLanguage();
	}

	@Override
	public Locale fromSql(String sqlValue) {
		return new Locale(sqlValue);
	}

	@Override
	public String fromString(String strValue) {
		return strValue;
	}

}
