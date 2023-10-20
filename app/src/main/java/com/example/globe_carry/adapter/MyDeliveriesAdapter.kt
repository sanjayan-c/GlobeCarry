package com.example.globe_carry.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.HomeItems
import com.example.globe_carry.MyDeliveries
import com.example.globe_carry.R
import com.example.globe_carry.Verification
import com.example.globe_carry.ViewVerificationRequest
import com.example.globe_carry.fragment.MyDeliveriesFragment

class MyDeliveriesAdapter(private val context: MyDeliveriesFragment, private val data: List<HomeItems>) :
    RecyclerView.Adapter<MyDeliveriesAdapter.ViewHolder>() {

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
        val button2: TextView = itemView.findViewById(R.id.button2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_my_deliveries_list, parent, false)
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

//        if (urgent!!) {
//            holder.homeItemUrgent.visibility = View.VISIBLE
//        }
        //  val homeItemDetailsTextView = findViewById<TextView>(R.id.homeItemDetails)

        holder.button2.setOnClickListener {
            // Code to execute when the TextView is clicked

            // Create an Intent to start the new activity
            val intent = Intent(context.requireContext(), ViewVerificationRequest::class.java)

            // Put the request ID as an extra in the Intent
            intent.putExtra("REQUEST_ID_KEY", item.no)

            // Start the new activity
            context.startActivity(intent)

            // For example, you can open a new activity or perform some other action.
        }

    }
    override fun getItemCount(): Int {
        return data.size
    }
}