package com.example.globe_carry

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.adapter.CommentAdapter
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import java.sql.SQLException

class CommonUserProfile : AppCompatActivity() {

    private var cusAccManagementBack: ImageView? = null
    private var cusMyBookingArrowUp: ImageView? = null
    private var cusMyBookingArrowUpLayout: RelativeLayout? = null
    private var cusMyBookingArrowDown: ImageView? = null
    private var cusMyBookingArrowDownLayout: RelativeLayout? = null
    private var profileContent: LinearLayout? = null
    private var cusAccountProfileImageFrame: CardView? = null
    private var cusAccountProfileImage: ImageView? = null
    private var cusAccountProfileImageEditFrame: FrameLayout? = null
    private var cusAccManageButton1: Button? = null
    private var cusAccManageButton2: Button? = null
    private var editTextFirstName: EditText? = null
    private var editTextFirstName2: TextView? = null
    private var viewInputLastName: EditText? = null
    private var viewInputLastName2: TextView? = null
    private var viewInputNIC: EditText? = null
    private var viewInputNIC2: TextView? = null
    private var viewInputPhoneNo: EditText? = null
    private var viewInputPhoneNo2: TextView? = null
    private var cusAccountAddressLine1: EditText? = null
    private var cusAccountAddressLine11: TextView? = null
    private var cusAccountAddressLine2: EditText? = null
    private var cusAccountAddressLine22: TextView? = null
    private var cusAccountAddressLine3: EditText? = null
    private var cusAccountAddressLine33: TextView? = null
    private var cusAccountCity1: EditText? = null
    private var cusAccountCity2: TextView? = null
    private var cusAccountPostalCode1: EditText? = null
    private var cusAccountPostalCode2: TextView? = null
    private var cusAccountCountry1: EditText? = null
    private var cusAccountCountry2: TextView? = null
    private var profileEditImage: ImageView? = null
    private var cusAccountButtons: LinearLayout? = null
    private var btnCusUpdate: AppCompatButton? = null
    private var btnCusCancel: AppCompatButton? = null

    private var cusMyHistoryArrowUp: ImageView? = null
    private var cusMyHistoryArrowUpLayout: RelativeLayout? = null
    private var cusMyHistoryArrowDown: ImageView? = null
    private var cusMyHistoryArrowDownLayout: RelativeLayout? = null
    private var myHistoryContent: LinearLayout? = null
    private var viewInputGmail: TextView? = null
    private var viewInputAccountCreated: TextView? = null
    private var viewInputcusMyParcelsCount: TextView? = null
    private var viewInputDeliveryCount: TextView? = null
    private var ratingBar: RatingBar? = null
    private var commentsRecyclerView: RecyclerView? = null

    private var imageData: String = ""
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var userAuth: FirebaseAuth
    private var editMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_common_user_profile)
        setContentView(R.layout.common_user_profile_splash)


        userAuth= FirebaseAuth.getInstance()

        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {

                // Your SQL query to fetch customer details
                val user = userAuth.currentUser?.uid ?: ""
                println("User : $user")
                val query = "SELECT firstName, lastName, nic, phoneNo, gmail, addressLine1, addressLine2, addressLine3, city, postalCode, country, userImage, signUpDate ,signUpTime " +
                        "FROM user " +
                        "WHERE userId = '$user' ";

                try {

                    // Create a statement
                    val statement = connection.createStatement()

                    // Execute the query
                    val resultSet = statement.executeQuery(query)

                    var firstName: String? = null
                    var lastName: String? = null
                    var nic: String? = null
                    var phoneNo: String? = null
                    var gmail: String? = null
                    var addressLine1: String? = null
                    var addressLine2: String? = null
                    var addressLine3: String? = null
                    var city: String? = null
                    var postalCode: String? = null
                    var country: String? = null
                    var signUpDate: String? = null
                    var signUpTime: String? = null

                    // Iterate through the result set and log the details
                    while (resultSet.next()) {
                        firstName = resultSet.getString("firstName")?: ""
                        lastName = resultSet.getString("lastName") ?: ""
                        nic = resultSet.getString("nic") ?: ""
                        phoneNo = resultSet.getString("phoneNo")?: ""
                        gmail = resultSet.getString("gmail")?: ""
                        addressLine1 = resultSet.getString("addressLine1")?: ""
                        addressLine2 = resultSet.getString("addressLine2")?: ""
                        addressLine3 = resultSet.getString("addressLine3")?: ""
                        city = resultSet.getString("city") ?: ""
                        postalCode = resultSet.getString("postalCode") ?: ""
                        country = resultSet.getString("country")?: ""
                        imageData = resultSet.getString("userImage")?: ""
                        signUpDate = resultSet.getString("signUpDate")?: ""
                        signUpTime = resultSet.getString("signUpTime")?: ""

                        // Log the customer details
                        Log.d("CustomerDetails", "firstName: $firstName")
                        Log.d("CustomerDetails", "lastName: $lastName")
                        Log.d("CustomerDetails", "nic: $nic")
                        Log.d("CustomerDetails", "phoneNo: $phoneNo")
                        Log.d("CustomerDetails", "gmail: $gmail")
                        Log.d("CustomerDetails", "addressLine1: $addressLine1")
                        Log.d("CustomerDetails", "addressLine2: $addressLine2")
                        Log.d("CustomerDetails", "addressLine3: $addressLine3")
                        Log.d("CustomerDetails", "city: $city")
                        Log.d("CustomerDetails", "postalCode: $postalCode")
                        Log.d("CustomerDetails", "country: $country")
                        Log.d("CustomerDetails", "userImage: $imageData")
                        Log.d("CustomerDetails", "signUpDate: $signUpDate")
                        Log.d("CustomerDetails", "signUpTime: $signUpTime")
                    }
                    // Close the statement and result set
                    statement.close()
                    resultSet.close()
                    switchToCustomerHomeLayout()
                    runOnUiThread {

                        editTextFirstName2?.text = firstName
                        viewInputLastName2?.text = lastName
                        viewInputNIC2?.text = nic
                        viewInputPhoneNo2?.text = phoneNo
                        cusAccountAddressLine11?.text = addressLine1
                        cusAccountAddressLine22?.text = addressLine2
                        cusAccountAddressLine33?.text = addressLine3
                        cusAccountCity2?.text = city
                        cusAccountPostalCode2?.text = postalCode
                        cusAccountCountry2?.text = country
                        viewInputAccountCreated?.text = signUpDate
                        editTextFirstName?.text = Editable.Factory.getInstance().newEditable(firstName)
                        viewInputLastName?.text = Editable.Factory.getInstance().newEditable(lastName)
                        viewInputNIC?.text = Editable.Factory.getInstance().newEditable(nic)
                        viewInputPhoneNo?.text = Editable.Factory.getInstance().newEditable(phoneNo)
                        cusAccountAddressLine1?.text = Editable.Factory.getInstance().newEditable(addressLine1)
                        cusAccountAddressLine2?.text = Editable.Factory.getInstance().newEditable(addressLine2)
                        cusAccountAddressLine3?.text = Editable.Factory.getInstance().newEditable(addressLine3)
                        cusAccountCity1?.text = Editable.Factory.getInstance().newEditable(city)
                        cusAccountPostalCode1?.text = Editable.Factory.getInstance().newEditable(postalCode)
                        cusAccountCountry1?.text = Editable.Factory.getInstance().newEditable(country)

                        viewInputGmail?.text = gmail
                        if (imageData != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            cusAccountProfileImage?.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            cusAccountProfileImage?.setImageResource(R.drawable.cus_image_not_found)
                        }



                        cusAccManagementBack?.setOnClickListener { // Start the CustomerAccountManagement activity
                            finish()
                        }

                        cusAccountProfileImageFrame?.setOnClickListener {
                            if(editMode) {
                                if (cusAccountProfileImageEditFrame?.visibility == View.GONE) {
                                    // Show the edit frame
                                    cusAccountProfileImageEditFrame?.visibility = View.VISIBLE
                                    val delayMillis = 5000 // 5000 milliseconds (5 seconds)

                                    val handler = Handler()
                                    handler.postDelayed({
                                        // Hide the edit frame after the specified delay
                                        cusAccountProfileImageEditFrame?.visibility = View.GONE
                                    }, delayMillis.toLong())
                                } else {
                                    // Hide the edit frame
                                    cusAccountProfileImageEditFrame?.visibility = View.GONE
                                }
                            }
                        }


                        cusAccManageButton1?.setOnClickListener {
                            openGallery()
                        }

                        cusAccManageButton2?.setOnClickListener {
                            if(imageData!="") {
                                // Set ImageDataSingleton.imageData to null
                                imageData = null.toString()

                                // Create an AlertDialog
                                val alertDialogBuilder = AlertDialog.Builder(this)

                                // Set the dialog message and title
                                alertDialogBuilder
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure you want to remove the image?")

                                // Add a "Cancel" button
                                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                                    // Dismiss the dialog if "Cancel" is clicked
                                    dialog.dismiss()
                                }

                                // Add a "Confirm" button
                                alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
                                    imageData = null.toString()
                                    // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                                    cusAccountProfileImage?.setImageResource(R.drawable.cus_image_not_found)
                                    // Dismiss the dialog
                                    dialog.dismiss()
                                }

                                // Create and show the AlertDialog
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            }else(
                                    Toast.makeText(this@CommonUserProfile, "No Image Found", Toast.LENGTH_SHORT,).show()

                                    )
                        }

                        cusMyBookingArrowUp?.setOnClickListener {
                            cusMyBookingArrowUpLayout?.visibility=View.GONE
                            profileContent?.visibility=View.GONE
                            cusMyBookingArrowDownLayout?.visibility=View.VISIBLE
                        }

                        cusMyBookingArrowDown?.setOnClickListener {
                            cusMyBookingArrowDownLayout?.visibility=View.GONE
                            profileContent?.visibility=View.VISIBLE
                            cusMyBookingArrowUpLayout?.visibility=View.VISIBLE
                        }

                        cusMyHistoryArrowUp?.setOnClickListener {
                            cusMyHistoryArrowUpLayout?.visibility=View.GONE
                            myHistoryContent?.visibility=View.GONE
                            cusMyHistoryArrowDownLayout?.visibility=View.VISIBLE
                        }

                        cusMyHistoryArrowDown?.setOnClickListener {
                            cusMyHistoryArrowDownLayout?.visibility=View.GONE
                            myHistoryContent?.visibility=View.VISIBLE
                            cusMyHistoryArrowUpLayout?.visibility=View.VISIBLE
                        }

                        profileEditImage?.setOnClickListener {
                            editMode = true
                            cusAccountButtons?.visibility=View.VISIBLE
                            profileEditImage?.visibility=View.GONE

                            editTextFirstName2?.visibility=View.GONE
                            viewInputLastName2?.visibility=View.GONE
                            viewInputNIC2?.visibility=View.GONE
                            viewInputPhoneNo2?.visibility=View.GONE
                            cusAccountAddressLine11?.visibility=View.GONE
                            cusAccountAddressLine22?.visibility=View.GONE
                            cusAccountAddressLine33?.visibility=View.GONE
                            cusAccountCity2?.visibility=View.GONE
                            cusAccountPostalCode2?.visibility=View.GONE
                            cusAccountCountry2?.visibility=View.GONE

                            editTextFirstName?.visibility=View.VISIBLE
                            viewInputLastName?.visibility=View.VISIBLE
                            viewInputNIC?.visibility=View.VISIBLE
                            viewInputPhoneNo?.visibility=View.VISIBLE
                            cusAccountAddressLine1?.visibility=View.VISIBLE
                            cusAccountAddressLine2?.visibility=View.VISIBLE
                            cusAccountAddressLine3?.visibility=View.VISIBLE
                            cusAccountCity1?.visibility=View.VISIBLE
                            cusAccountPostalCode1?.visibility=View.VISIBLE
                            cusAccountCountry1?.visibility=View.VISIBLE
                        }
                        btnCusUpdate?.setOnClickListener{
                            // Create an AlertDialog
                            val alertDialogBuilder = AlertDialog.Builder(this)

                            // Set the dialog message and title
                            alertDialogBuilder
                                .setTitle("Update Profile")
                                .setMessage("Are you sure you want to update your profile?")

                            // Add a "Cancel" button
                            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                                // Dismiss the dialog if "Cancel" is clicked
                                dialog.dismiss()
                            }

                            // Add a "Confirm" button
                            alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->

                                runOnUiThread {
                                    setContentView(R.layout.activity_common_user_profile)
                                }

                                val cusConSQL = ConnectionSQL()
                                cusConSQL.conclass { connection ->
                                    if (connection != null) {
                                        try {
                                            // Update query with placeholders for binding
                                            val query = "UPDATE user SET firstName = ?, lastName = ?, nic = ?, phoneNo = ?, addressLine1 = ?, addressLine2 = ?, addressLine3 = ?, city = ?, postalCode = ?, country = ?, userImage = ? WHERE userId = ?"

                                            val preparedStatement = connection.prepareStatement(query)
                                            preparedStatement.setString(1, editTextFirstName?.text.toString())
                                            preparedStatement.setString(2, viewInputLastName?.text.toString())
                                            preparedStatement.setString(3, viewInputNIC?.text.toString())
                                            preparedStatement.setString(4, viewInputPhoneNo?.text.toString())
                                            preparedStatement.setString(5, cusAccountAddressLine1?.text.toString())
                                            preparedStatement.setString(6, cusAccountAddressLine2?.text.toString())
                                            preparedStatement.setString(7, cusAccountAddressLine3?.text.toString())
                                            preparedStatement.setString(8, cusAccountCity1?.text.toString())
                                            preparedStatement.setString(9, cusAccountPostalCode1?.text.toString())
                                            preparedStatement.setString(10, cusAccountCountry1?.text.toString())
                                            preparedStatement.setString(11, imageData)
                                            preparedStatement.setString(12, user)

                                            // Execute the update query
                                            preparedStatement.executeUpdate()
                                            preparedStatement.close()

                                            runOnUiThread {
                                                Log.d("btnCusUpdate", "Clicked")
                                                recreate()
                                            }
                                            // Perform any UI updates or navigation as needed
                                            // For example, show a success message or navigate to another screen
                                        } catch (e: SQLException) {
                                            Log.e("Update Error", "SQL Exception: ${e.message}")
                                            e.printStackTrace()
                                            // Handle any errors that occur during the update
                                        } finally {
                                            // Close the connection in the finally block to ensure it's always closed
                                            connection.close()
                                        }
                                    } else {
                                        Log.e("Update Error", "Database connection is null")
                                        // Handle the case where the database connection is null
                                    }
                                }

                                // Dismiss the dialog
                                dialog.dismiss()
                            }

                            // Create and show the AlertDialog
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()

                        }


                        btnCusCancel?.setOnClickListener {
                            Log.d("btnCusCancel", "Clicked")
                            recreate()
//            cusAccountButtons?.visibility=View.GONE
//            profileEditImage?.visibility=View.VISIBLE
//            editTextFirstName?.visibility=View.GONE
//            viewInputLastName?.visibility=View.GONE
//            viewInputNIC?.visibility=View.GONE
//            viewInputPhoneNo?.visibility=View.GONE
//            editTextFirstName2?.visibility=View.VISIBLE
//            viewInputLastName2?.visibility=View.VISIBLE
//            viewInputNIC2?.visibility=View.VISIBLE
//            viewInputPhoneNo2?.visibility=View.VISIBLE
                        }

                        val comments = listOf("Comment 1", "Comment 2", "Comment 3", "Comment 4") // Replace with your comment data
                        val commentAdapter = CommentAdapter(comments)
                        commentsRecyclerView?.adapter = commentAdapter
                    }

                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()
                }finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }
            } else {
                // Handle connection error
                Log.e("TAG", "Connection Error")
            }
        }


    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.data

                // Convert the selected image to Base64
                val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                val bytes = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                    bytes.write(buffer, 0, bytesRead)
                }
                val imageBytes: ByteArray = bytes.toByteArray()
                imageData = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                // Log the Base64-encoded image string
                Log.d("Base64ImageString", imageData)
                // Set the selected image to the ImageView
                cusAccountProfileImage?.setImageURI(selectedImageUri)

                // Use the base64String as needed (e.g., store it in the database)
            }
        }
    }
    private fun switchToCustomerHomeLayout() {
        runOnUiThread {
            setContentView(R.layout.activity_common_user_profile)

            cusAccManagementBack = findViewById(R.id.cusAccManagementBack)
            cusMyBookingArrowUp= findViewById(R.id.cusMyBookingArrowUp)
            cusMyBookingArrowUpLayout= findViewById(R.id.cusMyBookingArrowUpLayout)
            cusMyBookingArrowDown= findViewById(R.id.cusMyBookingArrowDown)
            cusMyBookingArrowDownLayout= findViewById(R.id.cusMyBookingArrowDownLayout)
            profileContent= findViewById(R.id.profileContent)
            cusAccountProfileImageFrame= findViewById(R.id.cusAccountProfileImageFrame)
            cusAccountProfileImage= findViewById(R.id.cusAccountProfileImage)
            cusAccountProfileImageEditFrame= findViewById(R.id.cusAccountProfileImageEditFrame)
            cusAccManageButton1= findViewById(R.id.cusAccManageButton1)

            cusAccManageButton2= findViewById(R.id.cusAccManageButton2)
            editTextFirstName= findViewById(R.id.editTextFirstName)
            editTextFirstName2= findViewById(R.id.editTextFirstName2)
            viewInputLastName= findViewById(R.id.viewInputLastName)
            viewInputLastName2= findViewById(R.id.viewInputLastName2)
            viewInputNIC= findViewById(R.id.viewInputNIC)
            viewInputNIC2= findViewById(R.id.viewInputNIC2)
            viewInputPhoneNo= findViewById(R.id.viewInputPhoneNo)
            viewInputPhoneNo2= findViewById(R.id.viewInputPhoneNo2)

            cusAccountAddressLine1= findViewById(R.id.cusAccountAddressLine1)
            cusAccountAddressLine11= findViewById(R.id.cusAccountAddressLine11)
            cusAccountAddressLine2= findViewById(R.id.cusAccountAddressLine2)
            cusAccountAddressLine22= findViewById(R.id.cusAccountAddressLine22)
            cusAccountAddressLine3= findViewById(R.id.cusAccountAddressLine3)
            cusAccountAddressLine33= findViewById(R.id.cusAccountAddressLine33)
            cusAccountCity1= findViewById(R.id.cusAccountCity1)
            cusAccountCity2= findViewById(R.id.cusAccountCity2)
            cusAccountPostalCode1= findViewById(R.id.cusAccountPostalCode1)
            cusAccountPostalCode2= findViewById(R.id.cusAccountPostalCode2)
            cusAccountCountry1= findViewById(R.id.cusAccountCountry1)
            cusAccountCountry2= findViewById(R.id.cusAccountCountry2)

            profileEditImage= findViewById(R.id.profileEditImage)
            cusAccountButtons= findViewById(R.id.cusAccountButtons)
            btnCusUpdate= findViewById(R.id.btnCusUpdate)
            btnCusCancel= findViewById(R.id.btnCusCancel)
            cusMyHistoryArrowUp= findViewById(R.id.cusMyHistoryArrowUp)
            cusMyHistoryArrowUpLayout= findViewById(R.id.cusMyHistoryArrowUpLayout)
            cusMyHistoryArrowDown= findViewById(R.id.cusMyHistoryArrowDown)
            cusMyHistoryArrowDownLayout= findViewById(R.id.cusMyHistoryArrowDownLayout)

            myHistoryContent= findViewById(R.id.myHistoryContent)
            viewInputGmail= findViewById(R.id.viewInputGmail)
            viewInputAccountCreated= findViewById(R.id.viewInputAccountCreated)
//        viewInputcusMyParcelsCount= findViewById(R.id.viewInputcusMyParcelsCount)
//        viewInputDeliveryCount= findViewById(R.id.viewInputDeliveryCount)
//        ratingBar= findViewById(R.id.ratingBar)

            commentsRecyclerView= findViewById(R.id.commentsRecyclerView)

        }
    }

}