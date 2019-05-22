package com.dichotome.moviebrowser

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.dichotome.moviebrowser.api.APIModel
import com.dichotome.moviebrowser.api.APIService
import com.dichotome.moviebrowser.db.FiltersDatabase
import com.dichotome.moviebrowser.db.daos.DirectorsDao
import com.dichotome.moviebrowser.db.daos.GenresDao
import com.dichotome.moviebrowser.db.daos.YearsDao
import com.dichotome.moviebrowser.db.entities.Directors
import com.dichotome.moviebrowser.db.entities.Genres
import com.dichotome.moviebrowser.db.entities.Years
import com.dichotome.moviebrowser.ui.FragmentList.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Repository(application: Application) {

    private lateinit var genresDao: GenresDao
    private lateinit var yearsDao: YearsDao
    private lateinit var directorsDao: DirectorsDao

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        val database = FiltersDatabase.getInstance(application.applicationContext)
        database?.apply {
            genresDao = genresDao()
            yearsDao = yearsDao()
            directorsDao = directorsDao()
        }
    }

    private fun runAsync(action: () -> Unit) = coroutineScope.launch { action() }

    fun overwriteGenres(genres: SortedSet<String>) = runAsync {
        genresDao.overwrite(genres.map { Genres(it) })
    }

    fun selectGenres() = genresDao.selectAll()

    fun deleteGenres() = runAsync {
        genresDao.deleteAll()
    }

    fun overwriteYears(years: SortedSet<String>) = runAsync {
        yearsDao.overwrite(years.map { Years(it) })
    }

    fun selectYears() = yearsDao.selectAll()


    fun deleteYears() = runAsync {
        yearsDao.deleteAll()
    }

    fun overwriteDirectors(directors: SortedSet<String>) = runAsync {
        directorsDao.overwrite(directors.map { Directors(it) })
    }

    fun selectDirectors() = directorsDao.selectAll()

    fun deleteDirectors() = runAsync {
        directorsDao.deleteAll()
    }

    fun getMovies() = object : LiveData<List<APIModel.Movie>>() {
        override fun onActive() {
            super.onActive()

            APIService.create().fetchMovies().enqueue(object : Callback<APIModel.Response> {
                override fun onFailure(call: Call<APIModel.Response>, t: Throwable) {}

                override fun onResponse(c: Call<APIModel.Response>, resp: Response<APIModel.Response>) {
                    value = resp.body()?.values ?: listOf()
                }
            })
        }
    }
}
