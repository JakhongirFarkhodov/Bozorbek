package com.example.bozorbek_vol2.ui.main.basket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import kotlinx.android.synthetic.main.item_basket_container.view.*

class BasketAdapter(val onBasketItemClickListener: OnBasketItemClickListener,val onBasketOrderRemoveItemListener: OnBasketOrderRemoveItemListener, val requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BasketViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_basket_container, parent, false), onBasketItemClickListener, onBasketOrderRemoveItemListener, requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is BasketViewHolder ->{
                holder.bind(diffConfig.currentList[position])
//                holder.itemView.basket_container.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)
            }
        }
    }

    override fun getItemCount(): Int {
        return diffConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BasketOrderProduct>()
    {
        override fun areItemsTheSame(
            oldItem: BasketOrderProduct,
            newItem: BasketOrderProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BasketOrderProduct,
            newItem: BasketOrderProduct
        ): Boolean {
            return oldItem == newItem
        }

    }

    val diffConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<BasketOrderProduct>)
    {
        diffConfig.submitList(list)
    }

    inner class BasketViewHolder(itemView:View, val onBasketItemClickListener: OnBasketItemClickListener, val onBasketOrderRemoveItemListener: OnBasketOrderRemoveItemListener, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:BasketOrderProduct)
        {
            itemView.setOnClickListener {
                onBasketItemClickListener.onBasketItemClick(absoluteAdapterPosition, item)
            }

            itemView.item_cancel_image_basket.setOnClickListener {
                onBasketOrderRemoveItemListener.onBasketItemRemove(absoluteAdapterPosition, item)
            }

            requestManager.load(item.main_image).transition(withCrossFade()).into(itemView.item_fruit_image_basket)
            val size_in_gramme = String.format("%.1f", (item.sum_of_size/1000)).replace(",", ".").toFloat()
            itemView.item_mtv_weight.setText("${size_in_gramme} Кг")
            itemView.item_mtv_product_name.setText(item.name)
            itemView.item_tv_title_fruit_basket.setText(item.product_name)
            itemView.item_price_basket.setText("${item.sum_price_gramme.toInt()} Сум")

        }
    }

    interface OnBasketItemClickListener{
        fun onBasketItemClick(position:Int, item:BasketOrderProduct)
    }

    interface OnBasketOrderRemoveItemListener{
        fun onBasketItemRemove(position: Int, item: BasketOrderProduct)
    }

}