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
//import com.example.globe_carry.HomeItemsDataSingleton
import com.example.globe_carry.R
import com.example.globe_carry.adapter.MyParcelRequestAdapter
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyParcelRequestsFragment : Fragment() {
    private var progressBarLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null
    private var noTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.myparcel_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userAuth = FirebaseAuth.getInstance()
        val user = userAuth.currentUser?.uid ?: ""
        progressBarLayout = view.findViewById(R.id.RequestParcelProgressBarLayout)
        progressBar = view.findViewById(R.id.RequestParcelProgressBar)
        noTextView = view.findViewById(R.id.RequestParcelNoText)
        //val data = mutableListOf<HomeItems>()
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                showProgressBar()
                val user = userAuth.currentUser?.uid ?: ""

                val query = "SELECT AdPosts.*, verification.postId, COUNT(*) AS row_count\n" +
                        "FROM AdPosts\n" +
                        "INNER JOIN verification ON AdPosts.postId = verification.postId\n" +
                        "LEFT JOIN orderstatus ON AdPosts.postId = orderstatus.postId\n" +
                        "WHERE AdPosts.Created_by = ? AND verification.status = 1 AND orderstatus.postId IS NULL\n" +
                        "GROUP BY AdPosts.Created_by, verification.postId;"
// Assuming you want to filter posts with a delivery date earlier than the current date
                val currentDate = getCurrentDate() // Get the current date
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, user)
                    // preparedStatement.setString(2, formattedDate)
                    val resultSet = preparedStatement.executeQuery()
                    val imageSingleton = HomeItemImageSingleton.itemImageBase64 // Retrieve the image from the singlet
                    val filteredData = mutableListOf<HomeItems>()

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
                        val notificationCount = resultSet.getInt("row_count")
                        Log.d("NotificationCount", "Count: $notificationCount")
                        HomeItemImageSingleton.itemImageBase64 = imageBytes

                        val homeItem = HomeItems(
                            id = postId.toString(),
                            urgent = urgency,
                            //image = imageBytes,
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
                            notificationCount =notificationCount
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
            val recyclerView = view?.findViewById<RecyclerView>(R.id.myRequestsRecyclerView)
            val adapter = MyParcelRequestAdapter(filteredData)
            recyclerView?.adapter = adapter
            recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showProgressBar() {
        if (progressBarLayout != null && progressBar != null) {
            progressBarLayout?.visibility = View.VISIBLE
            progressBar?.visibility = View.VISIBLE
            noTextView?.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        if (progressBarLayout != null) {
            requireActivity().runOnUiThread {
                progressBarLayout?.visibility = View.GONE
            }
        }
    }
}