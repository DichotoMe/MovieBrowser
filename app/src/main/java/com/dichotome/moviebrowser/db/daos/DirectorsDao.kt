package com.dichotome.moviebrowser.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dichotome.moviebrowser.db.entities.Directors

@Dao
abstract class DirectorsDao {
    @Insert
    abstract fun insert(directors: List<Directors>)

    @Query("SELECT * FROM Directors")
    abstract fun selectAll(): LiveData<List<Directors>>

    @Query("DELETE FROM Directors")
    abstract fun deleteAll()

    @Query("DELETE FROM DIRECTORS")
    fun overwrite(directors: List<Directors>) {
        deleteAll()
        insert(directors)
    }
}