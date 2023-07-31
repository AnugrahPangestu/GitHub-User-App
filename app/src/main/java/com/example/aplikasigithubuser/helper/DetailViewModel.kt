package com.example.aplikasigithubuser.helper

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.database.FavoriteUser
import com.example.aplikasigithubuser.repository.FavoriteUserRepository
import com.example.aplikasigithubuser.service.ApiConfig
import com.example.aplikasigithubuser.service.response.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel(){

    private val _user = MutableLiveData<UserDetailResponse?>()
    val user: LiveData<UserDetailResponse?> = _user
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mFavoriteUserRepository.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}