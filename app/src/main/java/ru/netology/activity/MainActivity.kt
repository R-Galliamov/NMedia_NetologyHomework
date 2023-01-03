package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel
import java.math.BigDecimal
import java.math.RoundingMode
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

fun showCount(count: Int): String {
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
