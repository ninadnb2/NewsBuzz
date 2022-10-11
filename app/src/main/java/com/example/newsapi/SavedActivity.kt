package com.example.newsapi

import MyWorker
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_room.*
import java.util.concurrent.TimeUnit


class SavedActivity : AppCompatActivity(){
    //lateinit var binding: ActivitySavedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_room)

        // binding=ActivitySavedBinding.inflate(layoutInflater)


        val title: TextView = findViewById(R.id.news_Title)
        val author: TextView = findViewById(R.id.news_author)
        val image: ImageView = findViewById(R.id.news_image)
        val desc: TextView = findViewById(R.id.news_Description)
        val cont: TextView = findViewById(R.id.news_content)
        //val home:ImageButton=findViewById(R.id.homepage)
        //val detail:Button=findViewById(R.id.detail_news)


        Glide.with(this).load(intent.getStringExtra("newsimage")).into(news_image)
        val extras = intent.extras
        if (extras != null) {
            val newstitle = extras.getString("newstitle")
            val newsauthor = extras.getString("newsauthor")
            //val newsurl = extras.getString("newsurl")
            val newscontent = extras.getString("newscontent")
            val newsimage = extras.getInt("newsImage")
            val newsdescription = extras.getString("newsdescription")
            //The key argument here must match that used in the other activity
            title.text = newstitle
            author.text = newsauthor
            image.setImageResource(newsimage)
            desc.text = newsdescription
            cont.text = newscontent

        }








    }

    private fun myWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        val myRequest = PeriodicWorkRequest.Builder(MyWorker::class.java,
            15,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        //minimum interval is 15min, just wait 15 min,
        // I will cut this.. to show you
        //quickly

        //now is 0:15 let's wait until 0:30min

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "my_id",
                ExistingPeriodicWorkPolicy.KEEP,
                myRequest
            )
    }


    private fun simpleWork() {

        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .build()

        WorkManager.getInstance(this)
            .enqueue(mRequest)

    }



}