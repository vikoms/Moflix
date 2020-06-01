package com.example.movieapplication.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "EXTRA_TYPE"
    }

    private lateinit var type: String
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchViewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val bundle = intent.extras
        searchViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SearchViewModel::class.java]
        searchAdapter = SearchAdapter()
        if (bundle != null) {
            type = intent.getStringExtra(EXTRA_TYPE)
            edt_search.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    showLoading(true)
                    val query = edt_search.text.toString().trim()
                    if (type == "movie") {
                        searchMovie(query)
                    } else {
                        searchSeries(query)

                    }
                }

            })
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.search)

    }

    private fun searchSeries(query: String) {
        searchViewModel.searchSeries(query)
        searchViewModel.getSearchSeries().observe(this, Observer { series ->
            if (series != null) {
                searchAdapter.setData(type, null, series)
                showLoading(false)
            } else {
                showLoading(false)
            }
        })

        setUpRecyclerView()
    }

    private fun searchMovie(query: String) {
        searchViewModel.searchMovie(query)
        searchViewModel.getSearchMovie().observe(this, Observer { movies ->
            if (movies != null) {
                searchAdapter.setData(type, movies, null)
                showLoading(false)
            }
        })

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        with(rv_search) {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
            setHasFixedSize(true)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pg_search.visibility = View.VISIBLE
            rv_search.visibility = View.GONE
        } else {
            pg_search.visibility = View.GONE
            rv_search.visibility = View.VISIBLE
        }
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
