package com.example.globe_carry

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.util.Date

class AddTravellerRequest3 :AppCompatActivity(){
    private lateinit var ticketImg: ImageView
    private lateinit var passportNum: EditText
    private lateinit var fligthDate: Date
    private lateinit var eTxtValue: EditText
    private lateinit var destCountry: EditText
    private lateinit var destCity: EditText
    private lateinit var originCountry: EditText
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    private lateinit var btnNext3: Button
    private lateinit var btnCancel3: Button
    private var arrowImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_traveller_request3)
        arrowImageView = findViewById(R.id.arrowImageView)
        btnCancel3 = findViewById(R.id.btnCancel3)
        btnNext3 = findViewById(R.id.btnNext3)

        ticketImg=findViewById(R.id.flttcktImgUpld)

        ticketImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        val postId = intent.getStringExtra("postId")
        findViewById<TextView>(R.id.TxtParcelNo3).text=postId
        btnNext3.setOnClickListener {
            val ticketImageBase64 = convertImageToBase64(selectedImageUri)
            Log.d("AddTravellerRequest3", "Base64 Image: $ticketImageBase64")

            // Store the Base64 image string in the ImageSingleton
            TicketImgSingleton.ticketImageBase64 = ticketImageBase64

            // Create an Intent to start the next activity
            val nextActivityIntent = Intent(this, AddTravellerRequest4::class.java)

            Log.d("AddTravellerRequest3", postId ?: "No PostId available")
            // Pass the data to the next activity
            nextActivityIntent.putExtra("postId", postId)

            // Start the next activity
            startActivity(nextActivityIntent)

        }
        arrowImageView!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
        btnCancel3!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.data

                // Display the selected image in the ImageView
                ticketImg.setImageURI(selectedImageUri)
            }
        }
    }

    private fun convertImageToBase64(imageUri: Uri?): String {
        // Check if the URI is not null
        if (imageUri != null) {
            // Convert the selected image to Base64
            val inputStream = contentResolver.openInputStream(imageUri)
            val bytes = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                bytes.write(buffer, 0, bytesRead)
            }
            val imageBytes: ByteArray = bytes.toByteArray()
            Log.d("AddTravellerRequest3", "Image Size (bytes): ${imageBytes.size}")

            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }
        return "Image is null" // Return an empty string if URI is null
    }
}