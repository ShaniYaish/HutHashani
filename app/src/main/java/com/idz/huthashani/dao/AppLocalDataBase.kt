package com.idz.huthashani.dao


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class AppLocalDataBase : RoomDatabase() {
    abstract fun postDao(): PostDao
}

