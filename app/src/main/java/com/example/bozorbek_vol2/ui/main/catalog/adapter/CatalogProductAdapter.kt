package com.example.bozorbek_vol2.ui.main.catalog.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import kotlinx.android.synthetic.main.item_catalog_product.view.*

class CatalogProductAdapter constructor(val onCatalogProductItemClickListener: OnCatalogProductItemClickListener, val requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    private lateinit var lightVibrantSwatch: Palette.Swatch
//    private var count:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatalogProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_product, parent, false), onCatalogProductItemClickListener, requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is CatalogProductAdapter.CatalogProductViewHolder ->{
                holder.bind(differConfig.currentList[position])
                holder.itemView.catalog_product_constraint.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)

             }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CatalogProduct>()
    {
        override fun areItemsTheSame(oldItem: CatalogProduct, newItem: CatalogProduct): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CatalogProduct, newItem: CatalogProduct): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(OnItemChangeListener(this), AsyncDifferConfig.Builder(DIFF_CALLBACK).build())

    internal inner class OnItemChangeListener(val adapter: CatalogProductAdapter) : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

    }

    fun submitList(list: List<CatalogProduct>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class CatalogProductViewHolder(itemView:View, val onCatalogProductItemClickListener: OnCatalogProductItemClickListener, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:CatalogProduct)
        {
            itemView.setOnClickListener {
                onCatalogProductItemClickListener.onCatalogProductItemClick(absoluteAdapterPosition, item)
            }

            itemView.item_catalog_product_title.text = item.name
            if(item.unit.equals("GRAMME"))
            {
                val price = String.format("%,d", (item.price * 1000).toInt()).replace(",", ".")
                itemView.item_catalog_product_price.text = "${price} Сум"
                itemView.item_catalog_product_second_price.text = price
            }
            else{
                val price = String.format("%,d", item.price.toInt()).replace(",", ".")
                itemView.item_catalog_product_price.text = "${price} Сум"
                itemView.item_catalog_product_second_price.text = price
            }

            requestManager.load(item.image).transition(withCrossFade()).into(itemView.item_catalog_product_image)

//            if (item.name_catalog.lowercase().equals("зелень"))
//            {
//                itemView.catalog_product_item_constraint.background = ContextCompat.getDrawable(itemView.context, R.drawable.catalog_product_item_background)
//            }

        }
    }



    interface OnCatalogProductItemClickListener{
        fun onCatalogProductItemClick(position:Int, item:CatalogProduct)
    }

}