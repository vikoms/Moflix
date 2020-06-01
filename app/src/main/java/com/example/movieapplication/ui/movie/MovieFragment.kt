package com.example.movieapplication.ui.movie

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
import com.example.movieapplication.ui.movie.previewmovie.PreviewMovieNowPlayingAdapter
import com.example.movieapplication.ui.movie.previewmovie.PreviewMoviePopularAdapter
import com.example.movieapplication.ui.movie.previewmovie.PreviewMovieUpcomingAdapter
import com.example.movieapplication.ui.movie.viewmoremovie.ViewMoreMovieActivity
import com.example.movieapplication.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: PreviewMovieViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[PreviewMovieViewModel::class.java]
            edt_search_movie.setFocusable(false)
            edt_search_movie.isClickable = true
            setupPopularRecycler()
            setUpNowPlayingRecycler()
            setUpUpcomingRecycler()

            view_more_popular.setOnClickListener(this)
            view_more_upcoming.setOnClickListener(this)
            view_more_now_playing.setOnClickListener(this)
            edt_search_movie.setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java).apply {
                    putExtra(SearchActivity.EXTRA_TYPE, "movie")
                }
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

        }
    }

    private fun setUpUpcomingRecycler() {
        val previewUpcomingAdapter = PreviewMovieUpcomingAdapter()
        viewModel.setUpcomingMovies()
        viewModel.getDataUpcomingMovie().observe(this, Observer { upcomingMovies ->
            if (upcomingMovies != null) {
                pg_upcoming_movie.visibility = View.GONE
                previewUpcomingAdapter.setMovies(upcomingMovies)
                previewUpcomingAdapter.notifyDataSetChanged()

            }
        })


        with(rv_movie_upcoming) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = previewUpcomingAdapter
        }
    }

    private fun setUpNowPlayingRecycler() {
        val previewMovieNowPlayingAdapter = PreviewMovieNowPlayingAdapter()
        viewModel.setNowPlayingMovies()
        viewModel.getDataNowPlayingMovie().observe(this, Observer { nowPlayingMovies ->
            if (nowPlayingMovies != null) {
                pg_now_playing_movie.visibility = View.GONE
                previewMovieNowPlayingAdapter.setMovies(nowPlayingMovies)
                previewMovieNowPlayingAdapter.notifyDataSetChanged()
            }
        })


        with(rv_movie_now_laying) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = previewMovieNowPlayingAdapter
        }
    }

    private fun setupPopularRecycler() {
        val previewPopularAdapter = PreviewMoviePopularAdapter()
        viewModel.setPopularMovies()
        viewModel.getDataPopularMovie().observe(this, Observer { popularMovies ->
            if (popularMovies != null) {
                pg_popular_movie.visibility = View.GONE
                previewPopularAdapter.setMovies(popularMovies)
                previewPopularAdapter.notifyDataSetChanged()
            }
        })


        with(rv_movie_popular) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = previewPopularAdapter
        }


    }

    override fun onClick(item: View?) {
        val intent = Intent(activity, ViewMoreMovieActivity::class.java)
        when (item?.id) {
            R.id.view_more_upcoming -> {
                intent.apply {
                    putExtra(ViewMoreMovieActivity.EXTRA_TYPE_MOVIE, ViewMoreMovieActivity.UPCOMING)
                }
            }
            R.id.view_more_now_playing -> {
                intent.apply {
                    putExtra(
                        ViewMoreMovieActivity.EXTRA_TYPE_MOVIE,
                        ViewMoreMovieActivity.NOW_PLAYING
                    )
                }
            }
            R.id.view_more_popular -> {
                intent.apply {
                    putExtra(ViewMoreMovieActivity.EXTRA_TYPE_MOVIE, ViewMoreMovieActivity.POPULAR)
                }
            }
        }

        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}
