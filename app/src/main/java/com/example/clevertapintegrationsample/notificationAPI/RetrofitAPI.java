package com.example.clevertapintegrationsample.notificationAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitAPI {

    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @POST("1/send/push.json")
    //on below line we are creating a method to post our data.
    Call<NotificationResponse> createPost(@Header("X-CleverTap-Account-Id") String id,
                             @Header("X-CleverTap-Passcode") String passcode,
                             @Header("Content-Type") String contentType,
                             @Body NotificationRequest dataModal);


}