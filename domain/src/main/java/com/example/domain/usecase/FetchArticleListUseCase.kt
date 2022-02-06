package com.example.domain.usecase

import com.example.data.model.ArticleResponse
import com.example.data.repository.ArticleRepository
import com.example.data.repository.NetworkUtil
import com.example.domain.usecase.util.Constant

class FetchArticleListUseCase(
    private val networkUtil: NetworkUtil,
    private val articleRepository: ArticleRepository
) {
    suspend fun fetchArticles(): ArticleResponse {

        return try {
            if (!networkUtil.isInternetOn())
                ArticleResponse(error = com.example.data.model.Error(Constant.NO_INTERNET_CONNECTION))
            else
                articleRepository.fetchArticles()
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