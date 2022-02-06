package com.example.clean.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.repository.ArticleRepository
import com.example.data.repository.NetworkUtil
import com.example.domain.usecase.FetchArticleListUseCase
import com.example.clean.FakeArticleRepository
import com.example.clean.FakeNetworkUtil
import com.example.clean.getOrAwaitValue
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleListViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var userCase: FetchArticleListUseCase
    lateinit var networkUtil: NetworkUtil
    lateinit var articleRepository: ArticleRepository
    lateinit var viewModel: ArticleListViewModel

    @Test
    fun testIsOnlineAndValidArticlesList() {
        networkUtil = FakeNetworkUtil(true)
        articleRepository = FakeArticleRepository(1)
        userCase = FetchArticleListUseCase(networkUtil, articleRepository)
        viewModel = ArticleListViewModel(userCase)
        viewModel.fetchArticle()
        val size = viewModel.articleList.getOrAwaitValue().size
        assertTrue(size > 0)
    }

    @Test
    fun testIsOnlineAndError() {
        networkUtil = FakeNetworkUtil(true)
        articleRepository = FakeArticleRepository(-1)
        userCase = FetchArticleListUseCase(networkUtil, articleRepository)
        viewModel = ArticleListViewModel(userCase)
        viewModel.fetchArticle()
        val message = viewModel.error.getOrAwaitValue()
        assertEquals(message, "Oops. Something went wrong")
    }

    @Test
    fun testIsOnlineAndZeroArticleList() {
        networkUtil = FakeNetworkUtil(true)
        articleRepository = FakeArticleRepository(0)
        userCase = FetchArticleListUseCase(networkUtil, articleRepository)
        viewModel = ArticleListViewModel(userCase)
        viewModel.fetchArticle()
        val message = viewModel.error.getOrAwaitValue()
        assertEquals(message, "No Data")
    }

    @Test
    fun testNoInternet() {
        networkUtil = FakeNetworkUtil(false)
        articleRepository = FakeArticleRepository(0)
        userCase = FetchArticleListUseCase(networkUtil, articleRepository)
        viewModel = ArticleListViewModel(userCase)
        viewModel.fetchArticle()
        val message = viewModel.error.getOrAwaitValue()
        assertEquals(message, "No internet connection")
    }
}