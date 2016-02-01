package pottitrain.orianapps.topmovies2.Helpers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by claudiusouca on 1/28/16.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.orianapps.topmovies2.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class Favorite_Entry implements BaseColumns {
        // table name
        public static final String TABLE_FAV_MOVIES = "favmovies";
        // columns
        public static final String _ID = "_id";
        public static final String MOVIEID = "movieid";
        public static final String TITLE = "title";
        public static final String VOTE = "vote";
        public static final String OVERVIEW = "overview";
        public static final String RELEASEDATE = "releasedate";
        public static final String POSTERPATH = "posterpath";
        public static final String SAVEDREVIEW = "savedreview";
        public static final String SAVETRAILERURL = "savedtrailerurl";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAV_MOVIES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAV_MOVIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAV_MOVIES;

        // for building URIs on insertion
        public static Uri buildFlavorsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
