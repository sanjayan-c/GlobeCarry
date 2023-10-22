package com.example.globe_carry

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class ChatHistory : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var userAuth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var userDbRef: DatabaseReference
    private lateinit var cusWalletProgressBarLayout: LinearLayout
    private lateinit var runningManImageView1: ImageView
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_history)

        userAuth= FirebaseAuth.getInstance()
        userDbRef=FirebaseDatabase.getInstance().getReference()

        profileImageView = findViewById(R.id.profile_image)
        cusWalletProgressBarLayout = findViewById(R.id.cusWalletProgressBarLayout)
        runningManImageView1 = findViewById(R.id.runningManImageView1)

        userList=ArrayList()
        adapter= UserAdapter(this,userList)
        userRecyclerView=findViewById(R.id.userRecyclerView)


        // Calculate the width of the screen for animation bounds
        val screenWidth = resources.displayMetrics.widthPixels

        // Create an ObjectAnimator to animate translation from left to right
        val translationAnimator = ObjectAnimator.ofFloat(
            runningManImageView1,
            "translationX",
            -screenWidth.toFloat(),
            screenWidth.toFloat()
        )

        // Set the animator duration
        translationAnimator.duration = 2000  // Adjust the duration as needed

        // Set the repeat mode to reverse for back-and-forth animation
        translationAnimator.repeatMode = ObjectAnimator.RESTART
        translationAnimator.repeatCount = ObjectAnimator.INFINITE

        // Start the animation
        translationAnimator.start()


        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter

        profileImageView.setOnClickListener { view ->
            showPopupMenu(view)
        }

        fetchUsersWithMessages()

        // After loading the adapter with data
        adapter.notifyDataSetChanged()
        // Create a Handler and post a delayed action
        Handler().postDelayed({
            userRecyclerView.visibility = View.VISIBLE
            cusWalletProgressBarLayout.visibility = View.GONE
            translationAnimator.cancel()
        }, 2000) // 2000 milliseconds (2 seconds)

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

//        <item android:id="@+id/chat" android:title="Chat"/>
//        val chat = popupMenu.menu.findItem(R.id.chat)
//        chat.isVisible = false

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
                R.id.help -> {
                    val intent = Intent(this@ChatHistory,HelpCenter::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {

                R.id.profile -> {
                    val intent = Intent(this@ChatHistory, CommonUserProfile::class.java)
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