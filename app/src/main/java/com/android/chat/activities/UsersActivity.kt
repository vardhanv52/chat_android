package com.android.chat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chat.R
import com.android.chat.adapters.UsersAdapter
import com.android.chat.dtos.UserDTO
import com.android.chat.retrofit.APIManager
import com.android.chat.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersActivity : AppCompatActivity() {
    private val context: Context = this
    private var selectedPosition = 0
    private var adapter: UsersAdapter? = null
    private var users = ArrayList<UserDTO>()
    private val updateRequestCode = 567
    private val updateResultCode = 568
    private var search = ""
    private var pageNo = 1
    private var pageSize = 10
    private var isEndReached = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        initialise()
        setListeners()
        getUserRecords()
    }

    private fun getUserRecords() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Helper.displayProgressBar(context)
                isLoading = true
                val resp = APIManager.getUsers(pageNo++, pageSize, search)
                isLoading = false
                Helper.dismissProgressBar()
                if (resp.getBoolean("status")) {
                    val records = Gson().fromJson<ArrayList<UserDTO>>(
                        resp.getJSONArray("data").toString(),
                        object : TypeToken<ArrayList<UserDTO>>() {}.type
                    )
                    users.addAll(records)
                    adapter?.notifyDataSetChanged()
                    if(records.size < pageSize)
                        isEndReached = true
                } else
                    Helper.showToast(context, resp.getString("message"))
            } catch (ex: Exception) {
                ex.printStackTrace()
                Helper.dismissProgressBar()
                Helper.showToast(context, getString(R.string.went_wrong))
            }
        }
    }

    private fun setListeners() {
        back?.setOnClickListener {
            super.onBackPressed()
        }
        search_bar?.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                search = search_bar?.text.toString().trim()
                resetUsersList()
            }
            return@setOnEditorActionListener true
        }
        add_user?.setOnClickListener {
            startActivityForResult(
                Intent(context, AddUpdateUserActivity::class.java), updateRequestCode
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == updateRequestCode && resultCode == updateResultCode)
            resetUsersList()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun resetUsersList() {
        pageNo = 1
        pageSize = 10
        users.clear()
        adapter?.notifyDataSetChanged()
        getUserRecords()
    }

    private fun initialise() {
        search_bar?.background = ResourceUtil.getSolidRectDrawable(5, R.color.white)
        users_rv?.layoutManager = LinearLayoutManager(context)
        adapter = UsersAdapter(context, users, object : ListActions() {
            override fun onEditClicked(pos: Int) {
                val intent = Intent(context, AddUpdateUserActivity::class.java)
                intent.putExtra("user", users[pos])
                startActivityForResult(intent, updateRequestCode)
            }

            override fun loadMoreItems() {
                if(!isEndReached && !isLoading)
                    getUserRecords()
            }
        })
        users_rv?.adapter = adapter
    }
}