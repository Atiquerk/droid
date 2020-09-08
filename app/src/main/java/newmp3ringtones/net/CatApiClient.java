package newmp3ringtones.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CatApiClient {

    public static final String BASE_URL="https://techcircle.com.pk/";
    public static Retrofit retrofit=null;

    public static Retrofit getAPIClient(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
