package ru.netology.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.util.AndroidUtils
import ru.netology.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: PostViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val interactionListener by lazy {
        object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun shareById(post: Post) {
                viewModel.shareById(post.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val adapter = PostsAdapter(interactionListener)

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.list = posts
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                with(binding.editTextContent) {
                    requestFocus()
                    setText(post.content)
                    binding.cancelButton.visibility = View.VISIBLE
                }
            }
        }

        binding.saveButton.setOnClickListener {
            with(binding.editTextContent) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.changeContent(text.toString())
                    viewModel.save()
                    clearEditTextContent()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            clearEditTextContent()
        }
    }

    private fun clearEditTextContent() {
        with(binding.editTextContent) {
            setText("")
            clearFocus()
            AndroidUtils.hideKeyboard(this)
            binding.cancelButton.visibility = View.GONE
        }
    }
}
