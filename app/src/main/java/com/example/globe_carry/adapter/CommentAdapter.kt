package com.example.globe_carry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.CommentData
import com.example.globe_carry.CommonOtherUserProfile
import com.example.globe_carry.R

class CommentAdapter(private val context: Context, private val comments: List<CommentData>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var currentCommentIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = comments[currentCommentIndex]
        holder.commentText.text = item.comment
        holder.commentUserText.text = item.commentGmail

        if(item.commentId!="") {
            holder.commentUserText.setOnClickListener {
                val intent = Intent(context, CommonOtherUserProfile::class.java)
                intent.putExtra("userFromIntent", item.commentId)
                context.startActivity(intent)
            }
        }

        holder.arrowLeft.setOnClickListener {
            showPreviousComment()
        }

        holder.arrowRight.setOnClickListener {
            showNextComment()
        }
    }

    private fun showPreviousComment() {
        if (currentCommentIndex > 0) {
            currentCommentIndex--
            notifyDataSetChanged ()
        }
    }

    private fun showNextComment() {
        if (currentCommentIndex < comments.size - 1) {
            currentCommentIndex++
            notifyDataSetChanged ()
        }
    }
    override fun getItemCount(): Int {
        return 1  // Always display one comment at a time
    }
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentText: TextView = itemView.findViewById(R.id.commentText)
        val commentUserText: TextView = itemView.findViewById(R.id.commentUserText)
        val arrowLeft: ImageButton = itemView.findViewById(R.id.arrowLeft)
        val arrowRight: ImageButton = itemView.findViewById(R.id.arrowRight)
    }
}

