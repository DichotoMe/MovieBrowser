package com.dichotome.moviebrowser.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dichotome.moviebrowser.db.entities.Genres
import java.util.*

@Dao
abstract class GenresDao {
    @Insert
    abstract fun insert(genres: List<Genres>)

    @Query("SELECT * FROM Genres")
    abstract fun selectAll(): LiveData<List<Genres>>

    @Query("DELETE FROM Genres")
    abstract fun deleteAll()

    fun overwrite(genres: List<Genres>) {
        deleteAll()
        insert(genres)
    }
}