package com.android.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.chat.R
import com.android.chat.dtos.GroupDTO
import com.android.chat.utils.Helper
import com.android.chat.utils.ListActions
import com.android.chat.utils.ResourceUtil

class GroupsAdapter(
    val context: Context, val groups: ArrayList<GroupDTO>, val actions: ListActions
) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = groups[holder.adapterPosition]
        holder.name.text = item.name
        holder.description.text = "Members: ${item.membersCount}"
        holder.initials.text = Helper.getInitials(item.name!!)
        holder.initials.background = ResourceUtil.getSolidRectDrawable(50, R.color.secondaryColor)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val initials: TextView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener {
                actions.onItemClicked(adapterPosition)
            }
        }
    }
}