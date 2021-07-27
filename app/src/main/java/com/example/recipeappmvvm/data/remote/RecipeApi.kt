package com.example.recipeappmvvm.data.remote

import com.example.recipeappmvvm.data.remote.responses.Recipe
import com.example.recipeappmvvm.data.remote.responses.Result
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipeApi {
    @Headers("Authorization: Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
    @GET("search")
    suspend fun getRecipeList(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Recipe

    @Headers("Authorization: Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
    @GET("get")
    suspend fun getRecipeInfo(
        @Query("id") id: Int
    ): Result


}