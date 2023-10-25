package com.example.globe_carry

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.math.BigDecimal
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyDeliveriesFullView : AppCompatActivity() {

    private var cusMyBookingArrowUpLayout : RelativeLayout? = null
    private var cusMyBookingArrowDownLayout : RelativeLayout? = null
    private var hideParcelDetails : LinearLayout? = null
    private var cusMyBookingArrowUp : ImageView? = null
    private var cusMyBookingArrowDown : ImageView? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var arrowImageView : ImageView? = null

    var isAppColor = false // Initial state

    private var paymentImage : ImageView? = null
    private var receivedImage : ImageView? = null
    private var departedImage : ImageView? = null
    private var arrivedImage : ImageView? = null
    private var deliveredImage : ImageView? = null
    private var paymentImageLine : View? = null
    private var receivedImageLine : View? = null
    private var departedImageLine : View? = null
    private var arrivedImageLine : View? = null

    private var receivedImageButton : Button? = null
    private var departedImageButton : Button? = null
    private var arrivedImageButton : Button? = null
    private var deliveredImageButton : Button? = null
    private var receivedImageText: TextView? = null
    private var departedImageText : TextView? = null
    private var arrivedImageText : TextView? = null
    private var deliveredImageText : TextView? = null

    private var detailImage : ImageView? = null
    private var viewTxtUrgent1 : TextView? = null
    private var viewTxtUrgent2 : TextView? = null
    private var viewCusName : TextView? = null
    private var viewCusNum : TextView? = null
    private var viewdlvrydate : TextView? = null
    private var viewCategory : TextView? = null
    private var viewContent : TextView? = null
    private var viewValue : TextView? = null
    private var viewWeight : TextView? = null
    private var viewDimension : TextView? = null
    private var viewdlvryAddrs : TextView? = null
    private var viewCity : TextView? = null
    private var viewCountry : TextView? = null
    private var viewRecName : TextView? = null
    private var viewRecNum : TextView? = null
    private var viewSpclIns : TextView? = null

    private var viewParcelCharge : TextView? = null
    private var viewParcelAssignedUser : TextView? = null
    private var viewFlightDate : TextView? = null
    private var viewParcelDocuments1 : ImageView? = null
    private var viewParcelDocuments2 : ImageView? = null
    private var viewParcelDocuments3 : ImageView? = null

    private lateinit var userAuth: FirebaseAuth
    var translationAnimator : ObjectAnimator?= null

    private var fromDeliveredAdapter: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_user_profile_splash)

        userAuth= FirebaseAuth.getInstance()

        val runningManImageView = findViewById<ImageView>(R.id.runningManImageView1)

        // Calculate the width of the screen for animation bounds
        val screenWidth = resources.displayMetrics.widthPixels

        // Create an ObjectAnimator to animate translation from left to right
        translationAnimator = ObjectAnimator.ofFloat(
            runningManImageView,
            "translationX",
            -screenWidth.toFloat(),
            screenWidth.toFloat()
        )

        // Set the animator duration
        translationAnimator?.duration = 2000  // Adjust the duration as needed

        // Set the repeat mode to reverse for back-and-forth animation
        translationAnimator?.repeatMode = ObjectAnimator.RESTART
        translationAnimator?.repeatCount = ObjectAnimator.INFINITE

        // Start the animation
        translationAnimator?.start()


        val pId = intent.getIntExtra("postId",0)
        val oid = intent.getIntExtra("orderstatus_id",0)
        fromDeliveredAdapter =  intent.getBooleanExtra("fromDeliveredAdapter", false)
        Log.d("MyParcelFullView", pId.toString())
        Log.d("MyParcelFullView", oid.toString())
        Log.d("MyParcelFullView", fromDeliveredAdapter.toString())

        var postId : Int ?= 0
        var urgency : Boolean ?=false
        var category : String ?= null
        var content : String ?= null
        var value : BigDecimal ?= BigDecimal.ZERO
        var weight : BigDecimal ?= BigDecimal.ZERO
        var dlvryAddress : String ?= null
        var city : String ?= null
        var country : String ?= null
        var recipient : String ?= null
        var rcptContactNo : String ?= null
        var dlvryDate : String ?= null
        var instructions : String ?= null
        var ttlCharge : BigDecimal ?= BigDecimal.ZERO
        var dimension : String ?= null
        var createdDate : String ?= null
        var createdBy : String ?= null
        var imageBytes : String ?= null

        var orderstatus_id : Int ?= 0
        var received : Boolean ?=false
        var delivered : Boolean ?=false
        var paid : Boolean ?=false
        var departed : Boolean ?=false
        var reached : Boolean ?=false

        var firstName : String ?= null
        var lastName : String ?= null
        var phoneNo : String ?= null
        var cityOrgin : String ?= null
        var countryOrgin : String ?= null

        var flightDate : String ?= null
        var passport : String ?= null
        var orgin : String ?= null
        var passportImage : String ?= null
        var ticketImage : String ?= null
        var travellerImage : String ?= null

        var myFirstName : String ?= null
        var myLastName : String ?= null

        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                val user = userAuth.currentUser?.uid ?: ""

                val query =
                    "SELECT a.*, o.*, u.firstName AS firstName, u.lastName AS lastName, u.phoneNo as phoneNo, u.city AS cityOrgin, u.country AS countryOrgin, v.*, u2.firstName AS myFirstName, u2.lastName AS myLastName " +
                            "FROM AdPosts a, orderstatus o, user u, verification v, user u2 " +
                            "WHERE a.postid = o.postid AND o.postid = ? AND a.Created_by = u.userId AND v.Postid = o.postid AND v.TravellerID = o.acptdTravllerId AND o.acptdTravllerId = u2.userId AND o.acptdTravllerId = ?";

                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, pId.toString())
                    preparedStatement.setString(2, user)

                    val resultSet = preparedStatement.executeQuery()

                    while (resultSet.next()) {
                        // Parse data from the result set
                        postId = resultSet.getInt("postid")
                        urgency = resultSet.getBoolean("urgency")
                        category = resultSet.getString("category") ?: ""
                        content = resultSet.getString("content") ?: ""
                        value = resultSet.getBigDecimal("value") ?: BigDecimal.ZERO
                        weight = resultSet.getBigDecimal("weight") ?: BigDecimal.ZERO
                        dlvryAddress = resultSet.getString("dlvryAddress") ?: ""
                        city = resultSet.getString("city") ?: ""
                        country = resultSet.getString("country") ?: ""
                        recipient = resultSet.getString("recipient") ?: ""
                        rcptContactNo = resultSet.getString("rcptContactNo") ?: ""
                        dlvryDate = resultSet.getString("dlvryDate") ?: ""
                        instructions = resultSet.getString("instructions") ?: ""
                        ttlCharge = resultSet.getBigDecimal("ttlCharge") ?: BigDecimal.ZERO
                        dimension = resultSet.getString("dimension") ?: ""
                        createdDate = resultSet.getString("createdDate") ?: ""
                        createdBy = resultSet.getString("Created_by") ?: ""
                        imageBytes = resultSet.getString("image") ?: ""

                        orderstatus_id = resultSet.getInt("orderstatus_id")
                        received = resultSet.getBoolean("received")
                        delivered = resultSet.getBoolean("delivered")
                        paid = resultSet.getBoolean("paid")
                        departed = resultSet.getBoolean("departed")
                        reached = resultSet.getBoolean("reached")

                        firstName = resultSet.getString("firstName") ?: ""
                        lastName = resultSet.getString("lastName") ?: ""
                        phoneNo = resultSet.getString("phoneNo") ?: ""
                        cityOrgin = resultSet.getString("cityOrgin") ?: ""
                        countryOrgin = resultSet.getString("countryOrgin") ?: ""

                        flightDate = resultSet.getString("FlightDate") ?: ""
                        passport = resultSet.getString("passport") ?: ""
                        orgin = resultSet.getString("orgin") ?: ""
                        passportImage = resultSet.getString("PassportImage") ?: ""
                        ticketImage = resultSet.getString("TicketImage") ?: ""
                        travellerImage = resultSet.getString("TravellerImage") ?: ""

                        myFirstName = resultSet.getString("myFirstName") ?: ""
                        myLastName = resultSet.getString("myLastName") ?: ""

                        Log.d("Query ", "Query is successful")

                        Log.d("MyParcel", "PostNo: $postId")
                        Log.d("MyParcel", "urgency: $urgency")
                        Log.d("MyParcel", "category: $category")
                        Log.d("MyParcel", "content: $content")
                        Log.d("MyParcel", "value: $value")
                        Log.d("MyParcel", "weight: $weight")
                        Log.d("MyParcel", "dlvryAddress: $dlvryAddress")
                        Log.d("MyParcel", "city: $city")
                        Log.d("MyParcel", "country: $country")
                        Log.d("MyParcel", "recipient: $recipient")
                        Log.d("MyParcel", "rcptContactNo: $rcptContactNo")
                        Log.d("MyParcel", "dlvryDate: $dlvryDate")
                        Log.d("MyParcel", "instructions: $instructions")
                        Log.d("MyParcel", "ttlCharge: $ttlCharge")
                        Log.d("MyParcel", "dimension: $dimension")
                        Log.d("MyParcel", "createdDate: $createdDate")
                        Log.d("MyParcel", "createdBy: $createdBy")
                        Log.d("MyParcel", "imageBytes: $imageBytes")

                        Log.d("MyParcel", "orderstatus_id: $orderstatus_id")
                        Log.d("MyParcel", "received: $received")
                        Log.d("MyParcel", "delivered: $delivered")
                        Log.d("MyParcel", "paid: $paid")
                        Log.d("MyParcel", "departed: $departed")
                        Log.d("MyParcel", "reached: $reached")


                        Log.d("MyParcel", "firstName: $firstName")
                        Log.d("MyParcel", "lastName: $lastName")
                        Log.d("MyParcel", "phoneNo: $phoneNo")
                        Log.d("MyParcel", "cityOrgin: $cityOrgin")
                        Log.d("MyParcel", "countryOrgin: $countryOrgin")

                        Log.d("MyParcel", "flightDate: $flightDate")
                        Log.d("MyParcel", "passport: $passport")
                        Log.d("MyParcel", "orgin: $orgin")

                        Log.d("MyParcel", "myFirstName: $myFirstName")
                        Log.d("MyParcel", "myLastNamen: $myLastName")

                    }

                    resultSet.close()
                    preparedStatement.close()

                    switchToCustomerHomeLayout()

                    runOnUiThread {
                        val profileImageView = findViewById<ImageView>(R.id.profile_image)

                        profileImageView.setOnClickListener { view ->
                            showPopupMenu(view)
                        }


                        arrowImageView?.setOnClickListener {
                            if(fromDeliveredAdapter){
                                finish()
                            }else {
                                val intent = Intent(this, CommonHome::class.java)
                                intent.putExtra("FRAGMENT_TO_SHOW", "MyDeliveriesFragment")
                                startActivity(intent)
                            }
                        }

                        if(urgency == true) {
                            viewTxtUrgent1?.visibility = View.VISIBLE
                            viewTxtUrgent2?.visibility = View.VISIBLE
                        }

                        cusMyBookingArrowDown?.setOnClickListener {
                            hideParcelDetails?.visibility = View.VISIBLE
                            cusMyBookingArrowUpLayout?.visibility = View.VISIBLE
                            cusMyBookingArrowDownLayout?.visibility = View.GONE
                        }

                        cusMyBookingArrowUp?.setOnClickListener {
                            hideParcelDetails?.visibility = View.GONE
                            cusMyBookingArrowUpLayout?.visibility = View.GONE
                            cusMyBookingArrowDownLayout?.visibility = View.VISIBLE
                        }

                        swipeRefreshLayout?.setOnRefreshListener {
                            // This is where you handle the refresh action.
                            recreate()
                            swipeRefreshLayout?.isRefreshing = false
                        }

                        if (imageBytes != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(imageBytes, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            detailImage?.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            detailImage?.setImageResource(R.drawable.cus_image_not_found)
                        }

                        val name = "$firstName $lastName"
                        viewCusName?.text = name
                        viewCusNum?.text =  phoneNo
                        viewdlvrydate?.text = dlvryDate
                        viewCategory?.text = category
                        viewContent?.text = content
                        viewValue?.text = value.toString()
                        viewWeight?.text = weight.toString()
                        viewDimension?.text = dimension
                        viewdlvryAddrs?.text = dlvryAddress
                        viewCity?.text = city
                        viewCountry?.text = country
                        viewRecName?.text = recipient
                        viewRecNum?.text = rcptContactNo
                        viewSpclIns?.text = instructions

                        viewParcelCharge?.text = ttlCharge.toString()
                        val myName = myFirstName + " " +myLastName
                        viewParcelAssignedUser?.text = myName
                        viewFlightDate?.text = flightDate

                        if (passportImage != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(passportImage, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            viewParcelDocuments1?.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            viewParcelDocuments1?.setImageResource(R.drawable.cus_image_not_found)
                        }

                        if (ticketImage != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(ticketImage, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            viewParcelDocuments2?.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            viewParcelDocuments2?.setImageResource(R.drawable.cus_image_not_found)
                        }

                        if (travellerImage != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(travellerImage, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            viewParcelDocuments3?.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            viewParcelDocuments3?.setImageResource(R.drawable.cus_image_not_found)
                        }

                        detailImage?.setOnClickListener {
                            if (imageBytes != "") {
                                val decodedBytes = Base64.decode(imageBytes, Base64.DEFAULT)
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

                        viewParcelDocuments1?.setOnClickListener {
                            if (passportImage != "") {
                                val decodedBytes = Base64.decode(passportImage, Base64.DEFAULT)
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

                        viewParcelDocuments2?.setOnClickListener {
                            if (ticketImage != "") {
                                val decodedBytes = Base64.decode(ticketImage, Base64.DEFAULT)
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

                        viewParcelDocuments3?.setOnClickListener {
                            if (travellerImage != "") {
                                val decodedBytes = Base64.decode(travellerImage, Base64.DEFAULT)
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

                        if(paid == true) {
                            paymentImage?.setBackgroundResource(R.drawable.circle_background_delivery_appcolor)
                            paymentImage?.setImageResource(R.drawable.img_my_deliveries_payment_completed)
                        }
                        if(received == true){
                            receivedImage?.setBackgroundResource(R.drawable.circle_background_delivery_appcolor)
                            receivedImage?.setImageResource(R.drawable.img_my_deliveries_received_completed)
                            paymentImageLine?.setBackgroundResource(R.color.appcolour)
                        }
                        if(departed == true){
                            departedImage?.setBackgroundResource(R.drawable.circle_background_delivery_appcolor)
                            departedImage?.setImageResource(R.drawable.img_my_deliveries_departed_completed)
                            receivedImageLine?.setBackgroundResource(R.color.appcolour)
                        }
                        if(reached == true){
                            arrivedImage?.setBackgroundResource(R.drawable.circle_background_delivery_appcolor)
                            arrivedImage?.setImageResource(R.drawable.img_my_deliveries_arrived_completed)
                            departedImageLine?.setBackgroundResource(R.color.appcolour)
                        }
                        if(delivered == true){
                            deliveredImage?.setBackgroundResource(R.drawable.circle_background_delivery_appcolor)
                            deliveredImage?.setImageResource(R.drawable.img_my_deliveries_received_completed)
                            arrivedImageLine?.setBackgroundResource(R.color.appcolour)
                        }

                        if(delivered == true){

                        }else if(reached == true){
                            deliveredImageButton?.visibility = View.VISIBLE
                            deliveredImageText?.visibility = View.GONE
                        }else if(departed == true){
                            arrivedImageButton?.visibility = View.VISIBLE
                            arrivedImageText?.visibility = View.GONE
                        }else if(received == true){
                            departedImageButton?.visibility = View.VISIBLE
                            departedImageText?.visibility = View.GONE
                        }else if(paid == true){
                            receivedImageButton?.visibility = View.VISIBLE
                            receivedImageText?.visibility = View.GONE
                        }

                        deliveredImageButton?.setOnClickListener {
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

                            tv_title.text="Are you sure you want open the scanner?"
                            btnConfirmCusUpdate.text = "Open" // Change the text as needed
                            btnConfirmCusCancel.text = "Cancel" // Change the text as needed

                            btnConfirmCusCancel.setOnClickListener {
                                Log.d("Photo","Cancel")
                                dialog.dismiss()
                            }



                            btnConfirmCusUpdate.setOnClickListener {
                                val intent = Intent(this, QRscanner::class.java)
                                intent.putExtra("postId", postId)
                                intent.putExtra("orderstatus_id", orderstatus_id)
                                intent.putExtra("received", received)
                                intent.putExtra("delivered", delivered)
                                intent.putExtra("insideClass", true)
                                dialog.dismiss()
                                startActivity(intent)
                            }
                            dialog.show()
                        }

                        arrivedImageButton?.setOnClickListener {

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

                            tv_title.text="Are you sure you want update as reached country of recipient?"
                            btnConfirmCusUpdate.text = "Update" // Change the text as needed
                            btnConfirmCusCancel.text = "Cancel" // Change the text as needed

                            btnConfirmCusCancel.setOnClickListener {
                                Log.d("Photo","Cancel")
                                dialog.dismiss()
                            }



                            btnConfirmCusUpdate.setOnClickListener {
                                val cusConSQL2 = ConnectionSQL()
                                cusConSQL2.conclass { connection2 ->
                                    if (connection2 != null) {
                                        try {
                                            // Update query with placeholders for binding
                                            val query2 =
                                                "UPDATE orderstatus SET reached = ? WHERE orderstatus_id = ?"

                                            val preparedStatement2 =
                                                connection2.prepareStatement(query2)

                                            preparedStatement2.setBoolean(1, true)
                                            preparedStatement2.setInt(2, orderstatus_id!!)

                                            // Execute the update query
                                            preparedStatement2.executeUpdate()
                                            preparedStatement2.close()

                                            runOnUiThread {
                                                Log.d("btnCusUpdate", "Clicked")
                                                recreate()
                                            }

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
                                    }
                                }
                                dialog.dismiss()
                            }
                            dialog.show()
                        }

                        departedImageButton?.setOnClickListener {
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

                            tv_title.text="Are you sure you want update as departed?"
                            btnConfirmCusUpdate.text = "Update" // Change the text as needed
                            btnConfirmCusCancel.text = "Cancel" // Change the text as needed

                            btnConfirmCusCancel.setOnClickListener {
                                Log.d("Photo","Cancel")
                                dialog.dismiss()
                            }



                            btnConfirmCusUpdate.setOnClickListener {

                                val cusConSQL2 = ConnectionSQL()
                                cusConSQL2.conclass { connection2 ->
                                    if (connection2 != null) {
                                        try {
                                            // Update query with placeholders for binding
                                            val query2 =
                                                "UPDATE orderstatus SET departed = ? WHERE orderstatus_id = ?"

                                            val preparedStatement2 =
                                                connection2.prepareStatement(query2)

                                            preparedStatement2.setBoolean(1, true)
                                            preparedStatement2.setInt(2, orderstatus_id!!)

                                            // Execute the update query
                                            preparedStatement2.executeUpdate()
                                            preparedStatement2.close()

                                            runOnUiThread {
                                                Log.d("btnCusUpdate", "Clicked")
                                                recreate()
                                            }

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
                                    }
                                }
                                dialog.dismiss()
                            }
                            dialog.show()
                        }

                        receivedImageButton?.setOnClickListener {
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

                            tv_title.text="Are you sure you want open the scanner?"
                            btnConfirmCusUpdate.text = "Open" // Change the text as needed
                            btnConfirmCusCancel.text = "Cancel" // Change the text as needed

                            btnConfirmCusCancel.setOnClickListener {
                                Log.d("Photo","Cancel")
                                dialog.dismiss()
                            }

                            btnConfirmCusUpdate.setOnClickListener {
                                val intent = Intent(this, QRscanner::class.java)
                                intent.putExtra("postId", postId)
                                intent.putExtra("orderstatus_id", orderstatus_id)
                                intent.putExtra("received", received)
                                intent.putExtra("delivered", delivered)
                                intent.putExtra("insideClass", true)
                                dialog.dismiss()
                                startActivity(intent)
                            }
                            dialog.show()
                        }









//                        receivedImageButton?.setOnClickListener {
//                            isAppColor = !isAppColor // Toggle the state on each click
//                            // Change the background and image drawable based on the state
//                            if (isAppColor) {
//                                Log.d("If", "appColor")
//
//                            } else {
//                                Log.d("Else", "black")
//                                paymentImage?.setBackgroundResource(R.drawable.circle_background_delivery)
//                                paymentImage?.setImageResource(R.drawable.img_my_deliveries_payment)
//                                receivedImage?.setBackgroundResource(R.drawable.circle_background_delivery)
//                                receivedImage?.setImageResource(R.drawable.img_my_deliveries_received)
//                                departedImage?.setBackgroundResource(R.drawable.circle_background_delivery)
//                                departedImage?.setImageResource(R.drawable.img_my_deliveries_departed)
//                                arrivedImage?.setBackgroundResource(R.drawable.circle_background_delivery)
//                                arrivedImage?.setImageResource(R.drawable.img_my_deliveries_arrived)
//                                deliveredImage?.setBackgroundResource(R.drawable.circle_background_delivery)
//                                deliveredImage?.setImageResource(R.drawable.img_my_deliveries_received)
//
//                                paymentImageLine?.setBackgroundResource(R.color.black)
//                                receivedImageLine?.setBackgroundResource(R.color.black)
//                                departedImageLine?.setBackgroundResource(R.color.black)
//                                arrivedImageLine?.setBackgroundResource(R.color.black)
//                            }
//                    }

                    }





                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()

                } finally {
                    connection.close()
                }
            }
        }



    }

    private fun switchToCustomerHomeLayout() {
        runOnUiThread {
            translationAnimator?.cancel()
            setContentView(R.layout.activity_my_deliveries_parcel_details)

            cusMyBookingArrowUpLayout = findViewById(R.id.cusMyBookingArrowUpLayout)
            cusMyBookingArrowDownLayout = findViewById(R.id.cusMyBookingArrowDownLayout)
            hideParcelDetails = findViewById(R.id.hideParcelDetails)
            cusMyBookingArrowUp = findViewById(R.id.cusMyBookingArrowUp)
            cusMyBookingArrowDown = findViewById(R.id.cusMyBookingArrowDown)
            swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
            arrowImageView = findViewById(R.id.arrowImageView)

            detailImage = findViewById(R.id.detailImage)
            viewTxtUrgent1 = findViewById(R.id.viewTxtUrgent1)
            viewTxtUrgent2 = findViewById(R.id.viewTxtUrgent2)
            viewCusName = findViewById(R.id.viewCusName)
            viewCusNum = findViewById(R.id.viewCusNum)
            viewdlvrydate = findViewById(R.id.viewdlvrydate)
            viewCategory = findViewById(R.id.viewCategory)
            viewContent = findViewById(R.id.viewContent)
            viewValue = findViewById(R.id.viewValue)
            viewWeight = findViewById(R.id.viewWeight)
            viewDimension = findViewById(R.id.viewDimension)
            viewdlvryAddrs = findViewById(R.id.viewdlvryAddrs)
            viewCity = findViewById(R.id.viewCity)
            viewCountry = findViewById(R.id.viewCountry)
            viewRecName = findViewById(R.id.viewRecName)
            viewRecNum = findViewById(R.id.viewRecNum)
            viewSpclIns = findViewById(R.id.viewSpclIns)

            viewParcelCharge = findViewById(R.id.viewParcelCharge)
            viewParcelAssignedUser = findViewById(R.id.viewParcelAssignedUser)
            viewFlightDate = findViewById(R.id.viewFlightDate)
            viewParcelDocuments1 = findViewById(R.id.viewParcelDocuments1)
            viewParcelDocuments2 = findViewById(R.id.viewParcelDocuments2)
            viewParcelDocuments3 = findViewById(R.id.viewParcelDocuments3)

            receivedImageButton = findViewById(R.id.receivedImageButton)
            departedImageButton = findViewById(R.id.departedImageButton)
            arrivedImageButton = findViewById(R.id.arrivedImageButton)
            deliveredImageButton = findViewById(R.id.deliveredImageButton)
            receivedImageText = findViewById(R.id.receivedImageText)
            departedImageText = findViewById(R.id.departedImageText)
            arrivedImageText = findViewById(R.id.arrivedImageText)
            deliveredImageText = findViewById(R.id.deliveredImageText)


            paymentImage = findViewById(R.id.paymentImage)
            receivedImage = findViewById(R.id.receivedImage)
            departedImage = findViewById(R.id.departedImage)
            arrivedImage = findViewById(R.id.arrivedImage)
            deliveredImage = findViewById(R.id.deliveredImage)

            paymentImageLine = findViewById(R.id.paymentImageLine)
            receivedImageLine = findViewById(R.id.receivedImageLine)
            departedImageLine = findViewById(R.id.departedImageLine)
            arrivedImageLine = findViewById(R.id.arrivedImageLine)

        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    // Perform the logout action
                    userAuth= FirebaseAuth.getInstance()
                    userAuth.signOut()
                    val intent = Intent(this, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {
                R.id.help -> {
                    val intent = Intent(this,HelpCenter::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {

                R.id.profile -> {
                    val intent = Intent(this, CommonUserProfile::class.java)
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