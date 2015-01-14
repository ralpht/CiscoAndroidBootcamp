package com.cisco.yambalabs;

import android.os.AsyncTask;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * Created by ralph on 1/13/15.
 */
public class PostTask extends AsyncTask<String, Void, Boolean> {
    public interface OnPostTaskListener {
        void onPreExecute(PostTask postTask);
        void onError(PostTask postTask);
        void onSuccess(PostTask postTask);
    }

    private OnPostTaskListener mListener;

    public void setOnPostListener(OnPostTaskListener listener) {
        mListener = listener;
    }

    public void removeOnPostListener(OnPostTaskListener listener) {
        if (mListener == listener) {
            mListener = null;
        }
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onPreExecute(this);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        YambaClient yc = new YambaClient("student", "password");

        try {
            yc.postStatus(params[0]);
            return Boolean.TRUE;
        } catch (YambaClientException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if (mListener != null) {
            if (status) {
                mListener.onSuccess(this);
            } else {
                mListener.onError(this);
            }
        }
    }
}
