package com.dichotome.moviebrowser.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Directors")
data class Directors (
    val director: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)