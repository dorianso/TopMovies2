package pottitrain.orianapps.topmovies2.AsyncTasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import pottitrain.orianapps.topmovies2.Interfaces.ReviewTrailerListener;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import pottitrain.orianapps.topmovies2.Models.Review;
import pottitrain.orianapps.topmovies2.Models.ReviewList;
import pottitrain.orianapps.topmovies2.Models.Video;
import pottitrain.orianapps.topmovies2.Models.VideoList;
import pottitrain.orianapps.topmovies2.R;
import pottitrain.orianapps.topmovies2.RetrofitService;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by claudiusouca on 2/5/16.
 */
public class GetReviewTrailer extends AsyncTask {

    private Context context;
    private String key;
    private int movieid;
    private List<Review> allReviews;
    private List<Video> allVideos;
    private ReviewTrailerListener rtl;

    public GetReviewTrailer(ReviewTrailerListener rtl, Context context, int movieid) {
        this.context = context;
        this.key = context.getString(R.string.key);
        this.movieid = movieid;
        this.rtl = rtl;

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //Create service
        TMDBInterface tmdbService = new RetrofitService(context).getService(TMDBInterface.class);
        Call<ReviewList> reviews = tmdbService.loadMovieReview(Integer.valueOf(movieid), key);
        Call<VideoList> videos = tmdbService.loadMovieTrailers(Integer.valueOf(movieid), key);

        try {

            allReviews = reviews.execute().body().getReviews();
            allVideos = videos.execute().body().getVideos();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try {
            //Send back trailer and reviews to Listener
            rtl.callbackReviewTrailer(allReviews, allVideos);

        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }
}
