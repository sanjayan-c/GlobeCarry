package com.example.globe_carry

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.adapter.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var barTextView: TextView
    private lateinit var sendButton: ImageView
    private lateinit var userAuth: FirebaseAuth
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<com.example.globe_carry.Message>
    private lateinit var userDbRef: DatabaseReference
    private lateinit var profileImageView: ImageView
    private lateinit var cusAccManagementBack: ImageView

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        cusAccManagementBack = findViewById(R.id.cusAccManagementBack)
        barTextView = findViewById(R.id.barTextView)
        barTextView.text = name

        barTextView.setOnClickListener {
            userDbRef= FirebaseDatabase.getInstance().reference
            userDbRef.child("user").child(receiverUid!!).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userType = dataSnapshot.child("type").getValue(String::class.java)
                    if (userType == "user") {
                        // This is a user Proceed with the login
                        val intent = Intent(this@ChatActivity, CommonOtherUserProfile::class.java)
                        intent.putExtra("userFromIntent", receiverUid)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })

        }
        cusAccManagementBack.setOnClickListener {
            finish()
        }

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        userDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom =  receiverUid + senderUid
        receiverRoom =  senderUid + receiverUid

        chatRecyclerView = findViewById(R.id.chatRecylerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        profileImageView = findViewById(R.id.profile_image)

        profileImageView.setOnClickListener { view ->
            showPopupMenu(view)
        }

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


//
//        //logic for adding data to recycler
//        userDbRef.child("chats").child(senderRoom!!).child("messages")
//            .addValueEventListener(object:ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    messageList.clear()
//                    for (postSnapshot in snapshot.children){
//                        val message = postSnapshot.getValue(com.example.globe_carry.Message::class.java)
//                        messageList.add(message!!)
//                    }
//                    messageAdapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//        //adding messages to data base
//        sendButton.setOnClickListener{
//            val message = messageBox.text.toString()
//            val messageobject = Message(message,senderUid)
//
//            userDbRef.child("chats").child(senderRoom!!).child("messages").push()
//                .setValue(messageobject).addOnSuccessListener {
//                    userDbRef.child("chats").child(receiverRoom!!).child("messages").push()
//                        .setValue(messageobject)
//                }
//            messageBox.setText("")
//        }
//
        // Reading messages
        userDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(com.example.globe_carry.Message::class.java)

                        // Check if the message is from the receiver and mark it as read
                        if (message != null && message.senderId == receiverUid && !message.read) {
                            postSnapshot.ref.child("read").setValue(true) // Update read status in the database
                        }

                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        // Adding messages to the database
        sendButton.setOnClickListener{
            val messageText = messageBox.text.toString()

            if (messageText.isNotBlank()) {  // Check if the message is not empty
                val message = Message(messageText, senderUid!!,receiverUid!!)

                val senderMessageRef = userDbRef.child("chats").child(senderRoom!!).child("messages").push()
                val receiverMessageRef = userDbRef.child("chats").child(receiverRoom!!).child("messages").push()

                // Get the current timestamp
                val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(
                    Date()
                )

                // Set the read status to false for the sender's message and true for the receiver's message
                val senderMessage = message.copy(read = false, timeStamp = timestamp)
                val receiverMessage = message.copy(read = true, timeStamp = timestamp)

                senderMessageRef.setValue(senderMessage).addOnSuccessListener {
                    // Message sent successfully for the sender
                }

                receiverMessageRef.setValue(receiverMessage).addOnSuccessListener {
                    // Message sent successfully for the receiver
                }

                messageBox.setText("")

                // Check if it's the first message based on chat history
                checkFirstMessage(senderRoom!!, receiverRoom!!, senderUid!!,receiverUid!!)

            }else{
                val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_message_box)
                messageBox.background = ContextCompat.getDrawable(this, R.drawable.blink_border)
                messageBox.startAnimation(blinkAnimation)
                // Create a Handler to reset the messageBox after 2 seconds
                val handler = Handler()
                handler.postDelayed({
                    messageBox.background = ContextCompat.getDrawable(this, R.drawable.edt_background)
                }, 2000)
            }
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
                    val intent = Intent(this@ChatActivity, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {
                R.id.help -> {
                    val intent = Intent(this@ChatActivity,HelpCenter::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {

                R.id.profile -> {
                    val intent = Intent(this@ChatActivity, CommonUserProfile::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
        }

        popupMenu.show()
    }
    private fun checkFirstMessage(
        senderRoom: String,
        receiverRoom: String,
        senderUid: String,
        receiverUid: String
    ) {
        // Query the messages for the sender's room
        val senderMessagesRef = userDbRef.child("chats").child(senderRoom).child("messages")
        Log.d("Debug", "Check first message for senderRoom: $senderRoom, receiverRoom: $receiverRoom")
        senderMessagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(senderSnapshot: DataSnapshot) {
                // Query the messages for the receiver's room
                val receiverMessagesRef = userDbRef.child("chats").child(receiverRoom).child("messages")

                receiverMessagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(receiverSnapshot: DataSnapshot) {
                        // Check if both sender and receiver have at least one message
                        val senderHasMessages = senderSnapshot.childrenCount > 1
                        val receiverHasMessages = receiverSnapshot.childrenCount > 1
                        Log.d("senderHasMessages", senderSnapshot.childrenCount.toString())
                        Log.d("receiverHasMessages", receiverSnapshot.childrenCount.toString())
                        Log.d("senderHasMessages", senderHasMessages.toString())
                        Log.d("receiverHasMessages", receiverHasMessages.toString())
                        if (!senderHasMessages && !receiverHasMessages) {
                            // It's the first message between the users, send a "Welcome" message
                            sendWelcomeMessage(senderRoom, receiverRoom, senderUid, receiverUid)
                        }
                    }

                    override fun onCancelled(receiverError: DatabaseError) {
                        // Handle error
                    }
                })
            }

            override fun onCancelled(senderError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun sendWelcomeMessage(
        senderRoom: String,
        receiverRoom: String,
        senderUid: String,
        receiverUid: String
    ) {
        val welcomeMessageText = "Good Day! This is an automatic reply\nThank you for connecting with TourARound.\nOne of our associate will connect with you soon."
        val welcomeMessage = Message(welcomeMessageText, receiverUid!!, senderUid!!)
        val receiverMessageRef = userDbRef.child("chats").child(senderRoom).child("messages").push()
        val senderMessageRef= userDbRef.child("chats").child(receiverRoom).child("messages").push()

        // Get the current timestamp
        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(
            Date()
        )

        // Set the read status to false for the sender's message and true for the receiver's message
        val senderMessage = welcomeMessage.copy(read = true, timeStamp = timestamp)
        val receiverMessage = welcomeMessage.copy(read = true, timeStamp = timestamp)

        senderMessageRef.setValue(senderMessage).addOnSuccessListener {
            // "Welcome" message sent successfully for the sender
        }

        receiverMessageRef.setValue(receiverMessage).addOnSuccessListener {
            // "Welcome" message sent successfully for the receiver
        }
    }

}