package com.example.bozorbek_vol2.ui.main.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.network.main.network_services.search.response.SearchHistoryResponse
import kotlinx.android.synthetic.main.item_search_history.view.*

class SearchHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is SearchHistoryViewHolder -> {
                holder.bind(differConfig.currentList[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryResponse>()
    {
        override fun areItemsTheSame(oldItem: SearchHistoryResponse, newItem: SearchHistoryResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchHistoryResponse, newItem: SearchHistoryResponse): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<SearchHistoryResponse>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class SearchHistoryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:SearchHistoryResponse)
        {
            itemView.item_search_title.setText(item.query)
            itemView.item_search_date.setText(item.date_added)
        }
    }

}