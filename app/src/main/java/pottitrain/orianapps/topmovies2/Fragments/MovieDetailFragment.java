package pottitrain.orianapps.topmovies2.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import pottitrain.orianapps.topmovies2.Activities.MovieDetailActivity;
import pottitrain.orianapps.topmovies2.Activities.MovieListActivity;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import pottitrain.orianapps.topmovies2.JsonModel.MovieList;
import pottitrain.orianapps.topmovies2.JsonModel.Review;
import pottitrain.orianapps.topmovies2.JsonModel.ReviewList;
import pottitrain.orianapps.topmovies2.JsonModel.Video;
import pottitrain.orianapps.topmovies2.JsonModel.VideoList;
import pottitrain.orianapps.topmovies2.R;
import pottitrain.orianapps.topmovies2.RetrofitService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
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

    Activity activity;
    CollapsingToolbarLayout appBarLayout;
    Bundle bundle;
    ImageView posterImage;
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
        TextView releaseDate = (TextView) rootView.findViewById(R.id.textReleaseDate);
        TextView overview = (TextView) rootView.findViewById(R.id.textPlot);

        TextView vote = (TextView) rootView.findViewById(R.id.textAverageVote);
        String posterPath;

        // Set colors in detail view , dynamically, using Palette library
        View backgroundView = rootView.findViewById(R.id.movie_detail);
        String stitle = "";

        // Get information from bundle, and set in detail view
        stitle = bundle.getString(TITLE);
        posterPath = bundle.getString(POSTERPATH);
        overview.setText(bundle.getString(OVERVIEW));
        vote.setText("Average Vote Rating : " + bundle.getString(VOTE));
        releaseDate.setText("Release Date : " + bundle.getString(RELEASEDATE));

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
                vote.setTextColor(swatch.getBodyTextColor());
                releaseDate.setTextColor(swatch.getBodyTextColor());
                appBarLayout.setContentScrimColor(swatch.getRgb());
                appBarLayout.setExpandedTitleColor(swatch.getTitleTextColor());
                appBarLayout.setTitle(stitle);


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
                details.setText(allReviews.get(FIRST).getContent());

                TextView trailer = (TextView) activity.findViewById(R.id.textTrailers);
                trailer.setText(allVideos.get(0).getSite() + " Trailer");
                ImageButton iButton = (ImageButton) getView().findViewById(R.id.buttonPlay);
                iButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtubeBaseUrl)
                                + allVideos.get(FIRST).getKey())));
                    }
                });

            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }

}
