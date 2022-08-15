package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
const val DOWNLOAD_STATUS_CHANNEL_ID = "status_channel"
const val REPOSITORY_NAME = "RepositoryName"
const val DOWNLOAD_STATUS = "DownloadStatus"

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(
    repositoryName: String, downloadStatus: String,
    messageBody: String, applicationContext: Context
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(REPOSITORY_NAME, repositoryName)
    contentIntent.putExtra(DOWNLOAD_STATUS, downloadStatus)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val statusIntent = Intent(applicationContext, DetailActivity::class.java)
    statusIntent.putExtra("RepositoryName", repositoryName)
    statusIntent.putExtra("DownloadStatus", downloadStatus)
    val statusPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        statusIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        DOWNLOAD_STATUS_CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.action_check_the_status),
            statusPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
