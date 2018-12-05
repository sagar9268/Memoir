package com.sagar.memoir;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Card> cardList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

    public CardsAdapter(Context mContext, List<Card> cardList)
    {
        this.mContext = mContext;
        this.cardList = cardList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        Card card = cardList.get(position);
        holder.jDate.setText(card.getJournalDate());
        holder.jText.setText(card.getJournalText());
        //loading journal image with glide library
        Glide.with(mContext).load(card.getImage()).into(holder.jImage);
    }

    @Override
    public int getItemCount()
    {
        return cardList.size();
    }
}
