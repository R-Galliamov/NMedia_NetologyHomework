package ru.netology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post
import java.math.BigDecimal
import java.math.RoundingMode

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener

) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            likeButton.isChecked = post.likedByMe
            likeButton.text = showCount(post.likes)

            likeButton.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shareButton.text = showCount(post.shares)
            shareButton.setOnClickListener {
                onInteractionListener.shareById(post)
            }
        }
    }

    private fun showCount(count: Int): String {
        return if (count < 1_000) {
            count.toString()
        } else if (count in 1_000..9_999) {
            if ((count / 100) % 10 == 0) {
                "${count / 1_000}K"
            } else {
                "${BigDecimal(count / 1000.0).setScale(1, RoundingMode.FLOOR)}K"
            }
        } else if (count in 10_000..999_999) {
            "${count / 10_00}K"
        } else if ((count % 1_000_000) < 100_000) {
            "${count / 1_000_000}M"
        } else {
            return "${BigDecimal(count / 1000000.0).setScale(1, RoundingMode.FLOOR)}M"
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
