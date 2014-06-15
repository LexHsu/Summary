package com.andorid.lex.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {
    private final static String LOG_TAG = "MainActivity";

    private final static int ADD_ARTICAL_ACTIVITY = 1;
    private final static int EDIT_ARTICAL_ACTIVITY = 2;

    private ArticleManager mManager = null;
    private ArticleAdapter mAdapter = null;
    private ArticleObserver mObserver = null;

    private ListView mArticleList = null;
    private Button mAddButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mManager = new ArticleManager(this);
        mAdapter = new ArticleAdapter(this);
        mAddButton = (Button)findViewById(R.id.button_add);
        mArticleList = (ListView)findViewById(R.id.listview_article);
        mAddButton.setOnClickListener(this);
        mArticleList.setAdapter(mAdapter);
        mArticleList.setOnItemClickListener(this);

        mObserver = new ArticleObserver(new Handler());
        getContentResolver().registerContentObserver(App.CONTENT_URI, true, mObserver);


        Log.i(LOG_TAG, "MainActivity Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mAddButton)) {
            Intent intent = new Intent(this, ArticleActivity.class);
            startActivityForResult(intent, ADD_ARTICAL_ACTIVITY);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Intent intent = new Intent(this, ArticleActivity.class);

        Article article = mManager.getArticleByPos(pos);
        intent.putExtra(App.ID, article.getId());
        intent.putExtra(App.TITLE, article.getTitle());
        intent.putExtra(App.ABSTRACT, article.getAbstract());
        intent.putExtra(App.URL, article.getUrl());

        startActivityForResult(intent, EDIT_ARTICAL_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case ADD_ARTICAL_ACTIVITY: {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra(App.TITLE);
                String abs = data.getStringExtra(App.ABSTRACT);
                String url = data.getStringExtra(App.URL);

                Article article = new Article(-1, title, abs, url);
                mManager.insertArticle(article);
            }

            break;
        }

        case EDIT_ARTICAL_ACTIVITY: {
            if (resultCode == Activity.RESULT_OK) {
                int action = data.getIntExtra(ArticleActivity.EDIT_ARTICLE_ACTION, -1);
                if (action == ArticleActivity.MODIFY_ARTICLE) {
                    int id = data.getIntExtra(App.ID, -1);
                    String title = data.getStringExtra(App.TITLE);
                    String abs = data.getStringExtra(App.ABSTRACT);
                    String url = data.getStringExtra(App.URL);

                    Article article = new Article(id, title, abs, url);
                    mManager.updateArticle(article);
                } else if (action == ArticleActivity.DELETE_ARTICLE) {
                    int id = data.getIntExtra(App.ID, -1);

                    mManager.deleteArticle(id);
                }

            }

            break;
        }
        }
    }

    private class ArticleObserver extends ContentObserver {
        public ArticleObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ArticleAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ArticleAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mManager.getArticleCount();
        }

        @Override
        public Object getItem(int pos) {
            return mManager.getArticleByPos(pos);
        }

        @Override
        public long getItemId(int pos) {
            return mManager.getArticleByPos(pos).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article article = (Article) getItem(position);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item, null);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.textview_article_title);
            titleView.setText("Title: " + article.getTitle());

            TextView abstractView = (TextView) convertView.findViewById(R.id.textview_article_abstract);
            abstractView.setText("Abstract: " + article.getAbstract());

            TextView urlView = (TextView) convertView.findViewById(R.id.textview_article_url);
            urlView.setText("URL: " + article.getUrl());

            return convertView;
        }
    }
}
