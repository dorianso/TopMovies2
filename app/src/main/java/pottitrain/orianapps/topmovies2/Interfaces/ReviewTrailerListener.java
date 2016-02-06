package pottitrain.orianapps.topmovies2.Interfaces;

import java.util.List;

import pottitrain.orianapps.topmovies2.Models.Review;
import pottitrain.orianapps.topmovies2.Models.Video;

/**
 * Created by claudiusouca on 2/5/16.
 */
public interface ReviewTrailerListener {
    void callbackReviewTrailer(List<Review> reviewList,List<Video> trailerVideo);
}
