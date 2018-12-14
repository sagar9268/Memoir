package com.sagar.memoir;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.graphics.Color.GRAY;

public class JournalEntryActivity extends AppCompatActivity {

    private TextView mDate;
    private EditText mJournal;
    private Button mCancelButton;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //intialize reference to views
        mDate = (TextView) findViewById(R.id.TodayDate);
        mJournal = (EditText) findViewById(R.id.EditTextJournalEntry);
        mCancelButton = (Button) findViewById(R.id.buttonCancel);
        mSaveButton = (Button) findViewById(R.id.buttonSave);

        //Enable save button when there's text change and check the length of text
        mJournal.addTextChangedListener(watcher);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JournalEntryActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO - Save the data in database

                Toast.makeText(JournalEntryActivity.this,"Journal Entry Added", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(JournalEntryActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //getting current date to display
        String currentDate = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault()).format(new Date());
        mDate.setText(currentDate);
        
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //if(s.toString().trim().length()>0){
            //  mSaveButton.setEnabled(true);
            //} else {
            //  mSaveButton.setEnabled(false);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length()<3) {
                mSaveButton.setEnabled(false);
                mSaveButton.setBackgroundColor(GRAY);
            } else {
                mSaveButton.setEnabled(true);
                mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

    };
}
