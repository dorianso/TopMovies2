package pottitrain.orianapps.topmovies2.Interfaces;

import java.util.ArrayList;
import java.util.List;

import pottitrain.orianapps.topmovies2.Models.Movie;

/**
 * Created by claudiusouca on 2/5/16.
 */
public interface MovieListener {
    void displayMovies(List<Movie> movieList, ArrayList<String> posterUrl);
}
