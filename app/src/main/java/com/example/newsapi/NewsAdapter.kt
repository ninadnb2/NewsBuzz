package com.example.newsapi

import MyWorker
import android.content.Context
import android.content.Intent
import android.telephony.SubscriptionManager.from
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_layout.*
import java.util.Date.from

class NewsAdapter(val newsItemClicked:NewsItemClicked,val context: Context, val articles:List<Article>): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> (){

private val items:ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)

        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article:Article=articles[position]
        holder.newsTitle.text=article.title
        holder.newsDescription.text=article.description
        holder.newsAuthor.text=article.author
        Glide.with(holder.itemView.context).load(article.urlToImage).into(holder.newsImage)
        holder.layout.setOnClickListener{
            newsItemClicked.onItemClicked(article)
        }
       holder.share.setOnClickListener {
           val intent=Intent(Intent.ACTION_SEND)
           intent.type="text/plain"
           intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this News On \nNewsBuzz(Made by Ninad Bhase) \n${article.url}")

           val chooser=Intent.createChooser(intent,"Share this news using")
           context.startActivity(chooser)
           }



    }

    override fun getItemCount(): Int {
        return  articles.size
    }

    fun updateNews(updatedNews: ArrayList<News>){
        //items.clear()
        items.addAll(updatedNews)
       notifyDataSetChanged()
    }

    /*fun sharenews(){

        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey,Checkout this News $articles")
        val chooser=Intent.createChooser(intent,"Share this news using")
        context.startActivity(chooser)
    }*/



    class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var newsImage=itemView.findViewById<ImageView>(R.id.newsImage)
        var newsTitle=itemView.findViewById<TextView>(R.id.newsTitle)
        var newsDescription=itemView.findViewById<TextView>(R.id.newsDescription)
        var newsAuthor=itemView.findViewById<TextView>(R.id.newsAuthor)
        var layout=itemView.findViewById<LinearLayout>(R.id.layout)
        var share=itemView.findViewById<Button>(R.id.share)

    }





    interface NewsItemClicked{
        fun onItemClicked(item:Article)

    }

}