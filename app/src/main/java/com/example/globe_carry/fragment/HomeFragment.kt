package com.example.globe_carry.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView

import android.widget.LinearLayout

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.CommonHome
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.HomeItemImageSingleton
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
import com.example.globe_carry.adapter.HomeItemsAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.sql.SQLException


class HomeFragment : Fragment() {
    private lateinit var userAuth: FirebaseAuth
    private var progressBarLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null
    private var noTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the parent activity
        val parentActivity = activity

        if (parentActivity is CommonHome) {
            // Cast the activity to your specific activity type if needed
            val myActivity = parentActivity as CommonHome

            // Now, you can access views in the activity's layout
            val someView = myActivity.findViewById<LinearLayout>(R.id.toolBarSearchBar)
            someView.visibility = View.VISIBLE
            // Do something with the view
        }


        // Initialize the RecyclerView
        CoroutineScope(Dispatchers.IO).launch {
            if (!isAdded) {
                return@launch
            }
            val userAuth = FirebaseAuth.getInstance()
            val user = userAuth.currentUser?.uid ?: ""
            val data = mutableListOf<HomeItems>()
            progressBarLayout = view.findViewById(R.id.CustomerMyBookingsProgressBarLayout)
            progressBar = view.findViewById(R.id.CustomerMyBookingsProgressBar)
            noTextView = view.findViewById(R.id.CustomerMyBookingsNoText)

            // Replace with your database connection code
            val cusConSQL = ConnectionSQL()
            cusConSQL.conclass { connection ->
                if (connection != null) {
                    // Show the progress bar when loading data
                    showProgressBar()
                    val user = userAuth.currentUser?.uid ?: ""

                    val query = "SELECT AdPosts.*, user.*\n" +
                            "FROM AdPosts\n" +
                            "INNER JOIN user ON AdPosts.Created_by = user.userId;"
                    try {
                        // Create a statement
                        val statement = connection.createStatement()

                        // Execute the query
                        val resultSet = statement.executeQuery(query)
                        val imageSingleton =
                            HomeItemImageSingleton.itemImageBase64 // Retrieve the image from the singleton

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
                            val createdUserName = resultSet.getString("firstName")
                            val createdUserContactNo = resultSet.getString("phoneNo")
//
//                        Log.d("Query ","Query is successful")
//
//                        Log.d("Image Data", "Image from Database: $imageBytes")
//                        Log.d("PostDetail", "PostNo: $postId")
//                        Log.d("PostDetail", "urgency: $urgency")
//                        Log.d("PostDetail", "category: $category")
//                        Log.d("PostDetail", "content: $content")
//                        Log.d("PostDetail", "weight: $weight")
//                        Log.d("PostDetail", "value: $value")
//                        Log.d("PostDetail", "dlvryAddress: $dlvryAddress")
//                        Log.d("PostDetail", "city: $city")
//                        Log.d("PostDetail", "country: $country")
//                        Log.d("PostDetail", "dimension: $dimension")
//                        Log.d("PostDetail", "dlvryDate: $dlvryDate")
//                        Log.d("PostDetail", "recipient: $recipient")
//                        Log.d("PostDetail", "rcptContactNo: $rcptContactNo")
//                        Log.d("PostDetail", "recipient: $recipient")
//                        Log.d("PostDetail", "instructions: $instructions")
//                        Log.d("PostDetail", "ttlCharge: $ttlCharge")
//                        Log.d("PostDetail", "Created_by: $createdBy")
//                        Log.d("PostDetail", "Created_Num: $createdUserContactNo")
                            // Create a HomeItems object and add it to the data list

                            HomeItemImageSingleton.itemImageBase64 = imageBytes

                            val homeItem = HomeItems(
                                id = postId.toString(),
                                urgent = urgency,
                                //image = imageBytes,
                                //image = HomeItemImageSingleton.itemImageBase64,
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
                                createdUserName = createdUserName,
                                createdUserContactNo = createdUserContactNo
                            )

                            // Log.d("Image Data", "Image from Singleton: ${HomeItemImageSingleton.itemImageBase64}")
                            data.add(homeItem)
                            Log.d("PhoneData", "PhoneNum Customer: ${homeItem.createdUserContactNo}")
                        }

                        resultSet.close()
                        statement.close()
                        updateRecyclerView(data)
                        hideProgressBar()



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

    private fun updateRecyclerView(data: List<HomeItems>) {
        if (isAdded) {
            requireActivity().runOnUiThread {
                val recyclerView = view?.findViewById<RecyclerView>(R.id.homeRecyclerView)
                val adapter = HomeItemsAdapter(data)
                recyclerView?.adapter = adapter
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
            }
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
        val view = view ?: return
        requireActivity().runOnUiThread {
            progressBarLayout?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Cancel the coroutine when the fragment is destroyed
        CoroutineScope(Dispatchers.IO).cancel()

    }
}

