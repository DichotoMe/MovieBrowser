package com.dichotome.moviebrowser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dichotome.moviebrowser.R
import com.dichotome.moviebrowser.ui.main.MainActivity
import com.dichotome.moviebrowser.util.MovieListAdapter
import kotlinx.android.synthetic.main.fragment_list.*

class FragmentList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val parentActivity = activity as MainActivity
        val parentLogic = parentActivity.logic

        movie_list.apply {
            layoutManager = LinearLayoutManager(context)
        }

        filters_button.setOnClickListener {
            findNavController().navigate(R.id.action_set_filters)
        }

        fun mustFiltersBeVisible() = genres_filters.isVisible
                || years_filters.isVisible
                || directors_filters.isVisible

        fun hideGenreFilters() {
            genres_filters.isVisible = false
            filers_title_tv.isVisible = mustFiltersBeVisible()
        }

        fun hideYearFilters() {
            years_filters.isVisible = false
            filers_title_tv.isVisible = mustFiltersBeVisible()
        }

        fun hideDirectorFilters() {

            directors_filters.isVisible = false
            filers_title_tv.isVisible = mustFiltersBeVisible()
        }


        parentLogic.apply {

            appliedGenres.observe(parentActivity, Observer {
                it?.toList()?.let {
                    if (it.isNotEmpty()) {
                        filers_title_tv.isVisible = true
                        genres_filters.isVisible = true
                        genres_filters_tv.text =
                            getString(R.string.genres) + ": " + it.joinToString(", ")
                    } else hideGenreFilters()
                }
            })

            clear_genres.setOnClickListener {
                deleteAppliedGenres()
                hideGenreFilters()
            }

            appliedYears.observe(parentActivity, Observer {
                it?.toList()?.let {
                    if (it.isNotEmpty()) {
                        filers_title_tv.isVisible = true
                        years_filters.isVisible = true
                        years_filters_tv.text =
                            getString(R.string.years) + ": " + it.joinToString(", ")
                    } else hideYearFilters()
                }
            })

            clear_years.setOnClickListener {
                deleteAppliedYears()
                hideYearFilters()
            }

            appliedDirectors.observe(parentActivity, Observer {
                it?.toList()?.let {
                    if (it.isNotEmpty()) {
                        filers_title_tv.isVisible = true
                        directors_filters.isVisible = true
                        directors_filters_tv.text =
                            getString(R.string.directors) + ": " + it.joinToString(", ")
                    } else hideDirectorFilters()
                }
            })

            clear_directors.setOnClickListener {
                hideDirectorFilters()
                deleteAppliedDirectors()
            }

            movies.observe(parentActivity, Observer {
                it?.let { setupFilters() }
            })

            allDataReady.observe(parentActivity, Observer {
                if (it == true)
                    filterMovies(movies.value ?: listOf())
            })

            filteredMovies.observe(parentActivity, Observer {
                it?.let { data ->
                    if (data.isNullOrEmpty()) {
                        no_results_tv.visibility = View.VISIBLE
                        movie_list.visibility = View.INVISIBLE
                    } else {
                        no_results_tv.visibility = View.INVISIBLE
                        movie_list.visibility = View.VISIBLE
                    }

                    movie_list.apply {

                        adapter?.let {
                            (adapter as MovieListAdapter).updateAll(data)
                        } ?: run {
                            adapter = MovieListAdapter(data)
                        }
                        movie_list.scrollToPosition(0)
                    }
                }
            })
        }
    }

    companion object {
        const val TAG = "MovieBrowser"
    }
}