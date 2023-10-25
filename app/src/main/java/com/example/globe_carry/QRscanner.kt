package com.example.globe_carry

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.globe_carry.fragment.MyDeliveriesFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.sql.PreparedStatement
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

class QRscanner : AppCompatActivity() {

    private lateinit var userAuth: FirebaseAuth
    //private lateinit var scheduleId: String
    private var postId: Int = 0
    private var poId: Int = 0
    private var orderstatus_id: Int = 0
    private var received: Boolean = false
    private var delivered: Boolean = false
    private var insideClass: Boolean = false

    private val cusConSQL = ConnectionSQL()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        postId = intent.getIntExtra("postId", 0)
        poId = intent.getIntExtra("postId", 0)
        orderstatus_id = intent.getIntExtra("orderstatus_id", 0)
        received = intent.getBooleanExtra("received", false)
        delivered = intent.getBooleanExtra("delivered", false)
        insideClass = intent.getBooleanExtra("insideClass", false)
        Log.d("MyScanner", "PostNo: $postId")
        Log.d("MyScanner", "orderstatus_id: $orderstatus_id")
        Log.d("MyScanner", "received: $received")
        Log.d("MyScanner", "delivered: $delivered")
        userAuth= FirebaseAuth.getInstance()
        startQRCodeScanner()
    }

    private fun startQRCodeScanner() {
        val integrator = IntentIntegrator(this)

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        integrator.setPrompt(" ")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(false)

        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                val post = result.contents
                Log.d("QRScanner", "Scanned QR Code: $post")
                val postID=postId.toString()
                Log.d("QRScanner", "Scanned QR Code: $postID")
                if (post.trim() == postID.trim()){
                    processQRCodeResult(post, received, delivered)
                }else{
                    Toast.makeText(
                        this@QRscanner,
                        "Parcel no doesn't match",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        } else {
            // Handle case where QR code could not be scanned
        }
    }

    private fun processQRCodeResult(postId: String, received:Boolean, delivered:Boolean) {
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                try {
                    // Update query with placeholders for binding
                    val query: String
                    val preparedStatement : PreparedStatement
                    val user=userAuth.currentUser?.uid
                    if(!received){
                       query= "UPDATE orderstatus " +
                                "SET received = TRUE " +
                                "WHERE acptdTravllerId = ? " +
                                "AND postId = ?"

                        preparedStatement = connection.prepareStatement(query)
                        // Bind the values to the placeholders
                        preparedStatement.setString(1, user)
                        preparedStatement.setString(2, postId)
                    }else{
                        val (currentDate, currentTime) = getCurrentDateTime()
                        query="UPDATE orderstatus " +
                                "SET delivered = TRUE, orderCompletedDate = ?, orderCompletedTime = ? " +
                                "WHERE acptdTravllerId = ? " +
                                "AND postId = ?"

                        preparedStatement = connection.prepareStatement(query)
                        // Bind the values to the placeholders
                        preparedStatement.setString(1, currentDate)
                        preparedStatement.setString(2, currentTime)
                        preparedStatement.setString(3, user)
                        preparedStatement.setString(4, postId)
                    }



                    // Execute the insert query
                    val rowsAffected = preparedStatement.executeUpdate()

                    if (rowsAffected > 0) {
                        // Insert successful
                        println("Data inserted successfully.")
                    } else {
                        // Insert failed
                        println("Failed to insert data.")
                    }
                    if(insideClass){
                        val intent = Intent(this, MyDeliveriesFullView::class.java)
                        intent.putExtra("postId", poId)
                        intent.putExtra("orderstatus_id", orderstatus_id)
                        Log.e("Update Error", poId.toString())
                        Log.e("Update Error", orderstatus_id.toString())

                        startActivity(intent)
                    }else {
                        val intent = Intent(this, CommonHome::class.java)
                        intent.putExtra("FRAGMENT_TO_SHOW", "MyDeliveriesFragment")
                        startActivity(intent)
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
                // Handle the case where the database connection is null
            }
        }

    }
    fun getCurrentDateTime(): Pair<String, String> {
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val currentDate = currentDateTime.format(dateFormatter)
        val currentTime = currentDateTime.format(timeFormatter)
        return Pair(currentDate, currentTime)
    }

}
