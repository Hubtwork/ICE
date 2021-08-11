package com.example.ice.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ice.R
import com.example.ice.adapters.FilterListAdapter
import com.example.ice.models.Component
import com.example.ice.models.CustomFilter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        initRecyclerView()
        initCancelButton()
        initAddFilterButton()
    }

    private fun initRecyclerView() {
        // load RecyclerView and LayoutManager, Adapter Setting
        filterList = findViewById(R.id.list_filter)
        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        filterList.layoutManager = gridLayoutManager
        filterListAdapter = FilterListAdapter(this)
        // load dummies
        filterListAdapter.customfilters = loadDummies()
        filterListAdapter.notifyDataSetChanged()

        filterList.adapter = filterListAdapter
    }

    // Dummy data for ui checking
    private fun loadDummies(): MutableList<CustomFilter> {
        var dummyFilterLists = mutableListOf<CustomFilter>()
        dummyFilterLists.add(CustomFilter("Garlic-", R.drawable.garlic, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Eggs-", R.drawable.eggs, arrayListOf()))
        return dummyFilterLists
    }

    private fun initCancelButton() {
        cancelButton = findViewById(R.id.setting_main_button_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent()
            var componentList = arrayListOf<String>("Ractose", "Protein")
            intent.putExtra("components", componentList)
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
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val newFilterFromIntent = data!!.getSerializableExtra("filter")
                    if (newFilterFromIntent != null) {
                        val newFilter: CustomFilter = newFilterFromIntent as CustomFilter
                        filterListAdapter.customfilters.add(newFilter)
                        filterListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}