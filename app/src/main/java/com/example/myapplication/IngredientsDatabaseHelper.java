package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IngredientsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_INGREDIENTS + " (" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT" + ");";

    public IngredientsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
