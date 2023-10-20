package com.example.globe_carry

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.sql.SQLException
import java.sql.Timestamp

class ViewVerificationRequest: AppCompatActivity() {
    private val cusConSQL = ConnectionSQL()
    private var intVerficationId :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_document_view)

        val verficationId:String?=intent.getStringExtra("REQUEST_ID_KEY")
        val homeItemNo: TextView =findViewById(R.id.homeItemNo)
        val homeItemArea: TextView = findViewById(R.id.homeItemArea)
        val homeTravallerName: TextView = findViewById(R.id.Travaller)
        val FlightDate: TextView =findViewById(R.id.FlightDate)
        val Orgin: TextView =findViewById(R.id.Orgin)
//        val destination: TextView = findViewById(R.id.destination)
        val destinationView: TextView = findViewById(R.id.destinationView)
        val homeItemPostDate: TextView =findViewById(R.id.homeItemPostDate)
      //  val homeItemUrgent: TextView =findViewById(R.id.homeItemUrgent)


        val passport = findViewById<ImageView>(R.id.imageView)

        val UserImage = findViewById<ImageView>(R.id.imageView2)

        val TicketImage = findViewById<ImageView>(R.id.imageView3)


        var verficationNo: String? = null
        var PostID:String?=null
        var TravellerID:String?=null
        var Fname:String?=null
        var Lname:String?=null
        var flightdate:String?=null
        var orgin: String? = null
        var city: String?    = null
        var country: String? = null
        var requestedDate: String? = null
        var time: Timestamp? = null
        var requestid: String? = null
        var urgent: Boolean? = null
        var passportImage:String?=null
        var UserImage1:String?=null
        var TicketImage1:String?=null

        cusConSQL.conclass { connection ->
            if (connection != null) {
                intVerficationId = verficationId?.toInt() ?: 0
                try {
                    // Your database query logic here
                    // Example query using your commented code
                    val query = """
SELECT
    v.VerificationID,
    v.PostID,
    v.TravellerID,
    v.FlightDate,
    v.destination,
    v.orgin,
    v.city,
    v.time,
    u.firstName,
    u.lastName,
    v.PassportImage,
    v.TicketImage,
    v.TravellerImage
FROM
    verification v, user u
WHERE
    v.TravellerID = u.userId AND v.VerificationID = ?;


                """
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setInt(1, intVerficationId)


                    val resultSet = preparedStatement.executeQuery()

                    // Create a list to store the retrieved data


                    while (resultSet.next()) {
                        // Retrieve data from the result set and create InspectorTimeTableItems objects

                        verficationNo = resultSet.getString("VerificationID")
                        PostID= resultSet.getString("PostID")
                        TravellerID = resultSet.getString("TravellerID")
                        flightdate = resultSet.getString("FlightDate")
                        country=resultSet.getString("destination")
                        orgin=resultSet.getString("orgin")
                        Fname=resultSet.getString("firstName")
                        Lname=resultSet.getString("lastName")
                        city=resultSet.getString("city")
                        time=resultSet.getTimestamp("time")
                        passportImage=resultSet.getString("PassportImage")
                        UserImage1=resultSet.getString("TicketImage")
                        TicketImage1=resultSet.getString("TravellerImage")



                    }

                    preparedStatement.close()
                    resultSet.close()

                    runOnUiThread {
                         homeItemNo.text=PostID
                         homeItemArea.text=city+","+country
                         homeTravallerName.text=Fname+","+Lname
                         FlightDate.text=flightdate
                         Orgin.text=orgin
                         //destination.text=country
                         homeItemPostDate.text=time.toString()
                         destinationView.text=country



                        if (passportImage != null && UserImage1 !=null && TicketImage1 !=null) {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(passportImage, Base64.DEFAULT)
                            val decodedBitmap1 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            passport?.setImageBitmap(decodedBitmap1)

                            val decodedBytes2 = Base64.decode(UserImage1, Base64.DEFAULT)
                            val decodedBitmap2 = BitmapFactory.decodeByteArray(decodedBytes2, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            UserImage?.setImageBitmap(decodedBitmap2)

                            val decodedBytes3 = Base64.decode(TicketImage1, Base64.DEFAULT)
                            val decodedBitmap3 = BitmapFactory.decodeByteArray(decodedBytes3, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            TicketImage?.setImageBitmap(decodedBitmap3)



                        } else {
                            // If cusImage is null, you can set a default image or do nothing
                            passport?.setImageResource(R.drawable.cus_image_not_found)
                            UserImage?.setImageResource(R.drawable.cus_image_not_found)
                            TicketImage?.setImageResource(R.drawable.cus_image_not_found)
                        }


                        // You can use inspectorImage as needed for displaying images
                    }
                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                } catch (e: Exception) {
                    Log.e("General Error", "Error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }



        val acceptButton = findViewById<Button>(R.id.accept)
        val rejectButton = findViewById<Button>(R.id.rejectbutton)

        acceptButton.setOnClickListener {
            // When the "Accept" button is clicked, update the status to true (accepted)
            updateVerificationStatus(intVerficationId, true)
        }

        rejectButton.setOnClickListener {
            // When the "Reject" button is clicked, update the status to false (rejected)
            updateVerificationStatus(intVerficationId, false)
        }









    }


    fun updateVerificationStatus(verficationId: Int, status: Boolean) {
        val query = "UPDATE verification SET Status = ? WHERE VerificationID = ?"

        val intVerficationId = verficationId.toInt() // Make sure verficationId is an integer

        // Use a try-catch block for error handling
        try {
            cusConSQL.conclass { connection ->
                if (connection != null) {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setBoolean(1, status)
                    preparedStatement.setInt(2, intVerficationId)

                    val updatedRows = preparedStatement.executeUpdate()

                    if (updatedRows > 0) {
                        // Update was successful
                        // You can show a message or perform other actions as needed
                    } else {
                        // No rows were updated; handle this case if needed
                    }

                    preparedStatement.close()
                } else {
                    // Handle the case where the database connection is null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any errors, such as database connection errors or SQL errors
        }
    }



}