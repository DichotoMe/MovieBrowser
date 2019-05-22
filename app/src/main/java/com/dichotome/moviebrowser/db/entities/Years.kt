package com.dichotome.moviebrowser.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Years")
data class Years(
    val year: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)