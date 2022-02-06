package com.example.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val byline: String,
    val published_date: String,
    val section: String
) : Parcelable