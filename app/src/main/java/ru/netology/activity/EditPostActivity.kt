package ru.netology.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.R
import ru.netology.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.edit) {
            requestFocus()
            val text = intent.getStringExtra(POST_TEXT)
            setText(text)
        }

        binding.cancelButton.setOnClickListener{
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    this.getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT)
                    .show()
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(POST_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}
