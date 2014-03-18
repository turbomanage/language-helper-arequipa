package com.example.languagehelper.db;

import android.content.Context;

import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.api.Database;
import com.turbomanage.storm.api.DatabaseFactory;

@Database(name = "palabrasDb", version = 1)
public class PalabrasDb extends DatabaseHelper {

	public PalabrasDb(Context ctx, DatabaseFactory dbFactory) {
		super(ctx, dbFactory);
	}

	@Override
	public UpgradeStrategy getUpgradeStrategy() {
		return UpgradeStrategy.DROP_CREATE;
	}

}
