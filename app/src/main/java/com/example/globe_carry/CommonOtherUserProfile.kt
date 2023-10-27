package com.example.globe_carry

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.globe_carry.adapter.CommentAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.sql.SQLException

class CommonOtherUserProfile : AppCompatActivity() {

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
    private var editTextFirstName2: TextView? = null
    private var viewInputLastName2: TextView? = null
    private var viewInputNIC2: TextView? = null
    private var viewInputPhoneNo2: TextView? = null
    private var cusAccountAddressLine11: TextView? = null
    private var cusAccountAddressLine22: TextView? = null
    private var cusAccountAddressLine33: TextView? = null
    private var cusAccountCity2: TextView? = null
    private var cusAccountPostalCode2: TextView? = null
    private var cusAccountCountry2: TextView? = null

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

    private var runningManImageView: ImageView? = null

    private var imageData: String = ""
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var userAuth: FirebaseAuth
    private var editMode: Boolean = false

    private var commentEditTextInput: EditText? = null
    private var ratingBarInput: RatingBar? = null
    private var updateReview: Button? = null


    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    var user : String = ""
    var userFromIntent : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_common_user_profile)
        setContentView(R.layout.common_user_profile_splash)

        val runningManImageView = findViewById<ImageView>(R.id.runningManImageView1)

        // Calculate the width of the screen for animation bounds
        val screenWidth = resources.displayMetrics.widthPixels

        // Create an ObjectAnimator to animate translation from left to right
        val translationAnimator = ObjectAnimator.ofFloat(
            runningManImageView,
            "translationX",
            -screenWidth.toFloat(),
            screenWidth.toFloat()
        )

        // Set the animator duration
        translationAnimator.duration = 2000  // Adjust the duration as needed

        // Set the repeat mode to reverse for back-and-forth animation
        translationAnimator.repeatMode = ObjectAnimator.RESTART
        translationAnimator.repeatCount = ObjectAnimator.INFINITE

        // Start the animation
        translationAnimator.start()


        userAuth= FirebaseAuth.getInstance()
        user = userAuth.currentUser?.uid ?: ""
        userFromIntent = intent.getStringExtra("userFromIntent").toString()

        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                val intent = intent
                // Your SQL query to fetch customer details

                println("user : $user")
                println("userFromIntent : $userFromIntent")
                var query = "SELECT firstName, lastName, nic, phoneNo, gmail, addressLine1, addressLine2, addressLine3, city, postalCode, country, userImage, signUpDate ,signUpTime " +
                        "FROM user " +
                        "WHERE userId = '$userFromIntent' ";
//                val query = "SELECT u.*, SUM(r.ratings) AS totalRatings " +
//                        "FROM user u, ratings r " +
//                        "WHERE u.userId = r.ratingsToId " +
//                        "AND u.userId = '$userFromIntent' " +
//                        "GROUP BY u.userId"

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
                    var totalRatings = 0f
                    val comments = mutableListOf<CommentData>()
                    var myComment = ""
                    var myrating = 0f

                    var deliveryCount = 0
                    var parcelsCount = 0

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
//                        Log.d("CustomerDetails", "userImage: $imageData")
                        Log.d("CustomerDetails", "signUpDate: $signUpDate")
                        Log.d("CustomerDetails", "signUpTime: $signUpTime")
                    }
                    // Close the statement and result set
                    statement.close()
                    resultSet.close()

                    val query1 = "SELECT SUM(ratings)/COUNT(ratings) AS totalRatings " +
                            "FROM ratings " +
                            "WHERE ratingsToId = '$userFromIntent' " +
                            "GROUP BY ratingsToId"

                    try {

                        // Create a statement
                        val statement1 = connection.createStatement()

                        // Execute the query
                        val resultSet1 = statement1.executeQuery(query1)

                        // Iterate through the result set and log the details
                        while (resultSet1.next()) {
                            totalRatings = resultSet1.getFloat("totalRatings")
                            Log.d("CustomerDetails", "totalRatings: $totalRatings")
                        }
                        // Close the statement and result set
                        statement1.close()
                        resultSet1.close()
                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()
                    }


                    val query2 = "SELECT r.comments, u.userId, u.gmail " +
                            "FROM ratings r, user u " +
                            "WHERE r.ratingsToId = '$userFromIntent' AND r.ratingsFromId = u.userId "

                    try {

                        // Create a statement
                        val statement2 = connection.createStatement()

                        // Execute the query
                        val resultSet2 = statement2.executeQuery(query2)

                        // Iterate through the result set and log the details
                        while (resultSet2.next()) {
                            var comment = resultSet2.getString("comments") ?: ""
                            var commentUserId = resultSet2.getString("userId") ?: ""
                            var commentGmail = resultSet2.getString("gmail") ?: ""
                            if(comment!="") {
                                Log.d("CustomerDetails", "comments: $comment")
                                val commentItem = CommentData(comment,commentGmail,commentUserId)
                                comments.add(commentItem)
                            }
                        }
                        // Close the statement and result set
                        statement2.close()
                        resultSet2.close()
                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()
                    }

                    val query3 = "SELECT ratings,comments " +
                            "FROM ratings " +
                            "WHERE ratingsToId = '$userFromIntent' AND ratingsFromId = '$user'"

                    try {

                        // Create a statement
                        val statement3 = connection.createStatement()

                        // Execute the query
                        val resultSet3 = statement3.executeQuery(query3)

                        // Iterate through the result set and log the details
                        while (resultSet3.next()) {
                            myrating = resultSet3.getFloat("ratings")
                            myComment = resultSet3.getString("comments") ?: ""
                            Log.d("CustomerDetails", "myComments: $myrating")
                            Log.d("CustomerDetails", "myComments: $myComment")
                        }
                        // Close the statement and result set
                        statement3.close()
                        resultSet3.close()
                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()
                    }

                    val query10 = "SELECT COUNT(orderstatus_id) AS deliveryCount " +
                                    "FROM orderstatus " +
                                    "WHERE acptdTravllerId = '$userFromIntent' AND delivered = TRUE " +
                                    "GROUP BY acptdTravllerId "

                    try {

                        // Create a statement
                        val statement3 = connection.createStatement()

                        // Execute the query
                        val resultSet3 = statement3.executeQuery(query10)

                        // Iterate through the result set and log the details
                        while (resultSet3.next()) {
                            deliveryCount = resultSet3.getInt("deliveryCount")
                            Log.d("CustomerDetails", "deliveryCount: $deliveryCount")
                        }
                        // Close the statement and result set
                        statement3.close()
                        resultSet3.close()
                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()
                    }

                    val query11 = "SELECT count(o.orderstatus_id) AS parcelsCount " +
                            "FROM orderstatus o, AdPosts a " +
                            "WHERE a.Created_by = '$userFromIntent' AND a.postid = o.postid AND o.delivered = TRUE " +
                            "GROUP BY a.Created_by "

                    try {

                        // Create a statement
                        val statement3 = connection.createStatement()

                        // Execute the query
                        val resultSet3 = statement3.executeQuery(query11)

                        // Iterate through the result set and log the details
                        while (resultSet3.next()) {
                            parcelsCount = resultSet3.getInt("parcelsCount")
                            Log.d("CustomerDetails", "parcelsCount: $parcelsCount")
                        }
                        // Close the statement and result set
                        statement3.close()
                        resultSet3.close()
                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()
                    }


                    runOnUiThread {
                        runningManImageView?.visibility = View.GONE
                        translationAnimator.cancel()
                    }
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
                        viewInputGmail?.text = gmail

                        viewInputDeliveryCount?.text = deliveryCount.toString()
                        viewInputcusMyParcelsCount?.text = parcelsCount.toString()

                            ratingBar?.rating = totalRatings
                        // Set the retrieved values in the RatingBar and EditText
                        ratingBarInput?.rating = myrating // Set the rating in the RatingBar
                        commentEditTextInput?.setText(myComment) // Set the comment in the EditText

                        val originalRating = myrating // Store the original rating
                        val originalComment = myComment // Store the original comment

                        ratingBarInput?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            if (rating != originalRating || commentEditTextInput?.text.toString() != originalComment) {
                                // Changes detected, show the "Update" button
                                updateReview?.visibility = View.VISIBLE
                            } else {
                                // No changes, hide the "Update" button
                                updateReview?.visibility = View.GONE
                            }
                        }

                        commentEditTextInput?.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }

                            override fun afterTextChanged(editable: Editable?) {
                                val comment = editable.toString().trim()
                                if (ratingBarInput?.rating != originalRating || comment != originalComment) {
                                    // Changes detected, show the "Update" button
                                    updateReview?.visibility = View.VISIBLE
                                } else {
                                    // No changes, hide the "Update" button
                                    updateReview?.visibility = View.GONE
                                }
                            }
                        })

                        updateReview?.setOnClickListener {
                            Log.d("Update", "Confirm")

                            val updatedRating = ratingBarInput?.rating
                            val updatedComment = commentEditTextInput?.text.toString().trim()

                            // Check if ratings were changed or comments were changed or both are empty
                            val ratingChanged = updatedRating != myrating || updatedComment != myComment
                            val bothEmpty = (updatedRating == 0f || updatedRating.toString() == "0.0") && (updatedComment.isEmpty() || updatedComment == "")

                            Log.d("ratingChanged", ratingChanged.toString())
                            Log.d("bothEmpty", bothEmpty.toString())

                            Log.d("updatedRating", updatedRating.toString())
                            Log.d("updatedComment", updatedComment)
                            Log.d("myrating", myrating.toString())
                            Log.d("myComment", myComment)

                            if (ratingChanged) {

                                runOnUiThread {
                                    setContentView(R.layout.common_user_profile_splash)
                                }


                                val cusConSQL2 = ConnectionSQL()
                                cusConSQL2.conclass { connection2 ->
                                    if (connection2 != null) {
                                        try {

                                            var query4 : String = ""


                                            if (originalRating == 0f && originalComment.isEmpty()) {
                                                Log.d("if", "Insert")
                                                // No existing data found, so insert a new record
                                                query4 = "INSERT INTO ratings (ratingsFromId, ratingsToId, ratings, comments) VALUES (?, ?, ?, ?)"
                                                val preparedStatement = connection2.prepareStatement(query4)
                                                preparedStatement.setString(1, user)
                                                preparedStatement.setString(2, userFromIntent)
                                                preparedStatement.setString(3, updatedRating.toString())
                                                preparedStatement.setString(4,
                                                    commentEditTextInput?.text.toString()
                                                )
                                                // Execute the update query
                                                preparedStatement.executeUpdate()
                                                preparedStatement.close()
                                            } else {

                                                // Existing data found, so update the record
                                                if(bothEmpty){
                                                    Log.d("if", "Delete")
                                                    query4 = "DELETE FROM ratings " +
                                                            "WHERE ratingsFromId = ? AND ratingsToId = ?"
                                                    val preparedStatement =
                                                        connection2.prepareStatement(query4)
                                                    preparedStatement.setString(1, user)
                                                    preparedStatement.setString(2, userFromIntent)
                                                    // Execute the update query
                                                    preparedStatement.executeUpdate()
                                                    preparedStatement.close()
                                                }else {
                                                    Log.d("if", "Update")
                                                    query4 = "UPDATE ratings " +
                                                            "SET ratings = ?, comments = ? " +
                                                            "WHERE ratingsFromId = ? AND ratingsToId = ?"
                                                    val preparedStatement =
                                                        connection2.prepareStatement(query4)
                                                    preparedStatement.setString(
                                                        1,
                                                        updatedRating.toString()
                                                    )
                                                    preparedStatement.setString(2,  commentEditTextInput?.text.toString())
                                                    preparedStatement.setString(3, user)
                                                    preparedStatement.setString(4, userFromIntent)
                                                    // Execute the update query
                                                    preparedStatement.executeUpdate()
                                                    preparedStatement.close()
                                                }
                                            }

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
                                            connection2.close()
                                        }
                                    } else {
                                        Log.e("Update Error", "Database connection is null")
                                        // Handle the case where the database connection is null
                                    }
                                }
                            }else{
                                // Show a message to the user indicating that no changes were made
                                Toast.makeText(this, "No changes to update.", Toast.LENGTH_SHORT).show()
                            }
                        }

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

                        val profileImageView = findViewById<ImageView>(R.id.profile_image)

                        if (SingleProfile.profileImage != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(SingleProfile.profileImage, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            profileImageView.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            profileImageView.setImageResource(R.drawable.profile_place_holder)
                        }


                        profileImageView.setOnClickListener { view ->
                            showPopupMenu(view)
                        }


                        cusAccountProfileImage?.setOnClickListener {
                            if (imageData != "") {
                                val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
                                val decodedBitmap = BitmapFactory.decodeByteArray(
                                    decodedBytes,
                                    0,
                                    decodedBytes.size
                                )

                                // Inflate the dialog layout
                                val dialogView = LayoutInflater.from(this)
                                    .inflate(R.layout.dialog_large_image, null)

                                // Find the ImageView in the dialog layout
                                val largeImageView =
                                    dialogView.findViewById<ImageView>(R.id.largeImageView)

                                // Set the image to the larger ImageView
                                largeImageView.setImageBitmap(decodedBitmap)
                                // Create and show the dialog
                                val builder = AlertDialog.Builder(this)
                                builder.setView(dialogView)
                                val dialog = builder.create()
                                dialog.show()
                            }
                        }



                        cusAccManagementBack?.setOnClickListener { // Start the CustomerAccountManagement activity
                            finish()
                        }

                        swipeRefreshLayout?.setOnRefreshListener {
                            // This is where you handle the refresh action.
                            recreate()
                            swipeRefreshLayout?.isRefreshing = false
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

                                // Create a custom dialog
                                val dialog = Dialog(this)

                                // Set the custom layout for the dialog
                                dialog.setContentView(R.layout.profile_popup)

                                // Set the width of the dialog to match the parent's width
                                val layoutParams = WindowManager.LayoutParams()
                                layoutParams.copyFrom(dialog.window?.attributes)

                                // Get the display metrics to calculate the width
                                val displayMetrics = DisplayMetrics()
                                windowManager.defaultDisplay.getMetrics(displayMetrics)

                                val screenWidth = displayMetrics.widthPixels
                                val screenHeight = displayMetrics.heightPixels
                                val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

                                val dialogWidthPercent = if (isPortrait) 0.9 else 0.6
                                var dialogWidth = (if (isPortrait) screenHeight else screenWidth) * dialogWidthPercent

                                // Ensure the dialog width doesn't exceed the screen width
                                if (dialogWidth > screenWidth) {
                                    dialogWidth = screenWidth * 0.9 // Cap it at 80% of the screen width
                                }

                                // Set the calculated width to the layout parameters
                                layoutParams.width = dialogWidth.toInt()

                                dialog.window?.attributes = layoutParams

                                val btnConfirmCusUpdate = dialog.findViewById<AppCompatButton>(R.id.btnConfirmCusUpdate)
                                val btnConfirmCusCancel = dialog.findViewById<AppCompatButton>(R.id.btnConfirmCusCancel)
                                val tv_title = dialog.findViewById<TextView>(R.id.tv_title)

                                tv_title.text="Are you sure you want to remove the image?"
                                btnConfirmCusUpdate.text = "Update" // Change the text as needed
                                btnConfirmCusCancel.text = "Cancel" // Change the text as needed

                                btnConfirmCusCancel.setOnClickListener {
                                    Log.d("Photo","Cancel")
                                    dialog.dismiss()
                                }



                                btnConfirmCusUpdate.setOnClickListener {
                                    Log.d("Photo","Confirm")
                                    imageData = null.toString()
                                    // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                                    cusAccountProfileImage?.setImageResource(R.drawable.cus_image_not_found)
                                    // Dismiss the dialog
                                    dialog.dismiss()
                                }

                                dialog.show()

                            }else(
                                    Toast.makeText(this@CommonOtherUserProfile, "No Image Found", Toast.LENGTH_SHORT,).show()

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

                        if (comments.isEmpty()) {
                            Log.d("CustomerDetails", "No comments found.")
                            var comment = "No comments yet"
                            val commentItem = CommentData(comment,"","")
                            comments.add(commentItem)
                        }
                        val commentAdapter = CommentAdapter(this, comments)
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
            setContentView(R.layout.activity_common_other_user_profile)

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
            editTextFirstName2= findViewById(R.id.editTextFirstName2)
            viewInputLastName2= findViewById(R.id.viewInputLastName2)
            viewInputNIC2= findViewById(R.id.viewInputNIC2)
            viewInputPhoneNo2= findViewById(R.id.viewInputPhoneNo2)

            cusAccountAddressLine11= findViewById(R.id.cusAccountAddressLine11)
            cusAccountAddressLine22= findViewById(R.id.cusAccountAddressLine22)
            cusAccountAddressLine33= findViewById(R.id.cusAccountAddressLine33)
            cusAccountCity2= findViewById(R.id.cusAccountCity2)
            cusAccountPostalCode2= findViewById(R.id.cusAccountPostalCode2)
            cusAccountCountry2= findViewById(R.id.cusAccountCountry2)

            cusMyHistoryArrowUp= findViewById(R.id.cusMyHistoryArrowUp)
            cusMyHistoryArrowUpLayout= findViewById(R.id.cusMyHistoryArrowUpLayout)
            cusMyHistoryArrowDown= findViewById(R.id.cusMyHistoryArrowDown)
            cusMyHistoryArrowDownLayout= findViewById(R.id.cusMyHistoryArrowDownLayout)

            myHistoryContent= findViewById(R.id.myHistoryContent)
            viewInputGmail= findViewById(R.id.viewInputGmail)
            viewInputAccountCreated= findViewById(R.id.viewInputAccountCreated)
            viewInputcusMyParcelsCount= findViewById(R.id.viewInputcusMyParcelsCount)
            viewInputDeliveryCount= findViewById(R.id.viewInputDeliveryCount)
            ratingBar= findViewById(R.id.ratingBar)

            swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

            ratingBarInput= findViewById(R.id.ratingBarInput)
            commentEditTextInput= findViewById(R.id.commentEditTextInput)
            updateReview= findViewById(R.id.updateReview)

            commentsRecyclerView= findViewById(R.id.commentsRecyclerView)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    // Perform the logout action
                    userAuth= FirebaseAuth.getInstance()
                    userAuth.signOut()
                    val intent = Intent(this@CommonOtherUserProfile, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {
                R.id.help -> {
                    val intent = Intent(this@CommonOtherUserProfile,HelpCenter::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {

                R.id.profile -> {
                    val intent = Intent(this@CommonOtherUserProfile, CommonUserProfile::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
        }

        popupMenu.show()
    }

}