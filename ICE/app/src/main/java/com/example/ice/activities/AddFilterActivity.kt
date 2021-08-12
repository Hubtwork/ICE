package com.example.ice.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ice.R
import com.example.ice.adapters.FilterComponentAdapter
import com.example.ice.adapters.FilterListAdapter
import com.example.ice.models.Component
import com.example.ice.models.CustomFilter
import com.example.ice.models.Filters

class AddFilterActivity
    : AppCompatActivity() {

    companion object {
        private const val TAG = "AddFilterActivity"
    }

    private lateinit var filterList: RecyclerView
    private lateinit var filterComponentAdapter: FilterComponentAdapter

    private lateinit var filterName: TextView

    private lateinit var cancelButton: ImageView
    private lateinit var saveButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_filter_main)

        initRecyclerView()
        initCancelButton()
        initSaveButton()

        loadFilterInfo()
    }

    private fun loadFilterInfo() {
        val filter = intent.getSerializableExtra("filter") as CustomFilter
        filterName = findViewById(R.id.edit_text_filter_name)

        filter.components = arrayListOf(

            Component("Lecithine", R.drawable.chemistry, "Component"),
            Component("Mayonnaise", R.drawable.chemistry, "Component"),
            Component("Lipovitellin", R.drawable.chemistry, "Component"),
            Component("Phospholipids", R.drawable.chemistry, "Component"),
            Component("Protein Powder", R.drawable.chemistry, "Component"),
            Component("Globulin", R.drawable.chemistry, "Component"),
            Component("lysozyme", R.drawable.chemistry, "Component"),
            Component("Ovosucrol", R.drawable.chemistry, "Component"),
            Component("Ovotransfarine", R.drawable.chemistry, "Component"),
            Component("Phosvitine", R.drawable.chemistry, "Component"),
            Component("Albumin", R.drawable.chemistry, "Component"),
            Component("Avidin", R.drawable.chemistry, "Component"),
            Component("Conalbumin", R.drawable.chemistry, "Component"),

            Component("butter", R.drawable.chemistry, "Component"),
            Component("butter milk", R.drawable.chemistry, "Component"),
            Component("casein", R.drawable.chemistry, "Component"),
            Component("cheese", R.drawable.chemistry, "Component"),
            Component("curd", R.drawable.chemistry, "Component"),
            Component("cream", R.drawable.chemistry, "Component"),
            Component("custard", R.drawable.chemistry, "Component"),
            Component("lactobulin", R.drawable.chemistry, "Component"),
            Component("margarine", R.drawable.chemistry, "Component"),
            Component("sweetened", R.drawable.chemistry, "Component"),


            Component("wheat", R.drawable.chemistry, "Component"),
            Component("food starch", R.drawable.chemistry, "Component"),
            Component("soft flour", R.drawable.chemistry, "Component"),
            Component("hydrolyzed vegetable protein", R.drawable.chemistry, "Component"),
            Component("grain mixture", R.drawable.chemistry, "Component"),
            Component("grain protein", R.drawable.chemistry, "Component"),
            Component("glutenin", R.drawable.chemistry, "Component"),
            Component("spelt", R.drawable.chemistry, "Component"),
            Component("malt", R.drawable.chemistry, "Component"),
            Component("semolina", R.drawable.chemistry, "Component")

        )
        filterName.text = filter.name
        filterComponentAdapter.filterComponents = filter.components
        filterComponentAdapter.notifyDataSetChanged()


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
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun initSaveButton() {
        saveButton = findViewById(R.id.add_filter_main_button_save)
        saveButton.setOnClickListener {
            val intent = Intent()
            val newFilter = CustomFilter("newFilter", R.drawable.chemistry, false, arrayListOf())
            intent.putExtra("filter", newFilter)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}