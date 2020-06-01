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
import com.example.movieapplication.entity.Movie
import com.example.movieapplication.ui.detailmovie.DetailMovieActivity
import kotlinx.android.synthetic.main.item_credit_people.view.*

class PeopleMovieAdapter : RecyclerView.Adapter<PeopleMovieAdapter.PeopleMovieViewHolder>() {

    private val movies = ArrayList<Movie>()
    fun setData(movies: ArrayList<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleMovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit_people, parent, false)
        return PeopleMovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: PeopleMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }


    class PeopleMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                tv_item_people_name_detail.text = movie.title
                tv_item_people_charanddate_detail.text = movie.releaseDate
                Glide.with(context)
                    .load(movie.poster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_loading)
                    )
                    .centerCrop()
                    .into(img_item_credit_poster_detail)

                setOnClickListener {
                    val intent = Intent(context, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_ID, movie.id)
                        putExtra(DetailMovieActivity.EXTRA_POSTER, movie.poster)
                        putExtra(DetailMovieActivity.EXTRA_BACKDOP, movie.backdrop)
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