package com.dichotome.moviebrowser.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dichotome.moviebrowser.db.entities.Years
import java.util.*

@Dao
abstract class YearsDao {
    @Insert
    abstract fun insert(years: List<Years>)

    @Query("SELECT * FROM Years")
    abstract fun selectAll(): LiveData<List<Years>>

    @Query("DELETE FROM Years")
    abstract fun deleteAll()

    fun overwrite(years: List<Years>) {
        deleteAll()
        insert(years)
    }
}