package com.example.globe_carry.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.R
import com.example.globe_carry.Verification
import com.example.globe_carry.ViewVerificationRequest
import com.example.globe_carry.fragment.VerficationRequestFragment

class RequestItemsAdapter(private val context: VerficationRequestFragment, private val data: List<Verification>) :
    RecyclerView.Adapter<RequestItemsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeItemNo: TextView = itemView.findViewById(R.id.homeItemNo)
        val homeItemArea: TextView = itemView.findViewById(R.id.homeItemArea)
        val homeTravallerName: TextView = itemView.findViewById(R.id.Travaller)
        val FlightDate: TextView = itemView.findViewById(R.id.FlightDate)
        val Orgin: TextView = itemView.findViewById(R.id.Orgin)
        val destination: TextView = itemView.findViewById(R.id.destination)
        val homeItemDetailsTextView: TextView = itemView.findViewById(R.id.homeItemDetails)
        val homeItemPostDate: TextView = itemView.findViewById(R.id.homeItemPostDate)
        val homeItemUrgent: TextView = itemView.findViewById(R.id.homeItemUrgent)
       // val homeItemDetailsTextView:TextView = findViewById<TextView>(R.id.homeItemDetails)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.request_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.homeItemNo.text = item.no
        holder.homeItemArea.text = "${item.city} , ${item.country}"
        holder.homeTravallerName.text = item.Fname + item.Lname
        holder.FlightDate.text = item.flightdate
        holder.Orgin.text = item.orgin
        holder.destination.text = item.country
        //holder.homeItemDetailsTextView.text = item.requestid
        holder.homeItemPostDate.text = "${item.time}"
        val urgent = item.urgent

//        if (urgent!!) {
//            holder.homeItemUrgent.visibility = View.VISIBLE
//        }
      //  val homeItemDetailsTextView = findViewById<TextView>(R.id.homeItemDetails)

        holder.homeItemDetailsTextView.setOnClickListener {
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