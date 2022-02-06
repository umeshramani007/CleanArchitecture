package com.example.clean.di

import android.app.Application
import android.content.Context
import com.example.data.remote.ApiInterface
import com.example.data.repository.ArticleRepository
import com.example.data.repository.ArticleRepositoryImpl
import com.example.data.repository.NetworkUtil
import com.example.data.repository.NetworkUtilImpl
import com.example.domain.usecase.FetchArticleListUseCase
import com.example.clean.util.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = run {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideGSON(): Gson = run {
        GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOKHttpClient(): OkHttpClient = run {
        val httpClient = OkHttpClient.Builder()
        try {
            //Logging
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(loggingInterceptor)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        httpClient.build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideArticleRepository(
        apiInterface: ApiInterface
    ): ArticleRepository =
        ArticleRepositoryImpl(apiInterface)

    @Provides
    @Singleton
    fun provideFetchArticleListUseCase(
        networkUtil: NetworkUtil,
        articleRepository: ArticleRepository
    ): FetchArticleListUseCase {
        return FetchArticleListUseCase(networkUtil, articleRepository)
    }

    @Provides
    @Singleton
    fun provideNetworkUtil(application: Application): NetworkUtil {
        return NetworkUtilImpl(application)
    }
}