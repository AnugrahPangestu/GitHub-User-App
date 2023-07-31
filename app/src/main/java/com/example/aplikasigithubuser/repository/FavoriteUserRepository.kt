package com.example.aplikasigithubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.aplikasigithubuser.database.FavoriteUser
import com.example.aplikasigithubuser.database.FavoriteUserDao
import com.example.aplikasigithubuser.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDataBase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun getFavoriteUserByUsername(username: String) : LiveData<FavoriteUser> =  mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser){
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
}