package com.example.globe_carry.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
import com.example.globe_carry.adapter.HomeItemsAdapter
import java.math.BigDecimal

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeItemsList = listOf(
            HomeItems(
                "001",
                "New York",
                "USA",
                "Type A",
                "10 kg",
                "20x20x20 cm",
                BigDecimal("50.00"),
                "2023-10-01",
                "10:00 AM",
                "2023-10-01",
                true
            ),
            HomeItems(
                "002",
                "Los Angeles",
                "USA",
                "Type B",
                "8 kg",
                "15x15x15 cm",
                BigDecimal("40.00"),
                "2023-10-02",
                "11:30 AM",
                "2023-10-02",
                false
            ),
            HomeItems(
                "003",
                "London",
                "UK",
                "Type C",
                "12 kg",
                "25x25x25 cm",
                BigDecimal("60.00"),
                "2023-10-03",
                "12:45 PM",
                "2023-10-03",
                true
            ),
            HomeItems(
                "004",
                "Paris",
                "France",
                "Type A",
                "9 kg",
                "18x18x18 cm",
                BigDecimal("45.00"),
                "2023-10-04",
                "2:15 PM",
                "2023-10-04",
                false
            ),
            HomeItems(
                "005",
                "Tokyo",
                "Japan",
                "Type B",
                "7 kg",
                "12x12x12 cm",
                BigDecimal("35.00"),
                "2023-10-05",
                "3:30 PM",
                "2023-10-05",
                true
            )
        )
        // Set the retrieved data in the RecyclerView
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.homeRecyclerView)
        val adapter = HomeItemsAdapter(homeItemsList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
}