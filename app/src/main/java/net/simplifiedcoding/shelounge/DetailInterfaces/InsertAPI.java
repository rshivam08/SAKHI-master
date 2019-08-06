package net.simplifiedcoding.shelounge.DetailInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Shivam on 8/4/2017.
 */

public interface InsertAPI {
    @FormUrlEncoded
    @POST("/userdetail.php")
    public void insertUser(
            @Field("name") String name,
            @Field("age") String age,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("aadhar") String aadhar,
            @Field("imei") String imei,

            Callback<Response> callback);
}
