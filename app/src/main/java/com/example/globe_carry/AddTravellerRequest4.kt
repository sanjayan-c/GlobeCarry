package com.example.globe_carry

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Date

class AddTravellerRequest4 : AppCompatActivity() {

    private lateinit var passportImg: ImageView
    private lateinit var travellerImg: ImageView
    private lateinit var ticketImg: ImageView
    private lateinit var passportNum: EditText
    private lateinit var flightDate: EditText
    private lateinit var destCountry: EditText
    private lateinit var destCity: EditText
    private lateinit var originCountry: EditText
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    var base64String: String? = null
    private lateinit var btnSubmit4: Button
    private lateinit var btnPrevious4: Button
    private var postId: String? = null
    private var arrowImageView: ImageView? = null
    private lateinit var passportImageBitmap: Bitmap
    private lateinit var travellerImageBitmap: Bitmap
    private lateinit var ticketImageBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_traveller_request4)
        arrowImageView = findViewById(R.id.arrowImageView)
        btnPrevious4 = findViewById(R.id.btnPrevious4)
        btnSubmit4 = findViewById(R.id.btnSubmit4)

        passportNum = findViewById(R.id.passportNum)
        // Assuming you have a DatePicker widget with ID R.id.datePicker
        flightDate = findViewById(R.id.flightdate)
        destCountry = findViewById(R.id.trvlrDestination)
        destCity = findViewById(R.id.trvlrCity)
        originCountry = findViewById(R.id.trvlrorigin)
      //  val passportImageBase64 = PassportImgSingleton.passportImageBase64
       // val travellerImageBase64 = TravellerImgSingleton.travellerImageBase64

       // val ticketImageBase64 = TicketImgSingleton.ticketImageBase64
     //   passportImageBitmap = decodeBase64ToBitmap(passportImageBase64)
       // travellerImageBitmap = decodeBase64ToBitmap(travellerImageBase64)
      // ticketImageBitmap = decodeBase64ToBitmap(ticketImageBase64)

//        var passportImageBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//        var travellerImageBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//        var ticketImageBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)


            val receivedIntent = intent
            if (receivedIntent != null) {
                // Assign the postId to the class property
                postId = receivedIntent.getStringExtra("postId")
                Log.d("AddTravellerRequest4", postId ?: "No PostId available")
            }

        btnSubmit4.setOnClickListener {
            val passportNum = passportNum.text.toString()
            val flightDate = flightDate.text.toString()
            val destCountry = destCountry.text.toString()
            val destCity = destCity.text.toString()
            val originCountry = originCountry.text.toString()
            val currentDate = getCurrentDate()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userID = currentUser?.uid
            val connectSQL = ConnectionSQL()
            connectSQL.conclass { connection ->
                if (connection != null) {
                    try {
                        val query =
                            "INSERT INTO verification (PostID, TravellerID, FlightDate, passport, destination, orgin, PassportImage, TicketImage, TravellerImage, time, city)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                        val preparedStatement = connection.prepareStatement(query)
                        preparedStatement.setString(1, postId)
                        Log.d("AddTravellerRequest4 Addition", postId ?: "No PostId available")
                        preparedStatement.setString(2, userID)
                        preparedStatement.setString(3, flightDate)
                        preparedStatement.setString(4, passportNum)
                        preparedStatement.setString(5, destCountry)
                        preparedStatement.setString(6, originCountry)
                        preparedStatement.setString(7, PassportImgSingleton.passportImageBase64)
                        preparedStatement.setString(8, TravellerImgSingleton.travellerImageBase64)
                        preparedStatement.setString(9, TicketImgSingleton.ticketImageBase64)
                        preparedStatement.setTimestamp(10, java.sql.Timestamp(currentDate.time)) // Set the postDate
                        preparedStatement.setString(11, destCity)

                        preparedStatement.executeUpdate()
                        preparedStatement.close()

                        runOnUiThread {
                            // Show a success message or navigate to another screen
                            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread {
                            Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
                        }
                    } finally {
                        connection.close()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Database connection is null", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }


        }
    }
    private fun decodeBase64ToBitmap(base64String: String?): Bitmap {
        if (base64String.isNullOrEmpty()) {
            Log.e("ImageLog", "Base64-encoded image is null or empty")
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }

        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        Log.d("ImageLog", "Decoded image size: ${decodedBytes.size} bytes")

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getCurrentDate(): Date {
                    return Date() // This will give you the current date and time
                }
}