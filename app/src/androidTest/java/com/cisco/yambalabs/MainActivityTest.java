package com.cisco.yambalabs;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

/**
 * Created by ralph on 1/16/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testPostLaunch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // find button & click
            View view = solo.getView(R.id.main_post);
            solo.clickOnView(view);
        } else {
            // click on the menu (action bar item)
            solo.clickOnActionBarItem(R.id.main_post);
        }

        solo.waitForActivity(PostActivity.class);

        assertEquals("The PostActivity screen should be the current activity",
                PostActivity.class, solo.getCurrentActivity().getClass());
    }
}
