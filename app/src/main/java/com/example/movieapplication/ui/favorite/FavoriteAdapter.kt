package com.example.movieapplication.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.Favorite
import kotlinx.android.synthetic.main.item_view_more.view.*

class FavoriteAdapter(private val callback: FavoriteCallback) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    var favorites = ArrayList<Favorite>()
        set(favorites) {
            if (favorites.size > 0) {
                this.favorites.clear()
            }
            this.favorites.addAll(favorites)
            notifyDataSetChanged()
        }

    fun setData(list: ArrayList<Favorite>) {
        favorites.clear()
        favorites.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_more, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(favorites[position])
    }

    inner class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Favorite) {
            with(itemView) {
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
                        callback.onItemClicked(data, itemView)
                    }
                }
            }
        }
    }

}