package com.djaphar.retrofitoauthtest.Interfaces;

import com.djaphar.retrofitoauthtest.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubClient {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String secret,
            @Field("code") String code
    );
}
