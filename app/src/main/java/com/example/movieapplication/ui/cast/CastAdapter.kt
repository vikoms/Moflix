package com.example.movieapplication.ui.cast

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapplication.R
import com.example.movieapplication.entity.People
import com.example.movieapplication.ui.detailpeople.DetailPeopleActivity
import kotlinx.android.synthetic.main.item_people_detail.view.*

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val cast = ArrayList<People>()
    fun setData(cast: ArrayList<People>) {
        this.cast.clear()
        this.cast.addAll(cast)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_people_detail, parent, false)
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int = cast.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: People) {
            with(itemView) {
                Glide.with(context)
                    .load(data.profilePath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_person_pin)
                            .error(R.drawable.ic_person_pin)
                    )
                    .centerCrop()
                    .into(img_item_pople_profile_detail)
                tv_item_people_name_detail.text = data.name
                tv_item_people_charanddate_detail.text = data.charCredits

                setOnClickListener {
                    val intent = Intent(context, DetailPeopleActivity::class.java).apply {
                        putExtra(DetailPeopleActivity.EXTRA_DATA, data)
                    }
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        img_item_pople_profile_detail,
                        context.resources.getString(R.string.text_transition_poster_people)
                    )

                    context.startActivity(intent, options.toBundle())
                }
            }

        }
    }
}