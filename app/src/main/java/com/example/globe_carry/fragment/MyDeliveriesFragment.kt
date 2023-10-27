package com.example.globe_carry.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
import com.example.globe_carry.Verification
import com.example.globe_carry.adapter.MyDeliveriesAdapter
import com.google.firebase.auth.FirebaseAuth
import java.math.BigDecimal
import java.sql.Date
import java.sql.SQLException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class MyDeliveriesFragment: Fragment() {

//    private val cusConSQL = ConnectionSQL()
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_deliveries, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val userAuth = FirebaseAuth.getInstance()
//        val user = userAuth.currentUser?.uid ?: ""
//  //      val data = mutableListOf<MyDeliveries>()
//
//        // Replace with your database connection code
////        val cusConSQL = CusConSQL()
//
//        cusConSQL.conclass { connection ->
//            if (connection != null) {
//                val user = userAuth.currentUser?.uid ?: ""
//              //  val user = "4E7IHpQYswgcqzhsIJ0xPsOId772"
//
//                val query = """ SELECT A.*, O.acptdTravllerId, O.received. O.delivered
//FROM AdPosts AS A
//INNER JOIN orderstatus AS O ON A.postid = O.postid
//WHERE O.acptdTravllerId = ?
//"""
//// Assuming you want to filter posts with a delivery date earlier than the current date
//              //  val currentDate = getCurrentDate() // Get the current date
//               // val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
//                try {
//                    val preparedStatement = connection.prepareStatement(query)
//                    preparedStatement.setString(1, user)
//
//                    val resultSet = preparedStatement.executeQuery()
//                    val filteredData = mutableListOf<HomeItems>()
//
//                    while (resultSet.next()) {
//                        // Parse data from the result set
//                        val postId = resultSet.getInt("postid")
//                        val urgency = resultSet.getBoolean("urgency")
//                        val category = resultSet.getString("category")
//                        val content = resultSet.getString("content")
//                        val weight = resultSet.getString("weight")
//                        val value = resultSet.getFloat("value")
//                        val dlvryAddress = resultSet.getString("dlvryAddress")
//                        val city = resultSet.getString("city")
//                        val country = resultSet.getString("country")
//                        val dimension = resultSet.getString("dimension")
//                        val dlvryDate = resultSet.getString("dlvryDate")
//                        val instructions = resultSet.getString("instructions")
//                        val recipient = resultSet.getString("recipient")
//                        val rcptContactNo = resultSet.getString("rcptContactNo")
//                        val ttlCharge = resultSet.getFloat("ttlCharge")
//                        val imageBytes = resultSet.getString("image")
//                        val createdBy = resultSet.getString("Created_by")
//                        val received = resultSet.getString("received")
//                        val delivered = resultSet.getString("delivered")
//
//                        Log.d("Query ","Query is successful")
//
//                        Log.d("MyParcel", "PostNo: $postId")
//                        Log.d("MyParcel", "urgency: $urgency")
//                        Log.d("MyParcel", "category: $category")
//                        Log.d("MyParcel", "content: $content")
//                        Log.d("MyParcel", "weight: $weight")
//                        Log.d("MyParcel", "value: $value")
//                        Log.d("MyParcel", "dlvryAddress: $dlvryAddress")
//                        Log.d("MyParcel", "city: $city")
//                        Log.d("MyParcel", "country: $country")
//                        Log.d("MyParcel", "dimension: $dimension")
//                        Log.d("MyParcel", "dlvryDate: $dlvryDate")
//                        Log.d("MyParcel", "recipient: $recipient")
//                        Log.d("MyParcel", "rcptContactNo: $rcptContactNo")
//                        Log.d("MyParcel", "recipient: $recipient")
//                        Log.d("MyParcel", "instructions: $instructions")
//                        Log.d("MyParcel", "ttlCharge: $ttlCharge")
//                        Log.d("MyParcel", "Created_by: $createdBy")
//                        // Create a HomeItems object and add it to the data list
//                        val homeItem = HomeItems(
//                             postId.toString(),
//                             true,
//                             country,
//                             category,
//                             weight,
//                             dimension.toFloat(),
//                              ttlCharge.toString(),
//                            dlvryDate,
//                            null,
//                            null
//
//
//                            )
//
//
//                        filteredData.add(homeItem)
//                    }
//
//                    resultSet.close()
//                    preparedStatement.close()
//                    updateRecyclerView(filteredData) // Pass filteredData here
//
//                } catch (e: SQLException) {
//                    Log.e("SQL Error", "SQL Exception: " + e.message)
//                    e.printStackTrace()
//
//                }finally {
//                    connection.close()
//                }
//            }
//        }
//
//    }
//
//
//    private fun updateRecyclerView(filteredData: List<HomeItems>) {
//        requireActivity().runOnUiThread {
//            val recyclerView = view?.findViewById<RecyclerView>(R.id.MydelRecyclerView)
//            val adapter = MyDeliveriesAdapter(this,filteredData)
//            recyclerView?.adapter = adapter
//            recyclerView?.layoutManager = LinearLayoutManager(requireContext())
//        }
//    }

}