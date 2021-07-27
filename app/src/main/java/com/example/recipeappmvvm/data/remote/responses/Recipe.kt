package com.example.recipeappmvvm.data.remote.responses

data class Recipe(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)