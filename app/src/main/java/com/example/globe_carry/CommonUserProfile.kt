package com.example.globe_carry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.globe_carry.adapter.CommentAdapter

class CommonUserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_user_profile)

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.commentsRecyclerView)
        val comments = listOf("Comment 1", "Comment 2", "Comment 3", "Comment 4") // Replace with your comment data
        val commentAdapter = CommentAdapter(comments)
        commentsRecyclerView.adapter = commentAdapter
    }
}