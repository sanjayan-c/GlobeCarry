package com.example.globe_carry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL

import com.example.globe_carry.R
import com.example.globe_carry.Verification
import com.example.globe_carry.adapter.RequestItemsAdapter
import java.sql.Date
import java.sql.SQLException
import java.sql.Timestamp
import java.text.SimpleDateFormat

class VerficationRequestFragment: Fragment() {

    private val cusConSQL = ConnectionSQL()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.staff_request_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var verficationNo: String? = null
        var PostID:String?=null
        var TravellerID:String?=null
        var Fname:String?=null
        var Lname:String?=null
        var flightdate:String?=null
        var orgin: String? = null
        var city: String?    = null
        var country: String? = null
        var requestedDate: String? = null
        var time: Timestamp? = null
        var requestid: String? = null
        var urgent: Boolean? = null


        cusConSQL.conclass { connection ->
            if (connection != null) {

                try {
                    // Your database query logic here
                    // Example query using your commented code
                    val query = """
                 SELECT
    v.VerificationID,
    v.PostID,
    v.TravellerID,
    v.FlightDate,
    v.destination,
    v.orgin,
    v.city,
    v.time,
    u.firstName,
    u.lastName
FROM
    verification v, user u
WHERE
    v.Status = false AND v.TravellerID = u.userId;

                """
                    val preparedStatement = connection.prepareStatement(query)


                    val resultSet = preparedStatement.executeQuery()

                    // Create a list to store the retrieved data
                    val retrievedData = mutableListOf<Verification>()

                    while (resultSet.next()) {
                        // Retrieve data from the result set and create InspectorTimeTableItems objects

                        verficationNo = resultSet.getString("VerificationID")
                        PostID= resultSet.getString("PostID")
                        TravellerID = resultSet.getString("TravellerID")
                        flightdate = resultSet.getString("FlightDate")
                        country=resultSet.getString("destination")
                        orgin=resultSet.getString("orgin")
                        Fname=resultSet.getString("firstName")
                        Lname=resultSet.getString("lastName")
                        city=resultSet.getString("city")
                        time=resultSet.getTimestamp("time")


                        val timestamp = Timestamp(System.currentTimeMillis())

// Create a SimpleDateFormat instance with your desired date format
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

// Convert the Timestamp to a Date
                        val date = Date(timestamp.time)

// Format the Date to a string
                        val formattedDate = sdf.format(date)

                        // Create an InspectorTimeTableItems object and add it to the list
                        val VerificationRequest = Verification(
                            verficationNo,
                            Fname,
                            Lname,
                            flightdate,
                            orgin,
                            city,
                            country,
                            date.toString(),
                            time.toString(),
                            PostID,
                            false,
                            TravellerID











                        )
                        retrievedData.add(VerificationRequest)
                    }

                    resultSet.close()
                    preparedStatement.close()

                    // Check if retrievedData is empty
                    if (retrievedData.isEmpty()) {
//                        runOnUiThread {
//                            // Show the "Nothing to show" TextView
//                        }
                    } else {
                        activity?.runOnUiThread  {


                            // Set the retrieved data in the RecyclerView
                            val recyclerView =
                                view.findViewById<RecyclerView>(R.id.RequestsRecyclerView)
                            val adapter = RequestItemsAdapter(this,retrievedData)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    // Handle any errors
                } finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }
            } else {
                // Handle the case where the database connection is null
            }
        }

    }
}