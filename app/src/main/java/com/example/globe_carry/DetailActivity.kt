package com.example.globe_carry

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DetailActivity:AppCompatActivity() {
    private lateinit var data: List<HomeItems>
    private var arrowImageView: ImageView? = null
    private lateinit var btnCancel: Button
    private lateinit var userAuth: FirebaseAuth
    private lateinit var btnAceeptDlvry : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        arrowImageView = findViewById(R.id.arrowImageView)
        btnCancel = findViewById(R.id.btnCancel)
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

        // Now you have the clicked HomeItems object, and you can access its properties
        findViewById<TextView>(R.id.viewTxtUrgent).text = if (urgent) "Urgent" else "Not Urgent"
        findViewById<TextView>(R.id.viewChrge).text = ttlCharge.toString()
        findViewById<TextView>(R.id.viewdlvrydate).text = dlvryDate
        findViewById<TextView>(R.id.viewCategory).text = category
        findViewById<TextView>(R.id.viewValue).text = value.toString()
        findViewById<TextView>(R.id.viewContent).text = content
        findViewById<TextView>(R.id.viewdlvryAddrs).text = dlvryAddress
        findViewById<TextView>(R.id.viewSpclIns).text = instructions
        findViewById<TextView>(R.id.viewRecName).text = recipient
        findViewById<TextView>(R.id.viewRecNum).text = rcptContactNo
        findViewById<TextView>(R.id.viewDimension).text = dimensions
        // findViewById<TextView>(R.id.viewdlvryAddrs1).text = "" // Set this if needed
        findViewById<TextView>(R.id.viewCity).text = city
        findViewById<TextView>(R.id.viewCountry).text = country
        findViewById<TextView>(R.id.viewWeight).text = weight
        // Inside your DetailActivity's `onCreate` method

        val imageBase64 = intent.getStringExtra("image")
        val imageView = findViewById<ImageView>(R.id.detailImage)
        Log.d("Image", "ImageBase64 size: ${imageBase64?.length}")

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
        btnCancel!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }


        fetchCustomerDetails(createdBy)
        val btnAceeptDlvry = findViewById<Button>(R.id.btnAceeptDlvry)
        btnAceeptDlvry.setOnClickListener {
            // Define the intent to start the AdPostActivity
            val intent = Intent(this, AddTravellerRequest1::class.java)
            intent.putExtra("postId", postIdString)
            Log.d("DetailActivity", "postId: $postIdString")

            // Start the AdPostActivity
            startActivity(intent)
        }

    }

    private fun fetchCustomerDetails(createdBy: String?) {
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT firstName, phoneNo FROM user WHERE userId = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, createdBy)
                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    Log.d("Results of Customer", resultSet.toString())
                    if (resultSet.next()) {
                        val customerName = resultSet.getString("firstName")
                        val customerContactNumber = resultSet.getString("phoneNo")
                        Log.d("Results of Customer", resultSet.getString("firstName"))
                        Log.d("Results of Customer", resultSet.getString("phoneNo"))
                        // Update your views with customer details

                        runOnUiThread {
                            findViewById<TextView>(R.id.viewCusName).text = customerName
                            findViewById<TextView>(R.id.viewCusNum).text = customerContactNumber
                        }

                    }

                    resultSet.close()
                    preparedStatement.close()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    connection.close()
                }
            }
        }
    }

    // Update other views with data from the HomeItems object as needed

    private fun establishDatabaseConnection(): Connection {
        // Implement your database connection logic here using your existing connection class
        val url = "jdbc:mysql://your-database-url"
        val username = "your-username"
        val password = "your-password"

        return DriverManager.getConnection(url, username, password)
    }

}
