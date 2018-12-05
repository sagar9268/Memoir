package com.sagar.memoir;

import java.text.DateFormat;

public class Card {
    private String journalDate;
    private String journalText;
    private int image;

    public Card()
    {
    }

    public Card (String journalDate, String journalText, int image)
    {
        this.journalDate = journalDate;
        this.journalText = journalText;
        this.image = image;
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
