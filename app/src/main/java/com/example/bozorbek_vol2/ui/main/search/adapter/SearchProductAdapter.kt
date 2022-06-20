package com.example.bozorbek_vol2.ui.main.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.search.SearchProduct
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.item_search_product.view.*

class SearchProductAdapter(val requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_product, parent, false), requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is SearchViewHolder ->{
                holder.bind(differConfig.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchProduct>()
    {
        override fun areItemsTheSame(oldItem: SearchProduct, newItem: SearchProduct): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SearchProduct, newItem: SearchProduct): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<SearchProduct>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class SearchViewHolder(itemView:View, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView){
        fun bind(item:SearchProduct)
        {
            itemView.search_product_item_name_mtv.setText(item.name)
            requestManager.load(Constants.BASE_URL + item.absolute_url).into(itemView.search_product_item_imv)
        }
    }

}