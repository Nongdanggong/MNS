package com.example.kakaomaptest_1.repository

import androidx.lifecycle.LiveData
import com.example.kakaomaptest_1.data.MNSDao
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User

class MNSRepository(private val MNSDao: MNSDao) {

    val readAllUserData: LiveData<List<User>> = MNSDao.readAllUserData()
    val readAllPostData: LiveData<List<Post>> = MNSDao.readAllPostData()
    val readAllChatData: LiveData<List<Chat>> = MNSDao.readAllChatData()
//    val readAllPostDataDead: List<Post> = MNSDao.readAllPostDataDead()

    suspend fun addUser(user: User) {
        MNSDao.addUser(user)
    }

    suspend fun addPost(post: Post) {
        MNSDao.addPost(post)
    }

    suspend fun addChat(chat: Chat) {
        MNSDao.addChat(chat)
    }

    suspend fun updateUser(user: User) {
        MNSDao.updateUser(user)
    }

    suspend fun updatePost(post: Post) {
        MNSDao.updatePost(post)
    }

    suspend fun updateChat(chat: Chat) {
        MNSDao.updateChat(chat)
    }

}