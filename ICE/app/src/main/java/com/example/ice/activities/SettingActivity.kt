package com.example.ice.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ice.R
import com.example.ice.adapters.FilterListAdapter

class SettingActivity
    : AppCompatActivity()
{

    companion object {
        private const val TAG = "SettingActivity"
    }

    private lateinit var filterList: RecyclerView
    private lateinit var cancelButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_main)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        // load RecyclerView and LayoutManager, Adapter Setting
        filterList = findViewById(R.id.list_filter)
        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        filterList.layoutManager = gridLayoutManager
        var filterListAdapter = FilterListAdapter(this)
        filterList.adapter = filterListAdapter
    }

    private fun initCancelButton() {
        cancelButton = findViewById(R.id.setting_main_button_cancel)
        cancelButton.setOnClickListener {

        }
    }


}