package com.android.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.chat.R
import com.android.chat.dtos.MessageDTO
import com.android.chat.utils.Constants
import com.android.chat.utils.Helper
import com.android.chat.utils.ListActions
import com.android.chat.utils.ResourceUtil
import java.util.*

class ChatAdapter(
    val context: Context, private val messages: ArrayList<MessageDTO>, val actions: ListActions
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val userId = Helper.getUserDTO()?.id
    private val itemTypeMe = 1
    private val itemTypeOthers = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == itemTypeMe)
            MeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_me, parent, false))
        else
            OthersViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_others, parent, false)
            )
    }

    override fun onBindViewHolder(holder_1: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[holder_1.adapterPosition]
        if (getItemViewType(position) == itemTypeMe) {
            val holder = holder_1 as MeViewHolder
            holder.msg.text = msg.message
            holder.time.text = Constants.dd_mm_yyyy_dd_mm_a.format(Date(msg.insertedAt ?: 0))
            holder.like.setImageResource(if (msg.isLiked!!) R.drawable.filled_bookmark else R.drawable.empty_bookmark)
        } else {
            val holder = holder_1 as OthersViewHolder
            holder.initials.text = Helper.getInitials(msg.user?.name ?: "NA")
            holder.initials.background =
                ResourceUtil.getSolidRectDrawable(50, R.color.secondaryColor)
            holder.name.text = msg.user?.name
            holder.msg.text = msg.message
            holder.time.text = Constants.dd_mm_yyyy_dd_mm_a.format(Date(msg.insertedAt ?: 0))
            holder.like.setImageResource(if (msg.isLiked!!) R.drawable.filled_bookmark else R.drawable.empty_bookmark)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].authorId == userId)
            itemTypeMe
        else
            itemTypeOthers
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class OthersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msg: TextView = itemView.findViewById(R.id.msg)
        val time: TextView = itemView.findViewById(R.id.time)
        val like: ImageView = itemView.findViewById(R.id.like)
        val initials: TextView = itemView.findViewById(R.id.initials)
        val name: TextView = itemView.findViewById(R.id.name)

        init {
            like.setOnClickListener {
                actions.onLikeClicked(adapterPosition)
            }
        }
    }

    inner class MeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msg: TextView = itemView.findViewById(R.id.msg)
        val time: TextView = itemView.findViewById(R.id.time)
        val like: ImageView = itemView.findViewById(R.id.like)

        init {
            like.setOnClickListener {
                actions.onLikeClicked(adapterPosition)
            }
        }
    }
}