package com.example.recipeappmvvm.recipedetailscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.recipeappmvvm.data.remote.ObjectBox
import com.example.recipeappmvvm.data.remote.RecipeApi
import com.example.recipeappmvvm.data.remote.responses.Result
import com.example.recipeappmvvm.data.remote.responses.SavedDB
import com.example.recipeappmvvm.repository.RecipeRepository
import com.example.recipeappmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    val repository: RecipeRepository
): ViewModel() {
    suspend fun getRecipeInfo(id: Int): Resource<Result> {
        return repository.getRecipeInfo(id)
    }

    fun addToDB(recipeInfo: Resource<Result>){
        Log.i("fail hogya",recipeInfo.toString())
        val recipies = SavedDB(
            date_updated = recipeInfo.data!!.date_updated,
            title =  recipeInfo.data.title,
            featured_image =  recipeInfo.data.featured_image,
            ingredients =  recipeInfo.data.ingredients,
            publisher = recipeInfo.data.publisher,
            id =  recipeInfo.data.pk.toLong(),
            rating =  recipeInfo.data.rating
            )
        repository.addRecipeToDB(recipies)
        Log.i("data added success",recipies.toString())
    }


}