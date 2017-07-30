package net.simplifiedcoding.shelounge.DetailInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by tjuhi on 6/20/2017.
 */

public interface AvgRating {
    @FormUrlEncoded
    @POST("/avg.php")
    public void where(
            @Field("LONGITUDE") String SN,
            Callback<Response> callback
    );
}
