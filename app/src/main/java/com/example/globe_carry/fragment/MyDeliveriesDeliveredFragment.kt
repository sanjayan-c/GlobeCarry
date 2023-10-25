package com.example.globe_carry.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.MyDeliveryRequests
import com.example.globe_carry.R
import com.example.globe_carry.adapter.MyDeliveriesAdapter
import com.example.globe_carry.adapter.MyDeliveriesDeliveredAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.sql.SQLException

class MyDeliveriesDeliveredFragment : Fragment() {


    var translationAnimator : ObjectAnimator?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_deliveries_delivered, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userAuth = FirebaseAuth.getInstance()
        val user = userAuth.currentUser?.uid ?: ""
        //      val data = mutableListOf<MyDeliveries>()


        val runningManImageView = view.findViewById<ImageView>(R.id.runningManImageView1)
        val cusWalletProgressBarLayout = view.findViewById<FrameLayout>(R.id.cusWalletProgressBarLayout)
        cusWalletProgressBarLayout.visibility = View.VISIBLE

        // Calculate the width of the screen for animation bounds
        val screenWidth = resources.displayMetrics.widthPixels

        // Create an ObjectAnimator to animate translation from left to right
        translationAnimator = ObjectAnimator.ofFloat(
            runningManImageView,
            "translationX",
            -screenWidth.toFloat(),
            screenWidth.toFloat()
        )

        // Set the animator duration
        translationAnimator?.duration = 2000  // Adjust the duration as needed

        // Set the repeat mode to reverse for back-and-forth animation
        translationAnimator?.repeatMode = ObjectAnimator.RESTART
        translationAnimator?.repeatCount = ObjectAnimator.INFINITE

        // Start the animation
        translationAnimator?.start()


        CoroutineScope(Dispatchers.IO).launch {
            if (!isAdded) {
                return@launch
            }
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                val user = userAuth.currentUser?.uid ?: ""

                val query =
                    "SELECT a.*, o.*, u.firstName AS firstName, u.lastName AS lastName, u.phoneNo as phoneNo, u.city AS cityOrgin, u.country AS countryOrgin, v.*, u2.firstName AS myFirstName, u2.lastName AS myLastName " +
                            "FROM AdPosts a, orderstatus o, user u, verification v, user u2 " +
                            "WHERE a.postid = o.postid AND a.Created_by = u.userId AND v.Postid = o.postid AND v.TravellerID = o.acptdTravllerId AND o.acptdTravllerId = u2.userId AND o.delivered = TRUE AND o.acptdTravllerId = ?";

                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, user)

                    val resultSet = preparedStatement.executeQuery()
                    val filteredData = mutableListOf<MyDeliveryRequests>()

                    while (resultSet.next()) {
                        // Parse data from the result set
                        val postId = resultSet.getInt("postid")
                        val urgency = resultSet.getBoolean("urgency")
                        val category = resultSet.getString("category") ?: ""
                        val content = resultSet.getString("content") ?: ""
                        val value = resultSet.getBigDecimal("value") ?: BigDecimal.ZERO
                        val weight = resultSet.getBigDecimal("weight") ?: BigDecimal.ZERO
                        val dlvryAddress = resultSet.getString("dlvryAddress") ?: ""
                        val city = resultSet.getString("city") ?: ""
                        val country = resultSet.getString("country") ?: ""
                        val recipient = resultSet.getString("recipient") ?: ""
                        val rcptContactNo = resultSet.getString("rcptContactNo") ?: ""
                        val dlvryDate = resultSet.getString("dlvryDate") ?: ""
                        val instructions = resultSet.getString("instructions") ?: ""
                        val ttlCharge = resultSet.getBigDecimal("ttlCharge")
                        val dimension = resultSet.getString("dimension") ?: ""
                        val createdDate = resultSet.getString("createdDate") ?: ""
                        val createdBy = resultSet.getString("Created_by") ?: ""
//                       val imageBytes = resultSet.getString("image")

                        val orderstatus_id = resultSet.getInt("orderstatus_id")
                        val received = resultSet.getBoolean("received")
                        val delivered = resultSet.getBoolean("delivered")
                        val paid = resultSet.getBoolean("paid")
                        val departed = resultSet.getBoolean("departed")
                        val reached = resultSet.getBoolean("reached")

                        val firstName = resultSet.getString("firstName") ?: ""
                        val lastName = resultSet.getString("lastName") ?: ""
                        val phoneNo = resultSet.getString("phoneNo") ?: ""
                        val cityOrgin = resultSet.getString("cityOrgin") ?: ""
                        val countryOrgin = resultSet.getString("countryOrgin") ?: ""

                        val flightDate = resultSet.getString("FlightDate") ?: ""
                        val passport = resultSet.getString("passport") ?: ""
                        val orgin = resultSet.getString("orgin") ?: ""
//                        val passportImage = resultSet.getString("PassportImage")
//                        val ticketImage = resultSet.getString("TicketImage")
//                        val travellerImage = resultSet.getString("TravellerImage")

                        val myFirstName = resultSet.getString("myFirstName") ?: ""
                        val myLastName = resultSet.getString("myLastName") ?: ""

                        Log.d("Query ", "Query is successful")

                        Log.d("MyParcel", "PostNo: $postId")
                        Log.d("MyParcel", "urgency: $urgency")
                        Log.d("MyParcel", "category: $category")
                        Log.d("MyParcel", "content: $content")
                        Log.d("MyParcel", "value: $value")
                        Log.d("MyParcel", "weight: $weight")
                        Log.d("MyParcel", "dlvryAddress: $dlvryAddress")
                        Log.d("MyParcel", "city: $city")
                        Log.d("MyParcel", "country: $country")
                        Log.d("MyParcel", "recipient: $recipient")
                        Log.d("MyParcel", "rcptContactNo: $rcptContactNo")
                        Log.d("MyParcel", "dlvryDate: $dlvryDate")
                        Log.d("MyParcel", "instructions: $instructions")
                        Log.d("MyParcel", "ttlCharge: $ttlCharge")
                        Log.d("MyParcel", "dimension: $dimension")
                        Log.d("MyParcel", "createdDate: $createdDate")
                        Log.d("MyParcel", "createdBy: $createdBy")
//                        Log.d("MyParcel", "imageBytes: $imageBytes")

                        Log.d("MyParcel", "orderstatus_id: $orderstatus_id")
                        Log.d("MyParcel", "received: $received")
                        Log.d("MyParcel", "delivered: $delivered")
                        Log.d("MyParcel", "paid: $paid")
                        Log.d("MyParcel", "departed: $departed")
                        Log.d("MyParcel", "reached: $reached")


                        Log.d("MyParcel", "firstName: $firstName")
                        Log.d("MyParcel", "lastName: $lastName")
                        Log.d("MyParcel", "phoneNo: $phoneNo")
                        Log.d("MyParcel", "cityOrgin: $cityOrgin")
                        Log.d("MyParcel", "countryOrgin: $countryOrgin")

                        Log.d("MyParcel", "flightDate: $flightDate")
                        Log.d("MyParcel", "passport: $passport")
                        Log.d("MyParcel", "orgin: $orgin")
//                        Log.d("MyParcel", "passportImage: $passportImage")
//                        Log.d("MyParcel", "ticketImage: $ticketImage")
//                        Log.d("MyParcel", "travellerImage: $travellerImage")

                        Log.d("MyParcel", "myFirstName: $myFirstName")
                        Log.d("MyParcel", "myLastNamen: $myLastName")

                        // Create a HomeItems object and add it to the data list
                        val homeItem = MyDeliveryRequests(
                            postId, urgency,
                            category, content,
                            value, weight,
                            dlvryAddress, city,
                            country, recipient,
                            rcptContactNo, dlvryDate,
                            instructions, ttlCharge,
                            dimension, createdDate,
                            createdBy, "",
                            orderstatus_id, received, delivered, paid, departed, reached,
                            firstName, lastName, phoneNo, cityOrgin, countryOrgin,
                            flightDate, passport, orgin, "", "", "",
                            myFirstName, myLastName
                        )

                        filteredData.add(homeItem)
                    }

                    resultSet.close()
                    preparedStatement.close()
                    updateRecyclerView(filteredData) // Pass filteredData here

                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()

                } finally {
                    connection.close()
                }
            }
        }
    }

    }


    private fun updateRecyclerView(filteredData: List<MyDeliveryRequests>) {
        // Check if the fragment is attached to an activity
        if (isAdded) {
            requireActivity().runOnUiThread {
                val recyclerView = view?.findViewById<RecyclerView>(R.id.MydelRecyclerView)
                val adapter = MyDeliveriesDeliveredAdapter(this, filteredData)
                recyclerView?.adapter = adapter
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                val cusWalletProgressBarLayout = view?.findViewById<FrameLayout>(R.id.cusWalletProgressBarLayout)
                cusWalletProgressBarLayout?.visibility = View.GONE
                translationAnimator?.cancel()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        // Cancel the coroutine when the fragment is destroyed
        CoroutineScope(Dispatchers.IO).cancel()
    }
}