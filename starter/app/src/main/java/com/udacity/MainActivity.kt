package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.databinding.ContentMainBinding


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url =
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/"
    private var repositoryName = ""


    private lateinit var binding: ActivityMainBinding
    private lateinit var contentMain: ContentMainBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        contentMain = binding.contentUi

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            DOWNLOAD_STATUS_CHANNEL_ID,
            getString(R.string.status_notification_channel_name)
        )

        contentMain.downloadButton.setOnClickListener {
            checkSelectedRadioButton()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    repositoryName,
                    getStatus(),
                    context.getText(R.string.notification_description).toString(),
                    context
                )
            }
        }
    }

    private fun download() {
        val fileName = url.substring(url.lastIndexOf("/") + 1)
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    (Environment.DIRECTORY_DOWNLOADS),
                    "/$fileName.zip"
                )
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun checkSelectedRadioButton() {
        when (contentMain.radioGroup.checkedRadioButtonId) {
            R.id.glide_radioButton -> {
                url = "https://github.com/bumptech/glide"
                repositoryName = getString(R.string.radio_button_glide)
            }
            R.id.loadApp_radioButton -> {
                url =
                    "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                repositoryName = getString(R.string.radio_button_loadApp)
            }
            R.id.retrofit_radioButton -> {
                url = "https://github.com/square/retrofit"
                repositoryName = getString(R.string.radio_button_retrofit)
            }
            -1 -> {
                Toast.makeText(
                    this,
                    R.string.toast_message_select_file_to_download,
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }
        contentMain.downloadButton.buttonState = ButtonState.Clicked
        download()
    }

    private fun getStatus(): String {
        var statusMsg = ""
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadID)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val statusValue =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            statusMsg = when (statusValue) {
                DownloadManager.STATUS_SUCCESSFUL -> "Success"
                DownloadManager.STATUS_FAILED -> "Failed"
                else -> "Unknown status"
            }
        }
        return statusMsg
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = getString(R.string.notification_description)
            }
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val TAG = "MainActivity"
    }

}
