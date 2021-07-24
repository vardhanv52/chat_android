package com.android.chat.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chat.R
import com.android.chat.adapters.ChatAdapter
import com.android.chat.dtos.GroupDTO
import com.android.chat.dtos.MessageDTO
import com.android.chat.managers.SocketManager
import com.android.chat.retrofit.APIManager
import com.android.chat.utils.Helper
import com.android.chat.utils.ListActions
import com.android.chat.utils.ResourceUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {
    private val context: Context = this
    private var group: GroupDTO? = null
    private var user = Helper.getUserDTO()
    private var socketManager: SocketManager? = null
    private var adapter: ChatAdapter? = null
    private val messages = ArrayList<MessageDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        group = intent?.getSerializableExtra("group") as GroupDTO?
        if (group == null) {
            Helper.showToast(context, getString(R.string.went_wrong))
            return
        }
        initialise()
        setListeners()
        getMessageRecords()
    }

    private fun getMessageRecords() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Helper.displayProgressBar(context)
                val resp = APIManager.getMessages(group?.id!!)
                Helper.dismissProgressBar()
                if (resp.getBoolean("status")) {
                    val records = Gson().fromJson<ArrayList<MessageDTO>>(
                        resp.getJSONArray("data").toString(),
                        object : TypeToken<ArrayList<MessageDTO>>() {}.type
                    )
                    messages.addAll(records)
                    adapter?.notifyDataSetChanged()
                    msgs_rv?.scrollToPosition(messages.size - 1)
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
        msg?.background = ResourceUtil.getSolidRectDrawable(5, R.color.white)
        title_tv?.text = group?.name
        msgs_rv?.layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(context, messages, object : ListActions() {
            override fun onLikeClicked(pos: Int) {
                messages[pos].isLiked = !messages[pos].isLiked!!
                adapter?.notifyItemChanged(pos)
                postLike(pos)
            }
        })
        msgs_rv?.adapter = adapter
        socketManager = SocketManager(group?.id!!, object : SocketManager.ISocket {
            override fun receivedData(json: JSONObject) {
                Helper.dismissProgressBar()
                val msg = Gson().fromJson<MessageDTO>(
                    json.toString(), object : TypeToken<MessageDTO>() {}.type
                )
                messages.add(msg)
                adapter?.notifyDataSetChanged()
                msgs_rv?.scrollToPosition(messages.size - 1)
            }
        })
    }

    private fun setListeners() {
        send?.setOnClickListener {
            val message = msg?.text.toString().trim()
            if (message.isEmpty()) {
                Helper.showToast(context, getString(R.string.empty_msg))
                return@setOnClickListener
            }
            val json = JSONObject()
            json.put("group_id", group?.id!!)
            json.put("user_id", user?.id!!)
            json.put("message", message)
            socketManager?.postMessage(json)
            Helper.displayProgressBar(context)
            msg?.setText("")
        }

        back?.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun postLike(pos: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                APIManager.like(messages[pos].id!!, messages[pos].isLiked!!)
            } catch (ex: Exception) {
                ex.printStackTrace()
                Helper.dismissProgressBar()
                Helper.showToast(context, getString(R.string.went_wrong))
            }
        }
    }

    override fun onDestroy() {
        socketManager?.disconnect()
        super.onDestroy()
    }
}