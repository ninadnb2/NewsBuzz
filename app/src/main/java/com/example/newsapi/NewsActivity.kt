package com.example.newsapi


import MyWorker
import android.content.Intent
import android.os.Bundle
import android.os.DropBoxManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.bumptech.glide.Glide
import com.example.newsapi.databinding.ActivityMainBinding
import com.example.newsapi.databinding.ActivityNewsBinding
import com.example.newsapi.db.NewsDatabase
import com.example.newsapi.db.NewsEntity
import com.example.newsapi.utils.Constants
import com.example.newsapi.utils.Constants.NEWS_DATABASE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news.view.*
import kotlinx.android.synthetic.main.activity_news_room.*
import kotlinx.android.synthetic.main.activity_save_news.*
import kotlinx.android.synthetic.main.activity_save_news.view.*
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.item_layout.newsTitle
import java.util.concurrent.TimeUnit


class NewsActivity:AppCompatActivity() {


    //  private lateinit var binding: ActivityNewsBinding
    private lateinit var newsEntity: NewsEntity

    /*private val newsDB: NewsDatabase by lazy {
        Room.databaseBuilder(this, NewsDatabase::class.java, NEWS_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)


        val title: TextView = findViewById(R.id.Title)
        val author: TextView = findViewById(R.id.author)
        val image: ImageView = findViewById(R.id.image_news)
        val desc: TextView = findViewById(R.id.Description)
        val cont: TextView = findViewById(R.id.content)
        val share: ImageButton =findViewById(R.id.share)



        Glide.with(this).load(intent.getStringExtra("newsimage")).into(image_news)
        val extras = intent.extras
        if (extras != null) {
            val newstitle = extras.getString("newstitle")
            val newsauthor = extras.getString("newsauthor")
            val newscontent = extras.getString("newscontent")
            val newsimage = extras.getInt("newsImage")
            val urlimage=extras.getString("newsimageurl")
            val newsurl=extras.getString("newsurl")
            val newsdescription = extras.getString("newsdescription")
            //The key argument here must match that used in the other activity


            title.text = newstitle
            author.text = newsauthor
            image.setImageResource(newsimage)
            desc.text = newsdescription
            cont.text = newscontent

            save_btn.setOnClickListener {

                val title = Title.text.toString()
                val imageurl = urlimage.toString()
                val newsurl=newsurl.toString()
                val desc = Description.text.toString()
                val author = author.text.toString()
                val content = content.text.toString()


                if (title.isNotEmpty()) {

                    // newsDB.dao().insertNews(newsEntity)

                    val dao = NewsDatabase.getAppDb(this@NewsActivity)?.dao()
                    val existingNews = dao?.getNewsByTitle(title)

                    if(existingNews == null) {
                        newsEntity = NewsEntity(0, title, desc, author, content, imageurl, newsurl)

                        dao?.insertNews(newsEntity)
                        simpleWork()

                        // startActivity(Intent(this@NewsActivity,NewsRoomActivity::class.java))
                        val intent = Intent(this@NewsActivity, NewsRoomActivity::class.java)
                        /* intent.putExtra("newstitle",newstitle)
                    intent.putExtra("newsauthor",newsauthor)
                    intent.putExtra("newsdescription",newsdescription)
                    intent.putExtra("newscontent",newscontent)*/

                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@NewsActivity, "News already saved", Toast.LENGTH_LONG)
                            .show()
                    }

                } else {
                    Toast.makeText(this@NewsActivity, "No news to save", Toast.LENGTH_LONG)
                        .show()
                }


            }

            share.setOnClickListener{
                val intent=Intent(Intent.ACTION_SEND)
                intent.type="text/plain"
                intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this News On \nNewsBuzz(Made by Ninad Bhase) \n $newsurl")

                val chooser=Intent.createChooser(intent,"Share this news using")
                startActivity(chooser)
            }



        }
    }


    private fun simpleWork() {
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInitialDelay(2,TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance()
            .enqueue(mRequest)

    }
}
/* val title1: TextView = findViewById(R.id.Title)
 val author1: TextView = findViewById(R.id.author)
 //val image: ImageView = findViewById(R.id.image_news)
 val desc1: TextView = findViewById(R.id.Description)
 val cont1: TextView = findViewById(R.id.content)

 //Glide.with(this).load(intent.getStringExtra("newsimage")).into(image_news)


 val extras1 = intent.extras
 if (extras1 != null) {
     val newstitle = extras1.getString("newstitle")
     val newsauthor = extras1.getString("newsauthor")
     val newscontent = extras1.getString("newscontent")
     // val newsimage = extras.getInt("newsImage")
     val newsdescription = extras1.getString("newsdescription")
     //The key argument here must match that used in the other activity
     title1.text = newstitle
     author1.text = newsauthor
     //image.setImageResource(newsimage)
     desc1.text = newsdescription
     cont1.text = newscontent
 }*/

/*
    override fun onItemClicked(item: Article) {
        val intent=Intent(this@NewsActivity,NewsRoomActivity::class.java)
        //intent.putExtra("newsimage",item.urlToImage)
        intent.putExtra("newstitle",item.title)
        intent.putExtra("newsauthor",item.author)
        intent.putExtra("newsdescription",item.description)
        intent.putExtra("newscontent",item.content)
        startActivity(intent)
    }
*/







//







