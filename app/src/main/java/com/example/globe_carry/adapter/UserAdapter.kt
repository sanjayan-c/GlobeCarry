package com.example.globe_carry.adapter

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.ChatActivity
import com.example.globe_carry.R
import com.example.globe_carry.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserAdapter(val context: Context, val userList:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    private lateinit var userDbRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser=userList[position]
        holder.textName.text=currentUser.name

        // Get the chat room ID for the selected chat
        val chatRoom = FirebaseAuth.getInstance().currentUser?.uid+currentUser.uid // Replace with the actual chat room ID
        println(chatRoom)
        getUnreadMessageCount(chatRoom, holder.textUnreadCount,holder.msgItemType,holder.msgLayout)

        // Retrieve the last message's timestamp from Firebase
        getLastMessageTimestamp(chatRoom) { lastMessageTimestamp ->
            // Now, you have the last message's timestamp, set it to homeItemPostDate
            holder.msgItemPostDate.text = lastMessageTimestamp
        }

        val chat = "CHAT  >"
        val mSpannableString = SpannableString(chat)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        holder.msgItemDetails.text = mSpannableString

        holder.msgItemDetails.setOnClickListener{
            Log.d("Click","Click")
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
//            FirebaseAuth.getInstance().currentUser?.uid

            // Mark messages as read for the selected chat
            markMessagesAsRead(chatRoom)
            context.startActivity(intent)
        }
    }

    private fun markMessagesAsRead(chatRoom: String) {
        userDbRef = FirebaseDatabase.getInstance().getReference()
        val messagesRef = userDbRef.child("chats").child(chatRoom).child("messages")

        // Query for unread messages
        val query = messagesRef.orderByChild("read").equalTo(false)

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                var unreadMessageCount = 0
                for (postSnapshot in snapshot.children) {
                    // Update the read status in the database for each unread message
                    postSnapshot.ref.child("read").setValue(true)
//                    unreadMessageCount++
                }
                // Now, you can log the unread message count
//                Log.d("UnreadMessages", "Unread messages in chat $chatRoom: $unreadMessageCount")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun getUnreadMessageCount(chatRoom: String, textView: TextView, msgItemType: TextView, msgLayout: ConstraintLayout) {
        userDbRef = FirebaseDatabase.getInstance().getReference()
        val messagesRef = userDbRef.child("chats").child(chatRoom).child("messages")

        // Query for unread messages
        val query = messagesRef.orderByChild("read").equalTo(false)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val unreadMessageCount = snapshot.childrenCount.toInt()
                if (unreadMessageCount > 0) {
                    textView.text = "$unreadMessageCount"
                    textView.visibility = View.VISIBLE
                }

                // Retrieve the last unread message
                var lastUnreadMessage: String? = ""
                for (messageSnapshot in snapshot.children) {
                    lastUnreadMessage = messageSnapshot.child("message").getValue(String::class.java)
                }
                if(lastUnreadMessage!=""){
                    msgLayout.visibility=View.VISIBLE
                }
                // Display the last unread message in msgItemType
                msgItemType.text = lastUnreadMessage ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun getLastMessageTimestamp(chatRoom: String, callback: (String) -> Unit) {
        userDbRef = FirebaseDatabase.getInstance().getReference()
        val messagesRef = userDbRef.child("chats").child(chatRoom).child("messages")

        val query = messagesRef.limitToLast(1).orderByKey() // Get the last message

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val lastMessage = snapshot.children.first() // Get the last message
                    val timestamp = lastMessage.child("timeStamp").getValue(String::class.java) // Assuming "timestamp" is the field in your message object
                    if (timestamp != null) {
                        callback(timestamp)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val cardMessage=itemView.findViewById<CardView>(R.id.cardMessage)
        val textName=itemView.findViewById<TextView>(R.id.txt_name)
        val textUnreadCount: TextView = itemView.findViewById(R.id.txt_unread_count)
        val msgItemType: TextView = itemView.findViewById(R.id.msgItemType)
        val msgLayout: ConstraintLayout = itemView.findViewById(R.id.msgLayout)
        val msgItemDetails: TextView = itemView.findViewById(R.id.msgItemDetails)
        val msgItemPostDate: TextView = itemView.findViewById(R.id.msgItemPostDate)
    }

}