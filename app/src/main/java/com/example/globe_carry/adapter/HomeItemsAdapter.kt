package com.example.globe_carry.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.DetailActivity
import com.example.globe_carry.HomeItemImageSingleton
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
//import com.example.globe_carry.post


class HomeItemsAdapter (private var data: List<HomeItems>) :
    RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {
    private var arrowImageView : ImageView? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeItemNo: TextView = itemView.findViewById(R.id.homeItemNo)
        val homeItemCity: TextView = itemView.findViewById(R.id.homeItemCity)
        val homeItemCountry: TextView = itemView.findViewById(R.id.homeItemCountry)
        val homeItemType: TextView = itemView.findViewById(R.id.homeItemType)
        val homeItemWeight1: TextView = itemView.findViewById(R.id.homeItemWeight1)
        val homeItemDimensions1: TextView = itemView.findViewById(R.id.homeItemDimensions1)
        val homeItemCharge1: TextView = itemView.findViewById(R.id.homeItemCharge1)
        val homeItemDlvryDate1: TextView = itemView.findViewById(R.id.homeItemDlvryDate1)
        val homeItemPostDate: TextView = itemView.findViewById(R.id.homeItemPostDate)
        val homeItemUrgent: TextView = itemView.findViewById(R.id.homeItemUrgent)
        val homeItemdlvryAdrs: TextView = itemView.findViewById(R.id.viewDlvryAdrs)
        val homeSpecialIns: TextView = itemView.findViewById(R.id.viewSpclIns)
        val homeRecName: TextView = itemView.findViewById(R.id.homeRecName)
        val homeRecNum: TextView = itemView.findViewById(R.id.homeRecNum)
//        val homeCusNum: TextView = itemView.findViewById(R.id.cusNum)
//        val homeCusName: TextView = itemView.findViewById(R.id.cusName)
        val homeValue: TextView = itemView.findViewById(R.id.homeValue)
        val homeitemContent: TextView = itemView.findViewById(R.id.itemContent)
        val homeItemListLinear1: RelativeLayout = itemView.findViewById(R.id.homeItemListLinear1)
        // Change this to RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.homeItemNo.text = item.id
        holder.homeItemCity.text = item.city
        holder.homeItemCountry.text = item.country
        holder.homeItemType.text = item.category
        holder.homeRecName.text = item.recipient
        holder.homeRecNum.text = item.rcptContactNo
        holder.homeItemdlvryAdrs.text = item.dlvryAddress
        holder.homeSpecialIns.text = item.instructions
        holder.homeValue.text = item.value.toString()
        holder.homeitemContent.text = item.content
        holder.homeItemWeight1.text = item.weight
        holder.homeItemDimensions1.text = item.dimension
        holder.homeItemCharge1.text = item.ttlCharge.toString()
        holder.homeItemDlvryDate1.text = item.dlvryDate
        holder.homeItemPostDate.text = item.createdDate

       // Log.d("Image", item.image.toString())

        //Log.d("Image", item.image.toString())


        val urgent = item.urgent

        if (urgent == true) {
            holder.homeItemUrgent.visibility = View.VISIBLE
        } else {
            holder.homeItemUrgent.visibility = View.GONE // Hide the view when not urgent
        }

        holder.homeItemListLinear1.setOnClickListener {
            // Handle click event here
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("postId", item.id)
            intent.putExtra("urgent", item.urgent)
            intent.putExtra("city", item.city)
            intent.putExtra("country", item.country)
            intent.putExtra("category", item.category)
            intent.putExtra("weight", item.weight)
            intent.putExtra("dimensions", item.dimension)
            intent.putExtra("ttlCharge", item.ttlCharge)
            intent.putExtra("dlvryDate", item.dlvryDate)
            intent.putExtra("value", item.value)
            intent.putExtra("createdDate", item.createdDate)
            //intent.putExtra("image", item.image)
            intent.putExtra("dlvryAddress", item.dlvryAddress)
            intent.putExtra("instructions", item.instructions)
            intent.putExtra("recipient", item.recipient)
            intent.putExtra("rcptContactNo", item.rcptContactNo)
            intent.putExtra("content", item.content)
            intent.putExtra("createdBy", item.createdBy)
            intent.putExtra("createdUserName", item.createdUserName)
            intent.putExtra("createdUserContactNo", item.createdUserContactNo)

            context.startActivity(intent)

        }


//        Log.d("HomeItemsAdapter", "Item ID: ${item.id}")
//        Log.d("HomeItemsAdapter", "City: ${item.city}")
//        Log.d("HomeItemsAdapter", "Country: ${item.country}")
//        Log.d("HomeItemsAdapter", "Category: ${item.category}")
//        Log.d("HomeItemsAdapter", "Weight: ${item.weight}")
//        Log.d("HomeItemsAdapter", "Dimensions: ${item.dimension}")
//        Log.d("HomeItemsAdapter", "Total Charge: ${item.ttlCharge}")
//        Log.d("HomeItemsAdapter", "Delivery Date: ${item.dlvryDate}")
//        Log.d("HomeItemsAdapter", "Created Date: ${item.createdDate}")
//        Log.d("HomeItemsAdapter", "recipient: ${item.recipient}")
//        Log.d("HomeItemsAdapter", "Urgent: $urgent")
    }

    override fun getItemCount(): Int {
        return data.size
    }



}