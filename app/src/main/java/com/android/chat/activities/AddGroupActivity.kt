package com.android.chat.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chat.R
import com.android.chat.adapters.MembersAdapter
import com.android.chat.dtos.UserDTO
import com.android.chat.retrofit.APIManager
import com.android.chat.utils.Helper
import com.android.chat.utils.ResourceUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_group.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddGroupActivity : AppCompatActivity() {
    private val context: Context = this
    private val members = ArrayList<UserDTO>()
    private var adapter: MembersAdapter? = null
    private var isUpdated = false
    private val responseCode = 790

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)
        initialise()
        setListeners()
        getUserRecords()
    }

    private fun getUserRecords() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Helper.displayProgressBar(context)
                val resp = APIManager.getUsers(1, 10, "")
                Helper.dismissProgressBar()
                if (resp.getBoolean("status")) {
                    val records = Gson().fromJson<ArrayList<UserDTO>>(
                        resp.getJSONArray("data").toString(),
                        object : TypeToken<ArrayList<UserDTO>>() {}.type
                    )
                    members.addAll(records)
                    adapter?.notifyDataSetChanged()
                } else
                    Helper.showToast(context, resp.getString("message"))
            } catch (ex: Exception) {
                ex.printStackTrace()
                Helper.dismissProgressBar()
                Helper.showToast(context, getString(R.string.went_wrong))
            }
        }
    }

    private fun initialise() {
        name_et?.background = ResourceUtil.getHollowStrokeDrawable(5, R.color.white)
        submit?.background = ResourceUtil.getSolidRectDrawable(10, R.color.primaryColor)
        members_rv?.layoutManager = LinearLayoutManager(context)
        adapter = MembersAdapter(context, members)
        members_rv?.adapter = adapter
    }

    private fun setListeners() {
        back?.setOnClickListener {
            super.onBackPressed()
        }
        submit?.setOnClickListener {
            val name = name_et?.text.toString().trim()
            if(name.isEmpty()) {
                Helper.showToast(context, getString(R.string.check_name))
                return@setOnClickListener
            }
            val selectedUsers = ArrayList<String>()
            members.forEach {
                if(it.isSelected)
                    selectedUsers.add(it.id!!)
            }
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    Helper.displayProgressBar(context)
                    val resp = APIManager.createGroup(name, selectedUsers)
                    Helper.dismissProgressBar()
                    if (resp.getBoolean("status")) {
                        name_et?.setText("")
                        members.forEach {
                            it.isSelected = false
                        }
                        adapter?.notifyDataSetChanged()
                        isUpdated = true
                    }
                    Helper.showToast(context, resp.getString("message"))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Helper.dismissProgressBar()
                    Helper.showToast(context, getString(R.string.went_wrong))
                }
            }
        }
    }

    override fun onBackPressed() {
        if(isUpdated)
            setResult(responseCode)
        super.onBackPressed()
    }
}