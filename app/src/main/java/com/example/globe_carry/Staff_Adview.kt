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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.globe_carry.DetailDataSingleton.createdBy
import com.example.globe_carry.DetailDataSingleton.urgent
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class Staff_Adview:AppCompatActivity() {
    private lateinit var data: List<HomeItems>
    private var arrowImageView: ImageView? = null
//    private lateinit var btnCancel: Button
    private lateinit var userAuth: FirebaseAuth
    //private lateinit var btnAceeptDlvry : Button
    private val cusConSQL = ConnectionSQL()
   // private var PostId :Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_adview)

        arrowImageView = findViewById(R.id.arrowImageView)
//        btnCancel = findViewById(R.id.btnCancel)
        // Retrieve the clicked HomeItems object from intent extras
        val postIdString = intent.getStringExtra("PostID")
        Toast.makeText(this, "$postIdString", Toast.LENGTH_SHORT).show()
      //  var postId: String? = null
        var urgent: Boolean? = null
        var imageBytes: String? = null
        var category: String? = null
        var content: String? = null
        var value: Float? = null
        var weight: String? = null
        var dlvryAddress: String? = null
        var city: String? = null
        var country: String? = null
        var recipient: String? = null
        var rcptContactNo: String? = null
        var dlvryDate: String? = null
        var instructions: String? = null
        var ttlCharge: Float? = null
        var dimensions: String? = null
        var createdDate: String? = null
        var createdBy: String? = null



        cusConSQL.conclass { connection ->
            if (connection != null) {
//                val user = userAuth.currentUser?.uid ?: ""


                val query = "SELECT * FROM AdPosts WHERE PostID = '$postIdString'"
                try {
                    // Create a statement
                    val statement = connection.createStatement()
                    // Execute the query
                    val resultSet = statement.executeQuery(query)





                    while (resultSet.next()) {
                        // Parse data from the result set
                         urgent = resultSet.getBoolean("urgency")
                         category = resultSet.getString("category")
                         content = resultSet.getString("content")
                         weight = resultSet.getString("weight")
                         value = resultSet.getFloat("value")
                         dlvryAddress = resultSet.getString("dlvryAddress")
                         city = resultSet.getString("city")
                         country = resultSet.getString("country")
                         dimensions = resultSet.getString("dimension")
                         dlvryDate = resultSet.getString("dlvryDate")
                         instructions = resultSet.getString("instructions")
                         recipient = resultSet.getString("recipient")
                         rcptContactNo = resultSet.getString("rcptContactNo")
                         ttlCharge = resultSet.getFloat("ttlCharge")
                         imageBytes = resultSet.getString("image")
                         createdBy = resultSet.getString("Created_by")

                        Log.d("Query ","Query is successful")

                      //  Log.d("PostDetail", "PostNo: $postId")
                        Log.d("PostDetail", "urgency: $urgent")
                        Log.d("PostDetail", "category: $category")
                        Log.d("PostDetail", "content: $content")
                        Log.d("PostDetail", "weight: $weight")
                        Log.d("PostDetail", "value: $value")
                        Log.d("PostDetail", "dlvryAddress: $dlvryAddress")
                        Log.d("PostDetail", "city: $city")
                        Log.d("PostDetail", "country: $country")
                        Log.d("PostDetail", "dimension: $dimensions")
                        Log.d("PostDetail", "dlvryDate: $dlvryDate")
                        Log.d("PostDetail", "recipient: $recipient")
                        Log.d("PostDetail", "rcptContactNo: $rcptContactNo")
                        Log.d("PostDetail", "recipient: $recipient")
                        Log.d("PostDetail", "instructions: $instructions")
                        Log.d("PostDetail", "ttlCharge: $ttlCharge")
                        Log.d("PostDetail", "Created_by: $createdBy")
                        // Create a HomeItems object and add it to the data list



                    }


                    runOnUiThread{


                        // Now you have the clicked HomeItems object, and you can access its properties
                        // findViewById<TextView>(R.id.viewTxtUrgent).text = if (urgency) "Urgent" else "Not Urgent"
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

                      //  val imageBase64 = intent.getStringExtra("image")
                        val imageView = findViewById<ImageView>(R.id.detailImage)
                        Log.d("Image", "ImageBase64 size: ${imageBytes?.length}")

                        if (imageBytes != null && imageBytes!!.isNotEmpty()) {
                            // Decode the Base64 string to a ByteArray
                            val decodedBytes = Base64.decode(imageBytes, Base64.DEFAULT)
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
//        btnCancel!!.setOnClickListener { // Start the CustomerAccountManagement activity
//            finish()

                        fetchCustomerDetails(createdBy)
                    }

                    resultSet.close()
                    statement.close()


                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()

                }finally {
                    connection.close()
                }
            }

        }



//        val btnAceeptDlvry = findViewById<Button>(R.id.btnAceeptDlvry)
//        btnAceeptDlvry.setOnClickListener {
//            // Define the intent to start the AdPostActivity
//            val intent = Intent(this, AddTravellerRequest1::class.java)
//            intent.putExtra("postId", postIdString)
//            Log.d("DetailActivity", "postId: $postIdString")
//
//            // Start the AdPostActivity
//            startActivity(intent)
//        }

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
