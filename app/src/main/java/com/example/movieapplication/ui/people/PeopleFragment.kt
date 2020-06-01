package com.example.movieapplication.ui.people

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import kotlinx.android.synthetic.main.fragment_people.*

/**
 * A simple [Fragment] subclass.
 */
class PeopleFragment : Fragment() {

    private lateinit var viewModel: PeopleViewModel
    private lateinit var peopleAdapter: PeopleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[PeopleViewModel::class.java]
            peopleAdapter = PeopleAdapter()

            edt_search_people.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    var query = edt_search_people.text.toString().trim()
                    if (!query.isEmpty()) {
                        setUpSearchPeople(s.toString())
                        return
                    }
                    setUpPeopleRecyclerView()

                }

            })

        }
        setUpPeopleRecyclerView()
    }

    private fun setUpSearchPeople(querySearch: String) {
        viewModel.searchPeople(querySearch)
        viewModel.getPopularPeople().observe(viewLifecycleOwner, Observer { peoples ->
            if (peoples != null) {
                pg_people.visibility = View.GONE
                peopleAdapter.setPeople(peoples)
                peopleAdapter.notifyDataSetChanged()
            } else {
                pg_people.visibility = View.GONE
            }
        })

        with(rv_people) {
            layoutManager = LinearLayoutManager(activity)
            adapter = peopleAdapter
            setHasFixedSize(true)
        }
    }

    private fun setUpPeopleRecyclerView() {
        viewModel.setPopularPeople()
        viewModel.getPopularPeople().observe(this, Observer { peoples ->
            if (peoples != null) {
                pg_people.visibility = View.GONE
                peopleAdapter.setPeople(peoples)
                peopleAdapter.notifyDataSetChanged()
            } else {
                pg_people.visibility = View.GONE
            }
        })

        with(rv_people) {
            layoutManager = LinearLayoutManager(activity)
            adapter = peopleAdapter
            setHasFixedSize(true)
        }
    }

}
