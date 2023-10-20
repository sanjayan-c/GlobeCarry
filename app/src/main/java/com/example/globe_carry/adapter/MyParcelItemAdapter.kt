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
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R

class MyParcelItemAdapter (private var data: List<HomeItems>) :
        RecyclerView.Adapter<MyParcelItemAdapter.MyParcelViewHolder>() {


    private var arrowImageView : ImageView? = null
    inner class MyParcelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myParcelItemNo: TextView = itemView.findViewById(R.id.myParcelItemNo)
        val myParcelItemCity: TextView = itemView.findViewById(R.id.myParcelItemCity)
        val myParcelItemCountry: TextView = itemView.findViewById(R.id.myParcelItemCountry)
        val myParcelItemType: TextView = itemView.findViewById(R.id.myParcelItemType)
        val myParcelItemWeight1: TextView = itemView.findViewById(R.id.myParcelItemWeight1)
        val myParcelItemDimensions1: TextView = itemView.findViewById(R.id.myParcelItemDimensions1)
        val myParcelItemCharge1: TextView = itemView.findViewById(R.id.myParcelItemCharge1)
        val myParcelItemDlvryDate1: TextView = itemView.findViewById(R.id.myParcelItemDlvryDate1)
        val myParcelItemPostDate: TextView = itemView.findViewById(R.id.myParcelItemPostDate)
        val myParcelItemUrgent: TextView = itemView.findViewById(R.id.myParcelItemUrgent)
        val myParcelDlvryAdrs: TextView = itemView.findViewById(R.id.myParcelDlvryAdrs)
        val myParcelItemSpclIns: TextView = itemView.findViewById(R.id.myParcelItemSpclIns)
        val myParcelItemRecName: TextView = itemView.findViewById(R.id.myParcelItemRecName)
        val myParcelItemRecNum: TextView = itemView.findViewById(R.id.myParcelItemRecNum)
        val myParcelcusNum: TextView = itemView.findViewById(R.id.myParcelcusNum)
        val myParcelcusName: TextView = itemView.findViewById(R.id.myParcelcusName)
        val myParcelItemValue: TextView = itemView.findViewById(R.id.myParcelItemValue)
        val myParcelitemContent: TextView = itemView.findViewById(R.id.myParcelitemContent)
        val myParcelItemListLinear1: RelativeLayout = itemView.findViewById(R.id.myParcelItemListLinear1)
        // Change this to RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyParcelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myparcel_item_list, parent, false)
        return MyParcelViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyParcelViewHolder, position: Int) {
        val item = data[position]

        holder.myParcelItemNo.text = item.id
        holder.myParcelItemCity.text = item.city
        holder.myParcelItemCountry.text = item.country
        holder.myParcelItemType.text = item.category
        holder.myParcelItemRecName.text = item.recipient
        holder.myParcelItemRecNum.text = item.rcptContactNo
        holder.myParcelDlvryAdrs.text = item.dlvryAddress
        holder.myParcelItemSpclIns.text = item.instructions
        holder.myParcelItemValue.text = item.value.toString()
        holder.myParcelitemContent.text = item.content
        holder.myParcelItemWeight1.text = item.weight
        holder.myParcelItemDimensions1.text = item.dimension
        holder.myParcelItemCharge1.text = item.ttlCharge.toString()
        holder.myParcelItemDlvryDate1.text = item.dlvryDate
        holder.myParcelItemPostDate.text = item.createdDate
        Log.d("Image", item.image.toString())

        val urgent = item.urgent

        if (urgent == true) {
            holder.myParcelItemUrgent.visibility = View.VISIBLE
        } else {
            holder.myParcelItemUrgent.visibility = View.GONE // Hide the view when not urgent
        }

        holder.myParcelItemListLinear1.setOnClickListener {
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
            intent.putExtra("image", item.image)
            intent.putExtra("dlvryAddress", item.dlvryAddress)
            intent.putExtra("instructions", item.instructions)
            intent.putExtra("recipient", item.recipient)
            intent.putExtra("rcptContactNo", item.rcptContactNo)
            intent.putExtra("content", item.content)
            intent.putExtra("createdBy", item.createdBy)
            context.startActivity(intent)

        }


        Log.d("myParcelItemsAdapter", "Item ID: ${item.id}")
        Log.d("myParcelItemsAdapter", "City: ${item.city}")
        Log.d("myParcelItemsAdapter", "Country: ${item.country}")
        Log.d("myParcelItemsAdapter", "Category: ${item.category}")
        Log.d("myParcelItemsAdapter", "Weight: ${item.weight}")
        Log.d("myParcelItemsAdapter", "Dimensions: ${item.dimension}")
        Log.d("myParcelItemsAdapter", "Total Charge: ${item.ttlCharge}")
        Log.d("myParcelItemsAdapter", "Delivery Date: ${item.dlvryDate}")
        Log.d("myParcelItemsAdapter", "Created Date: ${item.createdDate}")
        Log.d("myParcelItemsAdapter", "recipient: ${item.recipient}")
        Log.d("myParcelItemsAdapter", "Urgent: $urgent")
    }

    override fun getItemCount(): Int {
        return data.size
    }



}