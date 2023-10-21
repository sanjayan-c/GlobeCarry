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
import com.example.globe_carry.adapter.HomeItemsAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.sql.SQLException


class HomeFragment : Fragment() {
    private lateinit var userAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView
        CoroutineScope(Dispatchers.IO).launch {
            if (!isAdded) {
                return@launch
            }

            val userAuth = FirebaseAuth.getInstance()
            val user = userAuth.currentUser?.uid ?: ""
            val data = mutableListOf<HomeItems>()

            // Replace with your database connection code
            val cusConSQL = ConnectionSQL()
            cusConSQL.conclass { connection ->
                if (connection != null) {
                    val user = userAuth.currentUser?.uid ?: ""

                    val query = "SELECT * FROM AdPosts"
                    try {
                        // Create a statement
                        val statement = connection.createStatement()

                        // Execute the query
                        val resultSet = statement.executeQuery(query)

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

                            Log.d("Query ", "Query is successful")

                            Log.d("PostDetail", "PostNo: $postId")
                            Log.d("PostDetail", "urgency: $urgency")
                            Log.d("PostDetail", "category: $category")
                            Log.d("PostDetail", "content: $content")
                            Log.d("PostDetail", "weight: $weight")
                            Log.d("PostDetail", "value: $value")
                            Log.d("PostDetail", "dlvryAddress: $dlvryAddress")
                            Log.d("PostDetail", "city: $city")
                            Log.d("PostDetail", "country: $country")
                            Log.d("PostDetail", "dimension: $dimension")
                            Log.d("PostDetail", "dlvryDate: $dlvryDate")
                            Log.d("PostDetail", "recipient: $recipient")
                            Log.d("PostDetail", "rcptContactNo: $rcptContactNo")
                            Log.d("PostDetail", "recipient: $recipient")
                            Log.d("PostDetail", "instructions: $instructions")
                            Log.d("PostDetail", "ttlCharge: $ttlCharge")
                            Log.d("PostDetail", "Created_by: $createdBy")
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

                            data.add(homeItem)

                        }

                        resultSet.close()
                        statement.close()
                        updateRecyclerView(data)

                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()

                    } finally {
                        connection.close()
                    }
                }
                updateRecyclerView(data)
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
    override fun onDestroyView() {
        super.onDestroyView()

        // Cancel the coroutine when the fragment is destroyed
        CoroutineScope(Dispatchers.IO).cancel()
    }
}

