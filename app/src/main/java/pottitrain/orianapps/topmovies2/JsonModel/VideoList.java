package pottitrain.orianapps.topmovies2.JsonModel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by claudiusouca on 1/22/16.
 */
public class VideoList {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Video> videos = new ArrayList<Video>();

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
     * @return The results
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     * @param videos The results
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}