package com.example.moviesapp.model

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val image: Int,
    val rating: Double? = null,
    var isFavorite: Boolean = false
)