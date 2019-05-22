package com.dichotome.moviebrowser.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Genres")
data class Genres(
    val genre: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)