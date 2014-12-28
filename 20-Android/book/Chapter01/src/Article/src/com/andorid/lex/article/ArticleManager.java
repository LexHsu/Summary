package com.andorid.lex.article;

import java.util.LinkedList;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public class ArticleManager {
    private static final String LOG_TAG = "ArticleManager";

    private ContentResolver mResolver = null;

    public ArticleManager(Context context) {
        mResolver = context.getContentResolver();
    }

    public long insertArticle(Article article) {
        ContentValues values = new ContentValues();
        values.put(App.TITLE, article.getTitle());
        values.put(App.ABSTRACT, article.getAbstract());
        values.put(App.URL, article.getUrl());
        Uri uri = mResolver.insert(App.CONTENT_URI, values);
        // {path, id}, index 1 just get the id
        String itemId = uri.getPathSegments().get(1);
        return Integer.valueOf(itemId).longValue();
    }

    public boolean updateArticle(Article article) {
        Uri uri = ContentUris.withAppendedId(App.CONTENT_URI, article.getId());
        ContentValues values = new ContentValues();
        values.put(App.TITLE, article.getTitle());
        values.put(App.ABSTRACT, article.getAbstract());
        values.put(App.URL, article.getUrl());
        int count = mResolver.update(uri, values, null, null);

        return count > 0;
    }

    public boolean deleteArticle(int id) {
        Uri uri = ContentUris.withAppendedId(App.CONTENT_URI, id);
        int count = mResolver.delete(uri, null, null);

        return count > 0;
    }

    public LinkedList<Article> getAllArticles() {
        LinkedList<Article> articles = new LinkedList<Article>();

        String[] projection = new String[] {App.ID, App.TITLE, App.ABSTRACT, App.URL};

        Cursor cursor = mResolver.query(App.CONTENT_URI, projection, null, null, App.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return articles;
        }
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String abs = cursor.getString(2);
                String url = cursor.getString(3);

                Article article = new Article(id, title, abs, url);
                articles.add(article);
            } while (cursor.moveToNext());
        }

        return articles;
    }

    /**
     * For better performance, use call() method to get data.
     * @return
     */
    public int getArticleCount() {
        int count = 0;
        try {
            ContentProviderClient provider = mResolver.acquireContentProviderClient(App.CONTENT_URI);
            if (provider == null) {
                return count;
            }
            Bundle bundle = provider.call(App.METHOD_GET_ITEM_COUNT, null, null);
            count = bundle.getInt(App.KEY_ITEM_COUNT, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return count;
        // return getAllArticles().size();
    }

    public Article getArticleById(int id) {
        Uri uri = ContentUris.withAppendedId(App.CONTENT_URI, id);
        String[] projection = new String[] {App.ID, App.TITLE, App.ABSTRACT, App.URL};
        Cursor cursor = mResolver.query(uri, projection, null, null, App.DEFAULT_SORT_ORDER);
        Log.i(LOG_TAG, "cursor.moveToFirst");
        if (!cursor.moveToFirst()) {
            return null;
        }
        String title = cursor.getString(1);
        String abs = cursor.getString(2);
        String url = cursor.getString(3);

        return new Article(id, title, abs, url);
    }

    public Article getArticleByPos(int pos) {
        Uri uri = ContentUris.withAppendedId(App.CONTENT_POS_URI, pos);
        String[] projection = new String[] {App.ID, App.TITLE, App.ABSTRACT, App.URL};
        Cursor cursor = mResolver.query(uri, projection, null, null, App.DEFAULT_SORT_ORDER);
        if (!cursor.moveToFirst()) {
            return null;
        }
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String abs = cursor.getString(2);
        String url = cursor.getString(3);

        return new Article(id, title, abs, url);
    }
}
