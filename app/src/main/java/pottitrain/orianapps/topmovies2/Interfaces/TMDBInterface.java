package pottitrain.orianapps.topmovies2.Interfaces;

import java.util.List;

import pottitrain.orianapps.topmovies2.JsonModel.MovieList;

import pottitrain.orianapps.topmovies2.JsonModel.Review;
import pottitrain.orianapps.topmovies2.JsonModel.ReviewList;
import pottitrain.orianapps.topmovies2.JsonModel.Video;
import pottitrain.orianapps.topmovies2.JsonModel.VideoList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by claudiusouca on 1/14/16.
 */

public interface TMDBInterface {

    @GET("/3/discover/movie")
    Call<MovieList> loadTopMovies(@Query("sort_by") String sort, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewList> loadMovieReview(@Path("id") int id, @Query("api_key") String apiKey);

    //http://api.themoviedb.org/3/movie/76341/videos?api_key=f2b09135edaec7a70b9913117d275a27
    @GET("/3/movie/{id}/videos")
    Call<VideoList> loadMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

}
