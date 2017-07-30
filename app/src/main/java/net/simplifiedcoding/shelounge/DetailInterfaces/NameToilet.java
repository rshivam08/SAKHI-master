package net.simplifiedcoding.shelounge.DetailInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by tjuhi on 6/20/2017.
 */

public interface NameToilet {
    @FormUrlEncoded
    @POST("/name.php")
    public void where(
            @Field("longitude") String longitude,
            Callback<Response> callback
    );
}
