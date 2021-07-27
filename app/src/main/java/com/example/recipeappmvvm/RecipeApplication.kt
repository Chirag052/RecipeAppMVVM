package com.example.recipeappmvvm

import android.app.Application
import com.example.recipeappmvvm.data.remote.ObjectBox
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RecipeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ObjectBox.init(this)
    }
}