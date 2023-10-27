package com.example.globe_carry

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.sql.Connection
import java.sql.SQLException

class TravellerDetailView :AppCompatActivity() {
    private lateinit var data: List<TravelerDetails>
    private var arrowImageView: ImageView? = null
    private lateinit var userAuth: FirebaseAuth
    private lateinit var Reject: Button
    private lateinit var Accept: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traveller_details)
        Reject = findViewById(R.id.btnReject)
        Accept = findViewById(R.id.btnAceept)
        arrowImageView = findViewById(R.id.arrowImageView)
        val name = intent.getStringExtra("name")
        val phoneNo = intent.getStringExtra("phoneNo")
        val passportNo = intent.getStringExtra("passportNo")
        val flightDate = intent.getStringExtra("flightDate")
        val DestCountry = intent.getStringExtra("DestCountry")
        val DestCity = intent.getStringExtra("DestCity")
        val Origin = intent.getStringExtra("Origin")
        val TravellerID = intent.getStringExtra("TravellerID")

        findViewById<TextView>(R.id.txtTravellerName).text = name
        findViewById<TextView>(R.id.txtTravellerNumber).text = phoneNo.toString()
        findViewById<TextView>(R.id.txtPassportNo).text = passportNo.toString()
        findViewById<TextView>(R.id.txtFlightDate).text = flightDate.toString()
        findViewById<TextView>(R.id.txtDestinationCountry).text = DestCountry
        findViewById<TextView>(R.id.txtDestinationCity).text = DestCity
        findViewById<TextView>(R.id.txtOrigin).text = Origin


        val imageBase64_1 = TravellerImgSingleton.travellerImageBase64
        val imageBase64_2 = PassportImgSingleton.passportImageBase64
        val imageBase64_3 = TicketImgSingleton.ticketImageBase64

        val imageView_1 = findViewById<ImageView>(R.id.detailImage1)
        val imageView_2 = findViewById<ImageView>(R.id.detailImage2)
        val imageView_3 = findViewById<ImageView>(R.id.detailImage3)
        val expandedImageView = ImageView(this)
        updateImageView(imageBase64_1, imageView_1)
        updateImageView(imageBase64_2, imageView_2)
        updateImageView(imageBase64_3, imageView_3)

        fun showExpandedImage(imageBase64: String?) {
            if (imageBase64 != null && imageBase64.isNotEmpty()) {
                val decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                expandedImageView.setImageBitmap(decodedBitmap)

                val dialog = Dialog(this)
                dialog.setContentView(R.layout.image_expander) // Use the XML layout for the expanded image
                val dialogImageView = dialog.findViewById<ImageView>(R.id.expandedImageView)
                dialogImageView.setImageDrawable(expandedImageView.drawable)

                // Add any other customization for the dialog (e.g., dimensions, background)
                dialog.show()
            }
        }

        imageView_1.setOnClickListener {
            showExpandedImage(imageBase64_1)
        }

        imageView_2.setOnClickListener {
            showExpandedImage(imageBase64_2)
        }

        imageView_3.setOnClickListener {
            showExpandedImage(imageBase64_3)
        }

        arrowImageView!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
        Reject.setOnClickListener { // Start the CustomerAccountManagement activity
            val postID = intent.getStringExtra("postID")
            val TravellerID = intent.getStringExtra("TravellerID")

            // Call a function to reject the request
            rejectRequest(postID, TravellerID)
        }
        Accept.setOnClickListener { // Start the CustomerAccountManagement activity
            val postID = intent.getStringExtra("postID")
            val TravellerID = intent.getStringExtra("TravellerID")

            // Call a function to accept the request
            acceptRequest(postID, TravellerID)
            updateVerificationAcceptRequest(postID, TravellerID)
        }

    }

    private fun updateImageView(imageBase64: String?, imageView: ImageView) {
        if (imageBase64 != null && imageBase64.isNotEmpty()) {
            val decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imageView.setImageBitmap(decodedBitmap)
        } else {
            // Set a placeholder image if there's no image data
            imageView.setImageResource(R.drawable.baseline_image_24)
        }
    }


    fun acceptRequest(postID: String?, TravellerID: String?) {
        val sql = "INSERT INTO orderstatus (postid, acptdTravllerId, orderStartedDate, orderStartedTime) VALUES (?, ?, CURDATE(), CURTIME())"


        val cusConSQL = ConnectionSQL() // Define the variable in the broader scope
        var connection: Connection? = null // Define the connection variable
        try {
            cusConSQL.conclass { conn ->
                connection = conn // Assign the connection
                val preparedStatement = connection?.prepareStatement(sql)


                preparedStatement?.setString(1, postID)
                preparedStatement?.setString(2, TravellerID)

                preparedStatement?.executeUpdate()
            }
        }catch (e: SQLException) {
            // Handle any SQL exceptions
            e.printStackTrace()
        } finally {
            connection?.close() // Close the connection
        }
    }
    fun rejectRequest(postID: String?, TravellerID: String?) {

        val sql = "UPDATE verification SET status = 0 WHERE postId = ? AND TravellerID = ?"

        val cusConSQL = ConnectionSQL() // Define the variable in the broader scope
        var connection: Connection? = null // Define the connection variable

        try {
            cusConSQL.conclass { conn ->
                connection = conn // Assign the connection
                val preparedStatement = connection?.prepareStatement(sql)

                preparedStatement?.setString(1, postID)
                preparedStatement?.setString(2, TravellerID)

                preparedStatement?.executeUpdate()
            }
        } catch (e: SQLException) {
            // Handle any SQL exceptions
            e.printStackTrace()
        } finally {
            connection?.close() // Close the connection
        }
    }

    fun updateVerificationAcceptRequest(postID: String?, TravellerID: String?) {
        val sql = "UPDATE verification SET status = 0 WHERE postId = ? AND TravellerID != ?"

        val cusConSQL = ConnectionSQL() // Define the variable in the broader scope
        var connection: Connection? = null // Define the connection variable

        try {
            cusConSQL.conclass { conn ->
                connection = conn // Assign the connection
                val preparedStatement = connection?.prepareStatement(sql)

                preparedStatement?.setString(1, postID)
                preparedStatement?.setString(2, TravellerID)

                preparedStatement?.executeUpdate()
            }
        } catch (e: SQLException) {
            // Handle any SQL exceptions
            e.printStackTrace()
        } finally {
            connection?.close() // Close the connection
        }
    }
}