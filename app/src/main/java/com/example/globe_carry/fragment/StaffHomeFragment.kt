package com.example.globe_carry.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.HomeItems
import com.example.globe_carry.R
import com.example.globe_carry.Verification
import com.example.globe_carry.adapter.HomeItemsAdapter
import com.example.globe_carry.adapter.RequestItemsAdapter
import java.math.BigDecimal
import java.sql.Date
import java.sql.SQLException
import java.sql.Timestamp
import java.text.SimpleDateFormat

class StaffHomeFragment: Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val cusConSQL = ConnectionSQL()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.staff_home, container, false)


        val pendingProgressBar: ProgressBar = view.findViewById(R.id.pendingProgressBar)
        val completedProgressBar: ProgressBar = view.findViewById(R.id.completedProgressBar)
        val totalProgressBar: ProgressBar = view.findViewById(R.id.totalProgressBar)


        // Find the TextViews from the inflated view
        getStatusCounts { pendingRequests, completedVerifications, totalOrders, completedOrders, pendingOrders ->
            activity?.runOnUiThread {
                // Find the TextViews from the inflated view
                val Totalorders: TextView = view.findViewById(R.id.totalOrd)
                val CompletedOrdersTextView: TextView = view.findViewById(R.id.completedOrders)
                val pendingRequestTextView: TextView = view.findViewById(R.id.pendingreq)
                val completedRequestTextView: TextView = view.findViewById(R.id.CompletedRequests)
                val pending_orders: TextView = view.findViewById(R.id.pendingorder)

                // Update the text of the TextViews with the counts
                pendingRequestTextView.text = "$pendingRequests"
                completedRequestTextView.text = "$completedVerifications"
                Totalorders.text = "$totalOrders"
                CompletedOrdersTextView.text = "$completedOrders"
                pending_orders.text = "$pendingOrders"

                pendingProgressBar.progress = calculateProgress(pendingRequests, totalOrders)
                completedProgressBar.progress = calculateProgress(completedVerifications, totalOrders)
                totalProgressBar.progress = calculateProgress(totalOrders, totalOrders)

            }
        }


        return view
    }




    fun getStatusCounts(callback: (Int, Int, Int, Int, Int) -> Unit) {
        cusConSQL.conclass { connection ->
            if (connection != null) {
                try {
                    val query = """
                    SELECT
                        (SELECT COUNT(*) FROM AdPosts) AS total_orders,
                        COUNT(CASE WHEN v.Status IS NULL THEN 1 END) AS pending_count,
                        COUNT(CASE WHEN v.Status IS NOT NULL THEN 1 END) AS completed_count,
                        (SELECT COUNT(*) FROM orderstatus WHERE delivered = TRUE) AS completed_orders,
                        (SELECT COUNT(*) FROM orderstatus WHERE delivered IS NULL) AS pending_orders
                         FROM verification v
                """
                    var totalOrders = 0
                    var pendingCount = 0
                    var completedCount = 0
                    var completedOrders = 0
                    var pendingOrders = 0

                    val preparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    if (resultSet.next()) {
                        totalOrders = resultSet.getInt("total_orders")
                        pendingCount = resultSet.getInt("pending_count")
                        completedCount = resultSet.getInt("completed_count")
                        completedOrders = resultSet.getInt("completed_orders")
                        pendingOrders = resultSet.getInt("pending_orders")
                    }

                    // Close the preparedStatement and resultSet
                    resultSet.close()
                    preparedStatement.close()

                    // Call the callback function with the data
                    callback(pendingCount, completedCount,totalOrders, completedOrders, pendingOrders)
                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                } catch (e: Exception) {
                    Log.e("General Error", "Error: ${e.message}")
                    e.printStackTrace()
                } finally {
                    connection.close() // Close the connection in the finally block
                }
            }
        }
    }

    private fun calculateProgress(count: Int, total: Int): Int {
        return if (total > 0) {
            (count * 100) / total
        } else {
            0
        }

    }

}