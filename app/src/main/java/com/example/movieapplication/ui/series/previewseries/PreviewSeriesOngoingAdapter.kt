package com.example.movieapplication.ui.series.previewseries

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
import kotlinx.android.synthetic.main.item_row.view.*

class PreviewSeriesOngoingAdapter :
    RecyclerView.Adapter<PreviewSeriesOngoingAdapter.SeriesViewHolder>() {

    private var seriesList = ArrayList<Series>()

    fun setSeries(series: ArrayList<Series>) {
        if (series == null) return
        seriesList.clear()
        seriesList.addAll(series)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return SeriesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        if (seriesList.size > 5) {
            return 10
        } else {
            return seriesList.size
        }
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(seriesList[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition_animation)

    }


    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Series) {
            with(itemView) {
                Glide.with(context)
                    .load(data.poster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_loading)
                    )
                    .centerCrop()
                    .into(item_img_1)
                item_text_1.text = data.title

                setOnClickListener {
                    val intent = Intent(context, DetailSeriesActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_ID, data.id)
                        putExtra(DetailMovieActivity.EXTRA_POSTER, data.poster)
                        putExtra(DetailMovieActivity.EXTRA_BACKDOP, data.backdrop)
                    }


                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        item_img_1,
                        context.resources.getString(R.string.text_transition_poster)
                    )
                    context.startActivity(intent, options.toBundle())
                }

            }
        }
    }
}