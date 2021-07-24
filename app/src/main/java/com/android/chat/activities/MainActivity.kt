package com.android.chat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chat.R
import com.android.chat.adapters.GroupsAdapter
import com.android.chat.dtos.GroupDTO
import com.android.chat.dtos.UserDTO
import com.android.chat.managers.PreferenceManager
import com.android.chat.retrofit.APIManager
import com.android.chat.utils.Constants
import com.android.chat.utils.Helper
import com.android.chat.utils.ListActions
import com.android.chat.utils.ResourceUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.search_bar
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val context: Context = this
    private val groups = ArrayList<GroupDTO>()
    private var adapter: GroupsAdapter? = null
    private var search = ""
    private val addGroupRequestCode = 789
    private val addGroupResponseCode = 790

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialise()
        setListeners()
        getGroupRecords()
    }

    private fun getGroupRecords() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Helper.displayProgressBar(context)
                val resp = APIManager.getGroups(1, 100, search)
                Helper.dismissProgressBar()
                if (resp.getBoolean("status")) {
                    val records = Gson().fromJson<ArrayList<GroupDTO>>(
                        resp.getJSONArray("data").toString(),
                        object : TypeToken<ArrayList<GroupDTO>>() {}.type
                    )
                    groups.clear()
                    groups.addAll(records)
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
        if(Helper.getUserDTO()?.role == Constants.ROLE_ADMIN)
            users_icon?.visibility = View.VISIBLE
        else
            users_icon?.visibility = View.GONE
        search_bar?.background = ResourceUtil.getSolidRectDrawable(5, R.color.white)
        groups_rv?.layoutManager = LinearLayoutManager(context)
        groups_rv?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = GroupsAdapter(context, groups, object : ListActions() {
            override fun onItemClicked(pos: Int) {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("group", groups[pos])
                startActivity(intent)
            }
        })
        groups_rv?.adapter = adapter
        val user = Helper.getUserDTO()
        name?.text = "${user?.name} - ${user?.role}"
        email?.text = user?.email
        logout?.background = ResourceUtil.getSolidRectDrawable(5, R.color.primaryColor)
    }

    private fun setListeners() {
        users_icon?.setOnClickListener {
            startActivity(Intent(context, UsersActivity::class.java))
        }
        add_group?.setOnClickListener {
            startActivityForResult(Intent(context, AddGroupActivity::class.java), addGroupRequestCode)
        }
        search_bar?.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                search = search_bar?.text.toString().trim()
                getGroupRecords()
            }
            return@setOnEditorActionListener true
        }
        logout?.setOnClickListener {
            PreferenceManager.clearUserPreferences()
            startActivity(Intent(context, LoginActivity::class.java))
            Helper.showToast(context, "Logged out successfully")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == addGroupRequestCode && resultCode == addGroupResponseCode) {
            search = ""
            getGroupRecords()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}