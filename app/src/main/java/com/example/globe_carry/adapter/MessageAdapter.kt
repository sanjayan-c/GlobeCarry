package com.example.globe_carry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.Message
import com.example.globe_carry.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context, val messageList : ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1;
    val ITEM_SENT = 2;
    var messageDate : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType==1){
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.received,parent,false)
            return ReceiveViewHolder(view)
        }else{
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            //sent view holder
            val parts = currentMessage.timeStamp.split(" ") // Split the timestamp by space
            val date = parts[0] // First part is the date (25/10/2023)
            val time = parts[1] // Second part is the time (01:33:35)
            val timeParts = parts[1].split(":")
            val timeWithoutSeconds = "${timeParts[0]}:${timeParts[1]}"
            println("Date: $date")
            println("Time: $time")
            println("MessageDate: $messageDate")
            val viewHolder = holder as SentViewHolder
            if(date!=messageDate){
                println("If 1")
                messageDate=date
                holder.sentMessageDateTextView.visibility = View.VISIBLE
                holder.sentMessageDateTextView.text = messageDate
            }
            holder.sentMessage.text = currentMessage.message
            holder.sentMessageTime.text = timeWithoutSeconds
        }else{
            //receive view holder

            val parts = currentMessage.timeStamp.split(" ") // Split the timestamp by space
            val date = parts[0] // First part is the date (25/10/2023)
            val time = parts[1] // Second part is the time (01:33:35)
            val timeParts = parts[1].split(":")
            val timeWithoutSeconds = "${timeParts[0]}:${timeParts[1]}"
            println("Date: $date")
            println("Time: $time")
            println("MessageDate: $messageDate")
            val viewHolder = holder as ReceiveViewHolder
            if(date!=messageDate){
                println("If 2")
                messageDate=date
                holder.receiveMessageDateTextView.visibility = View.VISIBLE
                holder.receiveMessageDateTextView.text = messageDate
            }
            holder.receiveMessage.text = currentMessage.message
            holder.receiveMessageTime.text = timeWithoutSeconds
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return  ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.txt_sent_message)
        val sentMessageTime=itemView.findViewById<TextView>(R.id.txt_sent_message_time)
        val sentMessageDateTextView=itemView.findViewById<TextView>(R.id.txt_sent_messageDateTextView)
    }

    class ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val receiveMessage=itemView.findViewById<TextView>(R.id.txt_receive_message)
        val receiveMessageTime=itemView.findViewById<TextView>(R.id.txt_receive_message_time)
        val receiveMessageDateTextView=itemView.findViewById<TextView>(R.id.txt_receive_messageDateTextView)

    }

}