package com.example.bozorbek_vol2.ui.main.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileNotification
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is NotificationViewHolder ->{
                holder.bind(differConfig.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfileNotification>()
    {
        override fun areItemsTheSame(
            oldItem: ProfileNotification,
            newItem: ProfileNotification
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProfileNotification,
            newItem: ProfileNotification
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list:List<ProfileNotification>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:ProfileNotification)
        {
            itemView.item_title_notification.setText(item.title)
            itemView.item_content_notification.setText(item.content)
        }
    }

}