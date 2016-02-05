package pottitrain.orianapps.topmovies2.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by claudiusouca on 1/28/16.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "favmovies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.Favorite_Entry.TABLE_FAV_MOVIES +
                "(" + MoviesContract.Favorite_Entry.MOVIEID + " TEXT, " +
                MoviesContract.Favorite_Entry.OVERVIEW + " TEXT, " +
                MoviesContract.Favorite_Entry.TITLE + " TEXT, " +
                MoviesContract.Favorite_Entry.VOTE + " TEXT, " +
                MoviesContract.Favorite_Entry.POSTERPATH + " TEXT, " +
                MoviesContract.Favorite_Entry.SAVEDREVIEW + " TEXT, " +
                MoviesContract.Favorite_Entry.SAVETRAILERURL + " TEXT, " +
                MoviesContract.Favorite_Entry.RELEASEDATE + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Favorite_Entry.TABLE_FAV_MOVIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.Favorite_Entry.TABLE_FAV_MOVIES + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
