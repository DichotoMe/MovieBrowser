package com.dichotome.moviebrowser.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dichotome.moviebrowser.R
import com.dichotome.moviebrowser.databinding.FragmentFiltersBinding
import com.dichotome.moviebrowser.ui.FragmentList.Companion.TAG
import com.dichotome.moviebrowser.ui.main.MainActivity
import com.dichotome.moviebrowser.ui.main.MainLogic
import kotlinx.android.synthetic.main.fragment_filters.*

class FragmentFilters : Fragment() {

    private lateinit var parentActivity: MainActivity
    private lateinit var parentLogic: MainLogic

    lateinit var binding: FragmentFiltersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_filters,
            container,
            false
        )
        binding.lifecycleOwner = this@FragmentFilters
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            logic = parentLogic
            executePendingBindings()
        }

        parentLogic.apply {
            selectedGenres.observe(parentActivity, Observer {
                prepareGenresAdapter()
            })

            selectedYears.observe(parentActivity, Observer {
                prepareYearsAdapter()
            })

            selectedDirectors.observe(parentActivity, Observer {
                prepareDirectorsAdapter()
            })
        }

        genres_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            parentLogic.genresAdapterReady.observe(parentActivity, Observer {
                if (it == true) {
                    visibility = View.VISIBLE
                    adapter = parentLogic.genresAdapter
                }
            })
        }

        years_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            parentLogic.yearsAdapterReady.observe(parentActivity, Observer {
                if (it == true) {
                    visibility = View.VISIBLE
                    adapter = parentLogic.yearsAdapter
                }
            })
        }

        directors_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            parentLogic.directorsAdapterReady.observe(parentActivity, Observer {
                if (it == true) {
                    visibility = View.VISIBLE
                    adapter = parentLogic.directorsAdapter
                }
            })
        }

        parentLogic.filtersReady.observe(parentActivity, Observer {
            if (it == true) {
                parentActivity.navController.popBackStack()
                parentLogic.filtersReady.value = false
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentActivity = activity as MainActivity
        parentLogic = parentActivity.logic
    }
}