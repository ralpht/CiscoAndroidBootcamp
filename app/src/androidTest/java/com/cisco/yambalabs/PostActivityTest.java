package com.cisco.yambalabs;

import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by ralph on 1/16/15.
 */
public class PostActivityTest extends ActivityInstrumentationTestCase2<PostActivity> {
    private Solo solo;

    public PostActivityTest() {
        super(PostActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testInitialState() {
        TextView counterView = (TextView) solo.getView(R.id.post_counter);
        View button = solo.getView(R.id.post_button);

        assertEquals("Counter should be set to 140", "140", counterView.getText().toString());
        assertEquals("Counter text should be green", Color.GREEN, counterView.getCurrentTextColor());
        assertFalse("The post button should be disabled", button.isEnabled());
    }

    public void testCounterLogic() {
        TextView counterView = (TextView) solo.getView(R.id.post_counter);
        EditText messageView = (EditText) solo.getView(R.id.post_message);

        // 10 characters
        solo.enterText(messageView, "0123456789");

        assertEquals("Counter should be set to 130", "130", counterView.getText().toString());
        assertEquals("Counter text should be green", Color.GREEN, counterView.getCurrentTextColor());

        // 130 characters
        solo.enterText(messageView, "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");

        assertEquals("Counter should be set to 10", "10", counterView.getText().toString());
        assertEquals("Counter text should be yellow", Color.YELLOW, counterView.getCurrentTextColor());

        // max of 140
        solo.enterText(messageView, "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");

        assertEquals("Counter should be 0", "0", counterView.getText().toString());
        assertEquals("Counter text should be red", Color.RED, counterView.getCurrentTextColor());
    }

    public void testPostLogic() {
        EditText messageView = (EditText) solo.getView(R.id.post_message);
        TextView counterView = (TextView) solo.getView(R.id.post_counter);
        View button = solo.getView(R.id.post_button);

        solo.enterText(messageView, "Robotium test");
        solo.clickOnView(button);
        solo.waitForText("Post success");

        assertEquals("Edit text should be empty", "", messageView.getText().toString());
        assertEquals("Counter should be set to 140", "140", counterView.getText().toString());
        assertEquals("Counter text should be green", Color.GREEN, counterView.getCurrentTextColor());
        assertFalse("The post button should be disabled", button.isEnabled());
    }
}
