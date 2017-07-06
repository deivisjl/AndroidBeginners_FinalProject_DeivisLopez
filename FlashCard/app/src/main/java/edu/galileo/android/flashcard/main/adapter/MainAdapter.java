package edu.galileo.android.flashcard.main.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.flashcard.R;
import edu.galileo.android.flashcard.data.CardListContract;
import edu.galileo.android.flashcard.widget.entities.Cards;

/**
 * Created by SAMSUNG on 1/07/2017.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private OnItemCardClickListener clickListener;
    private Cursor cursor;

    public MainAdapter( Cursor cursor, OnItemCardClickListener clickListener) {
        this.clickListener = clickListener;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bindView(cursor,clickListener);
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView)
        TextView textView;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }

        private void bindView(final Cursor cursor, final OnItemCardClickListener listener) {

            final int id = cursor.getInt(cursor.getColumnIndex(CardListContract.CardEntry._ID));
            final String question = cursor.getString(cursor.getColumnIndex(CardListContract.CardEntry.COLUMN_QUESTION));
            final String answer = cursor.getString(cursor.getColumnIndex(CardListContract.CardEntry.COLUMN_ANSWER));
            textView.setText(question + "?");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardClick(question,answer);
                }
            });
        }

    }
}
