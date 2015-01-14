package com.cisco.yambalabs;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(
                this, R.layout.list_item_status, null,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_post:
                startActivity(new Intent(this, PostActivity.class));
                return true;
            case R.id.main_refresh:
                startService(new Intent(this, YambaService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                this,
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
