package com.example.mysoreprintersproject_lvdmanagement.network

import com.example.mysoreprintersproject_lvdmanagement.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {




    @FormUrlEncoded
    @POST("/mobile/login/")
    suspend fun login(
        @Field("email")  username: String,
        @Field("password")  password: String
    ) : LoginResponse





}