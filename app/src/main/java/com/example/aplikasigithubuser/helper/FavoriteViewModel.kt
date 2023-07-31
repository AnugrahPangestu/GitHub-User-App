package com.example.aplikasigithubuser.helper

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.database.FavoriteUser
import com.example.aplikasigithubuser.repository.FavoriteUserRepository

class FavoriteViewModel (application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()

    //fun insert(favoriteUser: FavoriteUser) {
    //    mFavoriteUserRepository.insert(favoriteUser)
    //}

    //fun delete(favoriteUser: FavoriteUser) {
    //    mFavoriteUserRepository.delete(favoriteUser)
    //}
}