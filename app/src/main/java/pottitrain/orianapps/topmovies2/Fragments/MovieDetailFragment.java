package pottitrain.orianapps.topmovies2.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import pottitrain.orianapps.topmovies2.Activities.MovieDetailActivity;
import pottitrain.orianapps.topmovies2.Activities.MovieGridListActivity;
import pottitrain.orianapps.topmovies2.Helpers.MoviesContract;
import pottitrain.orianapps.topmovies2.Interfaces.ReviewTrailerListener;
import pottitrain.orianapps.topmovies2.AsyncTasks.GetReviewTrailer;
import pottitrain.orianapps.topmovies2.Models.Review;
import pottitrain.orianapps.topmovies2.Models.Video;
import pottitrain.orianapps.topmovies2.R;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieGridListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements ReviewTrailerListener {

    public static final int FIRST = 0;
    private android.support.v7.widget.ShareActionProvider share;
    private Intent shareIntent;
    private Activity activity;
    private CollapsingToolbarLayout appBarLayout;

    private String movieid;
    private String title;
    private String vote;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String savedReview;
    private String savedTrailerUrl;
    TextView reviewView;
    View rootView;

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
        //Sets option menu to true *Required in Fragment*
        setHasOptionsMenu(true);

        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        //Get bundle from arguements if not null, else get from activity extras
        Bundle bundle = (getArguments() != null) ? this.getArguments() : activity.getIntent().getExtras();

        // Get information from bundle, and set in detail view
        movieid = bundle.getString(MoviesContract.Favorite_Entry.MOVIEID);
        title = bundle.getString(MoviesContract.Favorite_Entry.TITLE);
        posterPath = bundle.getString(MoviesContract.Favorite_Entry.POSTERPATH);
        overview = bundle.getString(MoviesContract.Favorite_Entry.OVERVIEW);
        vote = bundle.getString(MoviesContract.Favorite_Entry.VOTE);
        releaseDate = bundle.getString(MoviesContract.Favorite_Entry.RELEASEDATE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Init main layout
        rootView = inflater.inflate(R.layout.movie_detail, container, false);

        //Instantiate divider to apply swatch color
        View divider = rootView.findViewById(R.id.divider);

        // Initialize views and set their strings and images
        ImageView posterImage = (ImageView) rootView.findViewById(R.id.imagePoster);
        TextView releaseDateView = (TextView) rootView.findViewById(R.id.textReleaseDate);
        TextView overviewView = (TextView) rootView.findViewById(R.id.textPlot);
        overviewView.setMovementMethod(new ScrollingMovementMethod());
        TextView voteView = (TextView) rootView.findViewById(R.id.textAverageVote);
        View backgroundView = rootView.findViewById(R.id.movie_detail);
        reviewView = (TextView) rootView.findViewById(R.id.textReview);
        reviewView.setText(" TESTING IF WE HAVE IT !!!");

        // Set colors in detail view , dynamically, using Palette library
        voteView.setText("Rating : " + vote);
        releaseDateView.setText("Released : " + releaseDate);
        overviewView.setText("Plot :" + "\n" + "\t\t\t" + overview);

        Picasso.with(rootView.getContext())
                .load(getResources().getString(R.string.imageBaseUrl) + posterPath)
                .into(posterImage);

        //Launch asynctask to get trailers and reviews
        GetReviewTrailer getReviewsTrailers = new GetReviewTrailer(this, getContext(), Integer.valueOf(movieid));
        getReviewsTrailers.execute();

        //Get image to build a palette swatch to add color to the layout
        try {
            Bitmap bitmap = ((BitmapDrawable) posterImage.getDrawable()).getBitmap();
            Palette pal = Palette.generate(bitmap);
            Palette.Swatch swatch = pal.getVibrantSwatch();

            if (swatch != null) {
                divider.setBackgroundColor(swatch.getRgb());
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().navigateUpTo(new Intent(getActivity(), MovieGridListActivity.class));
                return true;
            case android.R.id.shareText:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu resource
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Create easy share action
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        share = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        super.onPrepareOptionsMenu(menu);
    }

    //Callback used by Asynctask to send back reviews and trailers
    @Override
    public void callbackReviewTrailer(List<Review> reviewList, List<Video> trailerVideo) {
        if (trailerVideo != null && trailerVideo.size() > 0) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, (getString(R.string.youtubeBaseUrl)
                    + trailerVideo.get(FIRST).getKey()).toString());
            setShareIntent(shareIntent);
            savedTrailerUrl = (getString(R.string.youtubeBaseUrl) + trailerVideo.get(FIRST).getKey().toString());
            System.out.println(" Trailer is " + savedTrailerUrl);
        }
        initViews();
        savedReview = (reviewList != null && reviewList.size() > 0) ? reviewList.get(FIRST).getContent().toString() : " No Review ";
        reviewView.setText(savedReview);

    }

    //Initialize buttons and set share intents
    public void initViews() {


        //reviews.setText("Review : "+ "\n" + "\t\t\t" + savedReview);

        ImageButton iButton = (ImageButton) rootView.findViewById(R.id.buttonPlay);
        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(savedTrailerUrl)));
            }
        });

        LikeButton favButton = (LikeButton) rootView.findViewById(R.id.favImageButton);
        favButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addToFavorite();
                likeButton.setEnabled(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //Do nothing for now
            }

        });

        shareIntent.putExtra(Intent.EXTRA_TEXT, savedTrailerUrl);
        setShareIntent(shareIntent);

    }

    //Create movie and add to favorite movies/database
    private Uri addToFavorite() {
        Uri uri;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.Favorite_Entry.MOVIEID, movieid);
        contentValues.put(MoviesContract.Favorite_Entry.OVERVIEW, overview);
        contentValues.put(MoviesContract.Favorite_Entry.RELEASEDATE, releaseDate);
        contentValues.put(MoviesContract.Favorite_Entry.TITLE, title);
        contentValues.put(MoviesContract.Favorite_Entry.VOTE, vote);
        contentValues.put(MoviesContract.Favorite_Entry.POSTERPATH, posterPath);
        contentValues.put(MoviesContract.Favorite_Entry.SAVEDREVIEW, savedReview);
        contentValues.put(MoviesContract.Favorite_Entry.SAVETRAILERURL, savedTrailerUrl);

        uri = activity.getContentResolver().insert(MoviesContract.Favorite_Entry.CONTENT_URI, contentValues);
        System.out.println(" adding " + uri);
        return uri;

    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (share != null) {
            share.setShareIntent(shareIntent);
        }
    }
}