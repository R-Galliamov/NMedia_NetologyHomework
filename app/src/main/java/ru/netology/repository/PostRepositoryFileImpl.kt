package ru.netology.repository

import android.content.Context
import android.provider.Settings.Global.putLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        nextId = prefs.getLong("nextId", 1)

        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        } else {
            sync()
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun getPostById(id: Long) : Post? {
        return posts.find { it.id == id }
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            // TODO: remove hardcoded author & published
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
        saveNextId()
        sync()
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(likedByMe = !it.likedByMe)
        }
        posts = posts.map {
            if (it.id == id && it.likedByMe) it.copy(likes = it.likes + 1) else
                if (it.id == id && !it.likedByMe) it.copy(likes = it.likes - 1) else it
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun openVideo(post: Post) {}

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    private fun saveNextId(){
       prefs.edit().run{
            putLong("nextId", nextId)
            apply()
        }
    }
}
