package com.example.movieapplication.ui.movie.viewmoremovie

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.entity.Movie
import com.example.movieapplication.ui.movie.MovieClickCallback
import com.example.movieapplication.ui.movie.PreviewMovieViewModel
import kotlinx.android.synthetic.main.activity_view_more_movie.*

class ViewMoreMovieActivity : AppCompatActivity(), MovieClickCallback {

    private lateinit var viewModel: PreviewMovieViewModel

    companion object {
        const val EXTRA_TYPE_MOVIE = "EXTRA_TYPE_MOVIE"
        const val UPCOMING = "UPCOMING"
        const val POPULAR = "POPULAR"
        const val NOW_PLAYING = "NOW_PLAYING"
        const val RELEASE_NOW = "RELEASE_NOW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more_movie)

        val extras = intent.extras
        var title: String? = null
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[PreviewMovieViewModel::class.java]
        if (extras != null) {
            when (extras.getString(EXTRA_TYPE_MOVIE)) {
                UPCOMING -> {
                    setUpUpcomingMovie()
                    title = resources.getString(R.string.text_upcoming_movies)
                }
                POPULAR -> {
                    setUpPopularMovie()
                    title = resources.getString(R.string.text_popular)
                }
                NOW_PLAYING -> {
                    setUpNowPlayingMovie()
                    title = resources.getString(R.string.text_now_playing)
                }
                RELEASE_NOW -> {
                    setUpReleaseNowMovie()
                    title = resources.getString(R.string.text_release_now)
                }
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title

    }

    private fun setUpReleaseNowMovie() {
        val releaseNowAdapter = ViewMoreMovieAdapter(this)
        viewModel.setReleaseNowMovie()
        viewModel.getDataReleaseNowMovie().observe(this, Observer { releaseNowMovies ->
            if (releaseNowMovies != null) {
                pg_view_more_movie.visibility = View.GONE
                releaseNowAdapter.setMovies(releaseNowMovies)
                releaseNowAdapter.notifyDataSetChanged()
            } else {
                pg_view_more_movie.visibility = View.GONE
            }
        })

        with(rv_movie_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreMovieActivity)
            setHasFixedSize(true)
            adapter = releaseNowAdapter
        }
    }

    private fun setUpUpcomingMovie() {
        val upcomingAdapter = ViewMoreMovieAdapter(this)
        viewModel.setUpcomingMovies()
        viewModel.getDataUpcomingMovie().observe(this, Observer { upcomingMovies ->
            if (upcomingMovies != null) {
                pg_view_more_movie.visibility = View.GONE
                upcomingAdapter.setMovies(upcomingMovies)
                upcomingAdapter.notifyDataSetChanged()
            }
        })

        with(rv_movie_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreMovieActivity)
            setHasFixedSize(true)
            adapter = upcomingAdapter
        }

    }

    private fun setUpPopularMovie() {
        val upcomingAdapter = ViewMoreMovieAdapter(this)
        viewModel.setPopularMovies()
        viewModel.getDataPopularMovie().observe(this, Observer { popularMovies ->
            if (popularMovies != null) {
                pg_view_more_movie.visibility = View.GONE
                upcomingAdapter.setMovies(popularMovies)
                upcomingAdapter.notifyDataSetChanged()
            }
        })

        with(rv_movie_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreMovieActivity)
            setHasFixedSize(true)
            adapter = upcomingAdapter
        }

    }

    private fun setUpNowPlayingMovie() {
        val upcomingAdapter = ViewMoreMovieAdapter(this)
        viewModel.setNowPlayingMovies()
        viewModel.getDataNowPlayingMovie().observe(this, Observer { nowPlayingMovies ->
            if (nowPlayingMovies != null) {
                pg_view_more_movie.visibility = View.GONE
                upcomingAdapter.setMovies(nowPlayingMovies)
                upcomingAdapter.notifyDataSetChanged()
            }
        })

        with(rv_movie_view_more) {
            layoutManager = LinearLayoutManager(this@ViewMoreMovieActivity)
            setHasFixedSize(true)
            adapter = upcomingAdapter
        }

    }

    override fun onMovieClicked(data: Movie) {
//        val intent = Intent(this@ViewMoreMovieActivity, DetailMovieActivity::class.java).apply {
//            putExtra(DetailMovieActivity.EXTRA_DATA,data)
//        }
//        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
