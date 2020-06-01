package com.example.movieapplication.ui.people

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.People
import com.example.movieapplication.ui.detailpeople.DetailPeopleActivity
import kotlinx.android.synthetic.main.item_people.view.*

class PeopleAdapter :
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    private var peopleList = ArrayList<People>()

    fun setPeople(peoples: ArrayList<People>) {
        if (peoples == null) return
        peopleList.clear()
        peopleList.addAll(peoples)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return PeopleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {

        return peopleList.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(peopleList[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)

    }


    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: People) {
            with(itemView) {
                Glide.with(context)
                    .load(data.profilePath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_person_pin)
                            .error(R.drawable.ic_person_pin)
                    )
                    .centerCrop()
                    .into(item_img_people)
                tv_item_people_department.text = data.departement
                tv_item_people_name.text = data.name

                setOnClickListener {
                    val intent = Intent(context, DetailPeopleActivity::class.java).apply {
                        putExtra(DetailPeopleActivity.EXTRA_DATA, data)
                    }
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        item_img_people,
                        context.resources.getString(R.string.text_transition_poster_people)
                    )

                    context.startActivity(intent, options.toBundle())
                }
            }
        }
    }
}