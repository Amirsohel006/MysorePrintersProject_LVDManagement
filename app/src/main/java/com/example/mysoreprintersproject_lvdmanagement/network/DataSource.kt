package com.example.mysoreprintersproject_lvdmanagement.network

import com.example.mysoreprintersproject_lvdmanagement.responses.CheckInRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.CheckOutRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.ChecksResponses
import com.example.mysoreprintersproject_lvdmanagement.responses.LVDDashboard
import com.example.mysoreprintersproject_lvdmanagement.responses.LVDRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.ProfileResponses
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DataSource {


    @GET("/lvd/dashboard/")
    fun getLvdDashboard(
        @Header("Authorization") token: String,
        @Query("id")id:String,
        @Query("period")period:String
    ):Call<LVDDashboard>
//
    @GET("/lvd/ProfileSettingView/")
     fun getProfileOfLVD(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ):Call<ProfileResponses>


    @POST("/lvd/check_in/")
    suspend fun checkIn(
        @Header("Authorization") token: String,
        @Body checkInRequest: CheckInRequest
    ) : ChecksResponses



    @POST("/lvd/check_out/")
    suspend fun cheOut(
        @Header("Authorization") token: String,
        @Body checkOutRequest: CheckOutRequest
    ) : ChecksResponses


    @POST("/lvd/plant_edition/")
    fun postLVD(
        @Header("Authorization") token: String,
        @Body lvdRequest: LVDRequest
    ):Call<Void>
//
//    @POST("/app-executive/check_in/")
//    suspend fun checkIn(
//        @Header("Authorization") token: String,
//        @Body checkInRequest: CheckInRequest
//    ) : ChecksResponses
//
//
//
//    @POST("/app-executive/check_out/")
//    suspend fun cheOut(
//        @Header("Authorization") token: String,
//        @Body checkOutRequest:CheckOutRequest
//    ) : ChecksResponses
//
//
//    @GET("/app-executive/working_summary/")
//    fun getExecutiveDashboard(
//        @Header("Authorization") token: String,
//        @Query("id")id:String,
//        @Query("period")period:String
//    ):Call<ExecutiveDashboard>





}