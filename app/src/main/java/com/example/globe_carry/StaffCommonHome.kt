package com.example.globe_carry

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.globe_carry.fragment.HelpCenterFragment
import com.example.globe_carry.fragment.HomeFragment
import com.example.globe_carry.fragment.MyDeliveriesFragment
import com.example.globe_carry.fragment.MyParcelsFragment
import com.example.globe_carry.fragment.StaffHomeFragment
import com.example.globe_carry.fragment.VerficationRequestFragment
import com.google.firebase.auth.FirebaseAuth

class StaffCommonHome : AppCompatActivity() {

    private lateinit var toolBarSearchBar : LinearLayout
    private lateinit var linearNavbarItem1 : LinearLayout
    private lateinit var linearNavbarItem2 : LinearLayout
    private lateinit var linearNavbarItem3 : LinearLayout
    private lateinit var userAuth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_common_home)

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


        linearNavbarItem1 = findViewById(R.id.linearNavbarItem1)
        linearNavbarItem2 = findViewById(R.id.linearNavbarItem2)
        linearNavbarItem3 = findViewById(R.id.linearNavbarItem3)

        val StaffHomeFragment = StaffHomeFragment()
        val VerficationRequestFragment = VerficationRequestFragment()
        val HelpCenterFragment = HelpCenterFragment()

        val image = findViewById<ImageView>(R.id.imageFolder2)
        val text = findViewById<TextView>(R.id.txtHome)
        image.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
        text.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
        setFragment(StaffHomeFragment)

        // Set click listeners for your LinearLayouts (Transactions and Top Up)
        linearNavbarItem1.setOnClickListener {
            setFragment(VerficationRequestFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
            setFragment(VerficationRequestFragment)
            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(VerficationRequestFragment)
            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        linearNavbarItem2.setOnClickListener {
            setFragment(StaffHomeFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(StaffHomeFragment)
            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
            setFragment(StaffHomeFragment)
            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        linearNavbarItem3.setOnClickListener {
            setFragment(HelpCenterFragment)
            val image1 = findViewById<ImageView>(R.id.imageFolder1)
            val text1 = findViewById<TextView>(R.id.txtMyparcels)
            image1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text1.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(HelpCenterFragment)
            val image2 = findViewById<ImageView>(R.id.imageFolder2)
            val text2 = findViewById<TextView>(R.id.txtHome)
            image2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
            text2.setTextColor(ContextCompat.getColor(this, R.color.black))
            setFragment(HelpCenterFragment)
            val image3 = findViewById<ImageView>(R.id.imageFolder3)
            val text3 = findViewById<TextView>(R.id.txtMyDeliveries)
            image3.setColorFilter(ContextCompat.getColor(this, R.color.bluegray_100_87), PorterDuff.Mode.SRC_IN)
            text3.setTextColor(ContextCompat.getColor(this, R.color.bluegray_100_87))
        }

    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        val help = popupMenu.menu.findItem(R.id.help)
        help.isVisible = false

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    // Perform the logout action
                    userAuth= FirebaseAuth.getInstance()
                    userAuth.signOut()
                    val intent = Intent(this, Login::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                // Add more menu items and their actions here
                else -> false
            }

            when (item.itemId) {

                R.id.profile -> {
                    val intent = Intent(this, CommonUserProfile::class.java)
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