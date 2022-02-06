package com.example.clean

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.data.model.Article
import com.example.data.model.ArticleResponse
import com.example.data.model.Error
import com.example.data.repository.ArticleRepository
import com.example.data.repository.NetworkUtil
import org.junit.Ignore
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Ignore("Need to check how to share the class between test and androidTest package")
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
                ArticleResponse(results = list, Error("No Data"))
            }
            else -> ArticleResponse(
                results = list,
                Error("Oops. Something went wrong")
            )
        }
    }

}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}