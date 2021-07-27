package com.example.recipeappmvvm.di

import com.example.recipeappmvvm.data.remote.RecipeApi
import com.example.recipeappmvvm.repository.RecipeRepository
import com.example.recipeappmvvm.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        api: RecipeApi
    ) =RecipeRepository(api)


    @Singleton
    @Provides
    fun provideRecipeApi(): RecipeApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RecipeApi::class.java)
    }

}