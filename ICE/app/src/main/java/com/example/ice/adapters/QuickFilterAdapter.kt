package com.example.ice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ice.R
import com.example.ice.models.CustomFilter

class QuickFilterAdapter(private val context: Context)
    : RecyclerView.Adapter<QuickFilterAdapter.ViewHolder>() {

    var customfilters = mutableListOf<CustomFilter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_filter_quick_item, parent, false)
        return ViewHolder(view)
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int = customfilters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.bind(customfilters[position])
    }

    fun setItem(items: MutableList<CustomFilter>) {
        if (!items.isNullOrEmpty()) {
            customfilters = items
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.quick_filter_item_title)
        private val image: ImageView = itemView.findViewById(R.id.quick_filter_item_image)
        private val selected: ConstraintLayout = itemView.findViewById(R.id.quick_filter_item_selected)

        fun bind(item: CustomFilter) {
            title.text = item.name
            Glide.with(itemView).load(item.image).into(image)
            when (item.isSelected) {
                true -> {
                    selected.visibility = View.VISIBLE
                }
                false -> {
                    selected.visibility = View.GONE
                }
            }
        }
    }


}