package com.example.aplikasigithubuser.service

import com.example.aplikasigithubuser.service.response.ItemsItem
import com.example.aplikasigithubuser.service.response.UserDetailResponse
import com.example.aplikasigithubuser.service.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchUser(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users")
    fun getUser(): Call<List<ItemsItem>>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<List<ItemsItem>>

}

