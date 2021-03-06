package pottitrain.orianapps.topmovies2.AsyncTasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pottitrain.orianapps.topmovies2.Helpers.DataHelper;
import pottitrain.orianapps.topmovies2.Helpers.MoviesContract;
import pottitrain.orianapps.topmovies2.Interfaces.MovieListener;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import pottitrain.orianapps.topmovies2.Models.Movie;
import pottitrain.orianapps.topmovies2.Models.MovieList;
import pottitrain.orianapps.topmovies2.R;
import pottitrain.orianapps.topmovies2.RetrofitService;
import retrofit.Call;
import retrofit.Response;


/**
 * Created by claudiusouca on 2/5/16.
 */
public class GetMovies extends AsyncTask {

    private Context context;
    private String sort;
    private String key;
    private List<Movie> movieList;
    private MovieListener movieListener;
    private DataHelper dataHelper = new DataHelper();

    public GetMovies(MovieListener movieListener, Context context, String sort) {
        this.context = context;
        this.sort = sort;
        this.movieListener = movieListener;
        this.key = context.getString(R.string.key);

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        switch (sort) {
            case "favorite":
                GetFavorites();
                break;
            default:
                //Instantiate the TMDB service
                TMDBInterface tmdbService = new RetrofitService(context).getService(TMDBInterface.class);
                //Load full results using the sort order from pref  as the parameter in the method
                Call<MovieList> listofResults = tmdbService.loadTopMovies(sort, key);
                try {
                    Response<MovieList> response = listofResults.execute();
                    //If response is null object create new List of movies
                    movieList = (response.body() != null) ? response.body().getMovies() : new ArrayList<Movie>();
                    System.out.println("movie list is " + movieList.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        return null;

    }

    private void GetFavorites() {
        Cursor cursor = context.getContentResolver().query(MoviesContract.Favorite_Entry.CONTENT_URI,
                null,
                null,
                null,
                null);
        try {
            if(cursor.getCount()>0){
                dataHelper.setFavoritesMovies(cursor);
            }
            movieList = dataHelper.getFavoritesMovies();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(movieList != null && movieList.size() > 0){
            if(sort.equalsIgnoreCase(context.getString(R.string.sortByPopular))){
                //Flag that popular movies have been loaded
                dataHelper.setPopularLoaded(true);
            }
            else {
                // Flag that voted movies have been loaded
                dataHelper.setVotedLoaded(true);
            }
            //Callback display movies
            movieListener.displayMovies(movieList, dataHelper.getAllPosterUrls(movieList));

        }

    }

}
