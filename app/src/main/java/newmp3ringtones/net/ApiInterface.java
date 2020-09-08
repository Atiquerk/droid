package newmp3ringtones.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("Main/index")
    Call<List<DataResponse>> getRings(@Query("page_number") int page,@Query("item_count") int items);
}
