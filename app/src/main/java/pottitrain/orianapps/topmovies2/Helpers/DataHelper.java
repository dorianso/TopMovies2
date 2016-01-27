package pottitrain.orianapps.topmovies2.Helpers;

import java.util.ArrayList;
import java.util.List;

import pottitrain.orianapps.topmovies2.JsonModel.MovieList;
import pottitrain.orianapps.topmovies2.JsonModel.Movie;
import pottitrain.orianapps.topmovies2.JsonModel.Review;
import pottitrain.orianapps.topmovies2.JsonModel.ReviewList;
import pottitrain.orianapps.topmovies2.JsonModel.VideoList;

/**
 * Created by claudiusouca on 1/15/16.
 */
public class DataHelper {
    private MovieList movieList;
    private List<Movie> listofMovies;
    private List<Review> listofReviews;

    public DataHelper(MovieList movieList){
        if(movieList != null){
            listofMovies = movieList.getMovies();
        }

    }
    public DataHelper(VideoList videoList){
        if(movieList != null){
            listofMovies = movieList.getMovies();
        }

    }
    public DataHelper(ReviewList reviewList){
        if(movieList != null){
            listofMovies = movieList.getMovies();
        }

    }

    public List<Movie> getListofMovies() {
        return listofMovies;
    }

    public ArrayList<String> getAllPosterUrls(){

        ArrayList<String> urls = new ArrayList<>();

        for (Movie movie : listofMovies){
            urls.add(movie.getPosterPath());
        }

        return urls;
    }
}
