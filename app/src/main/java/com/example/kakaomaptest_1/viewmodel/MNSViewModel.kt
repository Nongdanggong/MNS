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
}