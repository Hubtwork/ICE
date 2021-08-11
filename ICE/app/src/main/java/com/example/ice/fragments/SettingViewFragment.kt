package com.example.ice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ice.R
import kotlin.random.Random

class SettingViewFragment
    : Fragment()
{
    private lateinit var filterList: ListView
    private lateinit var filterListAdapter: ArrayAdapter<String>

    var filterItems: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_setting_main, container, false)

        filterItems.add("Filter 1")
        filterItems.add("Filter 2")
        filterItems.add("Filter 3")

        filterListAdapter = ArrayAdapter<String>(
            activity!!,
            android.R.layout.simple_list_item_single_choice,
            filterItems
        )

        filterList = layout.findViewById(R.id.list_filter)
        filterList.adapter = filterListAdapter
        filterList.choiceMode = ListView.CHOICE_MODE_SINGLE

        return layout
    }

    fun filterButtonClick(view: View) {
        when (view.id) {
            R.id.button_add_filter -> {
                val filterString = "Filter " + Random(10000).nextInt().toString()
                filterItems.add(filterString)
            }
            R.id.button_delete_filter -> {
                val pos: Int = filterList.checkedItemPosition // 현재 선택된 항목의 첨자(위치값) 얻기
                if (pos != ListView.INVALID_POSITION) {     // 선택된 항목이 있으면
                    filterItems.removeAt(pos) // items 리스트에서 해당 위치의 요소 제거
                    filterList.clearChoices() // 선택 해제
                    filterListAdapter.notifyDataSetChanged()
                    // 어답터와 연결된 원본데이터의 값이 변경된을 알려 리스트뷰 목록 갱신
                }
            }
        }
    }

}