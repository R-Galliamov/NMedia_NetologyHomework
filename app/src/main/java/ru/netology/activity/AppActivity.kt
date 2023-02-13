package ru.netology.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.netology.R
import ru.netology.activity.NewPostFragment.Companion.textArg

class AppActivity : AppCompatActivity(R.layout.activity_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //handleIntent(intent)
    }

    //Обрабатывает intent.
    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_SEND -> {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (!text.isNullOrBlank()){
                    intent.removeExtra(Intent.EXTRA_TEXT)
                    sendTextToNewPostFragment(text)
                }
            }
        }
    }

    //Переводит в NewPostFragment. Передает тект.
    private fun sendTextToNewPostFragment(text: String) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_feedFragment_toNewPostFragment,
            Bundle().apply {
                textArg = text
            }
        )
    }
}
