package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.adapter.PostsAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter({ viewModel.likeById(it.id) }, { viewModel.shareById(it.id) })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.list = posts
        }
    }
}
