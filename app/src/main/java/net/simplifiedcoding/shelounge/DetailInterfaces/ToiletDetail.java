package net.simplifiedcoding.shelounge.DetailInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by tjuhi on 6/26/2017.
 */

public interface ToiletDetail {
    @FormUrlEncoded
    @POST("/toilet_detail.php")
    public void where(
            @Field("longitude") String LONGITUDE,
            Callback<Response> callback
    );
}
