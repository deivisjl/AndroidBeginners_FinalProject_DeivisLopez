package edu.galileo.android.flashcard.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SAMSUNG on 1/07/2017.
 */
public class CardListContract {
    public static final String CONTENT_AUTORITY = "edu.galileo.cardlist";
    public static final Uri BASE_CONTENT_URI =  Uri.parse("content://" + CONTENT_AUTORITY);
    public static final String PATH_CARD = "card";

    public static final String DATE_FORMAT = "yyyyMMdd";

    public static final String QUERY_EQUAL = "=?";

    public static String getDbDateString(Date date) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final class CardEntry implements BaseColumns {

        public static final String TABLE_NAME = "card";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATETEXT = "date";

        public static final String COLUMN_DUE_DATE_TEXT = "due_date";

        public static final String WHERE_CARD_ID = _ID + QUERY_EQUAL;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTORITY + "/" + PATH_CARD;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTORITY + "/" + PATH_CARD;
    }
}
