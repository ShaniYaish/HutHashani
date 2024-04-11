package com.idz.huthashani.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun getAll(): List<Post>

    @Query("SELECT * FROM post WHERE userEmail = :email")
    fun getAllPostByUserEmail(email: String): List<Post>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg post: Post)

    @Query("DELETE FROM POST WHERE id = :id")
    fun delete(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)
}