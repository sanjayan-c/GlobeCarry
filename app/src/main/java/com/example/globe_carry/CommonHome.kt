package com.example.globe_carry

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
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
import com.google.firebase.database.FirebaseDatabase

class CommonHome : AppCompatActivity() {

    private lateinit var toolBarSearchBar : LinearLayout
    private lateinit var linearNavbarItem1 : LinearLayout
    private lateinit var linearNavbarItem2 : LinearLayout
    private lateinit var linearNavbarItem3 : LinearLayout
    private lateinit var linearRowsend : LinearLayout
    private lateinit var userAuth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_home)

        toolBarSearchBar=findViewById(R.id.toolBarSearchBar)
        toolBarSearchBar.visibility=View.VISIBLE

        userAuth= FirebaseAuth.getInstance()

        profileImageView = findViewById(R.id.profile_image)

        profileImageView.setOnClickListener { view ->
            showPopupMenu(view)
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // This is where you handle the refresh action.
            // You can perform any data loading or refreshing here.
            // When you're done, call setRefreshing(false) to stop the refresh animation.
            recreate()
            // Example:
            // fetchData()
            swipeRefreshLayout.isRefreshing = false
        }

        linearRowsend = findViewById(R.id.linearRowsend)
        linearRowsend.setOnClickListener { view ->
            Log.d("Button Send Clicked","Button Send Clicked")
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
        setFragment(homeFragment)

        // Set click listeners for your LinearLayouts (Transactions and Top Up)
        linearNavbarItem1.setOnClickListener {
            setFragment(myParcelsFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
            setFragment(myParcelsFragment)
            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(myParcelsFragment)
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
            setFragment(myParcelsFragment)
            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
            setFragment(myParcelsFragment)
            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        linearNavbarItem3.setOnClickListener {
            setFragment(myDeliveriesFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(myParcelsFragment)
            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(myParcelsFragment)
            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
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
                R.id.chat -> {
                    val intent = Intent(this@CommonHome,ChatHistory::class.java)
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
    fun maskCusId(cusId: String): String {
        if (cusId.length >= 8) {
            val firstFour = cusId.take(4)
            val lastFour = cusId.takeLast(4)
            val maskedMiddle = "X".repeat(cusId.length - 8)
            return "$firstFour$maskedMiddle$lastFour"
        }
        return cusId
    }

}