package com.example.recipeappmvvm.savedlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeappmvvm.data.remote.responses.SavedDB
import com.example.recipeappmvvm.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedScreenViewModel @Inject constructor(
    val repository: RecipeRepository
): ViewModel() {

    val items = MutableLiveData<MutableList<SavedDB>>(mutableListOf())

    fun getAllSavedRecipes(): MutableList<SavedDB>?{
        val allrecipes = repository.getAllRecipies()
        Log.i("All Recipes",allrecipes.toString())
        return allrecipes
    }

    fun deleteRecipe(id: Long, index: SavedDB?){
        items.value = items.value?.filter { it != index}?.toMutableList()
        repository.removeRecipe(id)
    }

}