package pottitrain.orianapps.topmovies2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import pottitrain.orianapps.topmovies2.Adapters.MainAdapter;
import pottitrain.orianapps.topmovies2.Helpers.DataHelper;
import pottitrain.orianapps.topmovies2.Fragments.MovieDetailFragment;
import pottitrain.orianapps.topmovies2.Helpers.MoviesContract;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import pottitrain.orianapps.topmovies2.Models.MovieList;
import pottitrain.orianapps.topmovies2.Models.Movie;
import pottitrain.orianapps.topmovies2.R;
import pottitrain.orianapps.topmovies2.RetrofitService;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MovieGridListActivity extends AppCompatActivity {
    private boolean twoPane;
    private DataHelper dataHelper;
    private ArrayList<String> moviePosterUrls;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private List<Movie> movies;
    private List<Movie> favMovieList;
    private List<Movie> onlineMovieList;

    static final int FAVMOVIES = 0;
    static final int ONLINEMOVIES = 1;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //Get and set our toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Init our Grid
        gridView = (GridView) findViewById(R.id.movie_list);

        // Instantiate shared preferences
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //Helps us manage our movies
        dataHelper = new DataHelper();

        //Check if two pane layout is used
        if (findViewById(R.id.frame_movie_detail) != null) {
            twoPane = true;
        }

        //Display movies in Grid
        displayMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings_bypopular) {
            //Save sort order preference in SharedPreference, to be use if app closes or gets terminated
            putSharedPref(getString(R.string.sortByPopular));
            displayMovies();
            return true;
        }
        if (id == R.id.action_settings_byratings) {
            putSharedPref(getString(R.string.sortByVote));
            displayMovies();
            return true;
        }
        if (id == R.id.action_settings_byfavorites) {
            //if Favorite list is not displayed, show it
            if (!getSharedPref().equalsIgnoreCase("favorite")) {
                putSharedPref(getString(R.string.sortByFavorite));
                displayMovies();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayMovies() {
        if (getSharedPref().equalsIgnoreCase(getString(R.string.sortByFavorite))) {
            GetFavorites();
        } else {
            GetMovies getMovies = new GetMovies();
            getMovies.execute();
        }
    }

    private class GetMovies extends AsyncTask {
        //Instantiate the TMDB service
        private TMDBInterface tmdbService = new RetrofitService(getApplicationContext()).getService(TMDBInterface.class);

        //Load full results using the sort order from pref  as the parameter in the method
        private Call<MovieList> listofResults = tmdbService.loadTopMovies(getSharedPref(), getString(R.string.key));

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                onlineMovieList= listofResults.execute().body().getMovies();
                moviePosterUrls = dataHelper.getAllPosterUrls(onlineMovieList);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                // Initialize and populate grid with information
                gridView.setAdapter(new MainAdapter(getApplicationContext(), moviePosterUrls));
                // try to set gridview click listeners
                setOnClickListeners(gridView, ONLINEMOVIES);
            } catch (Exception exception) {
                exception.printStackTrace();
                Snackbar.make(findViewById(android.R.id.content), "Could Not Connect to Database", Snackbar.LENGTH_LONG)
                        .show();
            }

        }

    }

    private void GetFavorites() {
        Cursor cursor = getContentResolver().query(MoviesContract.Favorite_Entry.CONTENT_URI,
                null,
                null,
                null,
                null);

        try {
            dataHelper.setFavoritesMovies(cursor);
            favMovieList = dataHelper.getFavoritesMovies();
            moviePosterUrls = dataHelper.getAllPosterUrls(favMovieList);
            gridView.setAdapter(new MainAdapter(getApplicationContext(), moviePosterUrls));
            // try to set gridview click listeners
            setOnClickListeners(gridView, FAVMOVIES);
        } catch (Exception exception) {
            exception.printStackTrace();
            Snackbar.make(findViewById(android.R.id.content), "No favorite movies to Display", Snackbar.LENGTH_LONG)
                    .show();
        } finally {
            cursor.close();
        }
    }

    private void setOnClickListeners(GridView gridView, int x) {

        if (x == FAVMOVIES) {
            movies = favMovieList;
        } else if (x == ONLINEMOVIES) {
            movies = onlineMovieList;
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString(MoviesContract.Favorite_Entry.TITLE, movies.get(i).getOriginalTitle());
                bundle.putString(MoviesContract.Favorite_Entry.POSTERPATH, movies.get(i).getPosterPath());
                bundle.putString(MoviesContract.Favorite_Entry.OVERVIEW, movies.get(i).getOverview());
                bundle.putString(MoviesContract.Favorite_Entry.RELEASEDATE, movies.get(i).getReleaseDate());
                bundle.putString(MoviesContract.Favorite_Entry.VOTE, String.valueOf(movies.get(i).getVoteAverage()));
                bundle.putString(MoviesContract.Favorite_Entry.MOVIEID, String.valueOf(movies.get(i).getId()));

                if (twoPane) {
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_movie_detail, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

    }




    private String getSharedPref() {
        return sharedPref.getString(getString(R.string.sharedprefsort), getString(R.string.sortByPopular));
    }

    private void putSharedPref(String stringPref) {
        editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedprefsort), stringPref);
        editor.commit();
    }
}
