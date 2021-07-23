package com.android.chat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.chat.R

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }
}