package com.example.ice.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ice.R
import com.example.ice.adapters.FilterListAdapter
import com.example.ice.models.CustomFilter
import com.example.ice.models.Filters
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class SettingActivity
    : AppCompatActivity()
{

    companion object {
        private const val TAG = "SettingActivity"
    }

    private lateinit var filterList: RecyclerView
    private lateinit var filterListAdapter: FilterListAdapter
    private lateinit var cancelButton: ImageView
    private lateinit var addFilterButton: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_main)

        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)

        initRecyclerView()
        initCancelButton()
        initAddFilterButton()

        loadFilters()
    }

    private fun loadFilters() {
        val filters = intent.getSerializableExtra("filters") as Filters
        filterListAdapter.customfilters = filters.filters
        filterListAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        // load RecyclerView and LayoutManager, Adapter Setting
        filterList = findViewById(R.id.list_filter)
        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        filterList.layoutManager = gridLayoutManager
        filterListAdapter = FilterListAdapter(this)

        filterList.adapter = filterListAdapter
    }

    private fun initCancelButton() {
        cancelButton = findViewById(R.id.setting_main_button_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent()
            var filters = filterListAdapter.customfilters
            intent.putExtra("filters", Filters(filters))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initAddFilterButton() {
        addFilterButton = findViewById(R.id.button_add_filter)
        addFilterButton.setOnClickListener {
            openAddFilterIntent()
        }
    }

    private fun openAddFilterIntent() {
        // Success Code = 100
        val intent = Intent(this, AddFilterActivity::class.java)
        intent.putExtra("filter", CustomFilter("", R.drawable.chemistry, false, arrayListOf()))
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val newFilterFromIntent = data!!.getSerializableExtra("filter") as CustomFilter
                    if (newFilterFromIntent != null) {
                        filterListAdapter.customfilters.add(newFilterFromIntent)
                        filterListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}