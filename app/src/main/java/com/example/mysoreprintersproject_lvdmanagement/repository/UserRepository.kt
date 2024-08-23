package com.example.mysoreprintersproject_lvdmanagement.repository


import com.example.mysoreprintersproject.repository.BaseRepository
import com.example.mysoreprintersproject_lvdmanagement.network.DataSource
import com.example.mysoreprintersproject_lvdmanagement.repository.UserPreferences
import com.example.mysoreprintersproject_lvdmanagement.responses.CheckInRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.CheckOutRequest

class UserRepository (
    private val api: DataSource,
    private val preferences: UserPreferences

) : BaseRepository() {


    suspend fun saveAuthToken(token : String){
        preferences.saveAuthToken(token)
    }

    suspend fun getAccessToken(): String? {
        return preferences.getAuthToken()
    }
//    suspend fun getProfile(token: String,id:Int) {
//        api.getProfileOfExecutive(token,id)
//    }



    suspend fun checkIn(
        authorization:String,
        checkInRequest: CheckInRequest
    )=safeApiCall {
        api.checkIn(authorization,checkInRequest)
    }


    suspend fun checkOut(
        authorization:String,
        checkOutRequest: CheckOutRequest
    )=safeApiCall {
        api.cheOut(authorization,checkOutRequest)
    }
}