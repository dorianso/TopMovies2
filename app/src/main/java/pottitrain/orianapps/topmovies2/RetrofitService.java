package pottitrain.orianapps.topmovies2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.net.Authenticator;

import pottitrain.orianapps.topmovies2.Helpers.DataHelper;
import pottitrain.orianapps.topmovies2.Interfaces.TMDBInterface;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by claudiusouca on 1/15/16.
 */

//Instantiates Retrofit service defined by serviceClass
public class RetrofitService {

    Context context;

    public RetrofitService(Context context) {
        this.context = context;
    }

    public <T>T getService(Class<T> serviceClass) {
        //Logs retorift http requests.. used for testing only
        //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient httpClient = new OkHttpClient();
//      httpClient.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        //Returns Retrofit service
        return retrofit.create(serviceClass);

    }


}
