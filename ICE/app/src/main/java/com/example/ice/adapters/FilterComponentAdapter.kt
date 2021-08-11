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
import com.example.ice.models.Component
import com.example.ice.models.CustomFilter

class FilterComponentAdapter(private val context: Context)
    : RecyclerView.Adapter<FilterComponentAdapter.ViewHolder>() {

    var filterComponents = mutableListOf<Component>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_filter_component_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterComponents.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filterComponents[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.view_filter_component_item_title)
        private val image: ImageView = itemView.findViewById(R.id.view_filter_component_item_image)
        private val type: TextView = itemView.findViewById(R.id.view_filter_component_item_type)

        fun bind(item: Component) {
            type.text = item.type
            title.text = item.name
            Glide.with(itemView).load(item.image).into(image)
        }
    }

}