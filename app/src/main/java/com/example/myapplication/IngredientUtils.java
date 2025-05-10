package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class allows us to receive ingredient information
 */
public class IngredientUtils {
    /**
     * This returns the ingredients in the user's storage
     * @param context where the data is required
     * @return a list of ingredients
     */
    public static HashSet<String> getUserIngredients(Context context) {
        IngredientsDatabaseHelper dbHelper = new IngredientsDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        HashSet<String> userIngredients = new HashSet<>();
        Cursor cursor = db.query(
                IngredientsDatabaseHelper.TABLE_INGREDIENTS,
                new String[]{IngredientsDatabaseHelper.COLUMN_NAME},
                null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            userIngredients.add(cursor.getString(0).toLowerCase().trim());
        }

        cursor.close();
        db.close();
        return userIngredients;
    }
}
