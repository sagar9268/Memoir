package com.sagar.memoir;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.GRAY;
import static android.text.InputFilter.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int DEFAULT_BIO_LENGTH_LIMIT = 1000;
    public static final int RC_PHOTO_PICKER = 1;
    Button editProfileButton;
    Button saveProfileButton;
    Button profilePicChangeButton;
    ImageView profilePic;

    private TextView mNameTitle;
    private TextView mNameEditTextView;
    private TextView mGenderEditTextView;
    private TextView mDOBEditTextView;
    private TextView mBioEditTextView;

    private EditText mNameEdit;
    private EditText mBioEdit;
    private EditText mDOBEdit;

    private Spinner editGender;

    private String mGender;

    public static final String USER_PREFS = "mPrefsFile";

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        //Initialize reference to views
        mNameTitle = (TextView) rootView.findViewById(R.id.textViewTitleName);
        mNameEditTextView = (TextView) rootView.findViewById(R.id.textViewName);
        mDOBEditTextView = (TextView) rootView.findViewById(R.id.textViewDOB);
        mGenderEditTextView = (TextView) rootView.findViewById(R.id.textViewGender);
        mBioEditTextView = (TextView) rootView.findViewById(R.id.textViewBio);

        mNameEdit = (EditText) rootView.findViewById(R.id.editTextName);
        mBioEdit = (EditText) rootView.findViewById(R.id.editTextBio);
        mDOBEdit = (EditText) rootView.findViewById(R.id.editDOB);

        editProfileButton = (Button) rootView.findViewById(R.id.buttonEditProfile);
        saveProfileButton = (Button) rootView.findViewById(R.id.buttonSaveProfile);
        profilePicChangeButton = (Button) rootView.findViewById(R.id.buttonImageChange);
        profilePic = (ImageView) rootView.findViewById(R.id.imageViewPerson);

        editGender = (Spinner) rootView.findViewById(R.id.spinnerGender);

        //setting text to text views
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mNameTitle.setText(preferences.getString("sharedName", null));
        mNameEditTextView.setText(preferences.getString("sharedName", null));
        mGenderEditTextView.setText(preferences.getString("sharedGender", null));
        mDOBEditTextView.setText(preferences.getString("sharedDob", null));
        mBioEditTextView.setText(preferences.getString("sharedBio", null));

        profilePicChangeButton.setVisibility(View.INVISIBLE);

        //setting profile pic
        if(preferences.getString("sharedProfilePic",null) != null)
        {
            profilePic.setImageURI(Uri.parse(preferences.getString("sharedProfilePic",null)));
        }

        //Setting up Gender spinner
        setUpSpinner();

        //Setting up Date Picker
        setUpPicker();


        //Enable save button when there's text change and check the length of text
        mNameEdit.addTextChangedListener(watcher);
        mBioEdit.addTextChangedListener(watcher);

        mBioEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_BIO_LENGTH_LIMIT)});

        //Layout related code
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change visibility

                profilePicChangeButton.setVisibility(View.VISIBLE);
                mNameEditTextView.setVisibility(View.INVISIBLE);
                mBioEditTextView.setVisibility(View.INVISIBLE);
                editProfileButton.setVisibility(View.INVISIBLE);
                mGenderEditTextView.setVisibility(View.INVISIBLE);
                mDOBEditTextView.setVisibility(View.INVISIBLE);

                //Change visibility
                mNameEdit.setVisibility(View.VISIBLE);
                mBioEdit.setVisibility(View.VISIBLE);
                saveProfileButton.setVisibility(View.VISIBLE);
                editGender.setVisibility(View.VISIBLE);
                mDOBEdit.setVisibility(View.VISIBLE);

                //Set text to edit
                mNameEdit.setText(mNameEditTextView.getText().toString());
                mBioEdit.setText(mBioEditTextView.getText().toString());
                editGender.setSelection(((ArrayAdapter)editGender.getAdapter()).getPosition(mGenderEditTextView.getText().toString()));
                mDOBEdit.setText(mDOBEditTextView.getText().toString());


            }
        });

        final SharedPreferences pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change visibility

                profilePicChangeButton.setVisibility(View.INVISIBLE);
                mNameEditTextView.setVisibility(View.VISIBLE);
                mBioEditTextView.setVisibility(View.VISIBLE);
                editProfileButton.setVisibility(View.VISIBLE);
                mGenderEditTextView.setVisibility(View.VISIBLE);
                mDOBEditTextView.setVisibility(View.VISIBLE);

                //Change visibility
                mNameEdit.setVisibility(View.INVISIBLE);
                mBioEdit.setVisibility(View.INVISIBLE);
                saveProfileButton.setVisibility(View.INVISIBLE);
                editGender.setVisibility(View.INVISIBLE);
                mDOBEdit.setVisibility(View.INVISIBLE);



                //set text to text views
                mNameTitle.setText(mNameEdit.getText().toString());
                mNameEditTextView.setText(mNameEdit.getText().toString());
                mBioEditTextView.setText(mBioEdit.getText().toString());
                mGenderEditTextView.setText(mGender);
                mDOBEditTextView.setText(mDOBEdit.getText().toString());


                //updating shared preferences
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("sharedName", mNameEdit.getText().toString());
                edit.putString("sharedBio", mBioEdit.getText().toString());
                edit.putString("sharedGender",mGender);
                edit.putString("sharedDob",mDOBEdit.getText().toString());
                edit.apply();

                mListener.onProfileSettingsSaveFragmentInteraction();
            }
        });

        profilePicChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
            }
        });

        return rootView;
    }

    private void setUpPicker() {
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,day);
                String myFormat = "dd MMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mDOBEdit.setText(sdf.format(calendar.getTime()));
            }
        };

        mDOBEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)).show();
                saveProfileButton.setEnabled(true);
            }
        });
    }

    private void setUpSpinner() {
        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(getContext(),R.array.array_gender_options,android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGender.setAdapter(genderAdapter);
        editGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mGender = (String) editGender.getItemAtPosition(i);
                saveProfileButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mGender = "Others";
            }
        });
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //if(s.toString().trim().length()>0){
            //  saveProfileButton.setEnabled(true);
            //} else {
            //  saveProfileButton.setEnabled(false);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length()<3) {
                saveProfileButton.setEnabled(false);
                saveProfileButton.setBackgroundColor(GRAY);
            } else {
                saveProfileButton.setEnabled(true);
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            //String path = getPathFromURI(selectedImage);
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = this.getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //updating shared preferences
            SharedPreferences pref = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("sharedProfilePic", picturePath);
            edit.apply();

            profilePic.setImageURI(Uri.parse(picturePath));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileSettingsSaveFragmentInteraction();
    }
}
