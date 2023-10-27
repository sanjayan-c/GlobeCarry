package com.example.globe_carry

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.globe_carry.fragment.MyDeliveriesFragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.sql.SQLException
import kotlin.properties.Delegates

class QRscanner : AppCompatActivity() {


    //private lateinit var scheduleId: String
    private lateinit var PostId: String
    private val cusConSQL = ConnectionSQL()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        PostId = intent.getStringExtra("REQUEST_ID_KEY").toString()
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
                val customerId = result.contents
                Log.d("QRScanner", "Scanned QR Code: $customerId")
                processQRCodeResult(customerId)
            }

            val intent = Intent(this, MyDeliveriesFragment::class.java)
            startActivity(intent)
        } else {
            // Handle case where QR code could not be scanned
        }
    }

    private fun processQRCodeResult(customerId: String) {
        cusConSQL.conclass { connection ->
            val query: String

            query =
                """UPDATE orderstatus
SET received = CASE
    WHEN received IS NULL THEN TRUE
    ELSE received
END,
delivered = CASE
    WHEN received IS NOT NULL THEN TRUE
    ELSE delivered
END
WHERE postId = ?;  
"""


            if (connection != null) {
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, PostId)


                    val rowsReturned = preparedStatement.executeQuery()

                    preparedStatement.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                } finally {
                    connection.close()
                }
            } else {
                // Handle the case where the database connection is null
            }
        }
    }

    private fun updateDistanceTable(customerId: String) {

        }

}
