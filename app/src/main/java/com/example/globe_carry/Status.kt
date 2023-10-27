package com.example.globe_carry

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.sql.Connection
import java.sql.SQLException

class Status:AppCompatActivity() {
    private lateinit var data: List<HomeItems>
    private var arrowImageView: ImageView? = null
    private lateinit var userAuth: FirebaseAuth
    private lateinit var btnPayNow : Button
    private var cusMyHistoryArrowUp: ImageView? = null
    private var cusMyHistoryArrowUpLayout: RelativeLayout? = null
    private var cusMyHistoryArrowDown: ImageView? = null
    private var cusMyHistoryArrowDownLayout: RelativeLayout? = null
    private var myHistoryContent: LinearLayout? = null
    private var isPaymentMade = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_status)
        arrowImageView = findViewById(R.id.arrowImageView)
        btnPayNow = findViewById(R.id.btnPayNow)
        val postIdString = intent.getStringExtra("postId")
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
        val createdBy = intent.getStringExtra("createdBy")
        val status = intent.getStringExtra("status")
        val btnPayment = findViewById<ImageButton>(R.id.btnPayment)
        val btnHandedOver = findViewById<ImageButton>(R.id.btnHandedOver)
        val btnDeparted = findViewById<ImageButton>(R.id.btnDeparted)
        val btnLanded = findViewById<ImageButton>(R.id.btnLanded)
        val btnReceived = findViewById<ImageButton>(R.id.btnReceived)
        val travelerName = intent.getStringExtra("travellerName")
        val travelerPhoneNo =  intent.getStringExtra("travellerNum")

        Log.d("Debug", "travellerName: ${travelerName}")
        Log.d("Debug", "travellerNum: ${travelerPhoneNo}")


        findViewById<TextView>(R.id.pendviewTxtUrgent).text = if (urgent) "Urgent" else "Not Urgent"
        findViewById<TextView>(R.id.pendviewChrge).text = ttlCharge.toString()
        findViewById<TextView>(R.id.pendviewdlvrydate).text = dlvryDate
        findViewById<TextView>(R.id.pendviewCategory).text = category
        findViewById<TextView>(R.id.pendviewValue).text = value.toString()
        findViewById<TextView>(R.id.pendviewContent).text = content
        findViewById<TextView>(R.id.pendviewdlvryAddrs).text = dlvryAddress
        findViewById<TextView>(R.id.pendviewSpclIns).text = instructions
        findViewById<TextView>(R.id.pendviewRecName).text = recipient
        findViewById<TextView>(R.id.pendviewRecNum).text = rcptContactNo
        findViewById<TextView>(R.id.pendviewDimension).text = dimensions
        findViewById<TextView>(R.id.pendviewCity).text = city
        findViewById<TextView>(R.id.pendviewCountry).text = country
        findViewById<TextView>(R.id.pendviewWeight).text = weight
        findViewById<TextView>(R.id.txtTtlCharge).text=ttlCharge.toString()
        findViewById<TextView>(R.id.txtTravellerName).text=travelerName
        findViewById<TextView>(R.id.txttrvlrNum).text=travelerPhoneNo
        Log.d("Debug", "travellerName: ${travelerName}")
        Log.d("Debug", "travellerNum: ${travelerPhoneNo}")


        val imageBase64 =HomeItemImageSingleton.itemImageBase64
        val imageView = findViewById<ImageView>(R.id.penddetailImage)

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
        when (status) {
            "Paid" -> {
                btnPayment.setColorFilter(resources.getColor(R.color.appcolour))
                btnHandedOver.setColorFilter(resources.getColor(R.color.black))
                btnDeparted.setColorFilter(resources.getColor(R.color.black))
                btnLanded.setColorFilter(resources.getColor(R.color.black))
                btnReceived.setColorFilter(resources.getColor(R.color.black))
            }
//
            "Payment Pending" -> {
                btnPayment.setColorFilter(resources.getColor(R.color.black))
                btnHandedOver.setColorFilter(resources.getColor(R.color.black))
                btnDeparted.setColorFilter(resources.getColor(R.color.black))
                btnLanded.setColorFilter(resources.getColor(R.color.black))
                btnReceived.setColorFilter(resources.getColor(R.color.black))
            }
            "Handed Over" -> {
                btnPayment.setColorFilter(resources.getColor(R.color.appcolour))
                btnHandedOver.setColorFilter(resources.getColor(R.color.appcolour))
                btnDeparted.setColorFilter(resources.getColor(R.color.black))
                btnLanded.setColorFilter(resources.getColor(R.color.black))
                btnReceived.setColorFilter(resources.getColor(R.color.black))
            }
            "Parcel Departed" -> {
                btnPayment.setColorFilter(resources.getColor(R.color.appcolour))
                btnHandedOver.setColorFilter(resources.getColor(R.color.appcolour))
                btnDeparted.setColorFilter(resources.getColor(R.color.appcolour))
                btnLanded.setColorFilter(resources.getColor(R.color.black))
                btnReceived.setColorFilter(resources.getColor(R.color.black))
            }
            "Parcel Reached" -> {
                btnPayment.setColorFilter(resources.getColor(R.color.appcolour))
                btnHandedOver.setColorFilter(resources.getColor(R.color.appcolour))
                btnDeparted.setColorFilter(resources.getColor(R.color.appcolour))
                btnLanded.setColorFilter(resources.getColor(R.color.appcolour))
                btnReceived.setColorFilter(resources.getColor(R.color.black))
            }
            "Delivered" -> {
                btnPayment.setColorFilter(resources.getColor(R.color.appcolour))
                btnHandedOver.setColorFilter(resources.getColor(R.color.appcolour))
                btnDeparted.setColorFilter(resources.getColor(R.color.appcolour))
                btnLanded.setColorFilter(resources.getColor(R.color.appcolour))
                btnReceived.setColorFilter(resources.getColor(R.color.appcolour))
            }
        }

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
        btnPayNow?.setOnClickListener{
            if (!isPaymentMade) {
                // Update the payment status in the database
                if (postIdString != null) {
                    updatePaidStatusInDatabase(postIdString)
                }

                // Set the flag to true to indicate payment has been made
                isPaymentMade = true

                // Update UI: Hide the "Pay Now" button
                btnPayNow.visibility = View.GONE
            }
        }
        when (status) {
            "Paid", "Handed Over", "Parcel Departed", "Parcel Reached", "Delivered", "Zero Request" -> {
                btnPayNow.visibility = View.GONE
                // Handle other button visibility changes here if needed
            }
            "Payment Pending" -> {
                btnPayNow.visibility = View.VISIBLE
                // Handle other button visibility changes here if needed
            }
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
    fun updatePaidStatusInDatabase(postID: String) {
        val sql = "UPDATE orderstatus SET paid = 1 WHERE postid = ?"

        val cusConSQL = ConnectionSQL()
        var connection: Connection? = null

        try {
            cusConSQL.conclass { conn ->
                connection = conn
                val preparedStatement = connection?.prepareStatement(sql)

                preparedStatement?.setString(1, postID)
                val rowsUpdated = preparedStatement?.executeUpdate()

                if (rowsUpdated == 1) {
                    // The update was successful
                    isPaymentMade = true
                    btnPayNow.visibility = View.GONE
                } else {
                    // Handle the case when the update didn't affect any rows
                    // For example, show an error message or log a message.
                    // You can also set isPaymentMade to false if needed.
                }
            }
        } catch (e: SQLException) {
            // Handle SQL exceptions
            e.printStackTrace()
            // You can also show an error message to the user.
        } finally {
            connection?.close()
        }
    }
}