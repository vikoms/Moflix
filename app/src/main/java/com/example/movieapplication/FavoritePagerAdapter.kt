package com.example.movieapplication

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.movieapplication.ui.favoritemovie.FavoriteMovieFragment
import com.example.movieapplication.ui.favoriteseries.FavoriteSeriesFragment

class FavoritePagerAdapter(private val context: Context, private val fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    @StringRes
    private val TAB_TITLES =
        intArrayOf(R.string.text_movies, R.string.text_series)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FavoriteMovieFragment()
            1 -> fragment = FavoriteSeriesFragment()
        }

        return fragment as Fragment
    }

    override fun getCount(): Int = TAB_TITLES.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }
}