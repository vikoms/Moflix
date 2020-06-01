package com.example.movieapplication.ui.detailmovie

import android.app.ActionBar
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.BACKDROP
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.POSTER
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.RATING
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.RELEASE_DATE
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.TITLE
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion._ID
import com.example.movieapplication.entity.Movie
import com.example.movieapplication.helper.FavoriteHelper
import com.example.movieapplication.ui.cast.CastAdapter
import com.example.movieapplication.ui.cast.CastViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_POSTER = "EXTRA_POSTER"
        const val EXTRA_BACKDOP = "EXTRA_BACKDROP"

    }

    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var item: Movie
    private var isFavorite = false
    private var movieId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val bundle = intent.extras
        if (bundle != null) {
            movieId = bundle.getInt(EXTRA_ID)
            val backdrop = bundle.getString(EXTRA_BACKDOP)
            val poster = bundle.getString(EXTRA_POSTER)
            Glide.with(this).load(backdrop).apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
            ).into(img_detail_movie)
            Glide.with(this).load(poster).apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
            ).into(img_poster_detail_movie)
        }

        favoriteHelper = FavoriteHelper.getInstance(this)
        favoriteHelper.open()
        populateView()
        populateRecyclerView()
        val cursor = favoriteHelper.queryById(movieId.toString())
        if (cursor.count > 0) {
            isFavorite = true
            btn_favorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink)
            btn_favorite.text = resources.getString(R.string.text_unfavorite)
            btn_favorite.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            btn_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        }

        btn_favorite.setOnClickListener(this)
        back_movie_detail.setOnClickListener(this)
    }

    private fun populateRecyclerView() {
        showLoading(true)
        val castAdapter = CastAdapter()
        val castViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[CastViewModel::class.java]
        castViewModel.setData(movieId.toString(), "movie")
        castViewModel.setCast()
        castViewModel.getCast().observe(this, Observer { cast ->
            if (cast != null) {
                showLoading(false)
                castAdapter.setData(cast)
            } else {
                showLoading(false)
            }
        })

        with(rv_cast_detail_movie) {
            layoutManager =
                LinearLayoutManager(this@DetailMovieActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            castAdapter.notifyDataSetChanged()

        }
    }

    private fun populateView() {
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailMovieViewModel::class.java]
        viewModel.setMovieDetail(movieId.toString())
        viewModel.getDetailMovie().observe(this, Observer { movie ->
            if (movie != null) {
                item = movie
                pg_detail_movie.visibility = View.GONE
                tv_title_detail_movie.text = movie.title
                tv_rating_detail_movie.text = movie.rating.toString()
                detail_ratingbar_movie.rating = movie.rating!!.toFloat() / 2
                tv_runtime_detail_movie.text = String.format("%s min", movie.runtime)
                tv_date_detail_movie.text = movie.releaseDate
                tv_description_detail_movie.text = movie.description

                val layoutParams = LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
                )
                val typeface = ResourcesCompat.getFont(this, R.font.quicksand_medium)
                if (movie.genres?.size ?: 0 > 0) {
                    for (i in 0 until movie.genres?.size!!) {
                        val textGenre = TextView(this)
                        textGenre.text = movie.genres!!.get(i).title
                        textGenre.background =
                            ContextCompat.getDrawable(this, R.drawable.background_form_item)
                        textGenre.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                        layoutParams.setMargins(20, 0, 0, 0)
                        textGenre.setPadding(5, 10, 5, 10)
                        textGenre.layoutParams = layoutParams
                        textGenre.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textGenre.typeface = typeface
                        container_linear.addView(textGenre)
                    }
                } else {
                    val textGenre = TextView(this)
                    textGenre.text = resources.getString(R.string.genre_exception)
                    textGenre.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    layoutParams.setMargins(20, 0, 0, 0)
                    textGenre.setPadding(5, 10, 5, 10)
                    textGenre.layoutParams = layoutParams
                    textGenre.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textGenre.textSize = 15F
                    textGenre.typeface = typeface
                    container_linear.addView(textGenre)
                }
            }
        })
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_favorite) {
            val values = ContentValues()
            values.put(_ID, movieId)
            values.put(TYPE, "Movie")

            if (isFavorite) {
                val result = favoriteHelper.deleteById(item.id.toString())
                if (result > 0) {
                    btn_favorite.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_favorite_border)
                    btn_favorite.text = resources.getString(R.string.text_favorite)
                    btn_favorite.setBackgroundColor(0)
                    isFavorite = false
                    showSnackBar(view, resources.getString(R.string.text_updated))
                } else {
                    showSnackBar(view, resources.getString(R.string.text_updated_failed))
                }
            } else {
                values.put(TITLE, item.title)
                values.put(POSTER, item.poster)
                values.put(BACKDROP, item.backdrop)
                values.put(RELEASE_DATE, item.releaseDate)
                values.put(RATING, item.rating)
                val result = favoriteHelper.insert(values)
                if (result > 0) {
                    isFavorite = true
                    btn_favorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink)
                    btn_favorite.text = resources.getString(R.string.text_unfavorite)
                    btn_favorite.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.white
                        )
                    )
                    btn_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))

                    showSnackBar(view, resources.getString(R.string.text_updated))
                } else {
                    showSnackBar(view, resources.getString(R.string.text_updated_failed))
                }

            }

        } else if (view?.id == R.id.back_movie_detail) {
            finish()
        }
    }

    private fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pg_cast_detail_movie.visibility = View.VISIBLE
            rv_cast_detail_movie.visibility = View.GONE
        } else {
            pg_cast_detail_movie.visibility = View.GONE
            rv_cast_detail_movie.visibility = View.VISIBLE
        }
    }
}
