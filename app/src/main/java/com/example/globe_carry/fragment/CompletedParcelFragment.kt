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
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
import com.example.globe_carry.adapter.MyParcelItemAdapter
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompletedParcelFragment:Fragment() {
    private var progressBarLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null
    private var noTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Entered","Entered")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_parcels, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userAuth = FirebaseAuth.getInstance()
        val user = userAuth.currentUser?.uid ?: ""
        val data = mutableListOf<HomeItems>()
        progressBarLayout = view.findViewById(R.id.MyParcelProgressBarLayout)
        progressBar = view.findViewById(R.id.MyParcelProgressBar)
        noTextView = view.findViewById(R.id.MyParcelNoText)
        // Replace with your database connection code
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                showProgressBar()
                val user = userAuth.currentUser?.uid ?: ""

                val query = "SELECT * FROM AdPosts WHERE Created_by = ? "
// Assuming you want to filter posts with a delivery date earlier than the current date
                val currentDate = getCurrentDate() // Get the current date
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, user)
                    // preparedStatement.setString(2, formattedDate)
                    val resultSet = preparedStatement.executeQuery()
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

                        Log.d("Query ","Query is successful")

                        Log.d("com.example.globe_carry.fragment.MyParcel", "PostNo: $postId")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "urgency: $urgency")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "category: $category")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "content: $content")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "weight: $weight")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "value: $value")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "dlvryAddress: $dlvryAddress")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "city: $city")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "country: $country")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "dimension: $dimension")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "dlvryDate: $dlvryDate")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "recipient: $recipient")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "rcptContactNo: $rcptContactNo")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "recipient: $recipient")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "instructions: $instructions")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "ttlCharge: $ttlCharge")
                        Log.d("com.example.globe_carry.fragment.MyParcel", "Created_by: $createdBy")
                        // Create a HomeItems object and add it to the data list
                        val homeItem = HomeItems(
                            id = postId.toString(),
                            urgent = urgency,
                            image = imageBytes,
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
            val recyclerView = view?.findViewById<RecyclerView>(R.id.myParcelRecyclerView)
            val adapter = MyParcelItemAdapter(filteredData)
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