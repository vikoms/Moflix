package com.example.movieapplication.ui.detailpeople

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.People
import kotlinx.android.synthetic.main.activity_detail_people.*

class DetailPeopleActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    lateinit var people: People
    lateinit var viewModel: DetailPeopleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_people)

        people = intent.getParcelableExtra(EXTRA_DATA) as People
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailPeopleViewModel::class.java]
        if (people != null) {
            Glide.with(this).load(people.profilePath).apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
            ).into(img_detail_people)
            viewModel.setData(people.id.toString())
            updateUI()
            setUpMovies()
            setUpSeries()
        }

        back_people_detail.setOnClickListener(this)
    }

    private fun setUpSeries() {
        val adapterSeries = PeopleSeriesAdapter()
        viewModel.setSeries()
        viewModel.getDataSeries().observe(this, Observer { series ->
            if (series != null) {
                pg_rv_series_people.visibility = View.GONE
                adapterSeries.setData(series)
            }
        })

        with(rv_series_detail_people) {
            layoutManager = LinearLayoutManager(
                this@DetailPeopleActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = adapterSeries
            setHasFixedSize(true)
        }
    }

    private fun setUpMovies() {
        val moviePeopleAdapter = PeopleMovieAdapter()
        viewModel.setMovies()
        viewModel.getDataMovies().observe(this, Observer { movies ->
            if (movies != null) {
                pg_rv_movie_people.visibility = View.GONE
                moviePeopleAdapter.setData(movies)
            } else {
                pg_rv_movie_people.visibility = View.GONE
            }
        })

        with(rv_movie_detail_people) {
            layoutManager = LinearLayoutManager(
                this@DetailPeopleActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = moviePeopleAdapter
            setHasFixedSize(true)
        }
    }

    private fun updateUI() {
        viewModel.setPeople()
        viewModel.getData().observe(this, Observer { people ->
            if (people != null) {
                showLoading(false)
                tv_place_birth_detail_people.text =
                    people.placeOfBirth ?: resources.getString(R.string.data_not_found)
                tv_name_detail_people.text =
                    people.name ?: resources.getString(R.string.data_not_found)
                tv_gender_detail_people.text =
                    if (people.gender == "Female") resources.getString(R.string.text_female) else if (people.gender == "Male") resources.getString(
                        R.string.text_male
                    ) else resources.getString(R.string.data_not_found)
                tv_birthday_detail_people.text =
                    people.birthday ?: resources.getString(R.string.data_not_found)
                tv_department_detail_peole.text =
                    people.departement ?: resources.getString(R.string.data_not_found)
                tv_biography_detail_people.text =
                    people.biography ?: resources.getString(R.string.data_not_found)
            } else {
                showLoading(false)
            }
        })
    }

    fun showLoading(state: Boolean) {
        if (state) {
            pg_detail_people.visibility = View.VISIBLE
        } else {
            pg_detail_people.visibility = View.GONE
        }
    }

    override fun onClick(item: View) {
        if (item.id == R.id.back_people_detail) {
            finish()
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
