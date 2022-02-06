package com.example.data.model

data class ArticleResponse(
    val results: List<Article> = ArrayList(),
    val error: Error? = null
)