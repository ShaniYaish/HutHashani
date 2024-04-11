package com.idz.huthashani.dao

import android.content.Context
import androidx.room.Room

fun getLocalDatabase(context: Context): AppLocalDataBase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppLocalDataBase::class.java,
        "huthashaniDatabase"
    ).allowMainThreadQueries().build()
}