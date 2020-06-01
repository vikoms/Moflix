package com.example.movieapplication.ui.series.viewmoreseries

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.entity.Series
import com.example.movieapplication.ui.series.PreviewSeriesViewModel
import com.example.movieapplication.ui.series.SeriesClickCallback
import kotlinx.android.synthetic.main.activity_view_more_series.*

class ViewMoreSeriesActivity : AppCompatActivity(), SeriesClickCallback {

    companion object {
        const val EXTRA_TYPE_SERIES = "EXTRA_TYPE_SERIES"
        const val ONGOING = "ONGOING"
        const val POPULAR = "POPULAR"
        const val AIRING_TODAY = "AIRING_TODAY"
    }

    private lateinit var viewModel: PreviewSeriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more_series)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[PreviewSeriesViewModel::class.java]

        val extras = intent.extras
        var title: String? = null
        if (extras != null) {
            when (extras.getString(EXTRA_TYPE_SERIES)) {
                ONGOING -> {
                    setUpAiringTV()
                    title = resources.getString(R.string.text_ongoing_tv_series)
                }
                AIRING_TODAY -> {
                    setUpAiringTodaySeries()
                    title = resources.getString(R.string.text_airing_today)
                }
                POPULAR -> {
                    setUpPopularSeries()
                    title = resources.getString(R.string.text_popular)
                }
            }
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
    }

    private fun setUpAiringTodaySeries() {
        val airingTodayAdapter = ViewMoreSeriesAdapter(this)
        viewModel.setAiringTodaySeries()
        viewModel.getAiringTodaySeries().observe(this, Observer { airingTodaySeries ->
            if (airingTodaySeries != null) {
                pg_view_more_series.visibility = View.GONE
                airingTodayAdapter.setSeries(airingTodaySeries)
                airingTodayAdapter.notifyDataSetChanged()
            }
        })

        with(rv_series_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreSeriesActivity)
            setHasFixedSize(true)
            adapter = airingTodayAdapter
        }

    }

    private fun setUpPopularSeries() {
        val airingTodayAdapter = ViewMoreSeriesAdapter(this)
        viewModel.setPopularSeries()
        viewModel.getPopularSeries().observe(this, Observer { popularSeries ->
            if (popularSeries != null) {
                pg_view_more_series.visibility = View.GONE
                airingTodayAdapter.setSeries(popularSeries)
                airingTodayAdapter.notifyDataSetChanged()
            }
        })

        with(rv_series_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreSeriesActivity)
            setHasFixedSize(true)
            adapter = airingTodayAdapter
        }

    }

    private fun setUpAiringTV() {
        val airingTodayAdapter = ViewMoreSeriesAdapter(this)
        viewModel.setOngoingSeries()
        viewModel.getOngoingSeries().observe(this, Observer { ongoingSeries ->
            if (ongoingSeries != null) {
                pg_view_more_series.visibility = View.GONE
                airingTodayAdapter.setSeries(ongoingSeries)
                airingTodayAdapter.notifyDataSetChanged()
            }
        })

        with(rv_series_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreSeriesActivity)
            setHasFixedSize(true)
            adapter = airingTodayAdapter
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSeriesClicked(data: Series) {
        Toast.makeText(this, "${data.rating!!.toFloat()}", Toast.LENGTH_SHORT).show()

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
