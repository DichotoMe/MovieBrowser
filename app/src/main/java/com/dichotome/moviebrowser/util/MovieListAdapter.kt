package com.dichotome.moviebrowser.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dichotome.moviebrowser.R
import com.dichotome.moviebrowser.api.APIModel
import com.dichotome.moviebrowser.ui.FragmentList.Companion.TAG
import kotlinx.android.synthetic.main.item_movie_list.view.*
import java.util.*

class MovieListAdapter(movieData: List<APIModel.Movie> = listOf()) :
    RecyclerView.Adapter<MovieListHolder>() {

    override fun getItemViewType(position: Int): Int = R.layout.item_movie_list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListHolder =
        MovieListHolder(LayoutInflater.from(parent.context).inflate(viewType, null, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MovieListHolder, position: Int) =
        holder.bind(data[position])

    var data: List<APIModel.Movie>
        get() = linkedData
        set(value) {
            updateData(LinkedList(data), LinkedList(value))
        }

    private var linkedData = LinkedList(movieData)

    private fun updateData(
        oldData: LinkedList<APIModel.Movie>,
        newData: LinkedList<APIModel.Movie>
    ) {
        if (linkedData != newData) {

            linkedData = newData

            DiffUtilCallback(oldData, newData).let { cb ->
                DiffUtil.calculateDiff(cb).dispatchUpdatesTo(this)
            }
        }
    }

    fun updateAll(list: List<APIModel.Movie>) = updateData(LinkedList(linkedData), LinkedList(list))
}

class MovieListHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(dataItem: APIModel.Movie) = dataItem.run {
        itemView.run {
            Glide.with(this)
                .load(image)
                .into(imageView)

            title_tv.text = title
            year_tv.text = year.toString()
            director_tv.text = director
            genre_tv.text = genres.joinToString(", ")
            description_tv.text = description
        }
    }
}