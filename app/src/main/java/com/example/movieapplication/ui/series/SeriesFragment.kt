package com.example.movieapplication.ui.series

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.ui.search.SearchActivity
import com.example.movieapplication.ui.series.previewseries.PreviewSeriesAiringTodayAdapter
import com.example.movieapplication.ui.series.previewseries.PreviewSeriesOngoingAdapter
import com.example.movieapplication.ui.series.previewseries.PreviewSeriesPopularAdapter
import com.example.movieapplication.ui.series.viewmoreseries.ViewMoreSeriesActivity
import kotlinx.android.synthetic.main.fragment_series.*

/**
 * A simple [Fragment] subclass.
 */
class SeriesFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: PreviewSeriesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[PreviewSeriesViewModel::class.java]
            edt_search_series.setFocusable(false)
            edt_search_series.isClickable = true
            setUpRecyclerViewPopular()
            setUpRecyclerViewAiringToday()
            setUpRecyclerViewOngoing()


            view_more_ongoing.setOnClickListener(this)
            view_more_airing_today.setOnClickListener(this)
            view_more_popular.setOnClickListener(this)
            edt_search_series.setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java).apply {
                    putExtra(SearchActivity.EXTRA_TYPE, "tv")
                }
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun setUpRecyclerViewOngoing() {
        val ongoingAdapter = PreviewSeriesOngoingAdapter()
        viewModel.setOngoingSeries()
        viewModel.getOngoingSeries().observe(this, Observer { ongoingSeries ->
            if (ongoingSeries != null) {
                pg_ongoing_series.visibility = View.GONE
                ongoingAdapter.setSeries(ongoingSeries)
                ongoingAdapter.notifyDataSetChanged()
            } else {
                pg_airing_today_series.visibility = View.GONE
            }
        })

        with(rv_series_ongoing) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = ongoingAdapter
        }

    }

    private fun setUpRecyclerViewAiringToday() {
        val airingTodayAdapter = PreviewSeriesAiringTodayAdapter()
        viewModel.setAiringTodaySeries()
        viewModel.getAiringTodaySeries().observe(this, Observer { airingSeries ->
            if (airingSeries != null) {
                pg_airing_today_series.visibility = View.GONE
                airingTodayAdapter.setSeries(airingSeries)
                airingTodayAdapter.notifyDataSetChanged()
            } else {
                pg_airing_today_series.visibility = View.GONE
            }
        })

        with(rv_series_airing_today) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = airingTodayAdapter
        }
    }

    private fun setUpRecyclerViewPopular() {
        val popularAdapter = PreviewSeriesPopularAdapter()
        viewModel.setPopularSeries()
        viewModel.getPopularSeries().observe(this, Observer { popularSeries ->
            if (popularSeries != null) {
                pg_popular_series.visibility = View.GONE
                popularAdapter.setSeries(popularSeries)
                popularAdapter.notifyDataSetChanged()
            } else {
                pg_airing_today_series.visibility = View.GONE
            }
        })

        with(rv_series_popular) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = popularAdapter
        }
    }

    override fun onClick(item: View?) {
        val intent = Intent(activity, ViewMoreSeriesActivity::class.java)
        when (item?.id) {
            R.id.view_more_ongoing -> {
                intent.apply {
                    putExtra(
                        ViewMoreSeriesActivity.EXTRA_TYPE_SERIES,
                        ViewMoreSeriesActivity.ONGOING
                    )
                }
            }
            R.id.view_more_airing_today -> {
                intent.apply {
                    putExtra(
                        ViewMoreSeriesActivity.EXTRA_TYPE_SERIES,
                        ViewMoreSeriesActivity.AIRING_TODAY
                    )
                }
            }
            R.id.view_more_popular -> {
                intent.apply {
                    putExtra(
                        ViewMoreSeriesActivity.EXTRA_TYPE_SERIES,
                        ViewMoreSeriesActivity.POPULAR
                    )
                }
            }
        }

        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}
