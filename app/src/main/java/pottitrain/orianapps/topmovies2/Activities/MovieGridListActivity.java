package pottitrain.orianapps.topmovies2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import pottitrain.orianapps.topmovies2.Adapters.MainAdapter;
import pottitrain.orianapps.topmovies2.AsyncTasks.GetMovies;
import pottitrain.orianapps.topmovies2.Fragments.MovieDetailFragment;
import pottitrain.orianapps.topmovies2.Helpers.DataHelper;
import pottitrain.orianapps.topmovies2.Helpers.MoviesContract;
import pottitrain.orianapps.topmovies2.Interfaces.MovieListener;
import pottitrain.orianapps.topmovies2.Models.Movie;
import pottitrain.orianapps.topmovies2.Models.MovieList;
import pottitrain.orianapps.topmovies2.R;

import java.util.ArrayList;
import java.util.List;

public class MovieGridListActivity extends AppCompatActivity implements MovieListener {
    private boolean twoPane;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private List<Movie> movies;
    Context context;
    DataHelper dataHelper ;
    MovieList savedMovieList = new MovieList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        this.context = this;
        //Get and set our toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Instantiate shared preferences
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //Check if two pane layout is used,
        twoPane = findViewById(R.id.frame_movie_detail) != null;
        dataHelper = new DataHelper();

        if(savedInstanceState != null){
            String jsonObj = savedInstanceState.getString("savedMovies");
            MovieList movieList = new Gson().fromJson(jsonObj, MovieList.class);
            displayMovies(movieList.getMovies(), dataHelper.getAllPosterUrls(movieList.getMovies()));
        }
        else{
            displayMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Convert List<Movies> to MoviesList object to JsonObject and save in bundle as string
        String jsonObj = new Gson().toJson(savedMovieList);
        outState.putString("savedMovies", jsonObj);
        Log.d("IN ", "OnSave");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String jsonObj = savedInstanceState.getString("savedMovies");
        MovieList movieList = new Gson().fromJson(jsonObj, MovieList.class);
        displayMovies(movieList.getMovies(), dataHelper.getAllPosterUrls(movieList.getMovies()));
        Log.d("IN ", "onRestore");
        super.onRestoreInstanceState(savedInstanceState);
    }

    // TEST ON SAVE
    protected void onPause() {
        Log.d("IN ","onPause");
        super.onPause();
    }
    protected void onResume() {
        Log.d("IN ", "onResume");
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Save sort order preference in SharedPreference, to be use if app closes or gets terminated
        switch (id) {
            case R.id.action_settings_bypopular:
                if (!getSharedPref().equalsIgnoreCase(getString(R.string.sortByPopular))) {
                    putSharedPref(getString(R.string.sortByPopular));
                    displayMovies();
                }
                return true;
            case R.id.action_settings_byratings:
                if (!getSharedPref().equalsIgnoreCase(getString(R.string.sortByVote))) {
                    putSharedPref(getString(R.string.sortByVote));
                    displayMovies();
                }
                return true;
            case R.id.action_settings_byfavorites:
                // only if Favorite list is not displayed, show it
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
        GetMovies getMovies = new GetMovies(this, getBaseContext(), getSharedPref());
        getMovies.execute();
    }

    private void setOnClickListeners(GridView gridView, final List<Movie> movies) {
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
                    //If tablet/twopane create framgent
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_movie_detail, fragment)
                            .commit();
                } else {

                    // /If phone/onepane start activity
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public void displayMovies(List<Movie> movieList, ArrayList<String> posterUrl) {
        savedMovieList.setMovies(movieList);
        try {
            //Initialize and populate grid with Poster Images
            GridView gridView = (GridView) findViewById(R.id.movie_list);
            gridView.setAdapter(new MainAdapter(getApplicationContext(), posterUrl));

            //Set gridview click listeners
            setOnClickListeners(gridView, movieList);

        } catch (Exception exception) {
            exception.printStackTrace();
            Snackbar.make(findViewById(android.R.id.content), "Could Not Connect to Database", Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    private String getSharedPref() {
        //Return sort prefernce or the default: "sort by popular"
        return sharedPref.getString(getString(R.string.sharedprefsort), getString(R.string.sortByPopular));
    }

    private void putSharedPref(String stringPref) {
        editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedprefsort), stringPref);
        editor.commit();
    }

}
