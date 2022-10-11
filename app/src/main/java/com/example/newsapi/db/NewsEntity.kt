package com.example.newsapi.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapi.utils.Constants.NEWS_TABLE


@Entity(tableName = NEWS_TABLE)
data class NewsEntity (

    @PrimaryKey(autoGenerate = true)
    val newsId:Int,
    @ColumnInfo(name = "Title")
    val newsTitle:String,
    @ColumnInfo(name = "Description")
    val newsDesc:String,
    @ColumnInfo(name = "Source")
    val newsAuthor:String,
    @ColumnInfo(name = "Content")
    val newsContent:String,
    @ColumnInfo(name = "urlToImage")
    val urlToImage:String,
    @ColumnInfo(name = "newsurl")
    val url:String,

    )
