package edu.galileo.android.flashcard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SAMSUNG on 1/07/2017.
 */
public class CardDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cardlist.db";

    public CardDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TODO_TABLE = "CREATE TABLE " + CardListContract.CardEntry.TABLE_NAME + " (" +
                CardListContract.CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CardListContract.CardEntry.COLUMN_QUESTION + " TEXT NOT NULL, " +
                CardListContract.CardEntry.COLUMN_ANSWER + " TEXT NOT NULL, " +
                CardListContract.CardEntry.COLUMN_DATETEXT + " INTEGER, " +
                CardListContract.CardEntry.COLUMN_DUE_DATE_TEXT + " INTEGER, " +
                "UNIQUE (" + CardListContract.CardEntry.COLUMN_QUESTION + ", " + CardListContract.CardEntry.COLUMN_DATETEXT + ") ON " +
                "CONFLICT IGNORE" +
                " );";

        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + CardListContract.CardEntry.TABLE_NAME);
        onCreate(db);
    }
}
