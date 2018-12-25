package com.sagar.memoir;

import android.graphics.drawable.Drawable;

public class Card {
    public static final String TABLE_NAME = "cards";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JOURNAL = "journal";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IMAGE = "image";

    private int id;
    private String journalDate;
    private String journalText;
    private Drawable drawable;

    //Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_JOURNAL + " TEXT, "
            + COLUMN_IMAGE + " TEXT "
            + ");";

    public Card()
    {
    }

    public Card (int id, String journalDate, String journalText, Drawable drawable)
    {
        this.id = id;
        this.journalDate = journalDate;
        this.journalText = journalText;
        this.drawable = drawable;
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

    public void setPictureData(byte[] pictureData) {
        drawable = ImageUtils.byteToDrawable(pictureData);
    }

    public byte[] getPictureData(){
        return ImageUtils.drawableToByteArray(drawable);
    }

    public Drawable getImage(){ return drawable; }
}
