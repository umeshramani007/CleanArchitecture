package com.example.clean.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Article
import com.example.domain.usecase.FetchArticleListUseCase
import com.example.clean.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val fetchArticleListUseCase: FetchArticleListUseCase
) : ViewModel() {

    private var _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>>
        get() = _articleList

    private var _error = MutableLiveData<String>()
    var error: LiveData<String> = _error

    fun fetchArticle() {
        viewModelScope.launch {
            val articles = fetchArticleListUseCase.fetchArticles()
            when {
                articles.error != null -> _error.value = articles.error!!.errorMessage
                articles.results.isEmpty() -> _error.value = Constant.NO_DATA
                else -> _articleList.value = articles.results
            }
        }
    }

}