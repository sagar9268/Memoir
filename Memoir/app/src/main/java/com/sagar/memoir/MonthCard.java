package com.sagar.memoir;

public class MonthCard {
    private String journalDate;
    private String month;

    public MonthCard(){

    }

    public MonthCard(String journalDate, String month){
        this.journalDate = journalDate;
        this.month = month;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getJournalDate() {
        return journalDate;
    }

    public String getMonth() {
        return month;
    }
}
