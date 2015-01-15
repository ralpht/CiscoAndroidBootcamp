package com.cisco.yambalabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity implements YambaListFragment.OnStatusSelectedListener {
    @Override
    public void statusSelected(long id) {
        mId = id;
        mDetailsFragment.displayStatus(id);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.show(mDetailsFragment);
            ft.hide(mListFragment);
            ft.addToBackStack("Hide Details");
            ft.commit();

            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Yamba Post Refresh Complete", Toast.LENGTH_SHORT).show();
        }
    }

    public static final String REFRESH_FILTER = "com.cisco.yambalabs.REFRESH_FILTER";
    private static final String SHOW_LIST = "SHOW_LIST";
    private IntentFilter mFilter;
    private RefreshReceiver mReceiver;
    private long mId;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private YambaListFragment mListFragment;
    private DetailsFragment mDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFilter = new IntentFilter(REFRESH_FILTER);
        mReceiver = new RefreshReceiver();

        Configuration config = getResources().getConfiguration();
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            mListFragment = new YambaListFragment();
            mDetailsFragment = new DetailsFragment();

            ft.add(
                    R.id.main_list_fragment, mListFragment,
                    YambaListFragment.class.getSimpleName());

            ft.add(R.id.main_detail_fragment, mDetailsFragment,
                    DetailsFragment.class.getSimpleName());

            ft.commit();
        } else {
            FragmentManager fm = getFragmentManager();

            mListFragment = (YambaListFragment) fm.findFragmentByTag(
                    YambaListFragment.class.getSimpleName());

            mDetailsFragment = (DetailsFragment) fm.findFragmentByTag(
                    DetailsFragment.class.getSimpleName());

            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                FragmentTransaction ft = fm.beginTransaction();

                if (savedInstanceState.containsKey(SHOW_LIST)
                        && savedInstanceState.getLong(SHOW_LIST) > 0) {
                    ft.hide(mListFragment);
                } else {
                    ft.hide(mDetailsFragment);
                }

                ft.commit();
            }

        }

        mListFragment.setOnStatusSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(SHOW_LIST, mId);
        super.onSaveInstanceState(outState);
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
            case android.R.id.home:
                onBackPressed();
                return true;
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

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        Configuration config = getResources().getConfiguration();

        if (fm.getBackStackEntryCount() > 0) {
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                finish();
                return;
            }

            getActionBar().setDisplayHomeAsUpEnabled(false);
        }

        super.onBackPressed();
    }
}
