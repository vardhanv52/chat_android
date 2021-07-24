package com.android.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.chat.R
import com.android.chat.dtos.UserDTO
import com.android.chat.interfaces.IListActions
import com.android.chat.utils.Helper
import com.android.chat.utils.ResourceUtil

class UsersAdapter(
    val context: Context, val users: ArrayList<UserDTO>, val actions: IListActions?
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[holder.adapterPosition]
        if (holder.adapterPosition % 2 == 0)
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.light_white))
        else
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        holder.image.background = ResourceUtil.getSolidRectDrawable(50, R.color.secondaryColor)
        holder.image.text = Helper.getInitials(item.name!!)
        holder.title.text = "${item.name} - ${item.role}"
        holder.mobile.text = "+${item.countryCode ?: 91} - ${item.mobile}"
        holder.email.text = item.email

        if(holder.adapterPosition == users.size - 1)
            actions?.loadMoreItems()
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent: ConstraintLayout = itemView.findViewById(R.id.parent)
        val title: TextView = itemView.findViewById(R.id.title)
        val mobile: TextView = itemView.findViewById(R.id.mobile)
        val email: TextView = itemView.findViewById(R.id.email)
        val image: TextView = itemView.findViewById(R.id.image)
        val edit: ImageView = itemView.findViewById(R.id.edit)

        init {
            edit.setOnClickListener {
                actions?.onEditClicked(adapterPosition)
            }
        }
    }
}