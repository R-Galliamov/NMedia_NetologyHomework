package ru.netology.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: PostViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    private val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    private val sendPostLauncher =
        registerForActivityResult(SendPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            result.getStringExtra("post_id")?.let { viewModel.shareById(it.toLong()) }
        }

    private val interactionListener by lazy {
        object : OnInteractionListener {
            override fun onOpenVideo(post: Post) {
                viewModel.openVideo(post)
                val openVideoIntent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(post.video_url)
                }
                startActivity(openVideoIntent)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra("post_id", post.id)
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                sendPostLauncher.launch(shareIntent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = PostsAdapter(interactionListener)

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
        }
    }


}
