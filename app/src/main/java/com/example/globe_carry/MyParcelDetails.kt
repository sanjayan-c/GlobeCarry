package com.example.globe_carry

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MyParcelDetails: AppCompatActivity() {

    private lateinit var data: List<HomeItems>
    private var arrowImageView: ImageView? = null
    private lateinit var btnCancel: Button
    private lateinit var userAuth: FirebaseAuth
    private lateinit var btnAceeptDlvry : Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_parcel_details)

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
        val createdBy = intent.getStringExtra("createdBy")
        val createdName = intent.getStringExtra("createdUserName")
        val createdContactNo = intent.getStringExtra("createdUserContactNo")

        // Now you have the clicked HomeItems object, and you can access its properties
        findViewById<TextView>(R.id.myParcelviewTxtUrgent).text = if (urgent) "Urgent" else "Not Urgent"
        findViewById<TextView>(R.id.myParcelviewChrge).text = ttlCharge.toString()
        findViewById<TextView>(R.id.myParcelviewdlvrydate).text = dlvryDate
        findViewById<TextView>(R.id.myParcelviewCategory).text = category
        findViewById<TextView>(R.id.myParcelviewValue).text = value.toString()
        findViewById<TextView>(R.id.myParcelviewContent).text = content
        findViewById<TextView>(R.id.myParcelviewdlvryAddrs).text = dlvryAddress
        findViewById<TextView>(R.id.myParcelviewSpclIns).text = instructions
        findViewById<TextView>(R.id.myParcelviewRecName).text = recipient
        findViewById<TextView>(R.id.myParcelviewRecNum).text = rcptContactNo
        findViewById<TextView>(R.id.myParcelviewDimension).text = dimensions
        // findViewById<TextView>(R.id.viewdlvryAddrs1).text = "" // Set this if needed
        findViewById<TextView>(R.id.myParcelviewCity).text = city
        findViewById<TextView>(R.id.myParcelviewCountry).text = country
        findViewById<TextView>(R.id.myParcelviewWeight).text = weight


        // Inside your DetailActivity's `onCreate` method
        // fetchCustomerDetails(createdBy)
        val imageBase64 = HomeItemImageSingleton.itemImageBase64
        val imageView = findViewById<ImageView>(R.id.myParceldetailImage)
//        Log.d("Image", "ImageBase64 size: ${imageBase64?.length}")

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

        arrowImageView!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
    }
}