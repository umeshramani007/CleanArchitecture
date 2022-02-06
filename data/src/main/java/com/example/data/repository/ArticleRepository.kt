package com.example.data.repository

import com.example.data.model.ArticleResponse

interface ArticleRepository {
    suspend fun fetchArticles(): ArticleResponse
}