package com.example.movieapplication.ui.series.viewmoreseries

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.Series
import com.example.movieapplication.ui.detailmovie.DetailMovieActivity
import com.example.movieapplication.ui.detailseries.DetailSeriesActivity
import com.example.movieapplication.ui.series.SeriesClickCallback
import kotlinx.android.synthetic.main.item_view_more.view.*

class ViewMoreSeriesAdapter(private val callback: SeriesClickCallback) :
    RecyclerView.Adapter<ViewMoreSeriesAdapter.MovieViewHolder>() {

    private var seriesList = ArrayList<Series>()

    fun setSeries(series: ArrayList<Series>?) {
        if (series == null) return
        seriesList.clear()
        seriesList.addAll(series)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_more, parent, false)
        return MovieViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = seriesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(seriesList[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)
    }


    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Series) {
            with(itemView) {
                Glide.with(context)
                    .load(data.backdrop)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_loading)
                    )
                    .centerCrop()
                    .into(img_view_more)
                tv_title_view_more.text = data.title
                tv_release_view_more.text = data.releaseDate
                tv_rating_view_more.text = data.rating.toString()
                item_ratingbar.rating = data.rating!!.toFloat() / 2
                setOnClickListener {
                    val intent = Intent(context, DetailSeriesActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_ID, data.id)
                        putExtra(DetailMovieActivity.EXTRA_POSTER, data.poster)
                        putExtra(DetailMovieActivity.EXTRA_BACKDOP, data.backdrop)
                    }

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        img_view_more,
                        context.resources.getString(R.string.text_transition_backdrop)
                    )
                    context.startActivity(intent, options.toBundle())
                }

            }
        }
    }
}