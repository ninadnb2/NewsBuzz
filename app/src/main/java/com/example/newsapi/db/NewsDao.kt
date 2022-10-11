package com.example.newsapi.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapi.Article
import com.example.newsapi.News
import com.example.newsapi.utils.Constants.NEWS_TABLE
import java.sql.RowId

@Dao
interface NewsDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    fun insertNews(newsEntity: NewsEntity)

    @Delete()
    fun deleteNews(newsEntity: NewsEntity)

   /* @Query("DELETE FROM $NEWS_TABLE WHERE newsId LIKE :id")
    fun deleteNews(id:Int) : NewsEntity
*/


    @Query("SELECT * FROM NEWS_TABLE")
    fun getNews():List<NewsEntity>?
}