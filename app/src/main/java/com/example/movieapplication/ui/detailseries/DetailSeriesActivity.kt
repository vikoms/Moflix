package com.example.movieapplication.ui.detailseries

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
import com.example.movieapplication.database.DatabaseContract
import com.example.movieapplication.entity.Season
import com.example.movieapplication.entity.Series
import com.example.movieapplication.helper.FavoriteHelper
import com.example.movieapplication.ui.cast.CastAdapter
import com.example.movieapplication.ui.cast.CastViewModel
import com.example.movieapplication.ui.detailmovie.DetailMovieActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_series.*

class DetailSeriesActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_POSTER = "EXTRA_POSTER"
        const val EXTRA_BACKDOP = "EXTRA_BACKDROP"
    }

    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var item: Series
    private var isFavorite = false
    private var seriesId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_series)

        val bundle = intent.extras
        if (bundle != null) {
            seriesId = bundle.getInt(EXTRA_ID)
            val backdrop = bundle.getString(DetailMovieActivity.EXTRA_BACKDOP)
            val poster = bundle.getString(DetailMovieActivity.EXTRA_POSTER)

            Glide.with(this).load(backdrop).apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
            ).into(img_detail_series)
            Glide.with(this).load(poster).apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
            ).into(img_poster_detail_series)

            populateView()
            setupRecylcerCast()
        }


        favoriteHelper = FavoriteHelper.getInstance(this)
        favoriteHelper.open()
        val cursor = favoriteHelper.queryById(seriesId.toString())
        if (cursor.count > 0) {
            isFavorite = true
            btn_favorite_series.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink)
            btn_favorite_series.text = resources.getString(R.string.text_unfavorite)
            btn_favorite_series.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.white
                )
            )
            btn_favorite_series.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        }

        btn_favorite_series.setOnClickListener(this)
        back_series_detail.setOnClickListener(this)


    }


    private fun populateView() {
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailSeriesViewModel::class.java]
        viewModel.setSeriesDetail(seriesId.toString())
        viewModel.getDetailSeries().observe(this, Observer { series ->
            if (series != null) {
                item = series
                tv_title_detail_series.text = series.title
                tv_date_detail_series.text = series.releaseDate
                tv_episodes_detail_series.text = String.format(
                    resources.getString(R.string.text_total_episodes),
                    series.episodeTotal
                )
                tv_rating_detail_series.text = series.rating.toString()
                detail_ratingbar_series.rating = series.rating!!.toFloat() / 2
                tv_description_detail_series.text = series.description

                val layoutParams = LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
                )
                val typeface = ResourcesCompat.getFont(this, R.font.quicksand_medium)
                if (series.genres?.size ?: 0 > 0) {
                    for (i in 0 until series.genres?.size!!) {
                        val textGenre = TextView(this)
                        textGenre.text = series.genres!!.get(i).title
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

                setupRecylcerSeason(series.seasons)
            }
        })
    }

    private fun setupRecylcerSeason(seasons: ArrayList<Season>?) {
        val seasonAdapter = SeasonAdapter()
        if (seasons != null) {
            seasonAdapter.setData(seasons)
        }
        with(rv_season_detail_series) {
            layoutManager = LinearLayoutManager(
                this@DetailSeriesActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = seasonAdapter
            seasonAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecylcerCast() {
        showLoading(true)
        val castAdapter = CastAdapter()
        val castViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[CastViewModel::class.java]
        castViewModel.setData(seriesId.toString(), "tv")
        castViewModel.setCast()
        castViewModel.getCast().observe(this, Observer { cast ->
            if (cast != null) {
                showLoading(false)
                castAdapter.setData(cast)
            } else {
                showLoading(false)
            }
        })

        with(rv_cast_detail_series) {
            layoutManager = LinearLayoutManager(
                this@DetailSeriesActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = castAdapter
            castAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_favorite_series) {
            val values = ContentValues()
            values.put(DatabaseContract.FavoriteColumns._ID, item.id)
            values.put(DatabaseContract.FavoriteColumns.TYPE, "Series")

            if (isFavorite) {
                val result = favoriteHelper.deleteById(item.id.toString())
                if (result > 0) {
                    btn_favorite_series.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_favorite_border)
                    btn_favorite_series.text = resources.getString(R.string.text_favorite)
                    btn_favorite_series.setBackgroundColor(0)

                    showSnackBar(view, resources.getString(R.string.text_updated))
                } else {
                    showSnackBar(view, resources.getString(R.string.text_updated_failed))
                }
            } else {
                values.put(DatabaseContract.FavoriteColumns.TITLE, item.title)
                values.put(DatabaseContract.FavoriteColumns.POSTER, item.poster)
                values.put(DatabaseContract.FavoriteColumns.BACKDROP, item.backdrop)
                values.put(DatabaseContract.FavoriteColumns.RELEASE_DATE, item.releaseDate)
                values.put(DatabaseContract.FavoriteColumns.RATING, item.rating)

                val result = favoriteHelper.insert(values)
                if (result > 0) {
                    btn_favorite_series.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink)
                    btn_favorite_series.text = resources.getString(R.string.text_unfavorite)
                    btn_favorite_series.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.white
                        )
                    )
                    btn_favorite_series.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorAccent
                        )
                    )


                    showSnackBar(view, resources.getString(R.string.text_updated))
                } else {
                    showSnackBar(view, resources.getString(R.string.text_updated_failed))
                }
            }
        } else if (view?.id == R.id.back_series_detail) {
            finish()
        }
    }


    private fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pg_cast_detail_series.visibility = View.VISIBLE
            rv_cast_detail_series.visibility = View.GONE
        } else {
            pg_cast_detail_series.visibility = View.GONE
            rv_cast_detail_series.visibility = View.VISIBLE
        }
    }
}
