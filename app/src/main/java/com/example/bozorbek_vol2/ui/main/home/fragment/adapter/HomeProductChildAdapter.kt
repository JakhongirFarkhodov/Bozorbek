package com.example.bozorbek_vol2.ui.main.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.ui.main.home.fragment.adapter.model.HomeProduct
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.item_home_child_product.view.*

class HomeProductChildAdapter(val requestManager: RequestManager, val parentPosition:Int, val parentItem:HomeProduct, val onParentItemClickListener: HomeProductParentAdapter.OnPrentItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeProductChildViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_child_product, parent, false), requestManager, parentPosition, parentItem, onParentItemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is HomeProductChildViewHolder ->{
                holder.bind(differConfig.currentList[position])
//                holder.itemView.home_product_constraint.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeRandomProducts>(){
        override fun areItemsTheSame(
            oldItem: HomeRandomProducts,
            newItem: HomeRandomProducts
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: HomeRandomProducts,
            newItem: HomeRandomProducts
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<HomeRandomProducts>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class HomeProductChildViewHolder(itemView: View, val requestManager: RequestManager, val parentPosition: Int, val parentItem: HomeProduct, val onParentItemClickListener: HomeProductParentAdapter.OnPrentItemClickListener) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: HomeRandomProducts)
        {
            requestManager.load(Constants.BASE_URL + item.image).transition(withCrossFade()).into(itemView.item_home_product_image)
            if(item.unit.equals("GRAMME"))
            {
                itemView.item_home_product_title.setText(item.name)
                "${String.format("%,d", (item.price * 1000)).replace(",", ".")} Сум"
                itemView.item_home_product_price.setText("${String.format("%,d", (item.price * 1000)).replace(",", ".")}")
            }
           else {
                itemView.item_home_product_title.setText(item.name)
                itemView.item_home_product_price.setText("${String.format("%,d", (item.price)).replace(",", ".")}")
            }

            itemView.home_product_constraint.setOnClickListener {
                onParentItemClickListener.onItemClick(absoluteAdapterPosition, item, parentPosition, parentItem)
            }

        }
    }



}