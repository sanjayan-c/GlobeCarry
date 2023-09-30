package com.example.globe_carry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatHistory : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var userAuth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var userDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userAuth= FirebaseAuth.getInstance()
        userDbRef=FirebaseDatabase.getInstance().getReference()

        profileImageView = findViewById(R.id.profile_image)
        userList=ArrayList()
        adapter= UserAdapter(this,userList)
        userRecyclerView=findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter

        profileImageView.setOnClickListener { view ->
            showPopupMenu(view)
        }

        fetchUsersWithMessages()
    }
//    private fun fetchUsersWithMessages() {
//        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
//
//        // Use a HashSet to store unique user UIDs
//        val uniqueUserUids = HashSet<String>()
//
//        // Query the database to get users who have sent messages to the current user
//        userDbRef.child("chats").addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userList.clear()
//
//                for (chatSnapshot in snapshot.children) {
//                    // Check if the chat contains messages
//                    if (chatSnapshot.child("messages").exists()) {
//                        for (messageSnapshot in chatSnapshot.child("messages").children) {
//                            val message = messageSnapshot.getValue(Message::class.java)
//
//                            // Check if the message is from the current user and has been read
//                            if (message != null && message.senderId != currentUserUid && message.read) {
//                                // Get the UID of the sender
//                                val senderUid = message.senderId
//
//                                // Add the sender UID to the HashSet to ensure uniqueness
//                                uniqueUserUids.add(senderUid)
//                            }
//                        }
//                    }
//                }
//
//                // Fetch user information for the unique UIDs
//                for (senderUid in uniqueUserUids) {
//                    userDbRef.child("user").child(senderUid).addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(userSnapshot: DataSnapshot) {
//                            val user = userSnapshot.getValue(User::class.java)
//                            if (user != null) {
//                                userList.add(user)
//                                adapter.notifyDataSetChanged()
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            // Handle database error
//                        }
//                    })
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle database error
//            }
//        })
//    }
private fun fetchUsersWithMessages() {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    // Use two HashSets to store unique user UIDs for sent and received messages
    val sentMessageUids = HashSet<String>()
    val receivedMessageUids = HashSet<String>()

    // Query the database to get users who have sent messages to the current user
    userDbRef.child("chats").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (chatSnapshot in snapshot.children) {
                // Check if the chat contains messages
                if (chatSnapshot.child("messages").exists()) {
                    for (messageSnapshot in chatSnapshot.child("messages").children) {
                        val message = messageSnapshot.getValue(Message::class.java)

                        // Check if the message is from the current user and has been read
                        if (message != null && message.read) {
                            // Get the UID of the sender
                            val senderUid = message.senderId
                            val receiverUid = message.receiverId

                            // Add sender UID to the sentMessageUids set
                            if (receiverUid == currentUserUid) {
                                sentMessageUids.add(senderUid)
                            }

                            // Add receiver UID to the receivedMessageUids set
                            if (senderUid == currentUserUid) {
                                receivedMessageUids.add(receiverUid)
                            }
                        }
                    }
                }
            }

            // Combine the sets to get unique user UIDs
            val uniqueUserUids = HashSet<String>()
            uniqueUserUids.addAll(sentMessageUids)
            uniqueUserUids.addAll(receivedMessageUids)

            // Fetch user information for the unique UIDs
            for (uid in uniqueUserUids) {
                userDbRef.child("user").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(userSnapshot: DataSnapshot) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            userList.add(user)
                            adapter.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error
                    }
                })
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle database error
        }
    })
}

//        userDbRef.child("user").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userList.clear()
//                for (postSnapshot in snapshot.children){
//                    val currentUser=postSnapshot.getValue(User::class.java)
//
//                    if (userAuth.currentUser?.uid!=currentUser?.uid){
//                        userList.add(currentUser!!)
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    // Perform the logout action
                    userAuth= FirebaseAuth.getInstance()
                    userAuth.signOut()
                    val intent = Intent(this@ChatHistory, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {
                R.id.chat -> {
                    val intent = Intent(this@ChatHistory,ChatHistory::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {
                R.id.help -> {
                    val intent = Intent(this@ChatHistory,HelpCenter::class.java)
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