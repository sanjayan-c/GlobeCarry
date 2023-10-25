package com.example.globe_carry.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ConnectionSQL
import com.example.globe_carry.HomeItems
import com.example.globe_carry.MyDeliveriesFullView
import com.example.globe_carry.MyDeliveryRequests
import com.example.globe_carry.QRscanner
import com.example.globe_carry.R
import com.example.globe_carry.SingletonMyDeliveryRequests
import com.example.globe_carry.Verification
import com.example.globe_carry.ViewVerificationRequest
import com.example.globe_carry.fragment.MyDeliveriesFragment
import com.example.globe_carry.fragment.MyDeliveriesPendingFragment
import java.math.BigDecimal
import java.sql.SQLException

class MyDeliveriesAdapter(private val context: MyDeliveriesPendingFragment,
                          private val data: List<MyDeliveryRequests>,
                          private val activity: Activity
) :
    RecyclerView.Adapter<MyDeliveriesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNo: TextView = itemView.findViewById(R.id.homeItemNo)
        val toCountry: TextView = itemView.findViewById(R.id.homeItemArea)
        val sender: TextView = itemView.findViewById(R.id.itemSender)
        val urgent: TextView = itemView.findViewById(R.id.homeItemUrgent)
        val type: TextView = itemView.findViewById(R.id.homeItemType)
        val orgin: TextView = itemView.findViewById(R.id.homeItemDimensions1)
        val flightDate: TextView = itemView.findViewById(R.id.itemFlightDate1)
        val details: TextView = itemView.findViewById(R.id.homeItemDetails)
        val buttonScan: TextView = itemView.findViewById(R.id.button2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_my_deliveries_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val registerString = "Details >"
        val mSpannableString = SpannableString(registerString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        holder.details.text = mSpannableString


        val item = data[position]
        holder.itemNo.text = item.postId.toString()
        val location = "${item.city} , ${item.country}"
        holder.toCountry.text = location
        val name = item.firstName+" "+item.lastName
        holder.sender.text = name
        holder.type.text = item.category
        val orginLocation =  "${item.cityOrgin} , ${item.countryOrgin}"
        holder.orgin.text = item.orgin
        holder.flightDate.text = item.createdDate

        Log.d("Paid",item.paid.toString())
        Log.d("Received",item.received.toString())
        Log.d("Delivered",item.delivered.toString())
        Log.d("Departed",item.departed.toString())
        Log.d("Reached",item.reached.toString())

        if(item.reached == true){
            holder.buttonScan.text = "Scan QR"
        }else if(item.departed == true){
            holder.buttonScan.text = "Arrived"
        }else if(item.received == true){
            holder.buttonScan.text = "Departed"
        }else if(item.paid == true){
            holder.buttonScan.text = "Scan QR"
        }else{
            holder.buttonScan.visibility=View.GONE
        }

        holder.buttonScan.setOnClickListener {
            if(item.reached == true){
                // Create a custom dialog
                val dialog = Dialog(context.requireContext())

                // Set the custom layout for the dialog
                dialog.setContentView(R.layout.profile_popup)

                // Set the width of the dialog to match the parent's width
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)

                // Get the display metrics to calculate the width
                val displayMetrics = DisplayMetrics()
                context.requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels
                val isPortrait = context.requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

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
                    val intent = Intent(context.requireContext(), QRscanner::class.java)
                    intent.putExtra("postId", item.postId)
                    intent.putExtra("orderstatus_id", item.orderstatus_id)
                    intent.putExtra("received", item.received)
                    intent.putExtra("delivered", item.delivered)
                    dialog.dismiss()
                    context.startActivity(intent)
                }
                dialog.show()
            }else if(item.departed == true){
                        // Create a custom dialog
                        val dialog = Dialog(context.requireContext())

                        // Set the custom layout for the dialog
                        dialog.setContentView(R.layout.profile_popup)

                        // Set the width of the dialog to match the parent's width
                        val layoutParams = WindowManager.LayoutParams()
                        layoutParams.copyFrom(dialog.window?.attributes)

                        // Get the display metrics to calculate the width
                        val displayMetrics = DisplayMetrics()
                        context.requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                        val screenWidth = displayMetrics.widthPixels
                        val screenHeight = displayMetrics.heightPixels
                        val isPortrait = context.requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

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

                            val preparedStatement2 = connection2.prepareStatement(query2)

                            preparedStatement2.setBoolean(1, true)
                            preparedStatement2.setInt(2, item.orderstatus_id)

                            // Execute the update query
                            preparedStatement2.executeUpdate()
                            preparedStatement2.close()

                            activity.runOnUiThread {
                                Log.d("btnCusUpdate", "Clicked")
                                activity.recreate()
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
            }else if(item.received == true){

                // Create a custom dialog
                val dialog = Dialog(context.requireContext())

                // Set the custom layout for the dialog
                dialog.setContentView(R.layout.profile_popup)

                // Set the width of the dialog to match the parent's width
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)

                // Get the display metrics to calculate the width
                val displayMetrics = DisplayMetrics()
                context.requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels
                val isPortrait = context.requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

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

                                val preparedStatement2 = connection2.prepareStatement(query2)

                                preparedStatement2.setBoolean(1, true)
                                preparedStatement2.setInt(2, item.orderstatus_id)

                                // Execute the update query
                                preparedStatement2.executeUpdate()
                                preparedStatement2.close()

                                activity.runOnUiThread {
                                    Log.d("btnCusUpdate", "Clicked")
                                    activity.recreate()
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
            }else if(item.paid == true){
                // Create a custom dialog
                val dialog = Dialog(context.requireContext())

                // Set the custom layout for the dialog
                dialog.setContentView(R.layout.profile_popup)

                // Set the width of the dialog to match the parent's width
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)

                // Get the display metrics to calculate the width
                val displayMetrics = DisplayMetrics()
                context.requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels
                val isPortrait = context.requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

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
                    val intent = Intent(context.requireContext(), QRscanner::class.java)
                    intent.putExtra("postId", item.postId)
                    intent.putExtra("orderstatus_id", item.orderstatus_id)
                    intent.putExtra("received", item.received)
                    intent.putExtra("delivered", item.delivered)
                    dialog.dismiss()
                    context.startActivity(intent)
                }
                dialog.show()
            }


            // Code to execute when the TextView is clicked

            // For example, you can open a new activity or perform some other action.
        }
        holder.details.setOnClickListener {
//            SingletonMyDeliveryRequests.postId = item.postId
//            SingletonMyDeliveryRequests.urgency = item.urgency
//            SingletonMyDeliveryRequests.category = item.category
//            SingletonMyDeliveryRequests.content = item.content
//            SingletonMyDeliveryRequests.value = item.value
//            SingletonMyDeliveryRequests.weight = item.weight
//            SingletonMyDeliveryRequests.dlvryAddress = item.dlvryAddress
//            SingletonMyDeliveryRequests.city = item.city
//            SingletonMyDeliveryRequests.country = item.country
//            SingletonMyDeliveryRequests.recipient = item.recipient
//            SingletonMyDeliveryRequests.rcptContactNo = item.rcptContactNo
//            SingletonMyDeliveryRequests.dlvryDate = item.dlvryDate
//            SingletonMyDeliveryRequests.instructions = item.instructions
//            SingletonMyDeliveryRequests.ttlCharge = item.ttlCharge
//            SingletonMyDeliveryRequests.dimension = item.dimension
//            SingletonMyDeliveryRequests.createdDate = item.createdDate
//            SingletonMyDeliveryRequests.createdBy = item.createdBy
//            SingletonMyDeliveryRequests.imageBytes = item.imageBytes
//
//            SingletonMyDeliveryRequests.orderstatus_id = item.orderstatus_id
//            SingletonMyDeliveryRequests.received = item.received
//            SingletonMyDeliveryRequests.delivered = item.delivered
//            SingletonMyDeliveryRequests.paid = item.paid
//            SingletonMyDeliveryRequests.departed = item.departed
//            SingletonMyDeliveryRequests.reached = item.reached
//
//            SingletonMyDeliveryRequests.firstName = item.firstName
//            SingletonMyDeliveryRequests.lastName = item.lastName
//            SingletonMyDeliveryRequests.phoneNo = item.phoneNo
//            SingletonMyDeliveryRequests.cityOrgin = item.cityOrgin
//            SingletonMyDeliveryRequests.countryOrgin = item.countryOrgin
//
//            SingletonMyDeliveryRequests.flightDate = item.flightDate
//            SingletonMyDeliveryRequests.passport = item.passport
//            SingletonMyDeliveryRequests.orgin = item.orgin
//            SingletonMyDeliveryRequests.passportImage = item.passportImage
//            SingletonMyDeliveryRequests.ticketImage = item.ticketImage
//            SingletonMyDeliveryRequests.travellerImage = item.travellerImage
//
//            SingletonMyDeliveryRequests.myFirstName = item.myFirstName
//            SingletonMyDeliveryRequests.myLastName = item.myLastName

            val intent = Intent(context.requireContext(), MyDeliveriesFullView::class.java)
            intent.putExtra("postId", item.postId)
            intent.putExtra("orderstatus_id", item.orderstatus_id)
            context.startActivity(intent)
        }



    }
    override fun getItemCount(): Int {
        return data.size
    }
}