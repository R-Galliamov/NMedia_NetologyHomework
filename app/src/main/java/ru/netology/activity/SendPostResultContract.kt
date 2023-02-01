package ru.netology.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.dto.Post

class SendPostResultContract : ActivityResultContract<Post, Long?>() {
    val POST_ID = "postId"
    override fun createIntent(context: Context, input: Post): Intent =
        Intent(context, SendPostActivity::class.java).apply {
            putExtra(POST_ID, input.id)
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, input.content)
            type = "text/plain"
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Long? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(POST_ID)?.toLong()
        } else {
            null
        }
}
