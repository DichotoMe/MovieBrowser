package com.dichotome.moviebrowser.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dichotome.moviebrowser.Repository
import com.dichotome.moviebrowser.api.APIModel
import com.dichotome.moviebrowser.ui.FragmentList.Companion.TAG
import com.dichotome.moviebrowser.util.MovieFilterAdapter
import java.util.*

class MainLogic(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)

    var movies = MediatorLiveData<List<APIModel.Movie>>().apply {
        addSource(repository.getMovies()) {
            if (value != it) value = it
        }
    }

    var filteredMovies = MutableLiveData<List<APIModel.Movie>>()

    val genres = MutableLiveData<SortedSet<String>>()
    val directors = MutableLiveData<SortedSet<String>>()
    val years = MutableLiveData<SortedSet<String>>()

    var appliedGenres = MediatorLiveData<SortedSet<String>>().apply {
        addSource(repository.selectGenres()) { list ->
            value = list.map { it.genre }.toSortedSet()
        }
    }
    var appliedDirectors = MediatorLiveData<SortedSet<String>>().apply {
        addSource(repository.selectDirectors()) { list ->
            value = list.map { it.director }.toSortedSet()
        }
    }
    var appliedYears = MediatorLiveData<SortedSet<String>>().apply {
        addSource(repository.selectYears()) { list ->
            value = list.map { it.year }.toSortedSet()
        }
    }

    var selectedGenres = MediatorLiveData<SortedSet<String>>().apply {
        addSource(appliedGenres) { value = TreeSet(it) }
    }
    var selectedDirectors = MediatorLiveData<SortedSet<String>>().apply {
        addSource(appliedDirectors) { value = TreeSet(it) }
    }
    var selectedYears = MediatorLiveData<SortedSet<String>>().apply {
        addSource(appliedYears) { value = TreeSet(it) }
    }

    var genresAdapterReady = MediatorLiveData<Boolean>().apply {

        fun isAdapterReady() = genres.value != null && appliedGenres.value != null

        addSource(genres) { value = isAdapterReady() }
        addSource(appliedGenres) {
            value = isAdapterReady()
        }
    }
    var directorsAdapterReady = MediatorLiveData<Boolean>().apply {

        fun isAdapterReady() = directors.value != null && appliedDirectors.value != null

        addSource(directors) { value = isAdapterReady() }
        addSource(appliedDirectors) {
            value = isAdapterReady()
        }
    }
    var yearsAdapterReady = MediatorLiveData<Boolean>().apply {

        fun isAdapterReady() = years.value != null && appliedYears.value != null

        addSource(years) { value = isAdapterReady() }
        addSource(appliedYears) {
            value = isAdapterReady()
        }
    }
    var allDataReady = MediatorLiveData<Boolean>().apply {

        fun isAllDataReady() =
            genresAdapterReady.value == true && directorsAdapterReady.value == true && yearsAdapterReady.value == true


        addSource(genresAdapterReady) { if (isAllDataReady()) value = isAllDataReady() }
        addSource(directorsAdapterReady) { if (isAllDataReady()) value = isAllDataReady() }
        addSource(yearsAdapterReady) { if (isAllDataReady()) value = isAllDataReady() }
    }

    lateinit var genresAdapter: MovieFilterAdapter
        private set

    lateinit var yearsAdapter: MovieFilterAdapter
        private set

    lateinit var directorsAdapter: MovieFilterAdapter
        private set

    val filtersReady = MutableLiveData<Boolean>()

    fun prepareGenresAdapter() {
        genresAdapter = MovieFilterAdapter(
            genres.value ?: sortedSetOf(),
            selectedGenres.value ?: sortedSetOf()
        ) {
            genresAdapter.checkFilter(it)
        }
        genresAdapterReady.value = true
    }

    fun prepareDirectorsAdapter() {
        directorsAdapter =
            MovieFilterAdapter(
                directors.value ?: sortedSetOf(),
                selectedDirectors.value ?: sortedSetOf()
            ) {
                directorsAdapter.checkFilter(it)
            }
        directorsAdapterReady.value = true
    }

    fun prepareYearsAdapter() {
        yearsAdapter =
            MovieFilterAdapter(
                years.value ?: sortedSetOf(),
                selectedYears.value ?: sortedSetOf()
            ) {
                yearsAdapter.checkFilter(it)
            }
        yearsAdapterReady.value = true
    }

    fun setupFilters() {
        val newGenres = sortedSetOf<String>()
        val newDirectors = sortedSetOf<String>()
        val newYears = sortedSetOf<String>()

        movies.value?.forEach {
            newGenres.apply {
                it.genres.forEach { genre -> add(genre) }
            }
            newDirectors.add(it.director.trimStart())
            newYears.add(it.year.toString())
        }

        genres.value = newGenres
        directors.value = newDirectors
        years.value = newYears
    }

    fun deleteAppliedGenres() = repository.deleteGenres()
    fun deleteAppliedYears() = repository.deleteYears()
    fun deleteAppliedDirectors() = repository.deleteDirectors()

    fun clearFilters() {
        genresAdapter.clear()
        yearsAdapter.clear()
        directorsAdapter.clear()
    }

    fun finishFilters() {
        filtersReady.value = true
    }

    fun applyFilters() {
        repository.apply {

            overwriteGenres(genresAdapter.selectedFilters)
            overwriteYears(yearsAdapter.selectedFilters)
            overwriteDirectors(directorsAdapter.selectedFilters)
        }

        directorsAdapterReady.value = false
        genresAdapterReady.value = false
        yearsAdapterReady.value = false

        finishFilters()
    }

    fun filterMovies(movies: List<APIModel.Movie>) {
        val res: List<APIModel.Movie> = mutableListOf<APIModel.Movie>().apply {

            val appliedYears = appliedYears.value ?: return@apply
            val appliedDirectors = appliedDirectors.value ?: return@apply
            val appliedGenres = appliedGenres.value ?: return@apply

            movies.forEach {
                val yearValid = it.year.toString() in appliedYears || appliedYears.isEmpty()
                val directorValid = it.director in appliedDirectors || appliedDirectors.isEmpty()
                val genresValid =
                    it.genres.any { g -> g in appliedGenres } || appliedGenres.isEmpty()

                if (yearValid && directorValid && genresValid)
                    add(it)
            }
        }

        if (res != filteredMovies.value) filteredMovies.value = res
    }
}