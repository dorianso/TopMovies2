package pottitrain.orianapps.topmovies2.Helpers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pottitrain.orianapps.topmovies2.Models.Movie;

/**
 * Created by claudiusouca on 1/15/16.
 */
public class DataHelper {

    private List<Movie> favoriteMovies;

    public void setFavoritesMovies(Cursor cursor) {
        //Init favorites list
        favoriteMovies = new ArrayList<>();

        //Get store column index
        int movieId = cursor.getColumnIndex(MoviesContract.Favorite_Entry.MOVIEID);
        int movieOverview = cursor.getColumnIndex(MoviesContract.Favorite_Entry.OVERVIEW);
        int moviePosterPath = cursor.getColumnIndex(MoviesContract.Favorite_Entry.POSTERPATH);
        int movieReleaseDate = cursor.getColumnIndex(MoviesContract.Favorite_Entry.RELEASEDATE);
        int movieTitle = cursor.getColumnIndex(MoviesContract.Favorite_Entry.TITLE);
        int movieVote = cursor.getColumnIndex(MoviesContract.Favorite_Entry.VOTE);

        cursor.moveToFirst();
        //Iterate through rows and store movie info into Movie objects

        do {
            //Create movie object to store info
            Movie movie = new Movie();

            movie.setId(Integer.valueOf(cursor.getString(movieId)));
            movie.setOverview(cursor.getString(movieOverview));
            movie.setPosterPath(cursor.getString(moviePosterPath));
            movie.setReleaseDate(cursor.getString(movieReleaseDate));
            movie.setTitle(cursor.getString(movieTitle));
            movie.setVoteAverage(Double.valueOf(cursor.getString(movieVote)));

            favoriteMovies.add(movie);

            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        //Close cursor after getting all the rows
        cursor.close();
    }

    public List<Movie> getFavoritesMovies() {
        return favoriteMovies;
    }


    ArrayList<String> urls = new ArrayList<>();
    //Return posterUrls from each movie
    public ArrayList<String> getAllPosterUrls(List<Movie> movies) {

        if (movies != null) {

            for (Movie movie : movies) {
                urls.add(movie.getPosterPath());
            }

        }


        return urls;
    }

}
