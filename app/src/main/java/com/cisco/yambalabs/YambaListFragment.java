package com.cisco.yambalabs;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by ralph on 1/15/15.
 */
public class YambaListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 1001;
    private SimpleCursorAdapter mAdapter;

    private class YambaBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() == R.id.status_timestamp) {
                long ts = cursor.getLong(columnIndex);
                CharSequence tsValue = DateUtils.getRelativeTimeSpanString(ts);
                TextView tv = (TextView) view;

                tv.setText(tsValue);

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.list_item_status, null,
                new String [] {
                        YambaContract.Status.Column.MESSAGE,
                        YambaContract.Status.Column.USER,
                        YambaContract.Status.Column.TIMESTAMP
                },
                new int[] {
                        R.id.status_message,
                        R.id.status_user,
                        R.id.status_timestamp
                },
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mAdapter.setViewBinder(new YambaBinder());

        setListAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public interface OnStatusSelectedListener {
        void statusSelected(long id);
    }

    private OnStatusSelectedListener mListener;

    public void setOnStatusSelectedListener(OnStatusSelectedListener listener) {
        mListener = listener;
    }

    public void removeOnStatusSelectedListener(OnStatusSelectedListener listener) {
        if (mListener == listener) {
            mListener = null;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.statusSelected(id);
        }
    }

    private static final String[] PROJ = {
            YambaContract.Status.Column.ID,
            YambaContract.Status.Column.USER,
            YambaContract.Status.Column.TIMESTAMP,
            YambaContract.Status.Column.MESSAGE
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                YambaContract.Status.URI,
                PROJ,
                null, null,
                YambaContract.Status.Column.TIMESTAMP + " desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
