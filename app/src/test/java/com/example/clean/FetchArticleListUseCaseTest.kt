package com.example.clean

import com.example.data.model.Article
import com.example.data.model.ArticleResponse
import com.example.data.repository.ArticleRepository
import com.example.data.repository.NetworkUtil
import com.example.domain.usecase.FetchArticleListUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test


class FetchArticleListUseCaseTest {

    @Test
    fun testIsInternetConnectionAvailable() {
        val networkUtil = FakeNetworkUtil(true)
        Assert.assertEquals(networkUtil.isInternetOn(), true)
    }

    @Test
    fun testIsOnlineAndValidArticleList() {
        val networkUtil = FakeNetworkUtil(true)
        val repository = FakeArticleRepository(2)
        val testCase = FetchArticleListUseCase(networkUtil, repository)
        runBlocking {
            Assert.assertTrue(testCase.fetchArticles().results.size == 2)
        }
    }

    @Test
    fun testIsOnlineAndZeroSizeArticleList() {
        val networkUtil = FakeNetworkUtil(true)
        val repository = FakeArticleRepository(0)
        val testCase = FetchArticleListUseCase(networkUtil, repository)
        runBlocking {
            Assert.assertTrue(testCase.fetchArticles().error?.errorMessage == "No Data")
        }
    }

    @Test
    fun testIsOnlineAndError() {
        val networkUtil = FakeNetworkUtil(true)
        val repository = FakeArticleRepository(-1)
        val testCase = FetchArticleListUseCase(networkUtil, repository)
        runBlocking {
            Assert.assertTrue(testCase.fetchArticles().error?.errorMessage == "Oops. Something went wrong")
        }
    }

    @Test
    fun testOfflineAndError() {
        val networkUtil = FakeNetworkUtil(false)
        val repository = FakeArticleRepository(-1)
        val testCase = FetchArticleListUseCase(networkUtil, repository)
        runBlocking {
            Assert.assertTrue(testCase.fetchArticles().error?.errorMessage == "No internet connection")
        }
    }

    class FakeNetworkUtil(private val isOnline: Boolean) : NetworkUtil {
        override fun isInternetOn(): Boolean {
            return isOnline
        }
    }

    class FakeArticleRepository(private val arraySize: Int) : ArticleRepository {
        override suspend fun fetchArticles(): ArticleResponse {
            val list = ArrayList<Article>()
            return when {
                arraySize > 0 -> {

                    for (i in 0 until arraySize)
                        list.add(
                            Article(
                                title = "Test",
                                published_date = "2022-12-02",
                                byline = "By thomson",
                                section = "Section"
                            )
                        )
                    ArticleResponse(results = list, null)
                }
                arraySize == 0 -> {
                    ArticleResponse(results = list, com.example.data.model.Error("No Data"))
                }
                else -> ArticleResponse(
                    results = list,
                    com.example.data.model.Error("Oops. Something went wrong")
                )
            }
        }

    }

}