package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

    override fun getPostById(id: Long): Post {
        return dao.getPostEntityById(id).toDto()
        }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun openVideo(post: Post) { }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}
