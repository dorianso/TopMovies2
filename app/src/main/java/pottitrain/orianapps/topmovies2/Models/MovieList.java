package pottitrain.orianapps.topmovies2.Models;

/**
 * Created by claudiusouca on 1/14/16.
 * http://www.jsonschema2pojo.org/ ---> Used for all models to create Pojo
 *                              |
 *                              |-----> Time Saver!
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieList {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = new ArrayList<Movie>();
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    /**
     *
     * @return
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     * The results
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


}