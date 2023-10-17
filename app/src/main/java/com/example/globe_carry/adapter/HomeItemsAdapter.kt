package com.example.globe_carry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R


class HomeItemsAdapter (private val data: List<HomeItems>) :
    RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeItemNo: TextView = itemView.findViewById(R.id.homeItemNo)
        val homeItemArea: TextView = itemView.findViewById(R.id.homeItemArea)
        val homeItemType: TextView = itemView.findViewById(R.id.homeItemType)
        val homeItemWeight1: TextView = itemView.findViewById(R.id.homeItemWeight1)
        val homeItemDimensions1: TextView = itemView.findViewById(R.id.homeItemDimensions1)
        val homeItemCharge1: TextView = itemView.findViewById(R.id.homeItemCharge1)
        val homeItemDate1: TextView = itemView.findViewById(R.id.homeItemDate1)
        val homeItemPostDate: TextView = itemView.findViewById(R.id.homeItemPostDate)
        val homeItemUrgent: TextView = itemView.findViewById(R.id.homeItemUrgent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.homeItemNo.text = item.no
        holder.homeItemArea.text = "${item.city} , ${item.country}"
        holder.homeItemType.text = item.type
        holder.homeItemWeight1.text = item.weight
        holder.homeItemDimensions1.text = item.dimensions
        holder.homeItemCharge1.text = item.charge.toString()
        holder.homeItemDate1.text = item.expectedDate
        holder.homeItemPostDate.text = "${item.time} , ${item.date}"
        val urgent= item.urgent

        if(urgent!!){
            holder.homeItemUrgent.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}