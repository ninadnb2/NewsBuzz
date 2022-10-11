package com.example.newsapi

import android.R.attr.data
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapi.databinding.ActivityMainBinding
import com.example.newsapi.databinding.ActivityNewsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news.view.*
import kotlinx.android.synthetic.main.activity_news_room.*
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.item_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() ,NewsAdapter.NewsItemClicked{
    lateinit var binding:ActivityMainBinding
    private lateinit var mAdapter: NewsAdapter
    //private lateinit var swipeRefreshLayout: SwipeRefreshLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  val swipe_refresh: SwipeRefreshLayout = findViewById(R.id.swipe_refresh)

        getNews()
      //  onRefresh()

       /* swipe_refresh.setOnRefreshListener {
            NewService.newsInstance.getHeadLines("in",1,"")
        }*/

        home_save_news.setOnClickListener {
            startActivity(Intent(this@MainActivity,NewsRoomActivity::class.java))
        }
    }


    private fun getNews() {

        val news:Call<News> = NewService.newsInstance.getHeadLines("in",1 ,"")
        news.enqueue(object: Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val newsArray= ArrayList<News>()
                val news:News?= response.body()


                Log.d("news","in response block")
                if(news!=null){

                    Log.d("ninad",news.toString())
                    mAdapter= NewsAdapter(this@MainActivity,this@MainActivity,news.articles)
                    newsList.adapter=mAdapter
                    newsList.layoutManager = LinearLayoutManager(this@MainActivity)
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)

            }



            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Error","Error")
            }
        })
    }


    override fun onItemClicked(item: Article) {
        val intent=Intent(this@MainActivity,NewsActivity::class.java)
        intent.putExtra("newsimage",item.urlToImage)
        intent.putExtra("newsurl",item.url)
        intent.putExtra("newsimageurl",item.urlToImage)
        intent.putExtra("newstitle",item.title)
        intent.putExtra("newsauthor",item.author)
        intent.putExtra("newsdescription",item.description)
        intent.putExtra("newscontent",item.content)
        startActivity(intent)

    }

   /* override fun onRefresh() {
        NewService.newsInstance.getHeadLines("",1,"")

        fun onLoadSwipeRefresh() {
            swipe_refresh.post(
                Runnable() {

                    NewService.newsInstance.getHeadLines("", 1, "")

                }
            )
        }


    }*/


}
