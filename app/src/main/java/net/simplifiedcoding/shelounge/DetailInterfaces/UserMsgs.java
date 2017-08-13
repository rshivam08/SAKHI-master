package net.simplifiedcoding.shelounge.DetailInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by tjuhi on 8/6/2017.
 */

public interface UserMsgs {
    @FormUrlEncoded
    @POST("/userMsg.php")
    public void insertUser(

            @Field("imei") String imei,
            @Field("num1") String num1,
            @Field("num2") String num2,
            @Field("num3") String num3,
            @Field("num4") String num4,
            @Field("num5") String num5,

            @Field("date") String date,

            Callback<Response> callback);
}
