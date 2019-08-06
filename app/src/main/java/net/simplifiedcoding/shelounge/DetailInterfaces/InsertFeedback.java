package net.simplifiedcoding.shelounge.DetailInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by tjuhi on 6/1/2017.
 */

public interface InsertFeedback {
    @FormUrlEncoded
    @POST("/insert.php")
    public void insertUser(

            @Field("user") String user,
            @Field("rating") float rating,
            //  @Field("comment") String comment,
            @Field("date") String date,
            @Field("LONGITUDE") String SN,
            Callback<Response> callback);
}
