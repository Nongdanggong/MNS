package com.example.kakaomaptest_1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kakaomaptest_1.data.MNSDao
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun deleteSinglePost(creatorid: String, title: String) {
        MNSDao.deleteSinglePost(creatorid, title)
    }

    fun deleteSingleChat(postid: Int, date: Date, userid: String) {
        MNSDao.deleteSingleChat(postid, date, userid)
    }

}