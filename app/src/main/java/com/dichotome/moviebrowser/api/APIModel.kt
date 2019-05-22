package com.dichotome.moviebrowser.api

import com.dichotome.moviebrowser.util.DiffUtilItem
import com.google.gson.annotations.SerializedName

object APIModel {
    data class Response(val values: List<Movie>)
    data class Movie(
        val title: String,
        val year: Int,
        @SerializedName("genre") val genres: List<String>,
        val director: String,
        @SerializedName("desription") val description: String,
        val image: String
    )
}