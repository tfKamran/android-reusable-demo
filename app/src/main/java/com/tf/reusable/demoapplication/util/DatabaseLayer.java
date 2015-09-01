package com.tf.reusable.demoapplication.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tf.reusable.demoapplication.model.SimpleKeyValuePair;

import java.util.ArrayList;

/**
 * Created by kamran on 20/8/15.
 */
public class DatabaseLayer {
    private static final String TAG = "DatabaseLayer";
    private static final int VERSION = 1;

    private static SQLiteDatabase database;

    private static class TableOne {
        public static final String TABLE_NAME = "one";

        public static final String ID = "id";
        public static final String KEY = "key";
        public static final String VALUE = "value";

        public static void createTable() {
            executeUpdate("CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY + " TEXT, " + VALUE + " TEXT)");
        }
    }

    public static void initialize(Context context) {
        database = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().getAbsolutePath()
                + "/database.db", null);
        if (!isTablePresent(TableOne.TABLE_NAME))
            TableOne.createTable();

        database.setVersion(VERSION);
    }

    public static void addKeyValue(SimpleKeyValuePair keyValuePair) {
        executeUpdate("INSERT INTO " + TableOne.TABLE_NAME + " (" + TableOne.KEY + ", " + TableOne.VALUE + ") VALUES ('" + keyValuePair.getKey() + "', '" + keyValuePair.getValue() + "')");
    }

    public static void updateKeyValue(SimpleKeyValuePair keyValuePair) {
        executeUpdate("UPDATE " + TableOne.TABLE_NAME + " SET " + TableOne.VALUE + " = '" + keyValuePair.getValue() + "' WHERE " + TableOne.KEY + " = '" + keyValuePair.getValue() + "'");
    }

    public static ArrayList<SimpleKeyValuePair> getAllKeyValues() {
        ArrayList<SimpleKeyValuePair> events = new ArrayList<>();

        Cursor cursor = executeQuery("SELECT * FROM " + TableOne.TABLE_NAME);

        while (cursor.moveToNext()) {
            events.add(new SimpleKeyValuePair(cursor.getString(cursor.getColumnIndex(TableOne.KEY)),
                    cursor.getString(cursor.getColumnIndex(TableOne.VALUE))));
        }

        cursor.close();

        return events;
    }

    public static String getValue(String key) {
        String value = null;

        Cursor cursor = executeQuery("SELECT " + TableOne.VALUE + " FROM " + TableOne.TABLE_NAME + " WHERE " + TableOne.KEY + " = '" + key + "'");

        if (cursor.moveToFirst())
            value = cursor.getString(cursor.getColumnIndex(TableOne.VALUE));

        cursor.close();

        return value;
    }

    public static  void deleteKeyValue(SimpleKeyValuePair keyValuePair) {
        executeUpdate("DELETE FROM " + TableOne.TABLE_NAME + " WHERE " + TableOne.KEY + " = '" + keyValuePair.getKey() + "'");
    }

    private static boolean isTablePresent(String tableName) {
        Cursor cursor = executeQuery("SELECT name FROM sqlite_master WHERE name='" + tableName + "'");

        boolean isPresent = cursor.getCount() > 0;
        cursor.close();

        return isPresent;
    }

    private static Cursor executeQuery(String query) {
        return database.rawQuery(query, null);
    }

    private static void executeUpdate(String statement) {
        database.beginTransaction();

        database.execSQL(statement);

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
