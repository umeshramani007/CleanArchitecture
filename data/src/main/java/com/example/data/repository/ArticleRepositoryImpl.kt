package com.example.data.repository

import com.example.data.model.ArticleResponse
import com.example.data.remote.ApiInterface
import com.example.data.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepositoryImpl(
    private val apiInterface: ApiInterface
) : ArticleRepository {
    override suspend fun fetchArticles(): ArticleResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.getList()
            } catch (e: Exception) {
                e.printStackTrace()
                ArticleResponse(
                    error = com.example.data.model.Error(
                        e.message ?: Constant.ERROR_MSG
                    )
                )
            }
        }
    }
}