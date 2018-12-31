package com.sagar.memoir;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JournalEntryActivity extends AppCompatActivity {

    private TextView mDate;
    private EditText mJournal;
    private Button mCancelButton;
    private Button mSaveButton;
    private Button mImageButton;
    private ImageButton mSpeechButton;
    private ImageView mImageView;
    private ActionBar mAb;
    private DatabaseHelper db;
    private String KEY ="date";
    private int flag;

    private final int REQ_CODE_IMAGE_INPUT = 1;
    private final int REQ_CODE_SPEECH_INPUT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize reference to views
        mDate = (TextView) findViewById(R.id.TodayDate);
        mJournal = (EditText) findViewById(R.id.EditTextJournalEntry);
        mCancelButton = (Button) findViewById(R.id.buttonCancel);
        mSaveButton = (Button) findViewById(R.id.buttonSave);
        mImageButton = (Button) findViewById(R.id.buttonAddImage);
        mSpeechButton = (ImageButton) findViewById(R.id.buttonSpeech);
        mImageView = (ImageView) findViewById(R.id.addImage);
        mAb = getSupportActionBar();
        mAb.setTitle("Memoir");
        flag = 1;

        db = new DatabaseHelper(this);

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

        //getting current date to display
        Bundle dates = getIntent().getExtras();
        final String currentDate;
        if(dates == null){
            currentDate = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault()).format(new Date());
        }
        else{
            currentDate = dates.getString(KEY);
        }
        mDate.setText(currentDate);

        mSaveButton.setEnabled(false);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save the data in database
                Drawable drawable = mImageView.getDrawable();
                createJournal(mJournal.getText().toString(),currentDate,drawable);

                Toast.makeText(JournalEntryActivity.this,"Journal Entry Added", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(JournalEntryActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 1){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , REQ_CODE_IMAGE_INPUT);
                    mImageButton.setText("Remove Image");
                    flag = 0;
                }
                else {
                    mImageView.setImageURI(null);
                    mImageButton.setText("Add Image");
                    flag = 1;
                }
            }
        });

        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.enter_journal_message));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case REQ_CODE_IMAGE_INPUT:
                if(resultCode == RESULT_OK && data != null)
                {
                    Uri selectedImage = data.getData();
                    mImageView.setImageURI(selectedImage);
                }
                break;
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mJournal.setText(result.get(0));
                }
                break;
        }
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
               // mSaveButton.setBackgroundColor(getResources().getColor(R.color.grey));
               // mSaveButton.setTextColor(getResources().getColor(R.color.black));
            } else {
                mSaveButton.setEnabled(true);
               // mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
               // mSaveButton.setTextColor(getResources().getColor(R.color.white));
            }
        }

    };

    //Inserting new journal in database
    private void createJournal(String journal, String date, Drawable drawable) {
        // inserting journal in db and getting
        // newly inserted journal id
        long id = db.insertJournal(journal, date, drawable);
/*
        // get the newly inserted journal from db
        Card n = db.getCard(id);

        if (n != null) {
            // adding new note to array list at 0 position
            cardList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

        }
        */
    }
}