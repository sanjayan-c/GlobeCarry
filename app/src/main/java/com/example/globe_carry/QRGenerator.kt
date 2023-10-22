package com.example.globe_carry

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QRGenerator : AppCompatActivity(){

    private lateinit var imagePostQR : ImageView
    private lateinit var QRBack : ImageView
    private lateinit var greenDownload : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode)

        val intent = intent
        val postId = intent.getStringExtra("postId")
        Log.d("QR Generator", postId ?: "No PostId available")

        QRBack = findViewById(R.id.cusQRBack)
        imagePostQR = findViewById(R.id.imageCusQR)
        greenDownload = findViewById(R.id.greenDownlaod)

        QRBack.setOnClickListener { // Start the CustomerAccountManagement activity
           // finish()
        }
        greenDownload.setOnClickListener {
            // Get the Bitmap from the ImageView
            val qrBitmap = imagePostQR.drawable.toBitmap()

            // Create a download directory
            val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QR Codes")

            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            // Create a unique filename for the QR code image
            val fileName = "QRCode_${System.currentTimeMillis()}.png"

            // Create a file in the download directory
            val file = File(downloadDir, fileName)

            try {
                // Save the QR code image to the file
                val outputStream = FileOutputStream(file)
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()

                // Tell the user that the download was successful
                Toast.makeText(this, "QR Code downloaded to Downloads/$fileName", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle any errors that occur during the download
                Toast.makeText(this, "Failed to download QR Code", Toast.LENGTH_SHORT).show()
            }
        }

        try {
            // Generate QR Code
            val bitmap = generateQRCode(postId!!, 500, 500)

            // Set the generated QR code to the ImageView
            imagePostQR.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap {
        val multiFormatWriter = MultiFormatWriter()

        // Encode the content in a BitMatrix
        val bitMatrix: BitMatrix =
            multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height)

        // Create a Bitmap from the BitMatrix
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    private fun createHorizontalRotationAnimation(): RotateAnimation {
        val rotateAnimation = RotateAnimation(
            0.0f,
            360.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 1000 // Animation duration in milliseconds
        return rotateAnimation
    }

}


    //    private fun login(email:String,password:String){
//        userAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = userAuth.currentUser
//                    if (user != null && user.isEmailVerified) {
//                        // User is authenticated and their email is verified
//                        Log.d(ContentValues.TAG, "signInWithEmail:success")
//
//                        // Proceed to the next screen or perform any other actions
//                        val intent = Intent(this@Login, CommonHome::class.java)
//                        startActivity(intent)
//                    } else {
//                        // User is authenticated but their email is not verified
//                        Toast.makeText(
//                            this@Login,
//                            "Please verify your email address first.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    // Check the error message
//                    val errorMessage = task.exception?.message
//                    if (errorMessage != null) {
//                        if (errorMessage.contains("password")) {
//                            // Incorrect password
//                            Toast.makeText(
//                                this@Login,
//                                "Incorrect password",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (errorMessage.contains("no user record")) {
//                            // Email not found
//                            Toast.makeText(
//                                this@Login,
//                                "No account found for this email",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            // Other error, show a generic message
//                            Toast.makeText(
//                                this@Login,
//                                "Login failed. Please try again.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    } else {
//                        // Unexpected error, show a generic message
//                        Toast.makeText(
//                            this@Login,
//                            "Login failed. Please try again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//    }

