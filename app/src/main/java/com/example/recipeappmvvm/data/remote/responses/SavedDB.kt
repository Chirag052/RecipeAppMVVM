package com.example.recipeappmvvm.data.remote.responses

import io.objectbox.annotation.*

@Entity
data class SavedDB(
    @Id(assignable = true) var id: Long = 0,
    var date_updated: String? = null,
    var featured_image: String? = null,
    @Index var title: String? =null,
    @Convert(converter = StringConverter::class, dbType = String::class)
    val ingredients: List<String>,
    var publisher: String? = null,
    var rating: Int? = null,
    val pk: Int? = null,
)