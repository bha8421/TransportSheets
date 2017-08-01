package com.example.android.rawtodispatch.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bharat on 7/19/2017.
 */

public class DataContract {
    public static final String CONTENT_AUTHORITY="com.example.android.rawtodispatch";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RAW="raw";
    public static final String PATH_FINISH="finish";
    public static final String PATH_DISPATCH="dispatch";

    private DataContract(){}

    public static final class RawEntry extends CommonColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RAW);  /** The content URI to access the raw data in the provider */

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of raws.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RAW;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single raw.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RAW;

        public final static String TABLE_NAME = "raw";  /** Name of database table for raw material */




    }

    public static final class FinishEntry extends CommonColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FINISH);  /** The content URI to access the raw data in the provider */


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINISH;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINISH;

        public final static String TABLE_NAME = "finish";  /** Name of database table for finish material */




    }

    public static final class DispatchEntry extends CommonColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DISPATCH);  /** The content URI to access the raw data in the provider */


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISPATCH;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISPATCH;

        public final static String TABLE_NAME = "dispatch";  /** Name of database table for dispatch material */



        public static final String COLUMN_SOURCE="source";

        public static final String COLUMN_DESTINATION="destination";

    }

    public static class CommonColumns implements BaseColumns{


        public final static String _ID = BaseColumns._ID;

        /** Column names */
        public final static String COLUMN_VEHICLE="vehicle";

        public final static String COLUMN_QUANTITY="quantity";

        public final static String COLUMN_RATE="rate";

        public static final String COLUMN_LOADING_RECEIPT="lr";

        public static final String COLUMN_OTHER_CHARGES="othercharges";

        public static final String COLUMN_DATE="date";


    }

}
