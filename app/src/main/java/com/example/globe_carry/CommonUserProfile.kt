package com.example.globe_carry

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import java.io.ByteArrayOutputStream

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
    private var viewInputcusMyParcelsCount: TextView? = null
    private var viewInputDeliveryCount: TextView? = null
    private var ratingBar: RatingBar? = null
    private var commentsRecyclerView: RecyclerView? = null

    private var imageData: String = ""
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        profileEditImage= findViewById(R.id.profileEditImage)
        cusAccountButtons= findViewById(R.id.cusAccountButtons)
        btnCusUpdate= findViewById(R.id.btnCusUpdate)
        btnCusCancel= findViewById(R.id.btnCusCancel)
//
        cusMyHistoryArrowUp= findViewById(R.id.cusMyHistoryArrowUp)
        cusMyHistoryArrowUpLayout= findViewById(R.id.cusMyHistoryArrowUpLayout)
        cusMyHistoryArrowDown= findViewById(R.id.cusMyHistoryArrowDown)
        cusMyHistoryArrowDownLayout= findViewById(R.id.cusMyHistoryArrowDownLayout)

        myHistoryContent= findViewById(R.id.myHistoryContent)
//        viewInputGmail= findViewById(R.id.viewInputGmail)
//        viewInputcusMyParcelsCount= findViewById(R.id.viewInputcusMyParcelsCount)
//        viewInputDeliveryCount= findViewById(R.id.viewInputDeliveryCount)
//        ratingBar= findViewById(R.id.ratingBar)

        commentsRecyclerView= findViewById(R.id.commentsRecyclerView)

        cusAccManagementBack?.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }

        cusAccountProfileImageFrame?.setOnClickListener {
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
            cusAccountButtons?.visibility=View.VISIBLE
            profileEditImage?.visibility=View.GONE
            editTextFirstName?.visibility=View.VISIBLE
            viewInputLastName?.visibility=View.VISIBLE
            viewInputNIC?.visibility=View.VISIBLE
            viewInputPhoneNo?.visibility=View.VISIBLE
            editTextFirstName2?.visibility=View.GONE
            viewInputLastName2?.visibility=View.GONE
            viewInputNIC2?.visibility=View.GONE
            viewInputPhoneNo2?.visibility=View.GONE
        }
        btnCusUpdate?.setOnClickListener {
            Log.d("btnCusUpdate", "Clicked")
            recreate()
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

}