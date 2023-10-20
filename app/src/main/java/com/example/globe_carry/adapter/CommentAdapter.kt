package com.example.globe_carry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.R

class CommentAdapter(private val comments: List<String>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var currentCommentIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[currentCommentIndex]
        holder.commentText.text = comment

        holder.arrowLeft.setOnClickListener {
            showPreviousComment()
        }

        holder.arrowRight.setOnClickListener {
            showNextComment()
        }
    }

    override fun getItemCount() = 1

    private fun showPreviousComment() {
        if (currentCommentIndex > 0) {
            currentCommentIndex--
            notifyItemChanged(0)
        }
    }

    private fun showNextComment() {
        if (currentCommentIndex < comments.size - 1) {
            currentCommentIndex++
            notifyItemChanged(0)
        }
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentText: TextView = itemView.findViewById(R.id.commentText)
        val arrowLeft: ImageButton = itemView.findViewById(R.id.arrowLeft)
        val arrowRight: ImageButton = itemView.findViewById(R.id.arrowRight)
    }
}

