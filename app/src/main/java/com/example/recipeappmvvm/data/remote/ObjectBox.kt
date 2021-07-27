package com.example.recipeappmvvm.data.remote

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.recipeappmvvm.data.remote.responses.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.objectbox.android.BuildConfig

object ObjectBox {

    lateinit var boxStore: BoxStore
    private set

    fun init(context: Context){
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()

        if (BuildConfig.DEBUG) {
            Log.d(ContentValues.TAG, "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }

}