package com.example.mysoreprintersproject_lvdmanagement.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysoreprintersproject.network.Resource
import com.example.mysoreprintersproject_lvdmanagement.repository.UserRepository
import com.example.mysoreprintersproject_lvdmanagement.responses.CheckInRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.CheckOutRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.ChecksResponses

import kotlinx.coroutines.launch

class CheckInViewModel(
    private val repository: UserRepository
) : ViewModel(){
    private val _checkInResponse : MutableLiveData<Resource<ChecksResponses>> = MutableLiveData()
    val checkInResponse: LiveData<Resource<ChecksResponses>>
        get() = _checkInResponse



    private val _checkOutResponse: MutableLiveData<Resource<ChecksResponses>> = MutableLiveData()
    val checkOutResponse: LiveData<Resource<ChecksResponses>>
        get() = _checkOutResponse


    fun checkIn(
        autorization:String,
        checkInRequest: CheckInRequest
    )=viewModelScope.launch {
        _checkInResponse.value=Resource.Loading
        _checkInResponse.value=repository.checkIn(autorization,checkInRequest)
    }


    fun checkOut(
        authorization: String,
        checkOutRequest: CheckOutRequest
    ) = viewModelScope.launch {
        _checkOutResponse.value = Resource.Loading
        _checkOutResponse.value = repository.checkOut(authorization, checkOutRequest)
    }
}