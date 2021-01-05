package kheloindia.com.assessment.webservice;

/**
 * Created by CT13 on 2017-05-08.
 */


import android.content.Context;
import java.util.concurrent.TimeUnit;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.util.AppConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofit;
    private static Retrofit retrofit2;

    public static Retrofit getClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();


        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // for coach (NSRS)
    public static Retrofit getClient2(){
        OkHttpClient client2 = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();


        if (retrofit2==null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(AppConfig.NSRS_BASE_URL).client(client2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }

}
