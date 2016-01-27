package pottitrain.orianapps.topmovies2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import pottitrain.orianapps.topmovies2.Adapters.MainAdapter;
import pottitrain.orianapps.topmovies2.Helpers.DataHelper;
import pottitrain.orianapps.topmovies2.Fragments.MovieDetailFragment;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import pottitrain.orianapps.topmovies2.JsonModel.MovieList;
import pottitrain.orianapps.topmovies2.JsonModel.Movie;
import pottitrain.orianapps.topmovies2.R;
import pottitrain.orianapps.topmovies2.RetrofitService;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;


public class MovieListActivity extends AppCompatActivity {
    private boolean twoPane;
    private DataHelper dataHelper;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
         // Instantiate shared preferences
                sharedPref = getPreferences(Context.MODE_PRIVATE);
        //Check if two pane layout is used
        if (findViewById(R.id.frame_movie_detail) != null) {
            twoPane = true;
        }

        GetMovies getMovies = new GetMovies();
        getMovies.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences.Editor editor = sharedPref.edit();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_bypopular) {
            //Save sort order preference in SharedPreference, to be use if app closes or gets terminated
            editor.putString(getString(R.string.sharedprefsort), getString(R.string.sortByPopular));
            editor.commit();
            GetMovies getMovies = new GetMovies();
            getMovies.execute();
            return true;
        }
        if (id == R.id.action_settings_byratings) {
            editor.putString(getString(R.string.sharedprefsort), getString(R.string.sortByVote));
            editor.commit();
            GetMovies getMovies = new GetMovies();
            getMovies.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetMovies extends AsyncTask {

        // Instantiate shared preferences
        private SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        //Get shared prefs which stores last selection of sort order from user, else its default value --> "by popular"
        private String sort = sharedPref.getString(getString(R.string.sharedprefsort), getString(R.string.sortByPopular));

        //Instantiate the TMDB service
        private TMDBInterface tmdbService = new RetrofitService(getApplicationContext()).getService(TMDBInterface.class);

        //Load full results using the sort  as the parameter in the method
        private Call<MovieList> listofResults = tmdbService.loadTopMovies(sort, getString(R.string.key));

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Response<MovieList> response = listofResults.execute();
                dataHelper = new DataHelper(response.body());
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
                GridView gridView = (GridView) findViewById(R.id.movie_list);
                gridView.setAdapter(new MainAdapter(getApplicationContext(), dataHelper.getAllPosterUrls()));

                // try to set gridview click listeners
                setOnClickListeners(gridView);
            } catch (Exception e) {

                e.printStackTrace();
                Snackbar.make(findViewById(android.R.id.content), "Could Not Connect to Database", Snackbar.LENGTH_LONG)
                        .show();
            }

        }

    }

    private void setOnClickListeners(GridView gridView){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = dataHelper.getListofMovies().get(i);
                Bundle bundle = new Bundle();
                bundle.putString(MovieDetailFragment.TITLE, movie.getOriginalTitle());
                bundle.putString(MovieDetailFragment.POSTERPATH, movie.getPosterPath());
                bundle.putString(MovieDetailFragment.OVERVIEW, movie.getOverview());
                bundle.putString(MovieDetailFragment.RELEASEDATE, movie.getReleaseDate());
                bundle.putString(MovieDetailFragment.VOTE, String.valueOf(movie.getVoteAverage()));
                bundle.putString(MovieDetailFragment.ID, String.valueOf(movie.getId()));

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


}
