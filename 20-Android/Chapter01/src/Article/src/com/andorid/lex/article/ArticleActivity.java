package com.andorid.lex.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ArticleActivity extends Activity implements View.OnClickListener {
    private final static String LOG_TAG = "ArticleActivity";

    public final static String EDIT_ARTICLE_ACTION = "EDIT_ARTICLE_ACTION";
    public final static int MODIFY_ARTICLE = 1;
    public final static int DELETE_ARTICLE = 2;

    private int mArticleId = -1;

    private EditText mTitleEdit = null;
    private EditText mAbstractEdit = null;
    private EditText mUrlEdit = null;

    private Button mAddButton = null;
    private Button mModifyButton = null;
    private Button mDeleteButton = null;
    private Button mCancelButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);

        mTitleEdit = (EditText)findViewById(R.id.edit_article_title);
        mAbstractEdit = (EditText)findViewById(R.id.edit_article_abstract);
        mUrlEdit = (EditText)findViewById(R.id.edit_article_url);
        mAddButton = (Button)findViewById(R.id.button_add_article);
        mModifyButton = (Button)findViewById(R.id.button_modify);
        mDeleteButton = (Button)findViewById(R.id.button_delete);
        mCancelButton = (Button)findViewById(R.id.button_cancel);

        mAddButton.setOnClickListener(this);
        mModifyButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        Intent intent = getIntent();
        mArticleId = intent.getIntExtra(App.ID, -1);

        if (mArticleId != -1) {
            String title = intent.getStringExtra(App.TITLE);
            String abs = intent.getStringExtra(App.ABSTRACT);
            String url = intent.getStringExtra(App.URL);
            mTitleEdit.setText(title);
            mAbstractEdit.setText(abs);
            mUrlEdit.setText(url);
            mAddButton.setVisibility(View.GONE);
        } else {
            mModifyButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.GONE);
        }

        Log.i(LOG_TAG, "ArticleActivity Created");
    }

    @Override
    public void onClick(View v) {
        
        switch (v.getId()){
        case R.id.button_add_article:
            String addTitle = mTitleEdit.getText().toString();
            String addAbstract = mAbstractEdit.getText().toString();
            String addUrl = mUrlEdit.getText().toString();

            Intent result = new Intent();
            result.putExtra(App.TITLE, addTitle);
            result.putExtra(App.ABSTRACT, addAbstract);
            result.putExtra(App.URL, addUrl);

            setResult(Activity.RESULT_OK, result);
            finish();
            break;
        case R.id.button_modify:
            String modifyTitle = mTitleEdit.getText().toString();
            String modifyAbstract = mAbstractEdit.getText().toString();
            String modifyUrl = mUrlEdit.getText().toString();

            Intent modifyIntent = new Intent();
            modifyIntent.putExtra(App.ID, mArticleId);
            modifyIntent.putExtra(App.TITLE, modifyTitle);
            modifyIntent.putExtra(App.ABSTRACT, modifyAbstract);
            modifyIntent.putExtra(App.URL, modifyUrl);
            modifyIntent.putExtra(EDIT_ARTICLE_ACTION, MODIFY_ARTICLE);

            setResult(Activity.RESULT_OK, modifyIntent);
            finish();
            break;
        case R.id.button_delete:
            Intent deleteIntent = new Intent();
            deleteIntent.putExtra(App.ID, mArticleId);
            deleteIntent.putExtra(EDIT_ARTICLE_ACTION, DELETE_ARTICLE);

            setResult(Activity.RESULT_OK, deleteIntent);
            finish();
            break;
        case R.id.button_cancel:
            setResult(Activity.RESULT_CANCELED, null);
            finish();
            break;
        default:
            Log.i(LOG_TAG, "ignore");
        }
    }
}