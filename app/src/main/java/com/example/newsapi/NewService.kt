package com.example.newsapi

import kotlinx.coroutines.selects.select
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL="https://newsapi.org/"
const val API_KEY="4b25bf824be6442fa1abde40f2b1ee8c"

interface NewsInterface{

    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadLines(@Query("country")country:String, @Query("page")page:Int, @Query("category")category:String,@Query("language")language:String): Call<News>
}

object NewService{
    val newsInstance:NewsInterface
    init{
        val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsInstance=retrofit.create(NewsInterface::class.java)
    }
}