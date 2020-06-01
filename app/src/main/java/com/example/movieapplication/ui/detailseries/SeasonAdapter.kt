package com.example.movieapplication.ui.detailseries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.Season
import kotlinx.android.synthetic.main.item_season_series.view.*

class SeasonAdapter : RecyclerView.Adapter<SeasonAdapter.SeasonHolder>() {

    private val seasons = ArrayList<Season>()
    fun setData(seasons: ArrayList<Season>) {
        this.seasons.clear()
        this.seasons.addAll(seasons)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_season_series, parent, false)
        return SeasonHolder(view)
    }

    override fun getItemCount(): Int = seasons.size

    override fun onBindViewHolder(holder: SeasonHolder, position: Int) {
        holder.bind(seasons[position])
    }

    class SeasonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Season) {
            with(itemView) {
                tv_item_season_name.text = data.name
                tv_item_season_date.text = data.releaseDate
                Glide.with(context)
                    .load(data.poster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_local_movies)
                            .error(R.drawable.ic_local_movies)
                    )
                    .centerCrop()
                    .into(img_item_season_poster)
            }
        }
    }

}