package com.example.kakaomaptest_1.repository

import androidx.lifecycle.LiveData
import com.example.kakaomaptest_1.data.MNSDao
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*

class MNSRepository(private val MNSDao: MNSDao) {

    val readAllUserData: LiveData<List<User>> = MNSDao.readAllUserData()
    val readAllPostData: LiveData<List<Post>> = MNSDao.readAllPostData()
    val readAllChatData: LiveData<List<Chat>> = MNSDao.readAllChatData()
//    val readAllPostDataDead: List<Post> = MNSDao.readAllPostDataDead()

    fun addUser(user: User) {
        MNSDao.addUser(user)
    }

    fun addPost(post: Post) {
        MNSDao.addPost(post)
    }

    fun addChat(chat: Chat) {
        MNSDao.addChat(chat)
    }

    fun updateUser(user: User) {
        MNSDao.updateUser(user)
    }

    fun updatePost(post: Post) {
        MNSDao.updatePost(post)
    }

    fun updateChat(chat: Chat) {
        MNSDao.updateChat(chat)
    }

    fun deleteSingleChat(postid: Int, date: Date, userid: String) {
        MNSDao.deleteSingleChat(postid, date, userid)
    }

    fun isThisIdExists(id: String) : Boolean {
        return MNSDao.isThisIdExists(id)
    }

    fun getUser(id: String) : User {
        return MNSDao.getUser(id)
    }

    fun getPost(key: Int) : Post {
        return MNSDao.getPost(key)
    }

    fun getUserPosts(id: String) : LiveData<List<Post>> {
        return MNSDao.getUserPosts(id)
    }

    fun editUser(id: String, nickname: String, photoUri: String) {
        return MNSDao.editUser(id, nickname, photoUri)
    }

    fun isThisNickExists(nickname: String): Boolean {
        return MNSDao.isThisNickExists(nickname)
    }
}