package edu.galileo.android.flashcard.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

/**
 * Created by SAMSUNG on 1/07/2017.
 */
public class CardService extends IntentService {

    public static final String EXTRA_TASK_DESCRIPTION = "EXTRA_TASK_DESCRIPTION";

    public CardService() {
        super("CardService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String question = intent.getStringExtra(EXTRA_TASK_DESCRIPTION);
        String answer = intent.getStringExtra(EXTRA_TASK_DESCRIPTION);

        ContentValues contentValues = new ContentValues();
        contentValues.put(CardListContract.CardEntry.COLUMN_QUESTION,question);
        contentValues.put(CardListContract.CardEntry.COLUMN_ANSWER,answer);

        getContentResolver().insert(CardListContract.CardEntry.CONTENT_URI,contentValues);
    }
}
