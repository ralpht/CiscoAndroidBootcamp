package com.cisco.yambalabs;

import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ralph on 1/15/15.
 */
public class DetailsFragment extends Fragment {
    private static final String STATUS_ID = "STATUS_ID";

    private TextView mUserView;
    private TextView mMessageView;
    private TextView mTimestampView;

    private long mId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_details, container, false);

        mUserView = (TextView) parent.findViewById(R.id.details_user);
        mMessageView = (TextView) parent.findViewById(R.id.details_message);
        mTimestampView = (TextView) parent.findViewById(R.id.details_timestamp);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATUS_ID)) {
            displayStatus(savedInstanceState.getLong(STATUS_ID));
        }

        return parent;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATUS_ID, mId);

        super.onSaveInstanceState(outState);
    }

    private static final String[] PROJ = {
        YambaContract.Status.Column.USER,
        YambaContract.Status.Column.MESSAGE,
        YambaContract.Status.Column.TIMESTAMP
    };

    public void displayStatus(long id) {
        mId = id;

        Cursor c = getActivity().getContentResolver().query(
                ContentUris.withAppendedId(YambaContract.Status.URI, id),
                PROJ, null, null, null);

        try {
            if (c != null && c.moveToFirst()) {
                mUserView.setText(c.getString(0));
                mMessageView.setText(c.getString(1));
                mTimestampView.setText(DateUtils.getRelativeTimeSpanString(c.getLong(2)));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
}
