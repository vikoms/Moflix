package com.example.movieapplication.ui.favoriteseries

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.entity.Favorite
import com.example.movieapplication.helper.FavoriteHelper
import com.example.movieapplication.helper.MappingHelper
import com.example.movieapplication.ui.detailseries.DetailSeriesActivity
import com.example.movieapplication.ui.favorite.FavoriteAdapter
import com.example.movieapplication.ui.favorite.FavoriteCallback
import kotlinx.android.synthetic.main.fragment_favorite_series.*
import kotlinx.android.synthetic.main.item_view_more.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteSeriesFragment : Fragment(), FavoriteCallback {

    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var favoriteAdapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_series, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null) {
            favoriteHelper = FavoriteHelper.getInstance(activity!!)
            favoriteHelper.open()


            favoriteAdapter = FavoriteAdapter(this)
            with(rv_favorite_series) {
                adapter = favoriteAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
            }


            if (savedInstanceState == null) {
                loadFavoriteAsync()
            } else {
                val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
                if (list != null) {
                    favoriteAdapter.favorites = list
                }
            }
        }
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            pg_favorite_series.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryByType(arrayOf("Series"))
                MappingHelper.mapCursorToArrayList(cursor)
            }
            pg_favorite_series.visibility = View.INVISIBLE
            val favorites = deferredFavorite.await()
            if (favorites.size > 0) {
                favoriteAdapter.favorites = favorites
            } else {
                favoriteAdapter.favorites = ArrayList()

            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.favorites)
    }

    override fun onItemClicked(data: Favorite, view: View) {
        val intent = Intent(context, DetailSeriesActivity::class.java).apply {
            putExtra(DetailSeriesActivity.EXTRA_ID, data.favoriteId)
            putExtra(DetailSeriesActivity.EXTRA_BACKDOP, data.backdrop)
            putExtra(DetailSeriesActivity.EXTRA_POSTER, data.poster)
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context as Activity,
            view.img_view_more,
            activity?.resources!!.getString(R.string.text_transition_backdrop)
        )
        activity?.startActivity(intent, options.toBundle())
    }
}
