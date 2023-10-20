package com.example.globe_carry.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.CusConSQL
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
    private val cusConSQL = CusConSQL()
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

        // Find the TextViews from the inflated view
        getStatusCounts { (pendingRequests, completedVerifications) ->
            activity?.runOnUiThread {
                // Find the TextViews from the inflated view
                val pendingRequestTextView: TextView = view.findViewById(R.id.pendingRequest)
                val completedRequestTextView: TextView = view.findViewById(R.id.completedRequest)

                // Update the text of the TextViews with the counts
                pendingRequestTextView.text = "$pendingRequests"
                completedRequestTextView.text = "$completedVerifications"
            }
        }

        return view
    }


    fun getStatusCounts(callback: (Pair<Int, Int>) -> Unit) {
        cusConSQL.conclass { connection ->
            if (connection != null) {
                try {
                    val query = """
                    SELECT
                        COUNT(CASE WHEN v.Status IS NULL THEN 1 END) AS pending_count,
                        COUNT(CASE WHEN v.Status IS NOT NULL THEN 1 END) AS completed_count
                    FROM
                        verification v;
                """

                    var pendingCount = 0
                    var completedCount = 0

                    val preparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    if (resultSet.next()) {
                        pendingCount = resultSet.getInt("pending_count")
                        completedCount = resultSet.getInt("completed_count")
                    }

                    preparedStatement.close()
                    resultSet.close()

                    // Call the callback function with the data
                    callback(Pair(pendingCount, completedCount))
                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                } catch (e: Exception) {
                    Log.e("General Error", "Error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

}