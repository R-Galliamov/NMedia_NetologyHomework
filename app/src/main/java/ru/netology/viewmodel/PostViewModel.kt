package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryFileImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = ""
)

open class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun getPostById(id: Long) = repository.getPostById(id)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun openVideo(post: Post) = repository.openVideo(post)
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        clearEdited()
    }

    fun clearEdited(){
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}
