package com.example.movieapplication.ui.detailpeople

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.Series
import com.example.movieapplication.ui.detailmovie.DetailMovieActivity
import com.example.movieapplication.ui.detailseries.DetailSeriesActivity
import kotlinx.android.synthetic.main.item_credit_people.view.*

class PeopleSeriesAdapter : RecyclerView.Adapter<PeopleSeriesAdapter.PeopleSeriesViewHolder>() {

    private val series = ArrayList<Series>()
    fun setData(series: ArrayList<Series>) {
        this.series.clear()
        this.series.addAll(series)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleSeriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit_people, parent, false)
        return PeopleSeriesViewHolder(view)
    }

    override fun getItemCount(): Int = series.size

    override fun onBindViewHolder(holder: PeopleSeriesViewHolder, position: Int) {
        holder.bind(series[position])
    }

    class PeopleSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Series) {
            with(itemView) {
                tv_item_people_name_detail.text = data.title
                tv_item_people_charanddate_detail.text = data.releaseDate
                Glide.with(context)
                    .load(data.poster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_loading)
                    )
                    .centerCrop()
                    .into(img_item_credit_poster_detail)

                setOnClickListener {
                    val intent = Intent(context, DetailSeriesActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_ID, data.id)
                        putExtra(DetailMovieActivity.EXTRA_POSTER, data.poster)
                        putExtra(DetailMovieActivity.EXTRA_BACKDOP, data.backdrop)
                    }


                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        img_item_credit_poster_detail,
                        context.resources.getString(R.string.text_transition_poster)
                    )
                    context.startActivity(intent, options.toBundle())
                }

            }
        }

    }
}