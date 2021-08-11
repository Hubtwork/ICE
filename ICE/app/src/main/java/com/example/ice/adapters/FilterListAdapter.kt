package com.example.ice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ice.R
import com.example.ice.models.CustomFilter

class FilterListAdapter(private val context: Context)
    : RecyclerView.Adapter<FilterListAdapter.ViewHolder>() {

    var customfilters = mutableListOf<CustomFilter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_filter_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = customfilters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(customfilters[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.view_filter_item_title)
        private val image: ImageView = itemView.findViewById(R.id.view_filter_item_image)

        fun bind(item: CustomFilter) {
            title.text = item.name
            Glide.with(itemView).load(item.image).into(image)
        }
    }


}