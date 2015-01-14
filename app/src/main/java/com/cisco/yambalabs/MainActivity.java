package com.cisco.yambalabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_start_service).setOnClickListener(this);
        findViewById(R.id.main_stop_service).setOnClickListener(this);
        findViewById(R.id.main_start_iservice).setOnClickListener(this);
        findViewById(R.id.main_stop_iservice).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_start_service:
                Intent startDemo = new Intent(this, DemoService.class);
                startDemo.putExtra(DemoService.SPECIAL_DEMO_VALUE,
                        "Caller id " + this.toString());
                startService(startDemo);
                break;
            case R.id.main_stop_service:
                Intent stopDemo = new Intent(this, DemoService.class);
                stopService(stopDemo);
                break;
            case R.id.main_start_iservice:
                Intent startYamba = new Intent(this, YambaService.class);
                startService(startYamba);
                break;
            case R.id.main_stop_iservice:
                Intent stopYamba = new Intent(this, YambaService.class);
                stopService(stopYamba);
                break;
            default:
                // no default case
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
