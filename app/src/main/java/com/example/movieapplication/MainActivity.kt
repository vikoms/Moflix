package com.example.movieapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapplication.ui.favorite.FavoriteActivity
import com.example.movieapplication.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)
        val sectionsPagerAdapter = MainPagerAdapter(this, supportFragmentManager)
        tabs.setupWithViewPager(view_pager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.getTabAt(0)?.setIcon(R.drawable.ic_movie)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_local_movies)
        tabs.getTabAt(2)?.setIcon(R.drawable.ic_person)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true

        }
    }

}
