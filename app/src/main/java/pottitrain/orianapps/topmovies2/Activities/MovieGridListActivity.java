package pottitrain.orianapps.topmovies2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import pottitrain.orianapps.topmovies2.AsyncTasks.GetMovies;
import pottitrain.orianapps.topmovies2.Fragments.MovieDetailFragment;
import pottitrain.orianapps.topmovies2.Helpers.MoviesContract;
import pottitrain.orianapps.topmovies2.Interfaces.MovieListener;
import pottitrain.orianapps.topmovies2.Models.Movie;
import pottitrain.orianapps.topmovies2.R;

import java.util.ArrayList;
import java.util.List;

public class MovieGridListActivity extends AppCompatActivity implements MovieListener {
    private boolean twoPane;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //Get and set our toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Instantiate shared preferences
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //Check if two pane layout is used,
        twoPane = findViewById(R.id.frame_movie_detail) != null ;

        //Display movies in Grid
        displayMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //Save sort order preference in SharedPreference, to be use if app closes or gets terminated
        switch (id) {
            case R.id.action_settings_bypopular:
                putSharedPref(getString(R.string.sortByPopular));
                displayMovies();
                return true;
            case R.id.action_settings_byratings:
                putSharedPref(getString(R.string.sortByVote));
                displayMovies();
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

    private void setOnClickListeners(GridView gridView) {
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
                    //If phone/onepane start activity
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public void displayMovies(List<Movie> movieList, ArrayList<String> posterUrl) {
        this.movies = movieList;
        try {
            //Initialize and populate grid with Poster Images
            GridView gridView = (GridView) findViewById(R.id.movie_list);
            gridView.setAdapter(new MainAdapter(getApplicationContext(), posterUrl));

            //Set gridview click listeners
            setOnClickListeners(gridView);

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
