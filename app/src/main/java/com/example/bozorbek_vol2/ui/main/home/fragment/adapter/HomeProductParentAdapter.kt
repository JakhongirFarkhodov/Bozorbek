package com.example.bozorbek_vol2.ui.main.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.ui.main.home.fragment.adapter.model.HomeProduct
import kotlinx.android.synthetic.main.item_home_parent_product.view.*

class HomeProductParentAdapter(val requestManager: RequestManager, val onParentItemClickListener: OnPrentItemClickListener) : RecyclerView.Adapter<HomeProductParentAdapter.HomeProductParentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProductParentViewHolder {
        return HomeProductParentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_parent_product, parent, false), requestManager, onParentItemClickListener)
    }

    override fun onBindViewHolder(holder: HomeProductParentViewHolder, position: Int) {
        when(holder)
        {
            is HomeProductParentViewHolder ->{
                holder.bind(differConfig.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeProduct>()
    {
        override fun areItemsTheSame(oldItem: HomeProduct, newItem: HomeProduct): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HomeProduct, newItem: HomeProduct): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<HomeProduct>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class HomeProductParentViewHolder(itemView:View, val requestManager: RequestManager, val onParentItemClickListener: OnPrentItemClickListener) : RecyclerView.ViewHolder(itemView)
         {
        fun bind(item:HomeProduct)
        {
            itemView.item_child_rv.visibility = View.INVISIBLE
            val adapter = HomeProductChildAdapter(requestManager,absoluteAdapterPosition, item,onParentItemClickListener)
            adapter.submitList(item.productList.distinct().toList())
            itemView.item_child_rv.visibility = View.VISIBLE
            itemView.item_child_rv.adapter = adapter
            itemView.item_child_rv.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemView.item_parent_title.setText(item.title)


        }

    }

    interface OnPrentItemClickListener{
        fun onItemClick(childPosition:Int, childItem:HomeRandomProducts, parentPosition:Int, parentItem: HomeProduct)
    }
}