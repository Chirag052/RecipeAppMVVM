package com.example.recipeappmvvm.recipelist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappmvvm.data.models.RecipeListEntry
import com.example.recipeappmvvm.repository.RecipeRepository
import com.example.recipeappmvvm.utils.Constants.PAGE_SIZE
import com.example.recipeappmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel(){

    private var currPage = 1

    //var recipeList = mutableStateOf<List<RecipeListEntry>>(listOf())
        //var cachedRecipeList = listOf<RecipeListEntry>()
//    var loadError = mutableStateOf("")
//    var isLoading = mutableStateOf(false)
//    var endReached = mutableStateOf(false)
//    var isSearching = mutableStateOf(false)
//    var query1 = mutableStateOf("")
//    private var isSearchStarting = true
//


    var foodList = mutableStateOf<List<RecipeListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    private var cachedFoodList = listOf<RecipeListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)
    init {
        getAllFood()
    }


    fun searchRecipe(query: String){
        viewModelScope.launch(Dispatchers.Default) {
            val results = repository.getRecipeList(currPage,query)
            Log.i("result aagya naaa",results.data.toString())
            val recipeEntries =results.data!!.results.mapIndexed{index,entry ->
                RecipeListEntry(entry?.pk,entry.featured_image,entry.title,entry.rating)
            }
            currPage++
            foodList.value = recipeEntries
        }
    }

    fun searchFood(text : String)
    {
       currPage =1
        foodList.value = listOf()


        val listToSearch = if(isSearchStarting){
            foodList.value
        }else{
            cachedFoodList
        }
        viewModelScope.launch {
            isLoading.value = true
            if(text.isEmpty()){
                foodList.value = cachedFoodList
                isSearching.value=false
                isSearchStarting = true
                return@launch
            }
            val response = repository.getRecipeList(currPage, text)
            Log.i("response sussesss",response.data.toString())
            endReached.value = currPage * PAGE_SIZE >= response.data!!.count
            when (response)
            {
                is Resource.Success -> {
                    val foodentrie: List<RecipeListEntry> =
                        response.data.results.mapIndexed { index, entry ->
                            RecipeListEntry(entry?.pk,entry.featured_image,entry.title,entry.rating)
                        }

                    currPage++
                    loadError.value = ""
                    isLoading.value = false
                    foodList.value += foodentrie
                    isSearching.value = true
                    Log.i("bolleans", loadError.value.toString()+" "+isLoading.value.toString()+" "+endReached.value.toString())


                }

                is Resource.Error ->{
                    loadError.value = response.message!!
                    isLoading.value = false
                }


            }


        }
    }
    fun searchPokemonList(query: String){
        currPage=1
        val listToSearch = if(isSearchStarting){
            foodList.value
        }else{
            cachedFoodList
        }
        foodList.value = listOf()
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                foodList.value = cachedFoodList
                isSearching.value=false
                isSearchStarting = true
                return@launch
            }
            val results = repository.getRecipeList(currPage,query)
            val foodentrie =
                results.data!!.results.mapIndexed { index, entry ->
                    RecipeListEntry(entry.pk,entry.featured_image,entry.title,entry.rating)
                }
            //first time we search
            if(isSearchStarting){
                cachedFoodList = foodList.value
                isSearchStarting=false
            }
            foodList.value = foodentrie
            isSearching.value = true
        }
    }

    fun getAllFood()
    {
        viewModelScope.launch {

            isLoading.value = true
            val response = repository.getRecipeList(currPage,"")
            Log.i("response sussesss",response.data.toString())

            endReached.value = currPage * PAGE_SIZE >= response.data!!.count
            when(response) {
                is Resource.Success -> {

                    val foodentries : List<RecipeListEntry> =
                        response.data.results.mapIndexed { index, entry ->
                            RecipeListEntry(entry.pk,entry.featured_image,entry.title,entry.rating)
                        }
                    Log.i("allRecipies",foodentries.toString())
                    currPage++
                    loadError.value = ""
                    isLoading.value = false
                    foodList.value += foodentries

                }


                is Resource.Error ->{
                    loadError.value = response.message!!
                    isLoading.value = false
                }
            }
        }

    }


}




