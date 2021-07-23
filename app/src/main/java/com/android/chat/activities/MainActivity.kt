package com.android.chat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.chat.R
import com.android.chat.utils.Constants
import com.android.chat.utils.Helper
import com.android.chat.utils.ResourceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialise()
        setListeners()
    }

    private fun initialise() {
        if(Helper.getUserDTO()?.role == Constants.ROLE_ADMIN)
            users_icon?.visibility = View.VISIBLE
        else
            users_icon?.visibility = View.GONE
        search_bar?.background = ResourceUtil.getSolidRectDrawable(5, R.color.white)
    }

    private fun setListeners() {
        users_icon?.setOnClickListener {
            startActivity(Intent(context, UsersActivity::class.java))
        }
    }
}