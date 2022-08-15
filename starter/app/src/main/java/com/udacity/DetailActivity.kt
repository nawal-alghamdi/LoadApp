package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ContentDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var contentDetail: ContentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        contentDetail = binding.contentDetailUI

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()

        val intentExtra = intent.extras
        if (intentExtra != null) {
            val repoName = intentExtra.getString(REPOSITORY_NAME)
            val status = intentExtra.getString(DOWNLOAD_STATUS)
            contentDetail.fileNameTextView.text = repoName
            contentDetail.statusValueTextView.text = status
        }
    }

}
