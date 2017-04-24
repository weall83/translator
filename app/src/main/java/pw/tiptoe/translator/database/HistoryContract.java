package pw.tiptoe.translator.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class HistoryContract {

    private HistoryContract() {
    }


    public static final String CONTENT_AUTHORITY = "pw.tiptoe.translator";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_HISTORY = "history";

    public static final class HistoryEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HISTORY);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;


        public final static String TABLE_NAME = "translateHistory";
        public final static String COLUMN_ID = BaseColumns._ID;
        public final static String COLUMN_TEXT_INPUT = "textInput";
        public final static String COLUMN_TEXT_TRANSLATED = "textTranslated";
        public final static String COLUMN_LANGUAGES_FROM_TO = "languages_from_to";
        public final static String COLUMN_BOOKMARK = "bookmark";

    }
}
