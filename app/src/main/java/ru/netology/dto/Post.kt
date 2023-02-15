package ru.netology.dto

data class Post(
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
}
