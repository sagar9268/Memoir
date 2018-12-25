package com.sagar.memoir;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Object> cardList;
    private final int ENTRY = 1, MONTH = 0;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView jDate;
        public TextView jText;
        public ImageView jImage;

        public MyViewHolder(View view){
            super(view);
            jDate = (TextView) view.findViewById(R.id.journal_date);
            jText = (TextView) view.findViewById(R.id.journal_text);
            jImage = (ImageView) view.findViewById(R.id.journal_image);
        }
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        public TextView jMonth;

        public MyViewHolder2(View view) {
            super(view);
            jMonth = (TextView) view.findViewById(R.id.month);
        }
    }

    public CardsAdapter(Context mContext, List<Object> cardList)
    {
        this.mContext = mContext;
        this.cardList = cardList;
    }

    @Override
    public int getItemViewType(int position) {
        if(cardList.get(position) instanceof Card)
            return ENTRY;
        else if(cardList.get(position) instanceof MonthCard)
            return MONTH;
        else
            return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        switch (viewType){
            case ENTRY:
                itemView = inflater.inflate(R.layout.cards,parent,false);
                return new MyViewHolder(itemView);
            case MONTH:
                itemView = inflater.inflate(R.layout.month,parent,false);
                return new MyViewHolder2(itemView);
            default:
                itemView = inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
                return new MyViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case ENTRY:
                Card card = (Card)cardList.get(position);
                ((MyViewHolder)holder).jDate.setText(card.getJournalDate());
                ((MyViewHolder)holder).jText.setText(card.getJournalText());
                //loading journal image
                ((MyViewHolder)holder).jImage.setImageDrawable(card.getImage());
                break;
            case MONTH:
                MonthCard monthCard = (MonthCard)cardList.get(position);
                ((MyViewHolder2)holder).jMonth.setText(monthCard.getMonth());
                break;
             default:
                 return;

        }
    }


    @Override
    public int getItemCount()
    {
        return cardList.size();
    }
}
