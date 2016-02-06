package pottitrain.orianapps.topmovies2.Interfaces;

import pottitrain.orianapps.topmovies2.Models.MovieList;

import pottitrain.orianapps.topmovies2.Models.ReviewList;
import pottitrain.orianapps.topmovies2.Models.VideoList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by claudiusouca on 1/14/16.
 */

//Interface which defines the the methods used to and creates the Url's used by Retrofit
public interface TMDBInterface {

    @GET("/3/discover/movie")
    Call<MovieList> loadTopMovies(@Query("sort_by") String sort, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewList> loadMovieReview(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<VideoList> loadMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

}

