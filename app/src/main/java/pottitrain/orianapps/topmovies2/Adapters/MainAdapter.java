package pottitrain.orianapps.topmovies2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pottitrain.orianapps.topmovies2.R;

/**
 * Created by claudiusouca on 1/14/16.
 */
public class MainAdapter extends ArrayAdapter {

    ArrayList<String> imageUrls;
    Context context;


    public MainAdapter(Context context, ArrayList<String> imageUrls) {
        super(context, R.layout.movie_poster_content, imageUrls);
        this.imageUrls = imageUrls;
        this.context = context;

    }


    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public String getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.movie_poster_content, viewGroup, false);

        }

        Picasso.with(context)
                .load(context.getResources().getString(R.string.imageBaseUrl) + imageUrls.get(position))
                .into((ImageView) view);

        return view;
    }
}
