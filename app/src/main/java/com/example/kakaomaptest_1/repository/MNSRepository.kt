package com.example.kakaomaptest_1.repository

import androidx.lifecycle.LiveData
import com.example.kakaomaptest_1.data.MNSDao
import com.example.kakaomaptest_1.model.*
import java.util.*

class MNSRepository(private val MNSDao: MNSDao) {

    val readAllUserData: LiveData<List<User>> = MNSDao.readAllUserData()
    val readAllPostData: LiveData<List<Post>> = MNSDao.readAllPostData()
    val readAllChatData: LiveData<List<Chat>> = MNSDao.readAllChatData()
    val readAllScrapData: LiveData<List<Scrap>> = MNSDao.readAllScrapData()
//    val readAllPostDataDead: List<Post> = MNSDao.readAllPostDataDead()

    fun addUser(user: User) {
        MNSDao.addUser(user)
    }

    fun addScrap(scrap: Scrap){
        MNSDao.addScrap(scrap)
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

    fun deleteSinglePost(postId: Int) {
        MNSDao.deleteSinglePost(postId)
    }

    fun deleteSingleChat(postId: Int, date: Date, userid: String) {
        MNSDao.deleteSingleChat(postId, date, userid)
    }

    fun deleteSingleScrap(userId: String, postId: Int) {
        MNSDao.deleteSingleScrap(userId, postId)
    }

    fun deletePostChatLog(postId: Int) {
        MNSDao.deletePostChatLog(postId)
    }

    fun deleteConnectedScrap(postId: Int) {
        MNSDao.deleteConnectedScrap(postId)
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

    fun getScraps(id: String) : LiveData<List<Scrap>> {
        return MNSDao.getScraps(id)
    }

    fun getUserPosts(id: String) : LiveData<List<Post>> {
        return MNSDao.getUserPosts(id)
    }

    fun getPostWithUser(key: Int) : PostWithUser {
        return MNSDao.getPostWithUser(key)
    }

    fun editUser(id: String, nickname: String, photoUri: String) {
        return MNSDao.editUser(id, nickname, photoUri)
    }

    fun isThisNickExists(nickname: String): Boolean {
        return MNSDao.isThisNickExists(nickname)
    }

    fun isPostScrapped(id: String, postId: Int): Boolean {
        return MNSDao.isPostScrapped(id, postId)
    }
}