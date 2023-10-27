package com.example.globe_carry.fragment


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ChatHistory
import com.example.globe_carry.CommonUserProfile
import com.example.globe_carry.HelpCenter
import com.example.globe_carry.HomeItems
import com.example.globe_carry.Login
import com.example.globe_carry.Message
import com.example.globe_carry.MyDeliveryRequests
import com.example.globe_carry.R
import com.example.globe_carry.User
import com.example.globe_carry.adapter.HomeItemsAdapter
import com.example.globe_carry.adapter.MyDeliveriesDeliveredAdapter
import com.example.globe_carry.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class HelpCenterFragment: Fragment() {

    private lateinit var userList: ArrayList<User>
    private var adapter: UserAdapter? = null
    private lateinit var userAuth: FirebaseAuth
    private lateinit var userDbRef: DatabaseReference
    private lateinit var cusWalletProgressBarLayout: LinearLayout
    private lateinit var runningManImageView1: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_staff_help_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            if (!isAdded) {
                return@launch
            }
            userDbRef= FirebaseDatabase.getInstance().getReference()

            cusWalletProgressBarLayout = view.findViewById(R.id.cusWalletProgressBarLayout)
            runningManImageView1 = view.findViewById(R.id.runningManImageView1)

            userList=ArrayList()
            updateRecyclerView(userList)

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

            fetchUsersWithMessages()

            // Start the animation on the main thread (Dispatchers.Main)
            withContext(Dispatchers.Main) {
                translationAnimator.start()

                // Create a Handler and post a delayed action
                Handler().postDelayed({
                    val userRecyclerView = view?.findViewById<RecyclerView>(R.id.userRecyclerView)
                    userRecyclerView?.visibility = View.VISIBLE
                    cusWalletProgressBarLayout.visibility = View.GONE
                    translationAnimator.cancel()
                }, 2000) // 2000 milliseconds (2 seconds)
            }

        }
    }
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
                    userDbRef.child("user").child(uid).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(userSnapshot: DataSnapshot) {
                            val user = userSnapshot.getValue(User::class.java)
                            if (user != null) {
                                userList.add(user)
                                adapter?.notifyDataSetChanged()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(filteredData:  ArrayList<User>) {
        // Check if the fragment is attached to an activity
        if (isAdded) {
            requireActivity().runOnUiThread {
                val recyclerView = view?.findViewById<RecyclerView>(R.id.userRecyclerView)
                val adapter = UserAdapter(requireContext(), filteredData)
                recyclerView?.adapter = adapter
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                adapter.notifyDataSetChanged()
            }
        }
    }


}