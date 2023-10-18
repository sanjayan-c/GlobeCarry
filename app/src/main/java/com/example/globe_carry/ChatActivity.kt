package com.example.globe_carry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
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

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        barTextView = findViewById(R.id.barTextView)
        barTextView.text = name

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

            if (messageText.isNotBlank()) { // Check if the message is not empty
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
                R.id.chat -> {
                    val intent = Intent(this@ChatActivity,ChatHistory::class.java)
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
        }

        popupMenu.show()
    }
}