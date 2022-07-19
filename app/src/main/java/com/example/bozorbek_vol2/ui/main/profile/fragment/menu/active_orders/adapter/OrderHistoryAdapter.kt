package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileActiveOrHistoryOrder
import kotlinx.android.synthetic.main.item_active_or_history_order.view.*

class OrderHistoryAdapter(val onShowOrdersListener: OnShowOrdersListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_active_or_history_order, parent, false), onShowOrdersListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is OrderHistoryViewHolder ->{
                holder.bind(differConfig.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfileActiveOrHistoryOrder>()
    {
        override fun areItemsTheSame(
            oldItem: ProfileActiveOrHistoryOrder,
            newItem: ProfileActiveOrHistoryOrder
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProfileActiveOrHistoryOrder,
            newItem: ProfileActiveOrHistoryOrder
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<ProfileActiveOrHistoryOrder>?)
    {
        val newList = list?.toMutableList()

        differConfig.submitList(newList)
    }


    inner class OrderHistoryViewHolder(itemView: View, val onShowOrdersListener: OnShowOrdersListener) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: ProfileActiveOrHistoryOrder)
        {
            itemView.item_ah_order_number_mtv.setText("Заказ №${item.id}")
            itemView.item_ah_order_address_mtv.setText(item.address.toString())
            itemView.item_ah_price_mtv.setText("${String.format("%,d",item.total_cost.toInt()).replace(",",".")} Сум")
            itemView.item_ah_date_mtv.setText(item.created_at.substring(0, item.created_at.indexOf("T")))

            itemView.active_or_history_orders.setOnClickListener {
                onShowOrdersListener.onShowOrder(absoluteAdapterPosition,item)
            }
        }
    }

    interface OnShowOrdersListener{
        fun onShowOrder(position: Int, item:ProfileActiveOrHistoryOrder)
    }


}