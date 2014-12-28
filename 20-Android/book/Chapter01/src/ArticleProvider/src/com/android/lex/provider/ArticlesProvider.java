package com.android.lex.provider;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class ArticlesProvider extends ContentProvider {

    private static final String LOG_TAG = "ArticlesProvider";
    private static final String DB_NAME = "Articles.db";
    private static final String DB_TABLE = "ArticlesTable";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE = "create table " + DB_TABLE + " ("
            + App.ID + " integer primary key autoincrement, "
            + App.TITLE + " text not null, " + App.ABSTRACT
            + " text not null, " + App.URL + " text not null);";

    /**
     * URI Matcher
     */
    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(App.AUTHORITY, "item", App.ITEM);
        sUriMatcher.addURI(App.AUTHORITY, "item/#", App.ITEM_ID);
        sUriMatcher.addURI(App.AUTHORITY, "pos/#", App.ITEM_POS);
    }

    /**
     * The field mapping
     */
    private static final HashMap<String, String> sProjectionMap;
    static {
        sProjectionMap = new HashMap<String, String>();
        sProjectionMap.put(App.ID, App.ID);
        sProjectionMap.put(App.TITLE, App.TITLE);
        sProjectionMap.put(App.ABSTRACT, App.ABSTRACT);
        sProjectionMap.put(App.URL, App.URL);
    }

    private DBHelper mDBHelper = null;
    private ContentResolver mResolver = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mResolver = context.getContentResolver();
        mDBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);

        Log.i(LOG_TAG, "Articles Provider Create");

        return true;
    }

    /**
     * get MIME type of data
     */
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case App.ITEM:
            return App.CONTENT_TYPE;
        case App.ITEM_ID:
        case App.ITEM_POS:
            return App.CONTENT_ITEM_TYPE;
        default:
            throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != App.ITEM) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        long id = db.insert(DB_TABLE, App.ID, values);
        if (id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for "
                    + uri);
        }

        Uri newUri = ContentUris.withAppendedId(uri, id);
        mResolver.notifyChange(newUri, null);

        return newUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count = 0;

        switch (sUriMatcher.match(uri)) {
        case App.ITEM: {
            count = db.update(DB_TABLE, values, selection, selectionArgs);
            break;
        }
        case App.ITEM_ID: {
            String id = uri.getPathSegments().get(1);
            count = db.update(DB_TABLE, values, App.ID
                    + "="
                    + id
                    + (!TextUtils.isEmpty(selection) ? " and (" + selection
                            + ')' : ""), selectionArgs);
            break;
        }
        default:
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        mResolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count = 0;

        switch (sUriMatcher.match(uri)) {
        case App.ITEM: {
            count = db.delete(DB_TABLE, selection, selectionArgs);
            break;
        }
        case App.ITEM_ID: {
            String id = uri.getPathSegments().get(1);
            count = db.delete(DB_TABLE,
                    App.ID
                            + "="
                            + id
                            + (!TextUtils.isEmpty(selection) ? " and ("
                                    + selection + ')' : ""), selectionArgs);
            break;
        }
        default:
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        mResolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        Log.i(LOG_TAG, "ArticlesProvider.query: " + uri);

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // SQLiteQueryBuilder Avoid field exposure
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;

        switch (sUriMatcher.match(uri)) {
        case App.ITEM: {
            sqlBuilder.setTables(DB_TABLE);
            sqlBuilder.setProjectionMap(sProjectionMap);
            break;
        }
        case App.ITEM_ID: {
            String id = uri.getPathSegments().get(1);
            sqlBuilder.setTables(DB_TABLE);
            sqlBuilder.setProjectionMap(sProjectionMap);
            sqlBuilder.appendWhere(App.ID + "=" + id);
            break;
        }
        case App.ITEM_POS: {
            String pos = uri.getPathSegments().get(1);
            sqlBuilder.setTables(DB_TABLE);
            sqlBuilder.setProjectionMap(sProjectionMap);
            limit = pos + ", 1";
            break;
        }
        default:
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection,
                selectionArgs, null, null,
                TextUtils.isEmpty(sortOrder) ? App.DEFAULT_SORT_ORDER : sortOrder, limit);
        cursor.setNotificationUri(mResolver, uri);

        return cursor;
    }

    @Override
    public Bundle call(String method, String request, Bundle args) {
        Log.i(LOG_TAG, "ArticlesProvider.call: " + method);

        if (method.equals(App.METHOD_GET_ITEM_COUNT)) {
            return getItemCount();
        }

        throw new IllegalArgumentException("Error method call: " + method);
    }

    private Bundle getItemCount() {
        Log.i(LOG_TAG, "ArticlesProvider.getItemCount");

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DB_TABLE, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(App.KEY_ITEM_COUNT, count);

        cursor.close();
        db.close();

        return bundle;
    }

    /**
     * Avoid time-consuming operation,
     * SQLiteOpenHelper actually not open database immediately,
     * only call query，insert，update or delete method.
     * 
     * 
     * @author lex
     *
     */
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}