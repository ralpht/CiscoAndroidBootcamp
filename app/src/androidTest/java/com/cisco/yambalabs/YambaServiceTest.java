package com.cisco.yambalabs;

import android.content.Intent;
import android.database.Cursor;
import android.test.ServiceTestCase;

/**
 * Created by ralph on 1/16/15.
 */
public class YambaServiceTest extends ServiceTestCase<YambaService> {
    public YambaServiceTest() {
        super(YambaService.class);
    }

    @Override
    public void testAndroidTestCaseSetupProperly() {
        super.testAndroidTestCaseSetupProperly();
    }

    public void testNetworkCall() throws InterruptedException {
        getContext().getContentResolver().delete(
                YambaContract.Status.URI,
                null, null);

        Cursor c = getContext().getContentResolver().query(
                YambaContract.Status.URI,
                new String[] { YambaContract.Status.Column.ID },
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertEquals("We should have an empty db", 0, c.getCount());

        c.close();

        startService(new Intent(getContext(), YambaService.class));
        Thread.sleep(5000);

        c = getContext().getContentResolver().query(
                YambaContract.Status.URI,
                new String[] { YambaContract.Status.Column.ID },
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertTrue("We should have rows", c.getCount() > 0);

        c.close();
    }
}
