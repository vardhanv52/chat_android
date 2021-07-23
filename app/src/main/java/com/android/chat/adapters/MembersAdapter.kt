package com.android.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.chat.R
import com.android.chat.dtos.UserDTO

class MembersAdapter(val context: Context, val members: ArrayList<UserDTO>) :
    RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_member, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = members[holder.adapterPosition]
        if (holder.adapterPosition % 2 == 0)
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.light_white))
        else
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        holder.name.text = item.name
        holder.email.text = item.email
        holder.checkbox.isChecked = item.isSelected
    }

    override fun getItemCount(): Int {
        return members.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent: ConstraintLayout = itemView.findViewById(R.id.parent)
        val name: TextView = itemView.findViewById(R.id.title)
        val email: TextView = itemView.findViewById(R.id.description)
        val checkbox: CheckBox = itemView.findViewById(R.id.cb)

        init {
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                members[adapterPosition].isSelected = isChecked
            }
        }
    }
}