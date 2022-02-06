package com.example.data.remote

import com.example.data.model.ArticleResponse
import com.example.data.util.Constant
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("mostviewed/all-sections/7.json")
    suspend fun getList(@Query("api-key") apiKey:String = Constant.API_KEY): ArticleResponse

}