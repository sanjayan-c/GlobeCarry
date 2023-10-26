package com.example.globe_carry

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.globe_carry.adapter.UserAdapter
import com.example.globe_carry.fragment.HomeFragment
import com.example.globe_carry.fragment.MyDeliveriesFragment
import com.example.globe_carry.fragment.MyParcelsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.SQLException

class CommonHome : AppCompatActivity() {

    private lateinit var toolBarSearchBar : LinearLayout
    private lateinit var linearNavbarItem1 : LinearLayout
    private lateinit var linearNavbarItem2 : LinearLayout
    private lateinit var linearNavbarItem3 : LinearLayout
    private lateinit var linearRowsend : LinearLayout
    private lateinit var userAuth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var countMessage: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentFragment: Fragment? = null
    private lateinit var userDbRef: DatabaseReference
    private var userImage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_home)

        toolBarSearchBar=findViewById(R.id.toolBarSearchBar)
        toolBarSearchBar.visibility=View.VISIBLE

        userAuth= FirebaseAuth.getInstance()
        userDbRef=FirebaseDatabase.getInstance().reference

        profileImageView = findViewById(R.id.profile_image)

        profileImageView.setOnClickListener { view ->
            showPopupMenu(view)
        }

        countMessage=findViewById(R.id.countMessage)
        // Query staff users
        val staffQuery = userDbRef.child("user").orderByChild("type").equalTo("staff")
        staffQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val staffCount = snapshot.childrenCount.toInt()
                if (staffCount == 1) {
                    // There is only one staff member, so go directly to the chat
                    val staffUser = snapshot.children.first().getValue(User::class.java)
                    if (staffUser != null) {
                        // Log the uid of the staff member
                        val staffUid = staffUser.uid
                        Log.d("StaffUid", "Staff UID: $staffUid")
                        val chatRoom = FirebaseAuth.getInstance().currentUser?.uid+staffUid // Replace with the actual chat room ID
                        println(chatRoom)
                        getUnreadMessageCount(chatRoom) { unreadCount ->
                            // Handle the unread message count here
                            Log.d("UnreadMessages", "Unread messages: $unreadCount")
                            if(unreadCount>0){
                                countMessage.text=unreadCount.toString()
                                countMessage.visibility=View.VISIBLE
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event, if needed
            }
        })



        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // This is where you handle the refresh action.
            recreate()
            swipeRefreshLayout.isRefreshing = false
        }


        linearNavbarItem1 = findViewById(R.id.linearNavbarItem1)
        linearNavbarItem2 = findViewById(R.id.linearNavbarItem2)
        linearNavbarItem3 = findViewById(R.id.linearNavbarItem3)

        val homeFragment = HomeFragment()
        val myDeliveriesFragment = MyDeliveriesFragment()
        val myParcelsFragment = MyParcelsFragment()



        val image = findViewById<ImageView>(R.id.imageFolder2)
        val text = findViewById<TextView>(R.id.txtHome)
        image.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
        text.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))

        val fragmentToShow = intent.getStringExtra("FRAGMENT_TO_SHOW")

        if (fragmentToShow == "") {
            setFragment(homeFragment)
        }else if(fragmentToShow == "MyDeliveriesFragment") {
            setFragment(myDeliveriesFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))

            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
        }else if (fragmentToShow == "MyParcelsFragment") {
            setFragment(myParcelsFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
        }else{
            setFragment(homeFragment)
        }


        // Set click listeners for your LinearLayouts (Transactions and Top Up)
        linearNavbarItem1.setOnClickListener {
            setFragment(myDeliveriesFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))

            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        linearNavbarItem2.setOnClickListener {
            setFragment(homeFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))

            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        linearNavbarItem3.setOnClickListener {
            setFragment(myParcelsFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))

            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
        }
        val linearRowsendLayout = findViewById<LinearLayout>(R.id.linearRowsend)
        linearRowsendLayout.setOnClickListener {
            // Define the intent to start the AdPostActivity
            val intent = Intent(this, AdPostActivity::class.java)

            // Start the AdPostActivity
            startActivity(intent)
        }

        val cusConSQL2 = ConnectionSQL()
        cusConSQL2.conclass { connection2 ->
            if (connection2 != null) {
                try {
                    val userAuth = FirebaseAuth.getInstance()
                    val user = userAuth.currentUser?.uid ?: ""
                    // Update query with placeholders for binding
                    val query2 = "SELECT userImage FROM user WHERE userId = '$user' ";

                    // Create a statement
                    val statement2 = connection2.createStatement()

                    // Execute the query
                    val resultSet2 = statement2.executeQuery(query2)


                    // Iterate through the result set and log the details
                    while (resultSet2.next()) {
                        userImage = resultSet2.getString("userImage")
                        Log.d("inside","inside")
                    }

                    resultSet2.close()
                    statement2.close()

                    runOnUiThread {
                        if (userImage != "") {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(userImage, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            profileImageView.setImageBitmap(decodedBitmap)
                        } else {
                            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                            profileImageView.setImageResource(R.drawable.cus_image_not_found)
                        }


                    }
                } catch (e: SQLException) {
                    Log.e("Update Error", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                    // Handle any errors that occur during the update
                } finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection2.close()
                }
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
                    val intent = Intent(this@CommonHome, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {
                R.id.help -> {
                    val intent = Intent(this@CommonHome,HelpCenter::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
            when (item.itemId) {

                R.id.profile -> {
                    val intent = Intent(this@CommonHome, CommonUserProfile::class.java)
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the fragment container with the new fragment
        transaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment)

        // Commit the transaction
        transaction.commit()

        currentFragment = fragment
    }


    private fun getUnreadMessageCount(chatRoom: String, callback: (Int) -> Unit) {
        userDbRef = FirebaseDatabase.getInstance().getReference()
        val messagesRef = userDbRef.child("chats").child(chatRoom).child("messages")

        // Query for unread messages
        val query = messagesRef.orderByChild("read").equalTo(false)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val unreadMessageCount = snapshot.childrenCount.toInt()
                // Call the callback function and pass the unread message count
                Log.d("Count",unreadMessageCount.toString())
                callback(unreadMessageCount)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }





}