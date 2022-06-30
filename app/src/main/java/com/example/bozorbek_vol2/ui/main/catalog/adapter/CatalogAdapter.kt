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
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.item_catalog.view.*

class CatalogAdapter constructor(val onCatalogItemClickListener: OnCatalogItemClickListener, val requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatalogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog, parent, false), onCatalogItemClickListener, requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is CatalogViewHolder ->{
                holder.bind(differConfig.currentList[position])
                holder.itemView.catalog_constraint.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Catalog>()
    {
        override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(OnItemChangeListener(this), AsyncDifferConfig.Builder(DIFF_CALLBACK).build())

    internal inner class OnItemChangeListener(val adapter: CatalogAdapter) : ListUpdateCallback {
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


    fun submitList(list: List<Catalog>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)

    }



    inner class CatalogViewHolder(itemView:View, val onCatalogItemClickListener: OnCatalogItemClickListener, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:Catalog)
        {
            itemView.setOnClickListener {
                onCatalogItemClickListener.onCatalogItemClickListener(item, absoluteAdapterPosition)
            }

            itemView.item_catalog_title.text = item.name
            val base_url = Constants.BASE_URL + item.get_image
            requestManager.load(base_url).transition(withCrossFade()).into(itemView.item_catalog_image)
        }
    }

    interface OnCatalogItemClickListener{
        fun onCatalogItemClickListener(item: Catalog, position:Int)
    }

}