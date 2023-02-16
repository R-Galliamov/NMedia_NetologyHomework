package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var shares: Int = 0,
    var views: Int = 0,
    val likedByMe: Boolean = false,
    val videoUrl: String? = null
) {
    fun toDto() = Post(id, author, published, content, likes, shares, views, likedByMe, videoUrl)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.published,
                dto.content,
                dto.likes,
                dto.shares,
                dto.views,
                dto.likedByMe,
                dto.videoUrl
            )
    }
}
