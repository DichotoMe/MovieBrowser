package com.dichotome.moviebrowser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dichotome.moviebrowser.db.daos.DirectorsDao
import com.dichotome.moviebrowser.db.daos.GenresDao
import com.dichotome.moviebrowser.db.daos.YearsDao
import com.dichotome.moviebrowser.db.entities.Directors
import com.dichotome.moviebrowser.db.entities.Genres
import com.dichotome.moviebrowser.db.entities.Years

@Database(entities = [Directors::class, Genres::class, Years::class], version = 1)
abstract class FiltersDatabase : RoomDatabase() {

    abstract fun directorsDao(): DirectorsDao
    abstract fun genresDao(): GenresDao
    abstract fun yearsDao(): YearsDao

    companion object {

        private var instance: FiltersDatabase? = null

        fun getInstance(context: Context): FiltersDatabase? {
            instance ?: run {
                synchronized(FiltersDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FiltersDatabase::class.java,
                        "FiltersDatabase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return instance
        }
    }
}