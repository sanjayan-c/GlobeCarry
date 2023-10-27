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
import com.example.globe_carry.R
import com.example.globe_carry.Status


class ParcelPendingAdapter(private val data: List<HomeItems>) :
    RecyclerView.Adapter<ParcelPendingAdapter.ParcelPendingAdapterViewHolder>() {
    inner class ParcelPendingAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myParcelPendItemNo: TextView = itemView.findViewById(R.id.myParcelPendItemNo)
        val myParcelPendItemCity: TextView = itemView.findViewById(R.id.myParcelPendItemCity)
        val myParcelPendItemCountry: TextView = itemView.findViewById(R.id.myParcelPendItemCountry)
        val myParcelPendItemType: TextView = itemView.findViewById(R.id.myParcelPendItemType)
        val myParcelPendItemWeight1: TextView = itemView.findViewById(R.id.myParcelPendItemWeight1)
        val myParcelPendItemDimensions1: TextView = itemView.findViewById(R.id.myParcelPendItemDimensions1)
        val myParcelPendItemCharge1: TextView = itemView.findViewById(R.id.myParcelPendItemCharge1)
        val myParcelPendItemDlvryDate1: TextView = itemView.findViewById(R.id.myParcelPendItemDlvryDate1)
        val myParcelPendItemPostDate: TextView = itemView.findViewById(R.id.myParcelPendItemPostDate)
        val myParcelPendItemUrgent: TextView = itemView.findViewById(R.id.myParcelPendItemUrgent)
        val myParcelPendDlvryAdrs: TextView = itemView.findViewById(R.id.myParcelPendDlvryAdrs)
        val myParcelPendItemSpclIns: TextView = itemView.findViewById(R.id.myParcelPendItemSpclIns)
        val myParcelPendItemRecName: TextView = itemView.findViewById(R.id.myParcelPendItemRecName)
        val myParcelPendItemRecNum: TextView = itemView.findViewById(R.id.myParcelPendItemRecNum)
        val myParcelPendItemValue: TextView = itemView.findViewById(R.id.myParcelPendItemValue)
        val myParcelPendItemContent: TextView = itemView.findViewById(R.id.myParcelPenditemContent)
        val myParcelPendItemListLinear1: RelativeLayout = itemView.findViewById(R.id.myParcelPendListLinear1)
        val myParcelPendItemStatus: TextView = itemView.findViewById(R.id.myParcelPendItemStatus)



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParcelPendingAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pending_item, parent, false)
        return ParcelPendingAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ParcelPendingAdapterViewHolder, position: Int) {
        val item = data[position]

        holder.myParcelPendItemNo.text = item.id
        holder.myParcelPendItemCity.text = item.city
        holder.myParcelPendItemCountry.text = item.country
        holder.myParcelPendItemType.text = item.category
        holder.myParcelPendItemRecName.text = item.recipient
        holder.myParcelPendItemRecNum.text = item.rcptContactNo
        holder.myParcelPendDlvryAdrs.text = item.dlvryAddress
        holder.myParcelPendItemSpclIns.text = item.instructions
        holder.myParcelPendItemValue.text = item.value.toString()
        holder.myParcelPendItemContent.text = item.content
        holder.myParcelPendItemWeight1.text = item.weight
        holder.myParcelPendItemDimensions1.text = item.dimension
        holder.myParcelPendItemCharge1.text = item.ttlCharge.toString()
        holder.myParcelPendItemDlvryDate1.text = item.dlvryDate
        holder.myParcelPendItemPostDate.text = item.createdDate
        holder.myParcelPendItemStatus.text = item.status

        val urgent = item.urgent

        if (urgent == true) {
            holder.myParcelPendItemUrgent.visibility = View.VISIBLE
        } else {
            holder.myParcelPendItemUrgent.visibility = View.GONE // Hide the view when not urgent
        }
        holder.myParcelPendItemListLinear1.setOnClickListener {
            // Handle click event here


            val context = holder.itemView.context
            val intent = Intent(context, Status::class.java)
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
            intent.putExtra("dlvryAddress", item.dlvryAddress)
            intent.putExtra("instructions", item.instructions)
            intent.putExtra("recipient", item.recipient)
            intent.putExtra("rcptContactNo", item.rcptContactNo)
            intent.putExtra("content", item.content)
            intent.putExtra("createdBy", item.createdBy)
            intent.putExtra("status", item.status)
            intent.putExtra("travellerName", item.travellerName)
            intent.putExtra("travellerNum", item.travellerNum)
            Log.d("ParcelPendingAdapter", "travelerName: $item.travellerName")
            Log.d("ParcelPendingAdapter", "travelerPhoneNo: ${item.travellerNum}")
            Log.d("StartActivity", "Starting ")

            context.startActivity(intent)


        }
        //Log.d("Image", item.image.toString())

    }

}