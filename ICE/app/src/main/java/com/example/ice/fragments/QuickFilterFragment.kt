package com.example.ice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ice.R
import com.example.ice.adapters.QuickFilterAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QuickFilterFragment(var adapter: QuickFilterAdapter)
    : BottomSheetDialogFragment() {

    private lateinit var quickFilterList: RecyclerView


    private lateinit var closeButton: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_quick_filter,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quickFilterList = view.findViewById(R.id.view_filter_quick_list)
        quickFilterList.adapter = adapter

        adapter.setItemClickListener( object : QuickFilterAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                val originSelected = adapter.customfilters[position].isSelected
                adapter.customfilters[position].isSelected = !originSelected
                adapter.notifyDataSetChanged()
            }
        })

        closeButton = view.findViewById(R.id.quick_filter_button_cancel)
        closeButton.setOnClickListener {
            this.dismiss()
        }
    }
}