package edu.galileo.android.flashcard.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by SAMSUNG on 1/07/2017.
 */
public class CardListProvider extends ContentProvider {
    public static final int CARD = 100;
    public static final int CARD_ID = 101;

    public static final UriMatcher uriMatcher = buildUriMatcher();
    private CardDBHelper openHelper;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CardListContract.CONTENT_AUTORITY;

        matcher.addURI(authority,CardListContract.PATH_CARD, CARD);
        matcher.addURI(authority,CardListContract.PATH_CARD + "/#", CARD_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new CardDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)){
            case CARD:
                retCursor = openHelper.getReadableDatabase().query(CardListContract.CardEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            case CARD_ID:
                retCursor = openHelper.getReadableDatabase().query(
                        CardListContract.CardEntry.TABLE_NAME,
                        projection,
                        CardListContract.CardEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw  new UnsupportedOperationException("Unkown Uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case CARD:
                return CardListContract.CardEntry.CONTENT_TYPE;
            case CARD_ID:
                return CardListContract.CardEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case CARD:
                long _id = db.insert(CardListContract.CardEntry.TABLE_NAME,null,values);
                if (_id > 0){
                    returnUri = CardListContract.CardEntry.CONTENT_URI;
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unkown " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowDeleted;

        switch (match){
            case CARD:
                rowDeleted = db.delete(CardListContract.CardEntry.TABLE_NAME, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        if (selection == null || rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowUpdated;

        switch (match){
            case CARD:
                rowUpdated = db.update(CardListContract.CardEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match){
            case CARD:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(CardListContract.CardEntry.TABLE_NAME, null, value );
                        if (_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }

    }
}
