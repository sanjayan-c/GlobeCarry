package com.example.globe_carry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        getUnreadMessageCount(chatRoom, holder.textUnreadCount)

        holder.itemView.setOnClickListener{
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
    private fun getUnreadMessageCount(chatRoom: String, textView: TextView) {
        userDbRef = FirebaseDatabase.getInstance().getReference()
        val messagesRef = userDbRef.child("chats").child(chatRoom).child("messages")

        // Query for unread messages
        val query = messagesRef.orderByChild("read").equalTo(false)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val unreadMessageCount = snapshot.childrenCount.toInt()
                textView.text = if (unreadMessageCount > 0) "$unreadMessageCount new" else ""
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
        val textName=itemView.findViewById<TextView>(R.id.txt_name)
        val textUnreadCount: TextView = itemView.findViewById(R.id.txt_unread_count)
    }

}