package com.dichotome.moviebrowser.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.moviebrowser.R
import com.dichotome.moviebrowser.ui.FragmentList.Companion.TAG
import kotlinx.android.synthetic.main.item_filter_list.view.*
import java.util.*

class MovieFilterAdapter(val allFilters: SortedSet<String>, val selectedFilters: SortedSet<String>, val onClick: (String) -> Unit) :
    RecyclerView.Adapter<MovieFilterHolder>() {

    private val allDataList = allFilters.toList()

    override fun onBindViewHolder(holder: MovieFilterHolder, position: Int) {
        val filterName = allDataList[position]
        val isSelected = isFilterSelected(filterName)
        holder.bind(filterName, isSelected, onClick)
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_filter_list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFilterHolder =
        MovieFilterHolder(LayoutInflater.from(parent.context).inflate(viewType, null, false))

    override fun getItemCount(): Int = allFilters.size

    private fun isFilterSelected(filter: String) = filter in selectedFilters

    fun checkFilter(filter: String) {
        selectedFilters.apply {
            if (!isFilterSelected(filter)) add(filter) else remove(filter)
        }
    }

    fun clear() {
        selectedFilters.clear()
        notifyDataSetChanged()
    }
}

class MovieFilterHolder(item: View) : RecyclerView.ViewHolder(item) {

    private var filterName: String = ""
    private lateinit var onClickListener: (String) -> Unit

    init {
        itemView.setOnClickListener {
            onClickListener(filterName)
            it.checkbox.isChecked = !it.checkbox.isChecked
        }
    }

    fun bind(name: String, isChecked: Boolean, onClick: (String) -> Unit) = itemView.run {

        filterName = name

        filter_name_tv.text = name
        checkbox.isChecked = isChecked
        onClickListener = onClick
    }
}