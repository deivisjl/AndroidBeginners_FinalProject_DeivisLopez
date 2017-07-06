package edu.galileo.android.flashcard.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.galileo.android.flashcard.R;
import edu.galileo.android.flashcard.data.CardListContract;
import edu.galileo.android.flashcard.json.JsonReader;
import edu.galileo.android.flashcard.widget.entities.Cards;

/**
 * Implementation of App Widget functionality.
 */
public class CardAppWidget extends AppWidgetProvider {

    public static final String SEE_ANSWER = "edu.galileo.android.flashcard.UPDATE_WIDGET";
    private static final String DATA = "data";
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";

    public static int BAND = 1;
    private static Cards myCards;
    private List<Cards> cardsList;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int n = appWidgetIds.length;

        myCards = getContent(context);
        if(myCards == null){
            onUpdate(context,appWidgetManager,appWidgetIds);
        }

        for (int i = 0; i < n; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(),  R.layout.card_app_widget);
            views.setTextViewText(R.id.questionButton,context.getString(R.string.widget_message_answer));
            views.setTextViewText(R.id.mainTextView, getRandomCard(myCards,1));

            Intent intent = new Intent(context, CardAppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.nextButton, pendingIntent);

            Intent seeIntent = new Intent(SEE_ANSWER);
            seeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

            PendingIntent pendingIntentSee = PendingIntent.getBroadcast(context, appWidgetId, seeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.questionButton, pendingIntentSee);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SEE_ANSWER)) {
            int widgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

                RemoteViews views = new RemoteViews(context.getPackageName(),  R.layout.card_app_widget);

                if (BAND == 1){

                    views.setTextViewText(R.id.mainTextView, getRandomCard(myCards,2));
                    views.setTextViewText(R.id.questionButton,context.getString(R.string.widget_message_question));
                    BAND = 0;

                }else{

                    views.setTextViewText(R.id.mainTextView, getRandomCard(myCards,1));
                    views.setTextViewText(R.id.questionButton,context.getString(R.string.widget_message_answer));
                    BAND = 1;

                }

                Intent seeIntent = new Intent(SEE_ANSWER);
                seeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);

                PendingIntent pendingIntentSee = PendingIntent.getBroadcast(context, widgetId, seeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.questionButton, pendingIntentSee);

                AppWidgetManager.getInstance(context).updateAppWidget(widgetId,views);
        }else{
            super.onReceive(context, intent);
        }
    }


    private Cards getContent(Context context) {

        Cursor cursor = context.getContentResolver().query(CardListContract.CardEntry.CONTENT_URI,null,null,null,null);

        int index = randomValue(cursor.getCount(),1);

        if (index > 0) {

            String selectionArgs[] = new String[]{index + ""};

            String selection = CardListContract.CardEntry._ID + " = ?";

            cursor = context.getContentResolver().query(
                    CardListContract.CardEntry.CONTENT_URI, null, selection, selectionArgs, null);

            Cards card = new Cards();

            if (cursor.moveToFirst()){
                card.setQuestion(cursor.getString(cursor.getColumnIndex(CardListContract.CardEntry.COLUMN_QUESTION)));
                card.setAnswer(cursor.getString(cursor.getColumnIndex(CardListContract.CardEntry.COLUMN_ANSWER)));
            }else{
                return null;
            }

            cursor.close();

            return card;

        }else{

            cardsList = readJsonCard(context);

            int cardIndex = randomValue(cardsList.size(),2);

            return  cardsList.get(cardIndex);

        }


    }

    private int randomValue(int range, int band){

        if (band == 1){
            return  new Random().nextInt(range + 1);
        }else{
            return  new Random().nextInt(range);
        }


    }

    private String getRandomCard(Cards card, int band) {

        if (band == 1){

            return card.getQuestion() + "?";

        }else{

            return card.getAnswer();

        }

    }

    private ArrayList<Cards> readJsonCard(Context context){
        ArrayList<Cards> listCard = new ArrayList<>();

        String cardJson = JsonReader.LoadJSONFromAsset(context,"flashcard.json");

        try {
            JSONObject jsonObject = new JSONObject(cardJson);
            JSONArray jsonWithArrayData = jsonObject.getJSONArray(DATA);

            for(int i = 0; i < jsonWithArrayData.length(); i++){
                Cards card = new Cards();
                JSONObject jsonWithObjectData = jsonWithArrayData.getJSONObject(i);

                card.setQuestion(jsonWithObjectData.getString(QUESTION));
                card.setAnswer(jsonWithObjectData.getString(ANSWER));
                listCard.add(card);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listCard;
    }

}

