package pottitrain.orianapps.topmovies2.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pottitrain.orianapps.topmovies2.Activities.MovieDetailActivity;
import pottitrain.orianapps.topmovies2.Activities.MovieGridListActivity;
import pottitrain.orianapps.topmovies2.Helpers.MoviesContract;
import pottitrain.orianapps.topmovies2.Helpers.MoviesProvider;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import pottitrain.orianapps.topmovies2.JsonModel.Review;
import pottitrain.orianapps.topmovies2.JsonModel.ReviewList;
import pottitrain.orianapps.topmovies2.JsonModel.Video;
import pottitrain.orianapps.topmovies2.JsonModel.VideoList;
import pottitrain.orianapps.topmovies2.R;
import pottitrain.orianapps.topmovies2.RetrofitService;
import retrofit.Call;
import retrofit.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieGridListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String TITLE = "TITLE";
    public static final String POSTERPATH = "POSTERPATH";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String VOTE = "VOTE";
    public static final String RELEASEDATE = "RELEASEDATE";
    public static final String ID = "ID";

    public static final int FIRST = 0;

    private Activity activity;
    private CollapsingToolbarLayout appBarLayout;
    private Bundle bundle;
    private ImageView posterImage;
    private String movieid;
    private String title;
    private String vote;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String savedReview;
    private String savedTrailerUrl;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        if (getArguments() != null) {
            bundle = this.getArguments();
        } else {
            bundle = activity.getIntent().getExtras();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        GetReviewsTrailers getReviewsTrailers = new GetReviewsTrailers();
        getReviewsTrailers.execute();

        // Initialize views and set their strings and images
        posterImage = (ImageView) rootView.findViewById(R.id.imagePoster);
        TextView releaseDateView = (TextView) rootView.findViewById(R.id.textReleaseDate);
        TextView overviewView = (TextView) rootView.findViewById(R.id.textPlot);

        TextView voteView = (TextView) rootView.findViewById(R.id.textAverageVote);


        // Set colors in detail view , dynamically, using Palette library
        View backgroundView = rootView.findViewById(R.id.movie_detail);

        // Get information from bundle, and set in detail view
        movieid = bundle.getString(ID);
        title = bundle.getString(TITLE);
        posterPath = bundle.getString(POSTERPATH);
        overview = bundle.getString(OVERVIEW);
        vote = bundle.getString(VOTE);
        releaseDate = bundle.getString(RELEASEDATE);

        voteView.setText("Average Vote Rating : " + vote);
        releaseDateView.setText("Release Date : " + releaseDate);
        overviewView.setText(overview);

        Picasso.with(rootView.getContext())
                .load(getResources().getString(R.string.imageBaseUrl) + posterPath)
                .into(posterImage);

        //Get image to build a palette swatch to add color to the layout
        //Put in try block, because sometimes posterImage would return null
        //In emulator
        try {
            Bitmap bitmap = ((BitmapDrawable) posterImage.getDrawable()).getBitmap();
            Palette pal = Palette.generate(bitmap);
            Palette.Swatch swatch = pal.getVibrantSwatch();

            if (swatch != null) {
                backgroundView.setBackgroundColor(swatch.getRgb());
                voteView.setTextColor(swatch.getBodyTextColor());
                releaseDateView.setTextColor(swatch.getBodyTextColor());
                appBarLayout.setContentScrimColor(swatch.getRgb());
                appBarLayout.setExpandedTitleColor(swatch.getTitleTextColor());
                appBarLayout.setTitle(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private class GetReviewsTrailers extends AsyncTask{
        //Create service
        TMDBInterface tmdbService = new RetrofitService(activity.getApplicationContext()).getService(TMDBInterface.class);
        Call<ReviewList> reviews = tmdbService.loadMovieReview(Integer.valueOf(bundle.getString(ID)), activity.getString(R.string.key));
        Call<VideoList> videos = tmdbService.loadMovieTrailers(Integer.valueOf(bundle.getString(ID)), activity.getString(R.string.key));

        List<Review> allReviews;
        List<Video> allVideos;

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                Response<ReviewList> response = reviews.execute();
                allReviews = response.body().getReviews();
                Response<VideoList> response1 = videos.execute();
                allVideos = response1.body().getVideos();
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try{

                TextView details = (TextView) activity.findViewById(R.id.textDetails);
                savedReview = allReviews.get(FIRST).getContent();
                details.setText(savedReview);


                TextView trailer = (TextView) activity.findViewById(R.id.textTrailers);
                savedTrailerUrl = allVideos.get(FIRST).getSite() + " Trailer";
                trailer.setText(savedTrailerUrl);

                ImageButton iButton = (ImageButton) getView().findViewById(R.id.buttonPlay);
                iButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtubeBaseUrl)
                                + allVideos.get(FIRST).getKey())));
                    }
                });

                ImageButton favButton = (ImageButton) getView().findViewById(R.id.favImageButton);
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addToFavorite();
                    }
                });


            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        private Uri addToFavorite(){
            Uri uri;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.Favorite_Entry.MOVIEID,movieid);
            contentValues.put(MoviesContract.Favorite_Entry.OVERVIEW,overview);
            contentValues.put(MoviesContract.Favorite_Entry.RELEASEDATE,releaseDate);
            contentValues.put(MoviesContract.Favorite_Entry.TITLE,title);
            contentValues.put(MoviesContract.Favorite_Entry.VOTE, vote);
            contentValues.put(MoviesContract.Favorite_Entry.POSTERPATH, posterPath);
            contentValues.put(MoviesContract.Favorite_Entry.SAVEDREVIEW,savedReview);
            contentValues.put(MoviesContract.Favorite_Entry.SAVETRAILERURL, savedTrailerUrl);

            uri = activity.getContentResolver().insert(MoviesContract.Favorite_Entry.CONTENT_URI,contentValues );
            return uri;
        }
    }


}