package com.sagar.memoir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "journals.db";
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //create journals table
        db.execSQL(Card.CREATE_TABLE);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + Card.TABLE_NAME);
        //create tables again
        onCreate(db);
    }

    public long insertJournal(String journal, String date)
    {
        //get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //id will be inserted automatically, no need to add them
        values.put(Card.COLUMN_JOURNAL, journal);
        values.put(Card.COLUMN_TIMESTAMP, date);
        //TODO
        //code to insert images here
        values.put(Card.COLUMN_IMAGE,R.drawable.test_image);
        //insert row
        long id = db.insert(Card.TABLE_NAME, null, values);
        //close db connection
        db.close();
        //return newly inserted row id
        return id;
    }

    public Card getCard(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Card.TABLE_NAME,
                new String[]{Card.COLUMN_ID, Card.COLUMN_TIMESTAMP, Card.COLUMN_JOURNAL,Card.COLUMN_IMAGE},
                Card.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        //prepare card object
        Card card = new Card(
                cursor.getInt(cursor.getColumnIndex(Card.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Card.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(Card.COLUMN_JOURNAL)),
                cursor.getInt(cursor.getColumnIndex(Card.COLUMN_IMAGE)));
        //close the db connection
        cursor.close();
        db.close();

        return card;
    }

    public List<Card> getAllCards()
    {
        List<Card> cards = new ArrayList<>();
        //select all query
        String selectQuery = "SELECT * FROM "+ Card.TABLE_NAME + " ORDER BY " +
                Card.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(cursor.moveToFirst())
        {
            do {
                Card card = new Card();
                card.setId(cursor.getInt(cursor.getColumnIndex(Card.COLUMN_ID)));
                card.setJournalDate(cursor.getString(cursor.getColumnIndex(Card.COLUMN_TIMESTAMP)));
                card.setJournalText(cursor.getString(cursor.getColumnIndex(Card.COLUMN_JOURNAL)));
                card.setImage(cursor.getInt(cursor.getColumnIndex(Card.COLUMN_IMAGE)));
                cards.add(card);
            }while (cursor.moveToNext());
        }
        //close db connection
        cursor.close();
        db.close();

        // return cards list
        return cards;
    }

    public int getCardsCount() {
        String countQuery = "SELECT  * FROM " + Card.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Card.COLUMN_JOURNAL, card.getJournalText());
        //getting current date to display in journal entry
        String currentDate = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault()).format(new Date());
        values.put(Card.COLUMN_TIMESTAMP, currentDate);
        //TODO
        //code to insert images here
        values.put(Card.COLUMN_IMAGE,R.drawable.test_image);
        // updating row
        return db.update(Card.TABLE_NAME, values, Card.COLUMN_ID + " = ?",
                new String[]{String.valueOf(card.getId())});
    }

    public void deleteCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Card.TABLE_NAME, Card.COLUMN_ID + " = ?",
                new String[]{String.valueOf(card.getId())});
        db.close();
    }
}
