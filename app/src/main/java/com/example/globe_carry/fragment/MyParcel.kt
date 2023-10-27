package com.example.globe_carry.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import com.example.globe_carry.R
import com.example.globe_carry.fragment.MyParcelsFragment

class MyParcel : Fragment() {
    var fragmentShouldHideLinearRowsend: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_parcel_view, container, false)

        val card3 = view.findViewById<RelativeLayout>(R.id.pending_list)
        val card4 = view.findViewById<RelativeLayout>(R.id.sent_list)
        val myParcelLayout = view.findViewById<RelativeLayout>(R.id.my_parcel)
        myParcelLayout.setOnClickListener {
            val fragment = MyParcelsFragment()  // Create an instance of your fragment
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
// Replace the current fragment in the FrameLayout with the new fragment
            fragmentTransaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)  // Optional, to handle back button

            fragmentTransaction.commit()
        }
        val myParcelRequestLayout = view.findViewById<RelativeLayout>(R.id.request_list)
        myParcelRequestLayout.setOnClickListener {
            val fragment = MyParcelRequestsFragment()  // Create an instance of your fragment
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
// Replace the current fragment in the FrameLayout with the new fragment
            fragmentTransaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)  // Optional, to handle back button

            fragmentTransaction.commit()
        }

        val myParcelPendingLayout = view.findViewById<RelativeLayout>(R.id.pending_list)
        myParcelPendingLayout.setOnClickListener {
            val fragment = PendingParcelFragment()  // Create an instance of your fragment
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
// Replace the current fragment in the FrameLayout with the new fragment
            fragmentTransaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)  // Optional, to handle back button

            fragmentTransaction.commit()
        }

        return view
    }


}
