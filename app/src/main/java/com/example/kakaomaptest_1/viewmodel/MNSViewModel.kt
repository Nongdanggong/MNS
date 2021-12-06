package com.example.kakaomaptest_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kakaomaptest_1.data.MNSDatabase
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.repository.MNSRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MNSViewModel(application: Application): AndroidViewModel(application) {

    val readAllUserData: LiveData<List<User>>
    val readAllPostData: LiveData<List<Post>>
    val readAllChatData: LiveData<List<Chat>>
//    val readAllPostDataDead: List<Post>

    private val repository: MNSRepository


//     Fragment에서 이 viewmodel 호출 할 때 마다 user정보, post정보, 댓글 정보를 데이터 베이스에서 불러온다.
    init {
        val userDao = MNSDatabase.getDatabase(application).userDao()
        repository = MNSRepository(userDao)
        readAllUserData = repository.readAllUserData
        readAllPostData = repository.readAllPostData
        readAllChatData = repository.readAllChatData
//        readAllPostDataDead = repository.readAllPostDataDead
    }


    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPost(post)
        }
    }

    fun addChat(chat: Chat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChat(chat)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePost(post)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteSinglePost(creatorid: String, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSinglePost(creatorid, title)
        }
    }

    fun deleteSingleChat(postid: Int, date: Date, userid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSingleChat(postid, date, userid)
        }
    }
}