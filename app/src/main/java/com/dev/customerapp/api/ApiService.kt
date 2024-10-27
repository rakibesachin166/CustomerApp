package com.dev.customerapp.api;

import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.CommonResponse
import retrofit2.Call
import retrofit2.http.Body;
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST;

interface ApiService {
    /*@POST("createUser")
    fun createUser(@Body userRequest: UserDataModel): Call<CommonResponse<UserDataModel>>*/


    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<CommonResponse<UserDataModel>>

}
