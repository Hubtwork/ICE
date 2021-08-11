package com.example.ice.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ice.R
import com.example.ice.adapters.FilterComponentAdapter
import com.example.ice.adapters.FilterListAdapter
import com.example.ice.models.CustomFilter

class AddFilterActivity
    : AppCompatActivity() {

    companion object {
        private const val TAG = "AddFilterActivity"
    }

    private lateinit var filterList: RecyclerView
    private lateinit var filterComponentAdapter: FilterComponentAdapter

    private lateinit var cancelButton: ImageView
    private lateinit var saveButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_filter_main)

        initRecyclerView()
        initCancelButton()
        initSaveButton()
    }

    private fun initRecyclerView() {
        // load RecyclerView and LayoutManager, Adapter Setting
        filterList = findViewById(R.id.add_filter_list)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        filterList.layoutManager = linearLayoutManager
        filterComponentAdapter = FilterComponentAdapter(this)
        filterList.adapter = filterComponentAdapter
    }

    private fun initCancelButton() {
        cancelButton = findViewById(R.id.add_filter_main_button_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initSaveButton() {
        saveButton = findViewById(R.id.add_filter_main_button_save)
        saveButton.setOnClickListener {
            val intent = Intent()
            val newFilter = CustomFilter("newFilter", R.drawable.chemistry, filterComponentAdapter.filterComponents.toCollection(ArrayList()))
            intent.putExtra("filter", newFilter)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}