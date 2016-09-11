package com.soapwang.databasedemoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Acer on 2016/8/2.
 */
public class SimpleCardViewAdapter extends RecyclerView.Adapter<SimpleCardViewAdapter.ViewHolder> {
    ArrayList<MyNote> mItems;
    public MyNote myNote;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public SimpleCardViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final int index = i;
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        myNote = mItems.get(i);
        viewHolder.titleView.setText(myNote.getTitle());
        viewHolder.dateView.setText(myNote.getDate());

        if (mOnItemClickListener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setNoteList(ArrayList<MyNote> n) {
        mItems = n;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.titleView = (TextView)itemView.findViewById(R.id.row_title);
            this.dateView = (TextView)itemView.findViewById(R.id.row_date);
        }

    }
}
