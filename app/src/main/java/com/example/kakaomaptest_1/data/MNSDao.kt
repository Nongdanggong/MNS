package com.example.kakaomaptest_1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kakaomaptest_1.model.*
import java.util.*

@Dao
interface MNSDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addScrap(scrap: Scrap)

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

    @Query("SELECT * FROM scrap_table")
    fun readAllScrapData(): LiveData<List<Scrap>>

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE id = :id)")
    fun isThisIdExists(id: String): Boolean

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUser(id: String): User

    @Query("SELECT * FROM post_table WHERE userCreatorId = :id")
    fun getUserPosts(id: String): LiveData<List<Post>>

    @Query("SELECT * FROM scrap_table WHERE scraper = :id ORDER BY `key` ASC")
    fun getScraps(id: String): LiveData<List<Scrap>>

    @Transaction
    @Query("SELECT * FROM post_table where `key` = :key")
    fun getPostWithUser(key: Int): PostWithUser

    @Query("DELETE FROM post_table WHERE `key` = :postid")
    fun deleteSinglePost(postid: Int)

    @Query("DELETE FROM chat_table WHERE postId = :postid AND date = :date AND userId = :userid")
    fun deleteSingleChat(postid: Int, date: Date, userid: String)

    @Query("DELETE FROM chat_table WHERE postId = :postid")
    fun deletePostChatLog(postid: Int)

    @Query("DELETE FROM scrap_table WHERE scraper = :id AND `key` = :postId")
    fun deleteSingleScrap(id: String, postId: Int)

    @Query("DELETE FROM scrap_table WHERE `key` = :postId")
    fun deleteConnectedScrap(postId: Int)

    @Query("UPDATE user_table SET nickname = :nickname, photoUri = :photoUri WHERE id = :id")
    fun editUser(id: String, nickname: String, photoUri: String)

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE nickname = :nickname)")
    fun isThisNickExists(nickname: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM scrap_table WHERE scraper = :id AND `key` = :postId)")
    fun isPostScrapped(id: String, postId: Int): Boolean

    @Query("SELECT * FROM post_table WHERE `key` = :key")
    fun getPost(key: Int): Post

//    @Query("SELECT * FROM post_table")
//    fun readAllPostDataDead(): List<Post>
}
