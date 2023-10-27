package com.example.globe_carry.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.HomeItems
//import com.example.globe_carry.HomeItemsDataSingleton
import com.example.globe_carry.R
import com.example.globe_carry.RequestedPeopleNdParcelView

class MyParcelRequestAdapter(private val data: List<HomeItems>) :
    RecyclerView.Adapter<MyParcelRequestAdapter.MyParcelRequestViewHolder>()  {
  //  private val data: List<HomeItems> = HomeItemsDataSingleton.items
  init {
      Log.d("MyParcelRequestAdapter", "Adapter instantiated")
  }
    inner class MyParcelRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myParcelReqItemNo: TextView = itemView.findViewById(R.id.myParcelReqItemNo)
        val myParcelReqItemCity: TextView = itemView.findViewById(R.id.myParcelReqItemCity)
        val myParcelReqItemCountry: TextView = itemView.findViewById(R.id.myParcelReqItemCountry)
        val myParcelReqItemType: TextView = itemView.findViewById(R.id.myParcelReqItemType)
        val myParcelReqItemWeight1: TextView = itemView.findViewById(R.id.myParcelReqItemWeight1)
        val myParcelReqItemDimensions1: TextView = itemView.findViewById(R.id.myParcelReqItemDimensions1)
        val myParcelReqItemCharge1: TextView = itemView.findViewById(R.id.myParcelReqItemCharge1)
        val myParcelReqItemDlvryDate1: TextView = itemView.findViewById(R.id.myParcelReqItemDlvryDate1)
        val myParcelReqItemPostDate: TextView = itemView.findViewById(R.id.myParcelReqItemPostDate)
        val myParcelReqItemUrgent: TextView = itemView.findViewById(R.id.myParcelReqItemUrgent)
        val myParcelReqDlvryAdrs: TextView = itemView.findViewById(R.id.myParcelReqDlvryAdrs)
        val myParcelReqItemSpclIns: TextView = itemView.findViewById(R.id.myParcelReqItemSpclIns)
        val myParcelReqItemRecName: TextView = itemView.findViewById(R.id.myParcelReqItemRecName)
        val myParcelReqItemRecNum: TextView = itemView.findViewById(R.id.myParcelReqItemRecNum)
        val myParcelReqItemValue: TextView = itemView.findViewById(R.id.myParcelReqItemValue)
        val myParcelReqItemContent: TextView = itemView.findViewById(R.id.myParcelReqitemContent)
        val myParcelReqItemListLinear1: RelativeLayout = itemView.findViewById(R.id.myParcelReqListLinear1)
        val myParcelReqnotification: TextView = itemView.findViewById(R.id.notificationBadge)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyParcelRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myparcel_requests_list, parent, false)
        return MyParcelRequestViewHolder(view)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyParcelRequestViewHolder, position: Int) {
        val item = data[position]

        holder.myParcelReqItemNo.text = item.id
        holder.myParcelReqItemCity.text = item.city
        holder.myParcelReqItemCountry.text = item.country
        holder.myParcelReqItemType.text = item.category
        holder.myParcelReqItemRecName.text = item.recipient
        holder.myParcelReqItemRecNum.text = item.rcptContactNo
        holder.myParcelReqDlvryAdrs.text = item.dlvryAddress
        holder.myParcelReqItemSpclIns.text = item.instructions
        holder.myParcelReqItemValue.text = item.value.toString()
        holder.myParcelReqItemContent.text = item.content
        holder.myParcelReqItemWeight1.text = item.weight
        holder.myParcelReqItemDimensions1.text = item.dimension
        holder.myParcelReqItemCharge1.text = item.ttlCharge.toString()
        holder.myParcelReqItemDlvryDate1.text = item.dlvryDate
        holder.myParcelReqItemPostDate.text = item.createdDate
        holder.myParcelReqnotification.text  = item.notificationCount?.toString() ?: "0"
        //Log.d("Image", item.image.toString())

        val urgent = item.urgent

        if (urgent == true) {
            holder.myParcelReqItemUrgent.visibility = View.VISIBLE
        } else {
            holder.myParcelReqItemUrgent.visibility = View.GONE // Hide the view when not urgent
        }

        holder.myParcelReqItemListLinear1.setOnClickListener {
            // Handle click event here
            val context = holder.itemView.context
            val intent = Intent(context, RequestedPeopleNdParcelView::class.java)
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
            Log.d("StartActivity", "Starting RequestedPeopleNdParcelView")
            context.startActivity(intent)


        }
    }
}