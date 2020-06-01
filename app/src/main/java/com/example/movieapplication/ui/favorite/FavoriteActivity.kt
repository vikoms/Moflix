package com.example.movieapplication.ui.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapplication.FavoritePagerAdapter
import com.example.movieapplication.R
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.text_favorite)
        val favoritePagerAdapter = FavoritePagerAdapter(this, supportFragmentManager)
        tabs.setupWithViewPager(view_pager)
        view_pager.adapter = favoritePagerAdapter
        tabs.getTabAt(0)?.setIcon(R.drawable.ic_movie)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_local_movies)

    }
}
