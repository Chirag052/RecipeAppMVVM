package com.example.recipeappmvvm.data.remote

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.recipeappmvvm.R

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object RecipeList: Screen("recipe_list_screen", "Home", Icons.Default.Home)
    object RecipieInfo: Screen("all_saved_recipies", "Saved", Icons.Default.Save)

}