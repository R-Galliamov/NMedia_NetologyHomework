package ru.netology.adapter

import android.os.IBinder
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.FragmentCardPostBinding
import ru.netology.dto.Post
import java.math.BigDecimal
import java.math.RoundingMode

class PostViewHolder(
    private val binding: FragmentCardPostBinding,
    private val onInteractionListener: OnInteractionListener

) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            itemView.setOnClickListener{
                onInteractionListener.onItem(post)
            }

            if (post.video_url != null) {
                videoContainer.visibility = View.VISIBLE
            } else {
                videoContainer.visibility = View.GONE
            }

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
                with(onInteractionListener) {
                    onShare(post)
                }
            }
            playButton.setOnClickListener{
                onInteractionListener.onOpenVideo(post)
            }
            videoContainer.setOnClickListener {
                onInteractionListener.onOpenVideo(post)
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
