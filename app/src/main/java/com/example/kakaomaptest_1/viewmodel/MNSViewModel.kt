package com.example.kakaomaptest_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kakaomaptest_1.data.MNSDatabase
import com.example.kakaomaptest_1.model.*
import com.example.kakaomaptest_1.repository.MNSRepository
import kotlinx.coroutines.*
import java.util.*

class MNSViewModel(application: Application): AndroidViewModel(application) {

    val readAllUserData: LiveData<List<User>>
    val readAllPostData: LiveData<List<Post>>
    val readAllChatData: LiveData<List<Chat>>
    val readAllScrapData: LiveData<List<Scrap>>

    val repository: MNSRepository


//     Fragment에서 이 viewmodel 호출 할 때 마다 user정보, post정보, 댓글 정보를 데이터 베이스에서 불러온다.
    init {
        val userDao = MNSDatabase.getDatabase(application).userDao()
        repository = MNSRepository(userDao)
        readAllUserData = repository.readAllUserData
        readAllPostData = repository.readAllPostData
        readAllChatData = repository.readAllChatData
        readAllScrapData = repository.readAllScrapData

    }


    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun addScrap(scrap: Scrap) {
        viewModelScope.launch(Dispatchers.IO){
            repository.addScrap(scrap)
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

    fun deleteSinglePost(postid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSinglePost(postid)
        }
    }

    fun deleteSingleScrap(userId: String, postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSingleScrap(userId, postId)
        }
    }

    fun deletePostChatLog(postid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePostChatLog(postid)
        }
    }

    fun deleteConnectedScrap(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteConnectedScrap(postId)
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

    fun getScraps(id: String) : LiveData<List<Scrap>> {
        return repository.getScraps(id)
    }

    fun getPostWithUser(key: Int) : PostWithUser {
        return repository.getPostWithUser(key)
    }

    fun editUser(id: String, nickname: String, photoUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editUser(id, nickname, photoUri)
        }
    }

    fun isThisNickExists(nickname: String): Boolean {
        return repository.isThisNickExists(nickname)
    }

    fun isPostScrapped(id: String, postId: Int): Boolean {
        return repository.isPostScrapped(id, postId)
    }
}