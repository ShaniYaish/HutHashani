package com.idz.huthashani.dao


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey
    var id: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "postPicture")
    var picture: String = "",

    @ColumnInfo(name = "userEmail")
    var userEmail: String = "",



    ) {
    @Ignore
    constructor() : this("", "", "", "", "")
}

