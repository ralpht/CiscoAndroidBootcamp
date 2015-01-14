package com.cisco.yambalabs;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class YambaContract {
	private YambaContract() {}
	
    public static final String AUTHORITY = "com.cisco.yambalabs";

    public static final Uri BASE_URI = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT) // content://
        .authority(AUTHORITY)
        .build();

    public static final class Status {
        private Status() { }

        public static final String TABLE = "status";

        private static final String MINOR_TYPE = "/vnd." + AUTHORITY;

        public static final String ITEM_TYPE
            = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE; // vnd.android.cursor.item/vnd.com.cisco.yambalabs
        public static final String DIR_TYPE
            = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE; // vnd.android.cursor.dir/vnd.com.cisco.yambalabs

        public static final Uri URI
            = BASE_URI.buildUpon().appendPath(TABLE).build(); // content://com.cisco.yambalabs/status

        public static final class Column {
            private Column() { }

            public static final String ID = BaseColumns._ID; // ID, id, Id, _id

            public static final String SERVER_ID = "serverId";
            public static final String MAX_ID = "maxId";

            // This is a datetime
            public static final String TIMESTAMP = "time";

            public static final String USER = "user";
            public static final String MESSAGE = "message";
        }
    }
}
