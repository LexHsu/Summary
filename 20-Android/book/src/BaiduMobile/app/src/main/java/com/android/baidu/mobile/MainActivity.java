package com.android.baidu.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener{

    public static final String TAG = "MainActivity";

    private Button mSearchButton = null;

    private EditText mKeywords = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
    }

    private void findViewById() {
        mSearchButton = (Button)findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);

        mKeywords = (EditText)findViewById(R.id.text_input);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:

                String keywords = mKeywords.getText().toString();
                String url = Config.SEARCH_URL + keywords;
                Intent intent=new Intent(this, WebActivity.class);
                intent.putExtra(Config.FIELD_KEYWORDS, url);
                startActivity(intent);

                break;
            default:
                Log.e(TAG, "no view matched");
        }
    }
}
