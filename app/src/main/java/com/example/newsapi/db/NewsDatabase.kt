package com.example.newsapi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapi.utils.Constants

@Database(entities=[NewsEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun dao(): NewsDao?

    companion object {
        private var newsDB: NewsDatabase? = null //instance
        fun getAppDb(context: Context): NewsDatabase? {
            if(newsDB==null) {
                newsDB=Room.databaseBuilder<NewsDatabase>(
                    context.applicationContext, NewsDatabase::class.java, Constants.NEWS_DATABASE)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return newsDB
        }
    }
}