package com.example.kakaomaptest_1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User

@Dao
interface MNSDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChat(chat: Chat)

    @Update
    fun updateUser(user: User)

    @Update
    fun updatePost(post: Post)

    @Update
    fun updateChat(chat: Chat)

    @Query("SELECT * FROM chat_table")
    fun readAllChatData(): LiveData<List<Chat>>

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllUserData(): LiveData<List<User>>

    @Query("SELECT * FROM post_table")
    fun readAllPostData(): LiveData<List<Post>>

//    @Query("SELECT * FROM post_table")
//    fun readAllPostDataDead(): List<Post>
}