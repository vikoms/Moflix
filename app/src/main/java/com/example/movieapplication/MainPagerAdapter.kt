package com.example.movieapplication

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.movieapplication.ui.movie.MovieFragment
import com.example.movieapplication.ui.people.PeopleFragment
import com.example.movieapplication.ui.series.SeriesFragment

class MainPagerAdapter(private val context: Context, private val fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES =
        intArrayOf(R.string.text_movies, R.string.text_series, R.string.text_artist)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFragment()
            1 -> fragment = SeriesFragment()
            2 -> fragment = PeopleFragment()
        }

        return fragment as Fragment
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }


}