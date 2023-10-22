package com.example.globe_carry

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

class HelpCenter : AppCompatActivity() {

//    private lateinit var userRecyclerView: RecyclerView
//    private lateinit var userList: ArrayList<User>
//    private lateinit var adapter: UserAdapter
    private lateinit var userAuth: FirebaseAuth
//    private lateinit var profileImageView: ImageView
    private lateinit var userDbRef: DatabaseReference
    private var translationAnimator :ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_user_profile_splash)

        val runningManImageView = findViewById<ImageView>(R.id.runningManImageView1)

        // Calculate the width of the screen for animation bounds
        val screenWidth = resources.displayMetrics.widthPixels

        // Create an ObjectAnimator to animate translation from left to right
        translationAnimator = ObjectAnimator.ofFloat(
            runningManImageView,
            "translationX",
            -screenWidth.toFloat(),
            screenWidth.toFloat()
        )

        // Set the animator duration
        translationAnimator?.duration = 2000  // Adjust the duration as needed

        // Set the repeat mode to reverse for back-and-forth animation
        translationAnimator?.repeatMode = ObjectAnimator.RESTART
        translationAnimator?.repeatCount = ObjectAnimator.INFINITE

        // Start the animation
        translationAnimator?.start()

        userAuth = FirebaseAuth.getInstance()
        userDbRef = FirebaseDatabase.getInstance().getReference()

//        profileImageView = findViewById(R.id.profile_image)
//        userList = ArrayList()
//        adapter = UserAdapter(this, userList)
//        userRecyclerView = findViewById(R.id.userRecyclerView)
//
//        userRecyclerView.layoutManager = LinearLayoutManager(this)
//        userRecyclerView.adapter = adapter
//
//        profileImageView.setOnClickListener { view ->
//            showPopupMenu(view)
//        }

        // Query staff users
        val staffQuery = userDbRef.child("user").orderByChild("type").equalTo("staff")
        staffQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val staffCount = snapshot.childrenCount.toInt()
                if (staffCount == 1) {
                    // There is only one staff member, so go directly to the chat
                    val staffUser = snapshot.children.first().getValue(User::class.java)
                    if (staffUser != null) {
                        // Delay the start of the activity by 1 second
                        Handler().postDelayed({
                            startChatActivity(staffUser)
                        }, 1000) // 1000 milliseconds (1 second)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event, if needed
            }
        })
    }

    private fun startChatActivity(currentUser: User) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("name", currentUser.name)
        intent.putExtra("uid", currentUser.uid)

        // Replace with the actual chat room ID based on your logic
        val chatRoom = FirebaseAuth.getInstance().currentUser?.uid + currentUser.uid
        intent.putExtra("chatRoomId", chatRoom)

        // Mark messages as read for the selected chat (if needed)
        // markMessagesAsRead(chatRoom)
        translationAnimator?.cancel()
        startActivity(intent)
        finish() // Optional: Close the UserActivity if you don't need it anymore
    }



//    private fun showPopupMenu(view: View) {
//        val popupMenu = PopupMenu(this, view)
//        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
//
//        popupMenu.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.logout -> {
//                    // Perform the logout action
//                    userAuth= FirebaseAuth.getInstance()
//                    userAuth.signOut()
//                    val intent = Intent(this@HelpCenter, Login::class.java)
//                    finish()
//                    startActivity(intent)
//                    true
//                }
//                // Add more menu items and their actions here
//                else -> false
//            }
//            when (item.itemId) {
//                R.id.help -> {
//                    val intent = Intent(this@HelpCenter,HelpCenter::class.java)
//                    startActivity(intent)
//                    true
//                }
//                // Add more menu items and their actions here
//                else -> false
//            }
//
//                    when (item.itemId) {
//
//                        R.id.profile -> {
//                            val intent = Intent(this@HelpCenter, CommonUserProfile::class.java)
//                            startActivity(intent)
//                            true
//                        }
//                        // Add more menu items and their actions here
//                        else -> false
//                    }
//
//        }
//
//        popupMenu.show()
//    }

}