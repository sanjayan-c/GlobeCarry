package com.example.globe_carry.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.HomeItems
import com.example.globe_carry.MyDeliveryRequests
import com.example.globe_carry.QRscanner
import com.example.globe_carry.R
import com.example.globe_carry.Verification
import com.example.globe_carry.ViewVerificationRequest
import com.example.globe_carry.fragment.MyDeliveriesDeliveredFragment
import com.example.globe_carry.fragment.MyDeliveriesFragment
import com.example.globe_carry.fragment.MyDeliveriesPendingFragment

class MyDeliveriesDeliveredAdapter(private val context: MyDeliveriesDeliveredFragment, private val data: List<MyDeliveryRequests>) :
    RecyclerView.Adapter<MyDeliveriesDeliveredAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNo: TextView = itemView.findViewById(R.id.homeItemNo)
        val toCountry: TextView = itemView.findViewById(R.id.homeItemArea)
        val sender: TextView = itemView.findViewById(R.id.itemSender)
        val urgent: TextView = itemView.findViewById(R.id.homeItemUrgent)
        val type: TextView = itemView.findViewById(R.id.homeItemType)
        val orgin: TextView = itemView.findViewById(R.id.homeItemDimensions1)
        val flightDate: TextView = itemView.findViewById(R.id.itemFlightDate1)
        val details: TextView = itemView.findViewById(R.id.homeItemDetails)
        val buttonScan: TextView = itemView.findViewById(R.id.button2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_my_deliveries_delivered_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.itemNo.text = item.postId.toString()
        holder.toCountry.text = "${item.city} , ${item.country}"
        holder.sender.text = item.createdBy
        holder.type.text = item.category
        holder.orgin.text = "${item.cityOrgin} , ${item.countryOrgin}"
        holder.flightDate.text = item.createdDate

        Log.d("Paid",item.paid.toString())
        Log.d("Received",item.received.toString())
        Log.d("Delivered",item.delivered.toString())
        Log.d("Departed",item.departed.toString())
        Log.d("Reached",item.reached.toString())

        holder.flightDate.text = item.ttlCharge.toString()

    }
    override fun getItemCount(): Int {
        return data.size
    }
}