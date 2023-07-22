package com.example.newsapi

import MyWorker
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.newsapi.databinding.ActivityNewsBinding
import com.example.newsapi.databinding.ActivityNewsRoomBinding
import com.example.newsapi.db.NewsDao
import com.example.newsapi.db.NewsDatabase
import com.example.newsapi.db.NewsEntity
import com.example.newsapi.utils.Constants
import com.example.newsapi.utils.Constants.BUNDLE_NEWS_ID
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news_room.*
import kotlinx.android.synthetic.main.activity_save_news.*
import kotlinx.android.synthetic.main.activity_save_news.view.*
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.save_news.*
import java.util.concurrent.TimeUnit

class NewsRoomActivity : AppCompatActivity() ,SavedReAdapter.NewsItemClicked {

    private lateinit var binding: ActivityNewsRoomBinding
    private lateinit var sAdapter: SavedReAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_news)
        fetchnews()

    }

    fun fetchnews() {
        binding = ActivityNewsRoomBinding.inflate(layoutInflater)
        lateinit var newsentity: List<NewsEntity>

        //read data
        val dao = NewsDatabase.getAppDb(this)?.dao()
        newsentity = dao?.getNews()!!
        //getSavedNews(newsentity)
        saveNewsList.apply {
            layoutManager = LinearLayoutManager(this@NewsRoomActivity)
            adapter = SavedReAdapter(this@NewsRoomActivity, newsentity as ArrayList<NewsEntity>)

        }


    }









    override fun onItemClicked(item: List<NewsEntity>, position: Int) {
        val item=item[position]
        val intent = Intent(this@NewsRoomActivity, SavedActivity::class.java)
        intent.putExtra("newsimage",item.urlToImage)
        intent.putExtra("newsurl",item.url)
        intent.putExtra("newstitle", item.newsTitle)
        intent.putExtra("newsauthor", item.newsAuthor)
        intent.putExtra("newsdescription", item.newsDesc)
        intent.putExtra("newscontent", item.newsContent)
        startActivity(intent)
    }


    override fun deleteclick(itemList: MutableList<NewsEntity>, position: Int) {
        val itemToDelete = itemList[position]
        val dao = NewsDatabase.getAppDb(this@NewsRoomActivity)?.dao()
        dao?.deleteNews(itemToDelete)
    }

    override fun detailclick(item: NewsEntity, position: Int) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }


/*
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

    }*/

}

/* setContentView(R.layout.activity_save_news)
 val savelayout: Button = findViewById(R.id.delete_button)
 savelayout.setOnClickListener {
     val title = savenewsTitle.text.toString()
     val desc = savenewsDescription.text.toString()
     val author = savenewsAuthor.text.toString()
     val content = savenewsContent.text.toString()


     if (title.isNotEmpty()) {//
         //newsentity = NewsEntity(0, title, desc, author, content)
         // newsentity = NewsEntity(0, title, desc, author, content)
         val dao = NewsDatabase.getAppDb(this@NewsRoomActivity)?.dao()
         dao?.deleteNews(NewsEntity(0, title, desc, author, content))

         //finish()
     }
 }*/







