package com.example.recipeappmvvm.repository

import android.util.Log
import com.example.recipeappmvvm.data.remote.ObjectBox
import com.example.recipeappmvvm.data.remote.RecipeApi
import com.example.recipeappmvvm.data.remote.responses.Recipe
import com.example.recipeappmvvm.data.remote.responses.Result
import com.example.recipeappmvvm.data.remote.responses.SavedDB
import com.example.recipeappmvvm.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityScoped
class RecipeRepository @Inject constructor(
    private  val api: RecipeApi
) {
    private lateinit var recipeBox: Box<SavedDB>
    init {
        recipeBox =  ObjectBox.boxStore.boxFor()

    }
    suspend fun getRecipeList(page: Int,query: String): Resource<Recipe> {
       val response = try{
            api.getRecipeList(page, query)
       }catch(e: Exception){
           return Resource.Error("An unknown error occured.")
       }
        return Resource.Success(response)
     }

    suspend fun getRecipeInfo(id: Int): Resource<Result>{
        val response = try{
            api.getRecipeInfo(id)
        }catch(e: Exception){
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    fun addRecipeToDB(savedDb: SavedDB){
        
//        recipeBox.removeAll()
        recipeBox.put(savedDb)
    }
    fun getAllRecipies(): MutableList<SavedDB>? {
        Log.i("All Recipes",recipeBox.all.toString())
      return recipeBox.all
    }

    fun removeRecipe(id: Long){
        recipeBox.remove(id)
    }


}