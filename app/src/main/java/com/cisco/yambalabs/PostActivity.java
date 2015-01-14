package com.cisco.yambalabs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ralph on 1/12/15.
 */
public class PostActivity extends Activity
        implements View.OnClickListener, TextWatcher, PostTask.OnPostTaskListener {
    private static final String TAG = "PostActivity";

    private TextView mCounter;
    private EditText mPostMessage;
    private Button mPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mCounter = (TextView) findViewById(R.id.post_counter);
        mPostMessage = (EditText) findViewById(R.id.post_message);
        mPostButton = (Button) findViewById(R.id.post_button);

        mPostMessage.addTextChangedListener(this);
        mPostButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            mPostTask = (PostTask) getLastNonConfigurationInstance();

            if (mPostTask != null) {
                mProgressDlg = ProgressDialog.show(
                        this, "Post Update", "Updating now. Please wait!");
            }
        }

        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "OnCreate");
    }

    @Override
    protected void onDestroy() {
        hideProgress();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_button:
                mPostTask = new PostTask();

                mPostTask.setOnPostListener(this);
                mPostTask.execute(mPostMessage.getText().toString());
                break;

            default:
                // no default
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int remainder = 140 - s.toString().length();

        mCounter.setText(String.valueOf(remainder));

        if (remainder == 0) {
            mCounter.setTextColor(Color.RED);
        } else if (remainder < 11) {
            mCounter.setTextColor(Color.YELLOW);
        } else {
            mCounter.setTextColor(Color.GREEN);
        }

        mPostButton.setEnabled(remainder != 140);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mPostTask != null) {
            mPostTask.removeOnPostListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPostTask != null) {
            mPostTask.setOnPostListener(this);
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mPostTask;
    }

    private PostTask mPostTask;
    private ProgressDialog mProgressDlg;

    @Override
    public void onPreExecute(PostTask postTask) {
        mProgressDlg = ProgressDialog.show(this, "Post Update", "Updating now. Please wait!");
    }

    @Override
    public void onError(PostTask postTask) {
        mPostTask = null;

        hideProgress();
        Toast.makeText(this, "Post FAILED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(PostTask postTask) {
        mPostTask = null;

        mPostMessage.setText("");
        mCounter.setText(String.valueOf(140));
        mPostButton.setEnabled(false);
        hideProgress();
        Toast.makeText(this, "Post success", Toast.LENGTH_SHORT).show();
    }

    private void hideProgress() {
        if (mProgressDlg != null) {
            mProgressDlg.dismiss();
            mProgressDlg = null;
        }
    }
}
