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

class UserActivity : AppCompatActivity() {

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

        // Get the UID of the currently logged-in user
        val currentUserUid = userAuth.currentUser?.uid ?: ""
        // Use a HashSet to keep track of unique user UIDs
        val uniqueUserUids = HashSet<String>()
//        userDbRef.child("chats").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userList.clear()
//                for (chatSnapshot in snapshot.children) {
//                    val chatRoom = chatSnapshot.key
//                    if (chatRoom != null && chatRoom.contains(currentUserUid)) {
//                        val otherUserUid = chatRoom.replace(currentUserUid, "")
//                        if (!uniqueUserUids.contains(otherUserUid)) {
//                            uniqueUserUids.add(otherUserUid)
//                            userDbRef.child("user").child(otherUserUid)
//                                .addListenerForSingleValueEvent(object : ValueEventListener {
//                                    override fun onDataChange(userSnapshot: DataSnapshot) {
//                                        val currentUser = userSnapshot.getValue(User::class.java)
//                                        if (currentUser != null) {
//                                            userList.add(currentUser)
//                                            adapter.notifyDataSetChanged()
//                                        }
//                                    }
//
//                                    override fun onCancelled(error: DatabaseError) {
//                                        // Handle onCancelled
//                                    }
//                                })
//                        }
//                    }
//                }
//            }

        userDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if (currentUser != null && currentUser.type != "staff" && userAuth.currentUser?.uid!=currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
                    val intent = Intent(this@UserActivity, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }

            when (item.itemId) {
                R.id.help -> {
                    val intent = Intent(this@UserActivity,HelpCenter::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
        }

        popupMenu.show()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId==R.id.logout){
//            userAuth.signOut()
//            finish()
//            return true
//        }
//        return true
//    }


}