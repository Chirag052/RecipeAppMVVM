package com.example.recipeappmvvm.data.models

import androidx.annotation.DrawableRes

data class BottomMenuContent(
    val route: String,
    val title: String,
    @DrawableRes val iconId: Int
)
