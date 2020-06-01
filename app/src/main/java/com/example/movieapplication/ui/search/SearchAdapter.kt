package com.example.movieapplication.ui.search

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
import com.example.movieapplication.entity.Movie
import com.example.movieapplication.entity.Series
import com.example.movieapplication.ui.detailmovie.DetailMovieActivity
import com.example.movieapplication.ui.detailseries.DetailSeriesActivity
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val movies = ArrayList<Movie>()
    private val series = ArrayList<Series>()
    private lateinit var type: String
    fun setData(type: String, movies: ArrayList<Movie>?, series: ArrayList<Series>?) {
        this.type = type
        if (type == "movie") {
            if (movies != null) {
                this.movies.clear()
                this.movies.addAll(movies)
            }
        } else if (type == "tv") {
            if (series != null) {
                this.series.clear()
                this.series.addAll(series)
            }
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {

        return if (type == "movie") {
            movies.size
        } else if (type == "tv") {
            series.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (type == "movie") {
            holder.bindMovie(movies[position])
        } else if (type == "tv") {
            holder.bindSeries(series[position])
        }
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_transition_animation)
    }


    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindSeries(data: Series) {
            with(itemView) {
                Glide.with(context).load(data.poster).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_local_movies)
                        .error(R.drawable.ic_local_movies)
                )
                    .centerCrop().into(img_poster_search)
                tv_date_search.text =
                    data.releaseDate ?: context.resources.getString(R.string.release_date_exception)
                tv_title_search.text = data.title
                item_ratingbar_search.rating = data.rating?.toFloat()!! / 2
                tv_rating_search.text = data.rating.toString()

                setOnClickListener {
                    val intent = Intent(context, DetailSeriesActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_ID, data.id)
                        putExtra(DetailMovieActivity.EXTRA_POSTER, data.poster)
                        putExtra(DetailMovieActivity.EXTRA_BACKDOP, data.backdrop)
                    }


                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        img_poster_search,
                        context.resources.getString(R.string.text_transition_poster)
                    )
                    context.startActivity(intent, options.toBundle())
                }
            }
        }

        fun bindMovie(data: Movie) {
            with(itemView) {
                Glide.with(context).load(data.poster).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_local_movies)
                        .error(R.drawable.ic_local_movies)
                )
                    .centerCrop().into(img_poster_search)
                tv_date_search.text =
                    data.releaseDate ?: context.resources.getString(R.string.release_date_exception)
                tv_title_search.text = data.title
                item_ratingbar_search.rating = data.rating?.toFloat()!! / 2
                tv_rating_search.text = data.rating.toString()

                setOnClickListener {
                    val intent = Intent(context, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_ID, data.id)
                        putExtra(DetailMovieActivity.EXTRA_POSTER, data.poster)
                        putExtra(DetailMovieActivity.EXTRA_BACKDOP, data.backdrop)
                    }
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        img_poster_search,
                        context.resources.getString(R.string.text_transition_poster)
                    )
                    context.startActivity(intent, options.toBundle())
                }
            }

        }

    }
}