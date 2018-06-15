package integration.google.sunder.downloadfilefromserverusingretrofitlibraryexample;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

public interface RetrofitInterface {
    @GET("omer-Shahzad-performed-umrah-600x548.jpg")
    @Streaming
    Call<ResponseBody> downloadFile();
}
