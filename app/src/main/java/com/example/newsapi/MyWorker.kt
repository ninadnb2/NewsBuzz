import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.newsapi.MainActivity
import com.example.newsapi.NewsRoomActivity
import com.example.newsapi.R
import com.example.newsapi.SavedActivity
import com.example.newsapi.db.NewsEntity

class MyWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {

            Log.d(
                "success",
                "doWork: Success function called"
            )
            showNotification()
            return Result.success()
    }


    private fun showNotification() {
        val intent = Intent(applicationContext, NewsRoomActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext,0, intent,  PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID )
            .setSmallIcon(R.drawable.newslogo)
            .setContentTitle("News API")
            .setContentText("Saved News")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Read the news"))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }
            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notification.build())
        }
        Log.d("success",
            "show notification is working")
    }

}