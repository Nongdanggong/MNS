package com.example.kakaomaptest_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kakaomaptest_1.data.MNSDatabase
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.repository.MNSRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.*

class MNSViewModel(application: Application): AndroidViewModel(application) {

    val readAllUserData: LiveData<List<User>>
    val readAllPostData: LiveData<List<Post>>
    val readAllChatData: LiveData<List<Chat>>

    val repository: MNSRepository


//     Fragment에서 이 viewmodel 호출 할 때 마다 user정보, post정보, 댓글 정보를 데이터 베이스에서 불러온다.
    init {
        val userDao = MNSDatabase.getDatabase(application).userDao()
        repository = MNSRepository(userDao)
        readAllUserData = repository.readAllUserData
        readAllPostData = repository.readAllPostData
        readAllChatData = repository.readAllChatData

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

    fun deleteSingleChat(postid: Int, date: Date, userid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSingleChat(postid, date, userid)
        }
    }

    fun isThisIdExists(id: String) : LiveData<Boolean> {
        val bool = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            bool.postValue(repository.isThisIdExists(id))
        }
        return bool
    }

    fun getUser(id: String) : User {
        return repository.getUser(id)
    }

    fun getPost(key: Int) : Post {
        return repository.getPost(key)
    }

    fun getUserPosts(id: String) : LiveData<List<Post>> {
        return repository.getUserPosts(id)
    }

    fun editUser(id: String, nickname: String, photoUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editUser(id, nickname, photoUri)
        }
    }

    fun isThisNickExists(nickname: String): Boolean {
        return repository.isThisNickExists(nickname)
    }
}