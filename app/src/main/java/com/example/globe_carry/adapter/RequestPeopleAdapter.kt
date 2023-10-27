package com.example.globe_carry.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.DetailActivity
import com.example.globe_carry.R
import com.example.globe_carry.TravelerDetails
import com.example.globe_carry.TravellerDetailView
import java.sql.Connection
import java.sql.SQLException

class RequestPeopleAdapter(private var reqdata: MutableList<TravelerDetails>) :
    RecyclerView.Adapter<RequestPeopleAdapter.RequestPeopleAdapterViewHolder>() {
    init {
        Log.d("RequestPeopleAdapter", "Adapter instantiated")
    }
    private var detailArrpw: ImageView? = null
    inner class RequestPeopleAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reqTravellerName: TextView = itemView.findViewById(R.id.travellerName)
        val reqTravellerNum: TextView = itemView.findViewById(R.id.travellerNum)
        val acceptButton: Button = itemView.findViewById(R.id.btnTravellerAccept)
        val rejectButton: Button = itemView.findViewById(R.id.btnTravellerReject)
        val detailArrpw: ImageView = itemView.findViewById(R.id.arrowRight)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestPeopleAdapter.RequestPeopleAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.request_peoples, parent, false)
        return RequestPeopleAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reqdata.size
    }

    override fun onBindViewHolder(holder: RequestPeopleAdapterViewHolder, position: Int) {
        val item = reqdata[position]

        holder.reqTravellerName.text = item.name
        holder.reqTravellerNum.text = item.phoneNo

        holder.acceptButton.setOnClickListener {
            val postID = item.postID
            val TravellerID = item.TravellerID

            // Call a function to insert data into the "orderstatus" table
            insertDataIntoOrderStatus(postID, TravellerID)
            updateVerificationTable(postID, TravellerID)

            if (item.status == 0) {
                removeItem(position)
            }
        }

        holder.rejectButton.setOnClickListener {
            val postID = item.postID
            val TravellerID = item.TravellerID

            // Call a function to insert data into the "orderstatus" table
            updateRejectVerificationTable(postID, TravellerID)

            if (item.status == 0) {
                removeItem(position)
            }
        }
        holder.detailArrpw.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TravellerDetailView::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("phoneNo", item.phoneNo)
            intent.putExtra("passportNo", item.passportNo)
            intent.putExtra("flightDate", item.flightDate)
            intent.putExtra("DestCountry", item.DestCountry)
            intent.putExtra("DestCity", item.DestCity)
            intent.putExtra("Origin", item.Origin)
            intent.putExtra("TravellerID", item.TravellerID)
            context.startActivity(intent)
        }

    }

    fun insertDataIntoOrderStatus(postID: String, TravellerID: String) {
        val sql = "INSERT INTO orderstatus (postid, acptdTravllerId, orderStartedDate, orderStartedTime) VALUES (?, ?, CURDATE(), CURTIME())"


        val cusConSQL = ConnectionSQL() // Define the variable in the broader scope
        var connection: Connection? = null // Define the connection variable
        try {
            cusConSQL.conclass { conn ->
                connection = conn // Assign the connection
                val preparedStatement = connection?.prepareStatement(sql)


                preparedStatement?.setString(1, postID)
                preparedStatement?.setString(2, TravellerID)

                preparedStatement?.executeUpdate()
            }
        }catch (e: SQLException) {
                // Handle any SQL exceptions
                e.printStackTrace()
            } finally {
                 connection?.close() // Close the connection
            }
        }
    fun updateVerificationTable(postID: String, acceptedTravellerID: String) {
        val sql = "UPDATE verification SET status = 0 WHERE postId = ? AND TravellerID != ?"

        val cusConSQL = ConnectionSQL() // Define the variable in the broader scope
        var connection: Connection? = null // Define the connection variable

        try {
            cusConSQL.conclass { conn ->
                connection = conn // Assign the connection
                val preparedStatement = connection?.prepareStatement(sql)

                preparedStatement?.setString(1, postID)
                preparedStatement?.setString(2, acceptedTravellerID)

                preparedStatement?.executeUpdate()
            }
        } catch (e: SQLException) {
            // Handle any SQL exceptions
            e.printStackTrace()
        } finally {
            connection?.close() // Close the connection
        }
    }

    fun updateRejectVerificationTable(postID: String, acceptedTravellerID: String) {
        val sql = "UPDATE verification SET status = 0 WHERE postId = ? AND TravellerID = ?"

        val cusConSQL = ConnectionSQL() // Define the variable in the broader scope
        var connection: Connection? = null // Define the connection variable

        try {
            cusConSQL.conclass { conn ->
                connection = conn // Assign the connection
                val preparedStatement = connection?.prepareStatement(sql)

                preparedStatement?.setString(1, postID)
                preparedStatement?.setString(2, acceptedTravellerID)

                preparedStatement?.executeUpdate()
            }
        } catch (e: SQLException) {
            // Handle any SQL exceptions
            e.printStackTrace()
        } finally {
            connection?.close() // Close the connection
        }
    }
    fun removeItem(position: Int) {
        reqdata.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    }
