package com.example.newsapi

import MyWorker
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.newsapi.db.NewsDatabase
import com.example.newsapi.db.NewsEntity
import kotlinx.android.synthetic.main.activity_news_room.*
import kotlinx.android.synthetic.main.item_layout.*
import java.security.AccessController.getContext
import java.util.concurrent.TimeUnit

class SavedReAdapter(val newsItemClicked: NewsItemClicked, val savednews: List<NewsEntity>): RecyclerView.Adapter<SavedReAdapter.SavedNewsViewHolder> () {
    private var list = emptyList<News>()
    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_save_news, parent, false)
        return SavedNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedNewsViewHolder, position: Int) {


        val currentItem: NewsEntity = savednews[position]
        holder.newsTitle.text = currentItem.newsTitle
        holder.newsDescription.text = currentItem.newsDesc
        holder.newsAuthor.text = currentItem.newsAuthor
        Glide.with(holder.itemView.context).load(currentItem.urlToImage).into(holder.newsImage)
        /* holder.delete.setOnClickListener{
             val dao = NewsDatabase.getAppDb(currentItem)?.dao()
             dao?.deleteNews(NewsEntity(0, "title", "desc", "author", "content"))
         }*/
        holder.layout.setOnClickListener{
            newsItemClicked.onItemClicked(savednews, position)
        }
        holder.delete.setOnClickListener {
            newsItemClicked.deleteclick(savednews,position)
            Toast.makeText(holder.delete.context, "News Deleted", Toast.LENGTH_SHORT).show()
        }

        holder.detail.setOnClickListener {
            newsItemClicked.detailclick(currentItem,position)
        }

        holder.notify.setOnClickListener {

            if (savednews.isNotEmpty()){
                myWorkManager()
                Toast.makeText(holder.notify.context, "Notification in 1 hour", Toast.LENGTH_LONG).show()

            }


        }

    }

    override fun getItemCount(): Int {
        return savednews.size
    }




    class SavedNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsImage = itemView.findViewById<ImageView>(R.id.savenewsImage)
        var newsTitle = itemView.findViewById<TextView>(R.id.savenewsTitle)
        var newsDescription = itemView.findViewById<TextView>(R.id.savenewsDescription)
        var newsAuthor = itemView.findViewById<TextView>(R.id.savenewsAuthor)
        var delete = itemView.findViewById<Button>(R.id.delete_news)
        //var layout = itemView.findViewById<LinearLayout>(R.id.layout)
        var layout = itemView.findViewById<LinearLayout>(R.id.savelayout)
        var detail=itemView.findViewById<ImageButton>(R.id.detail_news)
        val notify=itemView.findViewById<ImageButton>(R.id.notification)


    }


        private fun myWorkManager() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(true)
                .build()
            val myRequest = PeriodicWorkRequest.Builder(MyWorker::class.java,
                60,
                TimeUnit.MINUTES
            ).setConstraints(constraints)
                .build()
            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                    "my_id",
                    ExistingPeriodicWorkPolicy.KEEP,
                    myRequest
                )

        }


        private fun simpleWork() {

            val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                //.setInitialDelay(2,TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance()
                .enqueue(mRequest)

        }




    interface NewsItemClicked {
        fun onItemClicked(item: List<NewsEntity>, position: Int)
        fun deleteclick(item: List<NewsEntity>, position: Int)
        fun detailclick(item:NewsEntity,position: Int)
    }
}