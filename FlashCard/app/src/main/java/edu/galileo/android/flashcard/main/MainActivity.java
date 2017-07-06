package edu.galileo.android.flashcard.main;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.flashcard.AddCardFragment;
import edu.galileo.android.flashcard.R;
import edu.galileo.android.flashcard.data.CardListContract;
import edu.galileo.android.flashcard.detail.DetailCardFragment;
import edu.galileo.android.flashcard.json.JsonReader;
import edu.galileo.android.flashcard.main.adapter.AddCardListener;
import edu.galileo.android.flashcard.main.adapter.MainAdapter;
import edu.galileo.android.flashcard.main.adapter.OnItemCardClickListener;
import edu.galileo.android.flashcard.widget.entities.Cards;

public class MainActivity extends AppCompatActivity implements AddCardListener,
                    LoaderManager.LoaderCallbacks<Cursor>, OnItemCardClickListener {

    @Bind(R.id.mainRecycleryView)
    RecyclerView mainRecyclerView;

    private MainAdapter mainAdapter;

    public static final String MAIN_DIALOG = "main_dialog";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecycler();

        getSupportLoaderManager().initLoader(0,null,this);
    }

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setHasFixedSize(true);
        mainAdapter = new MainAdapter(null,this);
        mainRecyclerView.setAdapter(mainAdapter);
    }


    @OnClick(R.id.fab)
    public void handleFab(){
        AddCardFragment cardFragment = new AddCardFragment();
        cardFragment.show(getSupportFragmentManager(),MAIN_DIALOG);
    }

    @Override
    public void onDialogPositiveClick(String question, String answer) {
        question = question.replace("?","");
        getContentResolver().insert(CardListContract.CardEntry.CONTENT_URI, getCardListContentValues(question,answer));
    }
    private ContentValues getCardListContentValues(String question,String answer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardListContract.CardEntry.COLUMN_QUESTION, question);
        contentValues.put(CardListContract.CardEntry.COLUMN_ANSWER, answer);
        contentValues.put(CardListContract.CardEntry.COLUMN_DATE, Calendar.getInstance().getTimeInMillis());
        return contentValues;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CardListContract.CardEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mainAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mainAdapter.swapCursor(null);
    }

    @Override
    public void onCardClick(String question,String answer) {
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION,question);
        bundle.putString(ANSWER,answer);

        DetailCardFragment detailCardFragment = new DetailCardFragment();
        detailCardFragment.setArguments(bundle);
        detailCardFragment.show(getSupportFragmentManager(),MAIN_DIALOG);
    }

}


