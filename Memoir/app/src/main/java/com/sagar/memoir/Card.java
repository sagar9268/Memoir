package com.sagar.memoir;

import java.text.DateFormat;

public class Card {
    public static final String TABLE_NAME = "cards";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JOURNAL = "journal";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IMAGE = "image";

    private int id;
    private String journalDate;
    private String journalText;
    private int image;

    //Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TIMESTAMP + "TEXT,"
            + COLUMN_JOURNAL + "TEXT,"
            + COLUMN_IMAGE + "INTEGER"
            + ")";

    public Card()
    {
    }

    public Card (int id, String journalDate, String journalText, int image)
    {
        this.id = id;
        this.journalDate = journalDate;
        this.journalText = journalText;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    public String getJournalDate() {
        return journalDate;
    }

    public void setJournalText(String journalText) {
        this.journalText = journalText;
    }

    public String getJournalText() {
        return journalText;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
