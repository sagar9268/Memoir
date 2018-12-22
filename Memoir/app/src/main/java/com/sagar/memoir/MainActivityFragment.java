package com.sagar.memoir;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private CardsAdapter adapter;
    private List<Card> cardList;
    private List<Object> objectList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFloatingActionButton;
    private DatabaseHelper db;

    HashMap<String,Date> map;

    private OnFragmentInteractionListener mListener;
    public MainActivityFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        //initialize reference to views
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionAddJournalButton);

        cardList = new ArrayList<>();
        objectList = new ArrayList<>();
        map = new HashMap<>();
        db = new DatabaseHelper(this.getActivity());

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), JournalEntryActivity.class);
                startActivity(i);
            }
        });

        cardList.addAll(db.getAllCards());

        for(Card c : cardList)
        {
            try {
                addCard(c);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        adapter = new CardsAdapter(this.getActivity(), objectList);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //prepare random cards for checking
        try {
            prepareCards();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Sorting the cards
        Collections.sort(objectList,CARD_COMPARATOR);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingFragment.OnFragmentInteractionListener) {
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

    /**
     * Adding few cards for testing
     */
    private void prepareCards() throws ParseException {
        /*
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};
        */
/*
        //getting current date to display
        String currentDate = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault()).format(new Date());


        DateFormat formatter = new SimpleDateFormat("E, MMM d, yyyy",Locale.getDefault());
        String entryDate;

        //creating an object of card class
        Card a = new Card(1,currentDate,"Hello World !",R.drawable.test_image);
        cardList.add(a);

        a = new Card(2,currentDate,"Hi there",R.drawable.test_image);
        cardList.add(a);

        a = new Card(3,currentDate,"It's my first journal entry.",R.drawable.test_image);
        cardList.add(a);


        entryDate = "Mon, Dec 17, 2018";
        a = new Card(entryDate,"It's my first journal entry.",R.drawable.test_image);
        addCard(a);
*/
        adapter.notifyDataSetChanged();
    }

    private void addCard(Card card) throws ParseException {
        String stringDate = card.getJournalDate();
        DateFormat formatter = new SimpleDateFormat("E, MMM d, yyyy");
        DateFormat monthFormatter =new SimpleDateFormat("MMMM");
        DateFormat yearFormatter = new SimpleDateFormat("MMMM yyyy");
        Date date = (Date)formatter.parse(stringDate);
        String month = monthFormatter.format(date);
        String monthWithYear = yearFormatter.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        date = calendar.getTime();
        String lastDate = formatter.format(date);

        //Checking if entry has been made this month
        if(!map.containsKey(monthWithYear)) {
            map.put(monthWithYear, date);
            objectList.add(new MonthCard(lastDate, month));
        }
        objectList.add(card);


    }

    //Comparator for Sorting
    static final Comparator<Object> CARD_COMPARATOR = new Comparator<Object>() {
                public int compare(Object card1, Object card2) {
                    String stringDate1;
                    String stringDate2;
                    if(card1 instanceof Card)
                        stringDate1 = ((Card) card1).getJournalDate();
                    else
                        stringDate1 = ((MonthCard) card1).getJournalDate();
                    if(card2 instanceof Card)
                        stringDate2 = ((Card) card2).getJournalDate();
                    else
                        stringDate2 = ((MonthCard) card2).getJournalDate();
                    DateFormat format = new SimpleDateFormat("E, MMM d, yyyy");
                    try {
                        Date date1 = (Date)format.parse(stringDate1);
                        Date date2 = (Date)format.parse(stringDate2);
                        return date2.compareTo(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
