package newmp3ringtones.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatApiInterface {

    @GET("Main/cats")
    Call<List<CatsResponse>> getCats(@Query("page_number") int page,@Query("item_count") int items);
}
