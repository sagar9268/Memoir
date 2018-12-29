package com.sagar.memoir;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Object> cardList;
    private final int ENTRY = 1, MONTH = 0;
    private DatabaseHelper db;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView jDate;
        public TextView jText;
        public ImageView jImage;
        public ImageView jOverflowMenu;

        public MyViewHolder(View view){
            super(view);
            jDate = (TextView) view.findViewById(R.id.journal_date);
            jText = (TextView) view.findViewById(R.id.journal_text);
            jImage = (ImageView) view.findViewById(R.id.journal_image);
            jOverflowMenu = (ImageView) view.findViewById(R.id.overflow_menu_card);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()){
            case ENTRY:
                Card card = (Card)cardList.get(position);
                ((MyViewHolder)holder).jDate.setText(card.getJournalDate());
                ((MyViewHolder)holder).jText.setText(card.getJournalText());
                //loading journal image
                byte[] byteArray = card.getPictureData();
                if(byteArray != null){
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    ((MyViewHolder)holder).jImage.setImageBitmap(bmp);
                }
                //overflow menu
                ((MyViewHolder) holder).jOverflowMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUpMenu(position, ((MyViewHolder) holder).jOverflowMenu);
                    }
                });
                break;
            case MONTH:
                MonthCard monthCard = (MonthCard)cardList.get(position);
                ((MyViewHolder2)holder).jMonth.setText(monthCard.getMonth());
                break;
             default:
                 return;

        }
    }

    private void showPopUpMenu(final int mPosition , View view)
    {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_card, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_edit:
                        //code to edit journal

                        return true;
                    case R.id.action_delete:
                        //code to delete journal
                        db = new DatabaseHelper(mContext);
                        Card card = (Card)cardList.get(mPosition);
                        db.deleteJournal(card);
                        cardList.remove(mPosition);
                        notifyItemRemoved(mPosition);
                        Toast.makeText(mContext,"Journal deleted!", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }


    @Override
    public int getItemCount()
    {
        return cardList.size();
    }
}
