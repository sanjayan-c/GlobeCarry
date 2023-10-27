package com.example.globe_carry.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.HomeItemImageSingleton
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
import com.example.globe_carry.TravelerDetails
import com.example.globe_carry.TravelerDetailsSingleton

import com.example.globe_carry.adapter.ParcelPendingAdapter
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PendingParcelFragment: Fragment() {
    private var pendingProgressBarLayout: FrameLayout? = null
    private var pendingProgressBar: ProgressBar? = null
    private var pendingNoTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pending_parcel_list, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userAuth = FirebaseAuth.getInstance()
        val user = userAuth.currentUser?.uid ?: ""
        //val data = mutableListOf<HomeItems>()
        pendingProgressBarLayout = view.findViewById(R.id.PendingProgressBarLayout)
        pendingProgressBar = view.findViewById(R.id.PendingProgressBar)
        pendingNoTextView = view.findViewById(R.id.PendingNoText)
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                showProgressBar()
                val user = userAuth.currentUser?.uid ?: ""
                val query = "SELECT\n" +
                        "    AdPosts.*,\n" +
                        "    CASE\n" +
                        "        WHEN orderstatus.orderstatus_id IS NOT NULL\n" +
                        "            AND AdPosts.postid IS NOT NULL\n" +
                        "            AND orderstatus.acptdTravllerId IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedTime IS NOT NULL\n" +
                        "            AND orderstatus.paid IS NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NULL\n" +
                        "            AND orderstatus.departed IS NULL\n" +
                        "            AND orderstatus.reached IS NULL\n" +
                        "            AND orderstatus.delivered IS NULL\n" +
                        "            AND orderstatus.orderCompletedDate IS NULL\n" +
                        "            AND orderstatus.orderDepartedDate IS NULL\n" +
                        "            AND orderstatus.orderDepartedTime IS NULL\n" +
                        "            AND orderstatus.orderReachedDate IS NULL\n" +
                        "            AND orderstatus.orderReachedTime IS NULL\n" +
                        "            AND orderstatus.orderCompletedTime IS NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NULL\n" +
                        "        THEN 'Payment Pending'\n" +
                        "\n" +
                        "        WHEN orderstatus.orderstatus_id IS NOT NULL\n" +
                        "            AND AdPosts.postid IS NOT NULL\n" +
                        "            AND orderstatus.acptdTravllerId IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedTime IS NOT NULL\n" +
                        "            AND orderstatus.paid IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NULL\n" +
                        "            AND orderstatus.departed IS NULL\n" +
                        "            AND orderstatus.reached IS NULL\n" +
                        "            AND orderstatus.delivered IS NULL\n" +
                        "            AND orderstatus.orderCompletedDate IS NULL\n" +
                        "            AND orderstatus.orderDepartedDate IS NULL\n" +
                        "            AND orderstatus.orderDepartedTime IS NULL\n" +
                        "            AND orderstatus.orderReachedDate IS NULL\n" +
                        "            AND orderstatus.orderReachedTime IS NULL\n" +
                        "            AND orderstatus.orderCompletedTime IS NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NULL\n" +
                        "        THEN 'Paid'\n" +
                        "\n" +
                        "        WHEN orderstatus.orderstatus_id IS NOT NULL\n" +
                        "            AND AdPosts.postid IS NOT NULL\n" +
                        "            AND orderstatus.acptdTravllerId IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedTime IS NOT NULL\n" +
                        "            AND orderstatus.paid IS NOT NULL\n" +
                        "            AND orderstatus.received IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NOT NULL\n" +
                        "            AND orderstatus.departed IS NULL\n" +
                        "            AND orderstatus.reached IS NULL\n" +
                        "            AND orderstatus.delivered IS NULL\n" +
                        "            AND orderstatus.orderCompletedDate IS NULL\n" +
                        "            AND orderstatus.orderDepartedDate IS NULL\n" +
                        "            AND orderstatus.orderDepartedTime IS NULL\n" +
                        "            AND orderstatus.orderReachedDate IS NULL\n" +
                        "            AND orderstatus.orderReachedTime IS NULL\n" +
                        "            AND orderstatus.orderCompletedTime IS NULL\n" +
                        "        THEN 'Handed Over'\n" +
                        "\n" +
                        "        WHEN orderstatus.orderstatus_id IS NOT NULL\n" +
                        "            AND AdPosts.postid IS NOT NULL\n" +
                        "            AND orderstatus.acptdTravllerId IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedTime IS NOT NULL\n" +
                        "            AND orderstatus.paid IS NOT NULL\n" +
                        "            AND orderstatus.received IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NOT NULL\n" +
                        "            AND orderstatus.departed IS NOT NULL\n" +
                        "            AND orderstatus.orderDepartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderDepartedTime IS NOT NULL\n" +
                        "            AND orderstatus.reached IS NULL\n" +
                        "            AND orderstatus.delivered IS NULL\n" +
                        "            AND orderstatus.orderCompletedDate IS NULL\n" +
                        "            AND orderstatus.orderReachedDate IS NULL\n" +
                        "            AND orderstatus.orderReachedTime IS NULL\n" +
                        "            AND orderstatus.orderCompletedTime IS NULL\n" +
                        "        THEN 'Parcel Departed'\n" +
                        "\n" +
                        "        WHEN orderstatus.orderstatus_id IS NOT NULL\n" +
                        "            AND AdPosts.postid IS NOT NULL\n" +
                        "            AND orderstatus.acptdTravllerId IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedTime IS NOT NULL\n" +
                        "            AND orderstatus.paid IS NOT NULL\n" +
                        "            AND orderstatus.received IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NOT NULL\n" +
                        "            AND orderstatus.departed IS NOT NULL\n" +
                        "            AND orderstatus.orderDepartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderDepartedTime IS NOT NULL\n" +
                        "            AND orderstatus.reached IS NOT NULL\n" +
                        "            AND orderstatus.orderReachedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderReachedTime IS NOT NULL\n" +
                        "            AND orderstatus.delivered IS NULL\n" +
                        "            AND orderstatus.orderCompletedDate IS NULL\n" +
                        "            AND orderstatus.orderCompletedTime IS NULL\n" +
                        "        THEN 'Parcel Reached'\n" +
                        "\n" +
                        "        WHEN orderstatus.orderstatus_id IS NOT NULL\n" +
                        "            AND AdPosts.postid IS NOT NULL\n" +
                        "            AND orderstatus.acptdTravllerId IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderStartedTime IS NOT NULL\n" +
                        "            AND orderstatus.paid IS NOT NULL\n" +
                        "            AND orderstatus.received IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedTime IS NOT NULL\n" +
                        "            AND orderstatus.orderReceivedDate IS NOT NULL\n" +
                        "            AND orderstatus.departed IS NOT NULL\n" +
                        "            AND orderstatus.orderDepartedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderDepartedTime IS NOT NULL\n" +
                        "            AND orderstatus.reached IS NOT NULL\n" +
                        "            AND orderstatus.orderReachedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderReachedTime IS NOT NULL\n" +
                        "            AND orderstatus.delivered IS NOT NULL\n" +
                        "            AND orderstatus.orderCompletedDate IS NOT NULL\n" +
                        "            AND orderstatus.orderCompletedTime IS NOT NULL\n" +
                        "        THEN 'Delivered'\n" +
                        "\n" +
                        "        ELSE 'Unknown'\n" +
                        "    END AS status,\n" +
                        "    CONCAT(userTraveler.firstName, ' ', userTraveler.lastName) AS travelerName,\n" +
                        "    userTraveler.phoneNo AS travelerPhoneNo,\n" +
                        "    orderstatus.acptdTravllerId AS TravellerId\n" +
                        "FROM\n" +
                        "    AdPosts\n" +
                        "LEFT JOIN orderstatus ON AdPosts.PostId = orderstatus.PostId\n" +
                        "LEFT JOIN user AS userTraveler ON orderstatus.acptdTravllerId = userTraveler.UserID\n" +
                        "WHERE\n" +
                        "    AdPosts.Created_by = ?;\n"

                val currentDate = getCurrentDate() // Get the current date
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, user)
                    // preparedStatement.setString(2, formattedDate)
                    val resultSet = preparedStatement.executeQuery()
                    val imageSingleton = HomeItemImageSingleton.itemImageBase64 // Retrieve the image from the singlet
                    val filteredData = mutableListOf<HomeItems>()
                    val travelerDetailsList = mutableListOf<TravelerDetails>()
                    while (resultSet.next()) {
                        // Parse data from the result set
                        val postId = resultSet.getInt("postid")
                        val urgency = resultSet.getBoolean("urgency")
                        val category = resultSet.getString("category")
                        val content = resultSet.getString("content")
                        val weight = resultSet.getString("weight")
                        val value = resultSet.getFloat("value")
                        val dlvryAddress = resultSet.getString("dlvryAddress")
                        val city = resultSet.getString("city")
                        val country = resultSet.getString("country")
                        val dimension = resultSet.getString("dimension")
                        val dlvryDate = resultSet.getString("dlvryDate")
                        val instructions = resultSet.getString("instructions")
                        val recipient = resultSet.getString("recipient")
                        val rcptContactNo = resultSet.getString("rcptContactNo")
                        val ttlCharge = resultSet.getFloat("ttlCharge")
                        val imageBytes = resultSet.getString("image")
                        val createdBy = resultSet.getString("Created_by")
                        val status = resultSet.getString("status")
                        // Handle potential null values
                        val travelerId = if (status != "Zero Requests") resultSet.getString("TravellerId") else "Unknown TravelerId"
                        val travelerName = if (status != "Zero Requests") resultSet.getString("travelerName") else "Unknown Traveler"
                        val travelerPhoneNo = if (status != "Zero Requests") resultSet.getString("travelerPhoneNo") else "Unknown Phone"
                        Log.d("Adapter", "TravellerId: $travelerId")
                        Log.d("Adapter", "TravellerName: $travelerName")
                        Log.d("Adapter", "TravellerPhoneNo: $travelerPhoneNo")
                        HomeItemImageSingleton.itemImageBase64 = imageBytes

                        Log.d("Fragment", "travellerName: ${ TravelerDetailsSingleton.travelerName}")
                        Log.d("Fragment", "TravellerID: ${ TravelerDetailsSingleton.travelerId}")
                        Log.d("Fragment", "TravellerPhoneNo:${ TravelerDetailsSingleton.travelerPhoneNo}")

                        val homeItem = HomeItems(
                            id = postId.toString(),
                            urgent = urgency,
                            category = category,
                            content = content,
                            value = value,
                            weight = weight.toString(),
                            dlvryAddress = dlvryAddress,
                            city = city,
                            country = country,
                            recipient = recipient,
                            rcptContactNo = rcptContactNo,
                            dlvryDate = dlvryDate,
                            instructions = instructions,
                            ttlCharge = ttlCharge,
                            dimension = dimension,
                            createdBy = createdBy,
                            status = status,
                            travellerName = travelerName,
                            travellerNum = travelerPhoneNo,

                        )

                        filteredData.add(homeItem)
                    }

                    resultSet.close()
                    preparedStatement.close()
                    updateRecyclerView(filteredData) // Pass filteredData here
                    hideProgressBar()

                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()

                }finally {
                    connection.close()
                }
            }
        }

    }
    fun getCurrentDate(): Date {
        val currentDate = Date() // Get the current date and time
        Log.d("CurrentDate", currentDate.toString()) // Log the current date
        return currentDate

    }

    private fun updateRecyclerView(filteredData: List<HomeItems>) {
        requireActivity().runOnUiThread {
            val recyclerView = view?.findViewById<RecyclerView>(R.id.PendingRecyclerView)
            val adapter = ParcelPendingAdapter(filteredData)
            recyclerView?.adapter = adapter
            recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showProgressBar() {
        if (pendingProgressBarLayout != null && pendingProgressBar != null) {
            pendingProgressBarLayout?.visibility = View.VISIBLE
            pendingProgressBar?.visibility = View.VISIBLE
            pendingNoTextView?.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        if (pendingProgressBarLayout != null && isAdded) {
            requireActivity().runOnUiThread {
                pendingProgressBarLayout?.visibility = View.GONE
            }
        }
    }
}