package com.example.globe_carry

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.adapter.RequestPeopleAdapter
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException

class RequestedPeopleNdParcelView: AppCompatActivity() {
    private var reqdata = mutableListOf<TravelerDetails>()
    private var arrowImageView: ImageView? = null

    private lateinit var userAuth: FirebaseAuth
    private var cusMyHistoryArrowUp: ImageView? = null
    private var cusMyHistoryArrowUpLayout: RelativeLayout? = null
    private var cusMyHistoryArrowDown: ImageView? = null
    private var cusMyHistoryArrowDownLayout: RelativeLayout? = null
    private var myHistoryContent: LinearLayout? = null
    private var progressBarLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null
    private var noTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.requested_people_view)
        userAuth= FirebaseAuth.getInstance()
        arrowImageView = findViewById(R.id.arrowImageView)


        // Retrieve the clicked HomeItems object from intent extras
        val postIdString = intent.getStringExtra("postId")
        val postId = postIdString?.toIntOrNull() // This returns null if the conversion fails
        val urgent = intent.getBooleanExtra("urgent", false)
        val city = intent.getStringExtra("city")
        val country = intent.getStringExtra("country")
        val category = intent.getStringExtra("category")
        val weight = intent.getStringExtra("weight")
        val ttlCharge = intent.getFloatExtra("ttlCharge", 0.0f)
        val createdDate = intent.getStringExtra("createdDate")
        val dlvryAddress = intent.getStringExtra("dlvryAddress")
        val dlvryDate = intent.getStringExtra("dlvryDate")
        val value = intent.getFloatExtra("value", 0.0f)
        val instructions = intent.getStringExtra("instructions")
        val recipient = intent.getStringExtra("recipient")
        val rcptContactNo = intent.getStringExtra("rcptContactNo")
        val content = intent.getStringExtra("content")
        val dimensions = intent.getStringExtra("dimensions")
        // Initialize progress bar and related UI components
        progressBarLayout = findViewById(R.id.RequestProgressBarLayout)
        progressBar = findViewById(R.id.RequestProgressBar)
        noTextView = findViewById(R.id.RequestNoText)

        findViewById<TextView>(R.id.reqviewTxtUrgent).text = if (urgent) "Urgent" else "Not Urgent"
        findViewById<TextView>(R.id.reqviewChrge).text = ttlCharge.toString()
        findViewById<TextView>(R.id.reqviewdlvrydate).text = dlvryDate
        findViewById<TextView>(R.id.reqviewCategory).text = category
        findViewById<TextView>(R.id.reqviewValue).text = value.toString()
        findViewById<TextView>(R.id.reqviewContent).text = content
        findViewById<TextView>(R.id.reqviewdlvryAddrs).text = dlvryAddress
        findViewById<TextView>(R.id.reqviewSpclIns).text = instructions
        findViewById<TextView>(R.id.reqviewRecName).text = recipient
        findViewById<TextView>(R.id.reqviewRecNum).text = rcptContactNo
        findViewById<TextView>(R.id.reqviewDimension).text = dimensions
        findViewById<TextView>(R.id.reqviewCity).text = city
        findViewById<TextView>(R.id.reqviewCountry).text = country
        findViewById<TextView>(R.id.reqviewWeight).text = weight

        // Inside your DetailActivity's `onCreate` method
        // fetchCustomerDetails(createdBy)
        val imageBase64 = HomeItemImageSingleton.itemImageBase64
        val imageView = findViewById<ImageView>(R.id.reqdetailImage)


        if (imageBase64 != null && imageBase64.isNotEmpty()) {
            // Decode the Base64 string to a ByteArray
            val decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            Log.d("Image", "Decoded image size: ${decodedBytes.size}")

            // Decode the ByteArray to a Bitmap
            val decodedBitmap =
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Set the decoded Bitmap as the image for the ImageView
            imageView.setImageBitmap(decodedBitmap)
        } else {
            // If there's no image data, you can set a placeholder image or do nothing
            // For example, set a placeholder image resource:
            imageView.setImageResource(R.drawable.baseline_image_24)
        }
        switchToCustomerHomeLayout()
        arrowImageView!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
        cusMyHistoryArrowUp?.setOnClickListener {
            cusMyHistoryArrowUpLayout?.visibility = View.GONE
            myHistoryContent?.visibility = View.GONE
            cusMyHistoryArrowDownLayout?.visibility = View.VISIBLE
        }

        cusMyHistoryArrowDown?.setOnClickListener {
            cusMyHistoryArrowDownLayout?.visibility = View.GONE
            myHistoryContent?.visibility = View.VISIBLE
            cusMyHistoryArrowUpLayout?.visibility = View.VISIBLE
        }

        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
showProgressBar()
                val user = userAuth.currentUser?.uid ?: ""

                val query = "SELECT CONCAT(u.firstName, ' ', u.lastName) AS fullName, u.phoneNo, v.postId, v.TravellerId, v.*\n" +
                        "FROM user u\n" +
                        "INNER JOIN verification v ON u.userID = v.TravellerID\n" +
                        "WHERE v.postId = ? AND v.status = 1;\n"
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setInt(1, postId ?: 0) // Use 0 as the default value when postId is null
                    val resultSet = preparedStatement.executeQuery()

                    // Execute the query


                    while (resultSet.next()) {
                        // Parse data from the result set
                        val name = resultSet.getString("fullName")
                        val phoneNo = resultSet.getString("phoneNo")
                        val postId = resultSet.getString("postId")
                        val TravellerId = resultSet.getString("TravellerId")
                        val passport = resultSet.getString("passport")
                        val FlightDate = resultSet.getString("FlightDate")
                        val destination = resultSet.getString("destination")
                        val city = resultSet.getString("city")
                        val orgin = resultSet.getString("orgin")
                        val passportImg = resultSet.getString("PassportImage")
                        val ticketImg = resultSet.getString("TicketImage")
                        val travellerImg = resultSet.getString("TravellerImage")

                        PassportImgSingleton.passportImageBase64=passportImg
                        TicketImgSingleton.ticketImageBase64=ticketImg
                        TravellerImgSingleton.travellerImageBase64=travellerImg

                        val travelerDet = TravelerDetails(
                            phoneNo = phoneNo ?: "Null", // Use "Null" if phoneNo is null
                            name = name,
                            postID =postId,
                            TravellerID = TravellerId,
                            status = 1,
                            passportNo = passport,
                            flightDate = FlightDate,
                            DestCountry =destination,
                            DestCity =city,
                            Origin =orgin
                        )

                        reqdata.add(travelerDet)
                    }

                    resultSet.close()
                    preparedStatement.close()
                    updateRecyclerView(reqdata)

                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()


                }finally {
                    connection.close()
                }
            }
            updateRecyclerView(reqdata)
            hideProgressBar()

        }


    }
    private fun switchToCustomerHomeLayout() {
        runOnUiThread {

            cusMyHistoryArrowUp = findViewById(R.id.cusMyHistoryArrowUp)
            cusMyHistoryArrowUpLayout = findViewById(R.id.cusMyHistoryArrowUpLayout)
            cusMyHistoryArrowDown = findViewById(R.id.cusMyHistoryArrowDown)
            cusMyHistoryArrowDownLayout = findViewById(R.id.cusMyHistoryArrowDownLayout)
            myHistoryContent = findViewById(R.id.myHistoryContent)
        }
    }
    private fun updateRecyclerView(reqdata: MutableList<TravelerDetails>) {
        runOnUiThread {
            val filteredData = reqdata.filter { it.status == 1 }.toMutableList()
            val recyclerView = findViewById<RecyclerView>(R.id.requestPeopleRecyclerView)
            val adapter = RequestPeopleAdapter(filteredData)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }
    }
    private fun showProgressBar() {
        progressBarLayout?.visibility = View.VISIBLE
        progressBar?.visibility = View.VISIBLE
        noTextView?.visibility = View.GONE
    }

    private fun hideProgressBar() {
        runOnUiThread {
            progressBarLayout?.visibility = View.GONE
            progressBar?.visibility = View.GONE
            noTextView?.visibility = View.GONE
        }
    }

}