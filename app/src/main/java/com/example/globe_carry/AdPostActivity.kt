package com.example.globe_carry

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import java.util.Date


class AdPostActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var switchUrgency: Switch
    private lateinit var spinnerCategory: Spinner
    private lateinit var eTxtContent: EditText
    private lateinit var eTxtValue: EditText
    private lateinit var eTxtWeight: EditText
    private lateinit var eTxtDeliveryAddress: EditText
    private lateinit var eTxtDeliveryAddress1: EditText
    private lateinit var eTxtCity: EditText
    private lateinit var eTxtCountry: EditText
    private lateinit var eTxtRecipient: EditText
    private lateinit var eTxtCountryCode: EditText
    private lateinit var eTxtRecNum: EditText
    private lateinit var eTxtDeliveryDate: EditText
    private lateinit var eTxtInstructions: EditText
    private lateinit var TxtTotalCharge: TextView
    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: Button
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var eTxtHeight: EditText
    private lateinit var eTxtWidth: EditText
    private lateinit var eTxtLength: EditText
    private var arrowImageView : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adpost)
        arrowImageView = findViewById(R.id.arrowImageView)
        btnCancel = findViewById(R.id.btnCancel)
        // Initialize UI elements

        switchUrgency = findViewById(R.id.switch1)
        spinnerCategory = findViewById(R.id.category)
        eTxtContent = findViewById(R.id.content)
        eTxtValue = findViewById(R.id.value)
        eTxtWeight = findViewById(R.id.addweight)
        eTxtDeliveryAddress = findViewById(R.id.dlvryAdrs)
        eTxtDeliveryAddress1 = findViewById(R.id.dlvryAdrs1)
        eTxtCity = findViewById(R.id.City)
        eTxtCountry = findViewById(R.id.Country)
        eTxtRecipient = findViewById(R.id.receptientName)
        eTxtRecNum = findViewById(R.id.editTextPhone)
        eTxtDeliveryDate = findViewById(R.id.editTextDate)
        eTxtInstructions = findViewById(R.id.specialInstrctns)
        eTxtHeight = findViewById(R.id.height)
        eTxtWidth = findViewById(R.id.width)
        eTxtLength = findViewById(R.id.length)
        TxtTotalCharge = findViewById(R.id.TtlChrge)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnCancel = findViewById(R.id.btnCancel)
        imageView = findViewById(R.id.uploadImage)
        val categories = arrayOf("Gifts", "Chocolates", "Electronics")

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        val urgencyChargeTextView = findViewById<TextView>(R.id.urgencyChrge)

        switchUrgency.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If urgency is checked, show the TextView
                urgencyChargeTextView.visibility = View.VISIBLE
            } else {
                // If urgency is not checked, hide the TextView
                urgencyChargeTextView.visibility = View.GONE
            }
        }
        eTxtWeight.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // When the eTxtWeight field loses focus, calculate the total charge
                val weight = eTxtWeight.text.toString().toDouble()
                var totalCharge = 0.1 * (800.0 * weight) + (800 * weight)
                if (switchUrgency.isChecked) {
                    // If urgency is true, add an extra 10% charge
                    totalCharge += totalCharge * 0.10
                }
                TxtTotalCharge.text = "Total Charge: $totalCharge"
            }
        }
        btnConfirm.setOnClickListener {
            // Extract data from UI elements
            val urgency = switchUrgency.isChecked
            val category = spinnerCategory.selectedItem.toString()
            val content = eTxtContent.text.toString()
            val value = eTxtValue.text.toString().toDouble()
            val weight = eTxtWeight.text.toString().toDouble()
            val deliveryAddress =
                eTxtDeliveryAddress.text.toString() + eTxtDeliveryAddress1.text.toString()
            val dimension = eTxtHeight.text.toString() +" X "+ eTxtWidth.text.toString()+" X "+eTxtLength.text.toString()
            val city = eTxtCity.text.toString()
            val country = eTxtCountry.text.toString()
            val recipient = eTxtRecipient.text.toString()
            val recipientNumber = eTxtRecNum.text.toString()
            val deliveryDate = eTxtDeliveryDate.text.toString()
            val instructions = eTxtInstructions.text.toString()
            val totalCharge =  0.01 * (800.0 * weight)+(800*0.2)
            val imageData = convertImageToBase64(selectedImageUri)
            TxtTotalCharge.text = "Total Charge: $totalCharge"
            val currentDate = getCurrentDate()

            val currentUser = FirebaseAuth.getInstance().currentUser
            val userID = currentUser?.uid

            // Insert the data into the database
            val connectSQL = ConnectionSQL()
            connectSQL.conclass { connection ->
                if (connection != null) {
                    try {
                        val query =
                            "INSERT INTO AdPosts (urgency, category, content, value, weight, dlvryAddress, city, country, recipient, rcptContactNo, dlvryDate, instructions, ttlCharge, image, dimension, CreatedDate, Created_by)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

                        val preparedStatement = connection.prepareStatement(query)
                        preparedStatement.setInt(1, if (urgency) 1 else 0)
                        preparedStatement.setString(2, category)
                        preparedStatement.setString(3, content)
                        preparedStatement.setDouble(4, value)
                        preparedStatement.setDouble(5, weight)
                        preparedStatement.setString(6, deliveryAddress)
                        preparedStatement.setString(7, city)
                        preparedStatement.setString(8, country)
                        preparedStatement.setString(9, recipient)
                        preparedStatement.setString(10, recipientNumber)
                        preparedStatement.setString(11, deliveryDate)
                        preparedStatement.setString(12, instructions)
                        preparedStatement.setDouble(13, totalCharge)
                        preparedStatement.setString(14, imageData)
                        preparedStatement.setString(15, dimension)
                        preparedStatement.setTimestamp(16, java.sql.Timestamp(currentDate.time)) // Set the postDate
                        preparedStatement.setString(17, userID) // Set the userID

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
        arrowImageView!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
        btnCancel!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.data

                // Display the selected image in the ImageView
                imageView.setImageURI(selectedImageUri)
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
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }
        return "Image is null" // Return an empty string if URI is null
    }
    fun getCurrentDate(): Date {
        return Date() // This will give you the current date and time
    }

}

