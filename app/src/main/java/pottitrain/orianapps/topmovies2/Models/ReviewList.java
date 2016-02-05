package pottitrain.orianapps.topmovies2.Models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by claudiusouca on 1/22/16.
 * http://www.jsonschema2pojo.org/ ---> Used for all models to create Pojo
 *                              |
 *                              |-----> Time Saver!
 */
public class ReviewList {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Review> reviews = new ArrayList<Review>();
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalReviews;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews The results
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * @return The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return The totalReviews
     */
    public int getTotalReviews() {
        return totalReviews;
    }

    /**
     * @param totalReviews The total_reviews
     */
    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

}