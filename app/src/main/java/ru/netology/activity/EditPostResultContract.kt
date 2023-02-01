package ru.netology.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
var POST_TEXT: String = "post text input"
class EditPostResultContract : ActivityResultContract<String, String?>() {

    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, EditPostActivity::class.java).run{putExtra(POST_TEXT, input)}

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(POST_TEXT)
        } else {
            null
        }
}
